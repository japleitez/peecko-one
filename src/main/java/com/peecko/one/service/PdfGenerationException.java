package com.peecko.one.service;

/**
 * Unchecked exception wrapping any failure during PDF generation.
 */
public class PdfGenerationException extends RuntimeException {

    public PdfGenerationException(String message) {
        super(message);
    }

    public PdfGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
