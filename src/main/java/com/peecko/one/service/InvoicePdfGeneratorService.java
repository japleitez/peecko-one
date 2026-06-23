package com.peecko.one.service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Generates an invoice PDF that mirrors the HTML template structure.
 *
 * <p>Usage:
 * <pre>
 *     Map&lt;String, Object&gt; data = new HashMap&lt;&gt;();
 *     data.put(InvoiceField.INVOICE_NUMBER, "2024-001");
 *     // … fill all fields …
 *     byte[] pdf = invoicePdfService.generate(data);
 * </pre>
 */
@Service
public class InvoicePdfGeneratorService {

    // ── Palette (matches the HTML template) ───────────────────────────────────
    private static final Color DARK_NAVY = new DeviceRgb(0x1a, 0x1a, 0x2e);
    private static final Color MID_GRAY = new DeviceRgb(0x6b, 0x72, 0x80);
    private static final Color LIGHT_GRAY = new DeviceRgb(0x9c, 0xa3, 0xaf);
    private static final Color BODY_TEXT = new DeviceRgb(0x37, 0x41, 0x51);
    private static final Color BORDER_GRAY = new DeviceRgb(0xe5, 0xe7, 0xeb);
    private static final Color ROW_ALT = new DeviceRgb(0xfa, 0xfa, 0xfa);
    private static final Color PERIOD_BG = new DeviceRgb(0xf8, 0xf9, 0xff);
    private static final Color BADGE_BG = new DeviceRgb(0xf0, 0xf4, 0xff);
    private static final Color BADGE_BLUE = new DeviceRgb(0x3b, 0x5b, 0xdb);
    private static final Color INDIGO_MUTED = new DeviceRgb(0xa5, 0xb4, 0xfc);
    private static final Color WHITE = new DeviceRgb(0xff, 0xff, 0xff);

    // ── Page margins (mm → pt: 1 mm = 2.8346 pt) ──────────────────────────────
    private static final float MARGIN_TOP = mm(18);
    private static final float MARGIN_SIDE = mm(15);
    // Bottom margin is enlarged to reserve space for the pinned footer so that
    // normal flow content never overlaps it.
    private static final float MARGIN_BOTTOM = mm(22);

    // ── Fixed footer geometry ─────────────────────────────────────────────────
    // Y coordinate (from page bottom) at which the footer divider is drawn.
    private static final float FOOTER_Y = mm(14);

