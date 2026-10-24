package com.peecko.one.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

/**
 * Sanitizes user supplied rich text before it is persisted.
 *
 * <p>Only a safe subset of HTML survives: formatting, headings, lists, tables, links and images.
 * Everything else - {@code <script>}, {@code <style>}, {@code <iframe>}, {@code <object>}, event
 * handler attributes such as {@code onclick}, and {@code javascript:} / {@code data:} URLs - is
 * stripped. Text content of removed tags is kept, which is jsoup's default behaviour.
 */
public final class HtmlSanitizer {

    /**
     * Maximum length accepted for a sanitized HTML document. Must stay in sync with the
     * {@code @Size} constraint on the entity fields that hold sanitized HTML.
     */
    public static final int MAX_HTML_LENGTH = 65535;

    private static final Safelist SAFELIST = buildSafelist();

    private HtmlSanitizer() {}

    private static Safelist buildSafelist() {
        return Safelist
            .relaxed()
            // relaxed() already allows: a, b, blockquote, br, caption, cite, code, col, colgroup,
            // dd, div, dl, dt, em, h1-h6, i, img, li, ol, p, pre, q, small, span, strike, strong,
            // sub, sup, table, tbody, td, tfoot, th, thead, tr, u, ul
            .addTags("hr", "figure", "figcaption", "s", "del", "ins", "mark", "abbr", "time")
            .addAttributes(":all", "class")
            .addAttributes("a", "target")
            .addAttributes("abbr", "title")
            .addAttributes("time", "datetime")
            .addAttributes("td", "colspan", "rowspan")
            .addAttributes("th", "colspan", "rowspan", "scope")
            // links and images may only point at http(s) (and mailto for links); this rules out
            // javascript:, data: and other script bearing schemes
            .addProtocols("a", "href", "http", "https", "mailto")
            .addProtocols("img", "src", "http", "https")
            // anchors that open a new tab must not hand window.opener to the target page
            .addEnforcedAttribute("a", "rel", "noopener noreferrer");
    }

    /**
     * Cleans the given HTML, keeping only safe formatting markup.
     *
     * @param html the untrusted HTML, may be {@code null}
     * @return the sanitized HTML, {@code null} if the input was {@code null}
     */
    public static String sanitize(String html) {
        if (html == null) {
            return null;
        }
        if (html.isBlank()) {
            return html;
        }
        // prettyPrint(false) keeps the author's own whitespace instead of re-indenting the document
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        return Jsoup.clean(html, "", SAFELIST, outputSettings);
    }
}
