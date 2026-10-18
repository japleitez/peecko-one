package com.peecko.one.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class HtmlSanitizerTest {

    @Test
    void nullAndBlankAreLeftUntouched() {
        assertThat(HtmlSanitizer.sanitize(null)).isNull();
        assertThat(HtmlSanitizer.sanitize("")).isEmpty();
        assertThat(HtmlSanitizer.sanitize("   ")).isEqualTo("   ");
    }

    @Test
    void plainTextIsPreserved() {
        assertThat(HtmlSanitizer.sanitize("Just a sentence.")).isEqualTo("Just a sentence.");
    }

    @Test
    void safeFormattingIsPreserved() {
        String html =
            "<h2>Breathing</h2><p>Inhale <strong>slowly</strong> and <em>exhale</em>.</p>" +
            "<ul><li>Sit down</li><li>Close your eyes</li></ul>";

        assertThat(HtmlSanitizer.sanitize(html)).isEqualTo(html);
    }

    @Test
    void tableAndFigureMarkupIsPreserved() {
        String html =
            "<figure><img src=\"https://cdn.peecko.com/a.png\" alt=\"pose\"><figcaption>Pose</figcaption></figure>" +
            "<table><tbody><tr><th scope=\"col\">Set</th><td colspan=\"2\">Reps</td></tr></tbody></table>";

        assertThat(HtmlSanitizer.sanitize(html)).isEqualTo(html);
    }

    @Test
    void whitespaceIsNotReformatted() {
        assertThat(HtmlSanitizer.sanitize("<p>one</p><p>two</p>")).isEqualTo("<p>one</p><p>two</p>");
    }

    @Test
    void scriptTagsAreRemoved() {
        assertThat(HtmlSanitizer.sanitize("<p>hello</p><script>alert('xss')</script>")).isEqualTo("<p>hello</p>");
    }

    @Test
    void styleAndFrameTagsAreRemoved() {
        assertThat(HtmlSanitizer.sanitize("<style>body{display:none}</style><p>hi</p>")).isEqualTo("<p>hi</p>");
        assertThat(HtmlSanitizer.sanitize("<iframe src=\"https://evil.com\"></iframe><p>hi</p>")).isEqualTo("<p>hi</p>");
        assertThat(HtmlSanitizer.sanitize("<object data=\"evil.swf\"></object><p>hi</p>")).isEqualTo("<p>hi</p>");
    }

    @Test
    void eventHandlerAttributesAreRemoved() {
        assertThat(HtmlSanitizer.sanitize("<p onclick=\"steal()\">text</p>")).isEqualTo("<p>text</p>");
        assertThat(HtmlSanitizer.sanitize("<img src=\"https://cdn.peecko.com/a.png\" onerror=\"steal()\">"))
            .isEqualTo("<img src=\"https://cdn.peecko.com/a.png\">");
    }

    @Test
    void styleAttributesAreRemoved() {
        assertThat(HtmlSanitizer.sanitize("<p style=\"position:fixed;top:0\">text</p>")).isEqualTo("<p>text</p>");
    }

    @Test
    void scriptBearingUrlsAreRemoved() {
        assertThat(HtmlSanitizer.sanitize("<a href=\"javascript:alert(1)\">click</a>"))
            .isEqualTo("<a rel=\"noopener noreferrer\">click</a>");
        // the src is dropped, only the (harmless) empty element survives
        assertThat(HtmlSanitizer.sanitize("<img src=\"data:text/html;base64,PHNjcmlwdD4=\">")).isEqualTo("<img>");
    }

    @Test
    void httpLinksArePreservedAndGetRelNoopener() {
        assertThat(HtmlSanitizer.sanitize("<a href=\"https://peecko.com\" target=\"_blank\">peecko</a>"))
            .isEqualTo("<a href=\"https://peecko.com\" target=\"_blank\" rel=\"noopener noreferrer\">peecko</a>");
    }

    @Test
    void textOfRemovedTagsIsKept() {
        assertThat(HtmlSanitizer.sanitize("<marquee>still readable</marquee>")).isEqualTo("still readable");
    }
}