    // ── Column widths for the line-items table (pt) ───────────────────────────
    private static final float COL_QTY = 42f;
    private static final float COL_UNIT_PRICE = 66f;
    private static final float COL_AMOUNT = 66f;

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Generates invoice PDF bytes from a placeholder map.
     *
     * @param data map whose keys are {@link InvoiceField} constants
     * @return raw PDF bytes ready to write to a file or HTTP response
     */
    public byte[] generate(Map<String, Object> data, Map<String, String> labels) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeTo(baos, data, labels);
        return baos.toByteArray();
    }

    /**
     * Writes the invoice PDF directly to an {@link OutputStream}.
     *
     * @param out  target stream (not closed by this method)
     * @param data map whose keys are {@link InvoiceField} constants
     */
    public void writeTo(OutputStream out, Map<String, Object> data, Map<String, String> labels) throws IOException {
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4);

        document.setMargins(MARGIN_TOP, MARGIN_SIDE, MARGIN_BOTTOM, MARGIN_SIDE);

        PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        float contentWidth = PageSize.A4.getWidth() - 2 * MARGIN_SIDE;

        // ── 1. Header row ─────────────────────────────────────────────────────
        document.add(buildHeader(data, labels, bold, regular, contentWidth));
        document.add(spacer(6));

        // ── 2. From / Bill To parties ─────────────────────────────────────────
        document.add(buildParties(data, labels, bold, regular, contentWidth));
        document.add(spacer(4));

        // ── 3. Period bar ─────────────────────────────────────────────────────
        document.add(buildPeriodBar(data, labels, bold, regular, contentWidth));
        document.add(spacer(6));

        // ── 4. Dark divider ───────────────────────────────────────────────────
        document.add(darkDivider(contentWidth));
        document.add(spacer(4));

        // ── 5. Line-items table ───────────────────────────────────────────────
        document.add(buildItemsTable(data, labels, bold, regular, contentWidth));

        // ── 6. Summary table ──────────────────────────────────────────────────
        document.add(buildSummaryTable(data, labels, bold, regular, contentWidth));
        document.add(spacer(8));

        // ── 7. Light divider ──────────────────────────────────────────────────
        document.add(lightDivider(contentWidth));

        // ── 8. Footer — pinned to the bottom of the page ──────────────────────
        // The divider line sits FOOTER_Y + ~10 pt above the page bottom edge,
        // and the text follows immediately below it, both at a fixed position
        // so they always appear at the very bottom regardless of content length.

        // Divider line: a zero-height paragraph with a top border
        Paragraph footerDivider = new Paragraph()
            .setBorderTop(new SolidBorder(BORDER_GRAY, 0.75f))
            .setMargin(0)
            .setPadding(0)
            .setFontSize(0)
            .setWidth(contentWidth)
            .setFixedPosition(MARGIN_SIDE, FOOTER_Y + 10, contentWidth);
        document.add(footerDivider);

        // Footer text
        Paragraph footerText = buildFooter(data, regular, contentWidth);
        footerText.setFixedPosition(MARGIN_SIDE, FOOTER_Y - 4, contentWidth);
        document.add(footerText);

        document.close();
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  Section builders
    // ═════════════════════════════════════════════════════════════════════════

    /** Header: left = "Invoice" + number, right = badge + dates */
    private Table buildHeader(Map<String, Object> data, Map<String, String> labels, PdfFont bold, PdfFont regular, float contentWidth) {
        Table table = twoColumnTable(contentWidth);

        // -- Left cell
        Cell left = new Cell().setBorder(Border.NO_BORDER).setPadding(0);
        left.add(
            new Paragraph(labels.get(InvoiceLabel.TITLE)).setFont(bold).setFontSize(22).setFontColor(DARK_NAVY).setMargin(0).setPadding(0)
        );
        left.add(
            new Paragraph("#" + val(data, InvoiceField.INVOICE_NUMBER))
                .setFont(regular)
                .setFontSize(11)
                .setFontColor(BADGE_BLUE)
                .setMarginTop(2)
                .setPadding(0)
        );
        table.addCell(left);

        // -- Right cell
        Cell right = new Cell().setBorder(Border.NO_BORDER).setPadding(0).setTextAlignment(TextAlignment.RIGHT);

        /** Badge
        right.add(
            new Paragraph("Due " + val(data, InvoiceField.INVOICE_DUE))
                .setFont(bold)
                .setFontSize(8)
                .setFontColor(BADGE_BLUE)
                .setBackgroundColor(BADGE_BG)
                .setBorderRadius(new BorderRadius(10))
                .setPaddingLeft(7)
                .setPaddingRight(7)
                .setPaddingTop(2)
                .setPaddingBottom(2)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginBottom(6)
        );
        */

        // Date grid (Issue / Due)
        Table dateGrid = new Table(new float[] { 1, 1 }).useAllAvailableWidth().setBorder(Border.NO_BORDER).setMargin(0).setPadding(0);
        dateGrid.addCell(
            labelValueCell(labels.get(InvoiceLabel.ISSUE_DATE), val(data, InvoiceField.INVOICE_ISSUE), regular, bold, TextAlignment.RIGHT)
        );
        dateGrid.addCell(
            labelValueCell(labels.get(InvoiceLabel.DUE_DATE), val(data, InvoiceField.INVOICE_DUE), regular, bold, TextAlignment.RIGHT)
        );
        right.add(dateGrid);

        table.addCell(right);
        return table;
    }

    /** Two-column "From / Bill To" block */
    private Table buildParties(Map<String, Object> data, Map<String, String> labels, PdfFont bold, PdfFont regular, float contentWidth) {
        Table table = twoColumnTable(contentWidth);

        // From
        String agencyBankAccount = val(data, InvoiceField.AGENCY_BANK_ACCOUNT);
        String agencyBankLine = StringUtils.hasText(agencyBankAccount)
            ? labels.get(InvoiceLabel.ACCOUNT).toUpperCase() + ": " + agencyBankAccount
            : "IBAN: " + val(data, InvoiceField.AGENCY_BANK_IBAN);
        table.addCell(
            partyCell(
                labels.get(InvoiceLabel.FROM),
                val(data, InvoiceField.AGENCY_NAME),
                new String[] {
                    val(data, InvoiceField.AGENCY_ADDRESS_STREET),
                    val(data, InvoiceField.AGENCY_ADDRESS_CITY),
                    val(data, InvoiceField.AGENCY_ADDRESS_COUNTRY),
                },
                new String[] {
                    labels.get(InvoiceLabel.VAT) + ": " + val(data, InvoiceField.AGENCY_VAT_NUMBER),
                    "SWIFT: " + val(data, InvoiceField.AGENCY_BANK_SWIFT),
                    agencyBankLine,
                },
                bold,
                regular,
                TextAlignment.LEFT
            )
        );

        // Bill To
        table.addCell(
            partyCell(
                labels.get(InvoiceLabel.BILL_TO),
                val(data, InvoiceField.CLIENT_NAME),
                new String[] {
                    val(data, InvoiceField.CLIENT_ADDRESS_STREET),
                    val(data, InvoiceField.CLIENT_ADDRESS_CITY),
                    val(data, InvoiceField.CLIENT_ADDRESS_COUNTRY),
                },
                new String[] { labels.get(InvoiceLabel.VAT) + ": " + val(data, InvoiceField.CLIENT_VAT_NUMBER) },
                bold,
                regular,
                TextAlignment.RIGHT
            )
        );

        return table;
    }

    /** Rounded period bar with period and customer code */
    private Table buildPeriodBar(Map<String, Object> data, Map<String, String> labels, PdfFont bold, PdfFont regular, float contentWidth) {
        Table bar = twoColumnTable(contentWidth);
        bar.setBackgroundColor(PERIOD_BG).setBorder(new SolidBorder(BORDER_GRAY, 0.5f)).setBorderRadius(new BorderRadius(5));

        // Left: period
        Cell left = new Cell().setBorder(Border.NO_BORDER).setPaddingLeft(12).setPaddingTop(8).setPaddingBottom(8);
        left.add(
            new Paragraph(labels.get(InvoiceLabel.PERIOD))
                .setFont(bold)
                .setFontSize(7)
                .setFontColor(LIGHT_GRAY)
                .setCharacterSpacing(0.5f)
                .setMarginBottom(2)
        );
        left.add(
            new Paragraph(val(data, InvoiceField.DATE_FROM) + " – " + val(data, InvoiceField.DATE_TO))
                .setFont(bold)
                .setFontSize(10)
                .setFontColor(DARK_NAVY)
        );
        bar.addCell(left);

        // Right: customer code
        Cell right = new Cell()
            .setBorder(Border.NO_BORDER)
            .setPaddingRight(12)
            .setPaddingTop(8)
            .setPaddingBottom(8)
            .setTextAlignment(TextAlignment.RIGHT)
            .setVerticalAlignment(VerticalAlignment.MIDDLE);
        right.add(
            new Paragraph(labels.get(InvoiceLabel.CUSTOMER_ACCOUNT) + ": ")
                .add(new Text(val(data, InvoiceField.CLIENT_CODE)).setFont(bold))
                .setFont(regular)
                .setFontSize(9)
                .setFontColor(MID_GRAY)
        );
        bar.addCell(right);

        return bar;
    }

    /** Line-items table with dark header */
    private Table buildItemsTable(Map<String, Object> data, Map<String, String> labels, PdfFont bold, PdfFont regular, float contentWidth) {
        float descWidth = contentWidth - COL_QTY - COL_UNIT_PRICE - COL_AMOUNT;
        Table table = new Table(new float[] { descWidth, COL_QTY, COL_UNIT_PRICE, COL_AMOUNT })
            .useAllAvailableWidth()
            .setBorder(Border.NO_BORDER);

        // Header row
        String[] headers = {
            labels.get(InvoiceLabel.DESCRIPTION),
            labels.get(InvoiceLabel.QUANTITY),
            labels.get(InvoiceLabel.UNIT_PRICE),
            labels.get(InvoiceLabel.AMOUNT),
        };
        for (int i = 0; i < headers.length; i++) {
            Cell hCell = new Cell().setBackgroundColor(DARK_NAVY).setBorder(Border.NO_BORDER).setPadding(7);
            hCell.add(
                new Paragraph(headers[i])
                    .setFont(bold)
                    .setFontSize(8)
                    .setFontColor(WHITE)
                    .setTextAlignment(i == 0 ? TextAlignment.LEFT : TextAlignment.RIGHT)
            );
            table.addHeaderCell(hCell);
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) data.get(InvoiceField.ITEMS);
        if (items != null) {
            for (int row = 0; row < items.size(); row++) {
                Map<String, Object> item = items.get(row);
                String[] rowValues = {
                    val(item, InvoiceField.ITEM_DESCRIPTION),
                    val(item, InvoiceField.ITEM_QUANTITY),
                    val(item, InvoiceField.ITEM_UNIT_PRICE),
                    val(item, InvoiceField.ITEM_SUBTOTAL),
                };
                Color bg = (row % 2 == 1) ? ROW_ALT : null;
                for (int i = 0; i < rowValues.length; i++) {
                    Cell cell = new Cell().setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(BORDER_GRAY, 0.5f)).setPadding(7);
                    if (bg != null) cell.setBackgroundColor(bg);
                    cell.add(
                        new Paragraph(rowValues[i])
                            .setFont(regular)
                            .setFontSize(10)
                            .setFontColor(BODY_TEXT)
                            .setTextAlignment(i == 0 ? TextAlignment.LEFT : TextAlignment.RIGHT)
                    );
                    table.addCell(cell);
                }
            }
        }

        return table;
    }

    /** Summary table: tax rate row + dark "Total Due" row */
    private Table buildSummaryTable(
        Map<String, Object> data,
        Map<String, String> labels,
        PdfFont bold,
        PdfFont regular,
        float contentWidth
    ) {
        float labelW = COL_UNIT_PRICE + mm(10);
        float valueW = COL_AMOUNT;
        float spacerW = contentWidth - labelW - valueW;

        Table table = new Table(new float[] { spacerW, labelW, valueW }).useAllAvailableWidth().setBorder(Border.NO_BORDER);

        List<Map<String, Object>> items = (List<Map<String, Object>>) data.get(InvoiceField.ITEMS);
        if (items != null && items.size() > 1) {
            // Subtotal row, this makes sense only when there are more than one invoice items
            table.addCell(emptyCell());
            table.addCell(summaryLabelCell(labels.get(InvoiceLabel.SUBTOTAL), regular, false));
            table.addCell(summaryValueCell(val(data, InvoiceField.INVOICE_SUBTOTAL), regular, false));
        }

        // Tax rate row
        table.addCell(emptyCell());
        table.addCell(
            summaryLabelCell(labels.get(InvoiceLabel.TAX_RATE) + " (" + val(data, InvoiceField.INVOICE_VAT_RATE) + "%)", regular, false)
        );
        table.addCell(summaryValueCell(val(data, InvoiceField.INVOICE_VAT), regular, false));

        // Total Due row
        table.addCell(emptyCell());
        table.addCell(summaryLabelCell(labels.get(InvoiceLabel.TOTAL_DUE), bold, true));
        table.addCell(summaryValueCell(val(data, InvoiceField.INVOICE_TOTAL), bold, true));

        return table;
    }

    /** Centered footer text — width is set by the caller via setFixedPosition. */
    private Paragraph buildFooter(Map<String, Object> data, PdfFont regular, float contentWidth) {
        return new Paragraph(val(data, InvoiceField.AGENCY_FOOTER_LINE1))
            .setFont(regular)
            .setFontSize(8.5f)
            .setFontColor(LIGHT_GRAY)
            .setTextAlignment(TextAlignment.CENTER)
            .setWidth(contentWidth)
            .setMargin(0);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  Cell / element helpers
    // ═════════════════════════════════════════════════════════════════════════

    private Cell partyCell(
        String role,
        String name,
        String[] addressLines,
        String[] metaLines,
        PdfFont bold,
        PdfFont regular,
        TextAlignment align
    ) {
        Cell cell = new Cell().setBorder(Border.NO_BORDER).setPadding(0).setTextAlignment(align);

        cell.add(new Paragraph(role).setFont(bold).setFontSize(7).setFontColor(LIGHT_GRAY).setCharacterSpacing(0.6f).setMarginBottom(4));
        cell.add(new Paragraph(name).setFont(bold).setFontSize(12).setFontColor(DARK_NAVY).setMarginBottom(3));

        Paragraph addr = new Paragraph().setFont(regular).setFontSize(10).setFontColor(BODY_TEXT).setMultipliedLeading(1.55f);
        for (int i = 0; i < addressLines.length; i++) {
            addr.add(addressLines[i]);
            if (i < addressLines.length - 1) addr.add("\n");
        }
        cell.add(addr);

        Paragraph meta = new Paragraph().setFont(regular).setFontSize(9).setFontColor(MID_GRAY).setMultipliedLeading(1.55f).setMarginTop(5);
        for (int i = 0; i < metaLines.length; i++) {
            meta.add(metaLines[i]);
            if (i < metaLines.length - 1) meta.add("\n");
        }
        cell.add(meta);

        return cell;
    }

    /** Small label + value stacked vertically (used in date grid) */
    private Cell labelValueCell(String label, String value, PdfFont regular, PdfFont bold, TextAlignment align) {
        Cell cell = new Cell().setBorder(Border.NO_BORDER).setPadding(0);
        cell.add(
            new Paragraph(label.toUpperCase())
                .setFont(regular)
                .setFontSize(7)
                .setFontColor(LIGHT_GRAY)
                .setCharacterSpacing(0.5f)
                .setTextAlignment(align)
                .setMarginBottom(1)
        );
        cell.add(new Paragraph(value).setFont(bold).setFontSize(10).setFontColor(DARK_NAVY).setTextAlignment(align));
        return cell;
    }

    private Cell summaryLabelCell(String text, PdfFont font, boolean isTotalRow) {
        Cell cell = new Cell().setBorder(Border.NO_BORDER).setPadding(isTotalRow ? 8 : 6).setTextAlignment(TextAlignment.RIGHT);
        if (isTotalRow) {
            cell.setBackgroundColor(DARK_NAVY);
            cell.add(new Paragraph(text).setFont(font).setFontSize(10).setFontColor(INDIGO_MUTED));
        } else {
            cell.add(new Paragraph(text).setFont(font).setFontSize(10).setFontColor(MID_GRAY));
        }
        return cell;
    }

    private Cell summaryValueCell(String text, PdfFont font, boolean isTotalRow) {
        Cell cell = new Cell().setBorder(Border.NO_BORDER).setPadding(isTotalRow ? 8 : 6).setTextAlignment(TextAlignment.RIGHT);
        if (isTotalRow) {
            cell.setBackgroundColor(DARK_NAVY);
            cell.add(new Paragraph(text).setFont(font).setFontSize(12).setFontColor(WHITE));
        } else {
            cell.add(new Paragraph(text).setFont(font).setFontSize(10).setFontColor(DARK_NAVY));
        }
        return cell;
    }

    private Cell emptyCell() {
        return new Cell().setBorder(Border.NO_BORDER).setPadding(0);
    }

    private Table twoColumnTable(float contentWidth) {
        return new Table(new float[] { 1, 1 }).useAllAvailableWidth().setBorder(Border.NO_BORDER).setMargin(0).setPadding(0);
    }

    private Table darkDivider(float contentWidth) {
        Table t = new Table(new float[] { contentWidth }).useAllAvailableWidth().setBorder(Border.NO_BORDER);
        t.addCell(new Cell().setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(DARK_NAVY, 1.5f)).setPadding(0));
        return t;
    }

    private Table lightDivider(float contentWidth) {
        Table t = new Table(new float[] { contentWidth }).useAllAvailableWidth().setBorder(Border.NO_BORDER);
        t.addCell(new Cell().setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(BORDER_GRAY, 0.75f)).setPadding(0));
        return t;
    }

    private Paragraph spacer(float height) {
        return new Paragraph().setMarginBottom(height).setFontSize(0);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  Utilities
    // ═════════════════════════════════════════════════════════════════════════

    /** Safe toString for a map value; returns empty string if null. */
    private static String val(Map<String, Object> data, String key) {
        Object v = data.get(key);
        return v == null ? "" : v.toString();
    }

    /** Convert millimetres to iText points (1 mm = 2.8346 pt). */
    private static float mm(float mm) {
        return mm * 2.8346f;
    }
}
