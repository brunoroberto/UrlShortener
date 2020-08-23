package com.github.brunoroberto.urlshortener.server.schedule;

import com.github.brunoroberto.urlshortener.server.repository.ShortUrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CleanUpTask {

    private Logger logger = LoggerFactory.getLogger(CleanUpTask.class);

    private final ShortUrlRepository shortUrlRepository;

    public CleanUpTask(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    @Scheduled(cron = "${urlShortener.cleanUpScheduleTime}")
    public void cleanUp() {
        logger.info("Clean up time - Preparing to clean expired short urls");
        try {
            shortUrlRepository.deleteByExpireOnLessThan(LocalDateTime.now());
            logger.info("Clean up finished");
        } catch (Exception e) {
            logger.error("Could not clean up expired short urls. Let's try again on the next clean up task", e);
        }
    }

}
