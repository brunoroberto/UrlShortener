package com.github.brunoroberto.urlshortener.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtil {

    private static ObjectMapper createObjectMapperWithJavaTimeModule() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    public static String toJsonString(Object request) throws JsonProcessingException {
        ObjectMapper objectMapper = createObjectMapperWithJavaTimeModule();
        return objectMapper.writeValueAsString(request);
    }

    public static Object toObject(MvcResult content, Class objectClass) throws UnsupportedEncodingException, JsonProcessingException {
        ObjectMapper objectMapper = createObjectMapperWithJavaTimeModule();
        return objectMapper.readValue(content.getResponse().getContentAsString(), objectClass);
    }

    public static void assertExpireTime(LocalDateTime expected, LocalDateTime actual) {
        assertEquals(expected.truncatedTo(ChronoUnit.MINUTES), actual.truncatedTo(ChronoUnit.MINUTES));
    }

}
