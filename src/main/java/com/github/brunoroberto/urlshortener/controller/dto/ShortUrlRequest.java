package com.github.brunoroberto.urlshortener.controller.dto;

public class ShortUrlRequest {

    private String url;
    private int expireTimeInHours;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getExpireTimeInHours() {
        return expireTimeInHours;
    }

    public void setExpireTimeInHours(int expireTimeInHours) {
        this.expireTimeInHours = expireTimeInHours;
    }
}
