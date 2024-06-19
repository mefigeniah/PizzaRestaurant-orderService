package com.mefigenia.orderService.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;

@Convert
public class ToppingsAttributeConverter implements AttributeConverter<ToppingObject, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String convertToDatabaseColumn(ToppingObject toppingObject) {
        try {
            return objectMapper.writeValueAsString(toppingObject);
        } catch (JsonProcessingException jpe) {
            System.out.println("Cannot convert Address into JSON");
            return null;
        }
    }

    @Override
    public ToppingObject convertToEntityAttribute(String value) {
        try {
            return objectMapper.readValue(value, ToppingObject.class);
        } catch (JsonProcessingException e) {
            System.out.println("Cannot convert JSON into Address");
            return null;
        }
    }
}
