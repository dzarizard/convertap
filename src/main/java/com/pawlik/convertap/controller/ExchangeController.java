package com.pawlik.convertap.controller;

import com.pawlik.convertap.service.ExchangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @PostMapping("/{identifier}/{fromCurrency}/{toCurrency}/{amount}")
    public ResponseEntity<BigDecimal> exchange(@PathVariable String identifier,
                                               @PathVariable String fromCurrency,
                                               @PathVariable String toCurrency,
                                               @PathVariable BigDecimal amount) {
        BigDecimal convertedAmount = exchangeService.exchange(identifier, fromCurrency, toCurrency, amount);
        return ResponseEntity.ok(convertedAmount);
    }


}
