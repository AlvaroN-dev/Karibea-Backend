package com.microservice.shopcart.infrastructure.scheduler;

import com.microservice.shopcart.domain.port.in.ExpireCartPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled task for expiring inactive shopping carts.
 * Runs periodically to clean up carts that have exceeded their expiration time.
 */
@Component
@EnableScheduling
public class CartExpirationScheduler {

    private static final Logger log = LoggerFactory.getLogger(CartExpirationScheduler.class);

    private final ExpireCartPort expireCartPort;

    public CartExpirationScheduler(ExpireCartPort expireCartPort) {
        this.expireCartPort = expireCartPort;
    }

    /**
     * Runs every hour to expire inactive carts.
     * Cron: 0 0 * * * * (every hour at minute 0)
     */
    @Scheduled(cron = "${cart.expiration.cron:0 0 * * * *}")
    public void expireInactiveCarts() {
        log.info("Starting scheduled cart expiration job");
        
        try {
            int expiredCount = expireCartPort.expireInactiveCarts();
            log.info("Cart expiration job completed. Expired {} carts", expiredCount);
        } catch (Exception e) {
            log.error("Cart expiration job failed: {}", e.getMessage(), e);
        }
    }
}
