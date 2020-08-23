package com.github.brunoroberto.urlshortener.server.validator;

import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlRequest;
import com.github.brunoroberto.urlshortener.server.config.UrlShortenerConfig;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;

@Component
public class ShortUrlRequestValidator {

    private final UrlShortenerConfig urlShortenerConfig;

    public ShortUrlRequestValidator(UrlShortenerConfig urlShortenerConfig) {
        this.urlShortenerConfig = urlShortenerConfig;
    }

    public void validate(ShortUrlRequest shortUrlRequest) {
        validateNotNull(shortUrlRequest);
        validateUrl(shortUrlRequest);
        validateExpireTime(shortUrlRequest);
    }

    private void validateNotNull(ShortUrlRequest shortUrlRequest) {
        if (shortUrlRequest == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
    }

    private void validateUrl(ShortUrlRequest shortUrlRequest) {
        if (!UrlValidator.getInstance().isValid(shortUrlRequest.getUrl())) {
            throw new IllegalArgumentException("Invalid URL");
        }
    }

    private void validateExpireTime(ShortUrlRequest shortUrlRequest) {
        int expireTimeInHours = shortUrlRequest.getExpireTimeInHours();
        if (expireTimeInHours <= 0 || expireTimeInHours > urlShortenerConfig.getExpireTimeInHours()) {
            String errorMsg = String.format("Expire time must be between 1 and %d", urlShortenerConfig.getExpireTimeInHours());
            throw new IllegalArgumentException(errorMsg);
        }
    }

}
