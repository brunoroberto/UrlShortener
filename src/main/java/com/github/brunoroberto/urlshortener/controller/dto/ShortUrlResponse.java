package com.github.brunoroberto.urlshortener.controller.dto;

import java.time.LocalDateTime;

public class ShortUrlResponse {

    private String urlID;
    private LocalDateTime expireOn;

    public String getUrlID() {
        return urlID;
    }

    public void setUrlID(String urlID) {
        this.urlID = urlID;
    }

    public LocalDateTime getExpireOn() {
        return expireOn;
    }

    public void setExpireOn(LocalDateTime expireOn) {
        this.expireOn = expireOn;
    }
}
