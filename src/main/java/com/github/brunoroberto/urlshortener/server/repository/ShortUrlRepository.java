package com.github.brunoroberto.urlshortener.server.repository;

import com.github.brunoroberto.urlshortener.server.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    ShortUrl findBySlug(String slug);

}
