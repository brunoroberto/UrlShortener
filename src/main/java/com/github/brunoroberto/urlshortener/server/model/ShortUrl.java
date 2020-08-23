package com.github.brunoroberto.urlshortener.server.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "short_urls", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id"}), @UniqueConstraint(columnNames = {"url_id"})
})
public class ShortUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url_id")
    private String urlID;

    @Column(name = "original_url")
    private String originalUrl;

    @Column(name = "expire_on")
    private LocalDateTime expireOn;

    @CreationTimestamp
    private Instant createdOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlID() {
        return urlID;
    }

    public void setUrlID(String urlID) {
        this.urlID = urlID;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public LocalDateTime getExpireOn() {
        return expireOn;
    }

    public void setExpireOn(LocalDateTime expireOn) {
        this.expireOn = expireOn;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }
}
