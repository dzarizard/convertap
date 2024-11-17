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
    @CacheEvict(value = "exchangeUSDRate", allEntries = true)
    public void clearExchangeUSDRateCache() {
    }

    @Scheduled(cron = "0 1 12 * * ?", zone = "Europe/Warsaw")
    public void fetchExchangeUSDRate() {
        rateService.getExchangeUSDRate();
    }
}