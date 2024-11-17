package com.pawlik.convertap.controller;

import com.pawlik.convertap.service.RateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/rates")
public class RateController {

    private final RateService rateService;

    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping("/getUSD")
    public BigDecimal getUSDRate() {
        return rateService.getExchangeUSDRate();
    }
}