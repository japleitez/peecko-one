package com.peecko.one.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TemplateLoader {

    public static String loadTemplateFromPath(String filePath) {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new RuntimeException("Template file not found at: " + filePath);
        }
        if (!Files.isReadable(path)) {
            try {
                throw new IOException("Template file is not readable: " + filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String content;
        try {
            content = Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            content = "";
            throw new RuntimeException(e);
        }
        return content;
    }
}
