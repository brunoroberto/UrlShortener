package com.github.brunoroberto.urlshortener.server.error;

public class ShortUrlNotFoundException extends RuntimeException {

    public ShortUrlNotFoundException(String msg) {
        super(msg);
    }

}
