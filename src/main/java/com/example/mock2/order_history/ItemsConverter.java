package com.example.mock2.order_history;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.Map;

@Converter
public class ItemsConverter implements AttributeConverter<Map<String, Integer>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String convertToDatabaseColumn(Map<String, Integer> stringIntegerMap) {
        String itemsInfo = null;
        try{
            itemsInfo = objectMapper.writeValueAsString(stringIntegerMap);
        } catch (final JsonProcessingException e){
            System.out.println(e.getMessage());
        }
        return itemsInfo;
    }

    @Override
    public Map<String, Integer> convertToEntityAttribute(String s) {
        Map<String, Integer> items = null;
        try {
            items = objectMapper.readValue(s, new TypeReference<Map<String, Integer>>() {});
        } catch (final IOException e){
            System.out.println(e.getMessage());
        }
        return items;
    }
}
