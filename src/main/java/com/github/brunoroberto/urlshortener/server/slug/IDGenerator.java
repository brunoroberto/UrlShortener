package com.github.brunoroberto.urlshortener.server.slug;

import java.util.UUID;

public class IDGenerator {

    public static String generate() {
        return UUID.randomUUID().toString();
    }

}
