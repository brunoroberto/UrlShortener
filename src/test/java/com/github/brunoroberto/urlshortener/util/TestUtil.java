package com.github.brunoroberto.urlshortener.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

public class TestUtil {

    public static String toJsonString(Object request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(request);
    }

    public static Object toObject(MvcResult content, Class objectClass) {
        return null;
    }

}
