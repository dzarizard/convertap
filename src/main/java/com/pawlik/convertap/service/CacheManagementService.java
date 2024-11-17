package com.pawlik.convertap.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CacheManagementService {

    private final RateService rateService;

    public CacheManagementService(RateService rateService) {
        this.rateService = rateService;
    }

    @Scheduled(cron = "0 0 12 * * ?", zone = "Europe/Warsaw")
    @CacheEvict(value = "exchangeRates", allEntries = true)
    public void clearExchangeRateCache() {
        System.out.println("Clearing exchange rate cache...");
    }

    @Scheduled(cron = "0 1 12 * * ?", zone = "Europe/Warsaw")
    public void prefetchExchangeRates() {
        rateService.getExchangeUSDRate();
        System.out.println("Prefetched exchange rates into cache...");
    }
}