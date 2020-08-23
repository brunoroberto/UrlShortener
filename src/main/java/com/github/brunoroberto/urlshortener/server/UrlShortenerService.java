package com.github.brunoroberto.urlshortener.server;

import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlRequest;
import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlResponse;
import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlResponseGenerator;
import com.github.brunoroberto.urlshortener.server.model.ShortUrl;
import com.github.brunoroberto.urlshortener.server.model.ShortUrlGenerator;
import com.github.brunoroberto.urlshortener.server.repository.ShortUrlRepository;
import com.github.brunoroberto.urlshortener.server.validator.ShortUrlRequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UrlShortenerService {

    private Logger logger = LoggerFactory.getLogger(UrlShortenerService.class);

    private ShortUrlRepository shortUrlRepository;

    public UrlShortenerService(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    public ShortUrlResponse createShortUrl(ShortUrlRequest shortUrlRequest) {
        logger.info("Preparing to create a short url");
        validateShortUrlRequest(shortUrlRequest);
        ShortUrl shortUrl = ShortUrlGenerator.generate(shortUrlRequest);
        shortUrl = shortUrlRepository.save(shortUrl);
        logger.info("New short url generate: url: {}, urlID: {} expires in {} hours", shortUrl.getOriginalUrl(),
                shortUrl.getUrlID(), shortUrl.getExpireOn());
        return ShortUrlResponseGenerator.generate(shortUrl);
    }

    private void validateShortUrlRequest(ShortUrlRequest shortUrlRequest) {
        logger.info("Validating short url request");
        ShortUrlRequestValidator shortUrlRequestValidator = new ShortUrlRequestValidator(shortUrlRequest);
        shortUrlRequestValidator.notNull().validUrl().isValidExpireTime();
    }

}
