package com.github.brunoroberto.urlshortener.server.validator;

import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlRequest;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;

@Component
public class ShortUrlRequestValidator {

    private final ShortUrlRequest shortUrlRequest;

    public ShortUrlRequestValidator(ShortUrlRequest shortUrlRequest) {
        this.shortUrlRequest = shortUrlRequest;
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
        if (expireTimeInHours <= 0 && expireTimeInHours > default)
    }

}
