package com.github.brunoroberto.urlshortener.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlShortenerConfig {

    @Value("${urlShortener.expireTimeInHours:48}")
    private int expireTimeInHours;

    public int getExpireTimeInHours() {
        return expireTimeInHours;
    }

}
