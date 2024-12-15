package com.conexa.backend.scheduling.presentation.api.v1.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter ISO_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter SPACE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateStr = p.getText();

        try {
            return LocalDateTime.parse(dateStr, ISO_FORMAT);
        } catch (DateTimeParseException ex1) {
            try {
                return LocalDateTime.parse(dateStr, SPACE_FORMAT);
            } catch (DateTimeParseException ex2) {
                throw new RuntimeException("Invalid date format. Use 'yyyy-MM-dd HH:mm:ss' or ISO-8601 'yyyy-MM-dd'T'HH:mm:ss'");
            }
        }
    }
}

