package com.pawlik.convertap.service;

import com.pawlik.convertap.dto.RateResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class RateService {

    private final RestTemplate restTemplate;

    public RateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Cacheable("exchangeRates")
    public BigDecimal getExchangeUSDRate() {
        String url = "https://api.nbp.pl/api/exchangerates/rates/A/USD/";
        try {
            ResponseEntity<RateResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    RateResponse.class
            );
            return Objects.requireNonNull(response.getBody()).getRates().getFirst().getMid();

        } catch (RestClientException e) {
            throw new RuntimeException("Failed to fetch exchange rate: " + e.getMessage(), e);
        }
    }

}