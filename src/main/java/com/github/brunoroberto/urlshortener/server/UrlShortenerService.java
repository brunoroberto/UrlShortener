package com.github.brunoroberto.urlshortener.server;

import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlRequest;
import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlResponse;
import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlResponseGenerator;
import com.github.brunoroberto.urlshortener.server.error.ShortUrlNotFoundException;
import com.github.brunoroberto.urlshortener.server.model.ShortUrl;
import com.github.brunoroberto.urlshortener.server.model.ShortUrlGenerator;
import com.github.brunoroberto.urlshortener.server.repository.ShortUrlRepository;
import com.github.brunoroberto.urlshortener.server.validator.ShortUrlRequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class UrlShortenerService {

    private Logger logger = LoggerFactory.getLogger(UrlShortenerService.class);

    private final ShortUrlRepository shortUrlRepository;
    private final ShortUrlRequestValidator shortUrlRequestValidator;
    private final UrlIDGenerator urlIDGenerator;

    public UrlShortenerService(ShortUrlRepository shortUrlRepository, ShortUrlRequestValidator shortUrlRequestValidator,
                               UrlIDGenerator urlIDGenerator) {
        this.shortUrlRepository = shortUrlRepository;
        this.shortUrlRequestValidator = shortUrlRequestValidator;
        this.urlIDGenerator = urlIDGenerator;
    }

    public ShortUrlResponse createShortUrl(ShortUrlRequest shortUrlRequest) {
        try {
            logger.info("Preparing to create a short url");
            validateShortUrlRequest(shortUrlRequest);
            ShortUrl shortUrl = ShortUrlGenerator.generate(shortUrlRequest);
            shortUrl.setUrlID(urlIDGenerator.generate());
            shortUrl = shortUrlRepository.save(shortUrl);
            logger.info("New short url generated: url: {}, urlID: {} expires in {}", shortUrl.getOriginalUrl(),
                    shortUrl.getUrlID(), shortUrl.getExpireOn().truncatedTo(ChronoUnit.SECONDS));
            return ShortUrlResponseGenerator.generate(shortUrl);
        } catch (IllegalArgumentException e) {
            logger.error("Could not validate the request", e);
            throw e;
        } catch (Exception e) {
            logger.error("Something unexpected happened :(", e);
            throw e;
        }
    }

    private void validateShortUrlRequest(ShortUrlRequest shortUrlRequest) {
        logger.info("Validating short url request");
        shortUrlRequestValidator.validate(shortUrlRequest);
    }

    public String getOriginalUrl(String urlID) {
        logger.info("Preparing to retrieve original url for urlID: {}", urlID);
        try {
            ShortUrl shortUrl = shortUrlRepository.findByUrlID(urlID);
            if (shortUrl == null) {
                throw new ShortUrlNotFoundException("short url not found");
            }
            deleteIfExpired(shortUrl);
            logger.info("Returning original url {}", shortUrl.getOriginalUrl());
            return shortUrl.getOriginalUrl();
        } catch (Exception e) {
            logger.error("Could not retrieve original url for urlID: {}", urlID, e);
            throw e;
        }
    }

    private void deleteIfExpired(ShortUrl shortUrl) {
        logger.info("Verifying if the short url expired");
        if (isExpired(shortUrl)) {
            logger.info("Short url {} - {} expired. Preparing to delete it", shortUrl.getUrlID(), shortUrl.getOriginalUrl());
            deleteShortUrl(shortUrl);
            return;
        }
        logger.info("Short url {} - {} still valid - expires on {}", shortUrl.getUrlID(), shortUrl.getOriginalUrl(),
                shortUrl.getExpireOn());
    }

    private void deleteShortUrl(ShortUrl shortUrl) {
        shortUrlRepository.delete(shortUrl);
        logger.info("Short url {} - {} deleted", shortUrl.getUrlID(), shortUrl.getOriginalUrl());
    }

    private boolean isExpired(ShortUrl shortUrl) {
        return LocalDateTime.now().isAfter(shortUrl.getExpireOn());
    }

}
