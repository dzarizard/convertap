package com.pawlik.convertap.dto;

import java.math.BigDecimal;
import java.util.List;

public class RateResponse {

    private String table;
    private String currency;
    private String code;
    private List<Rate> rates;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }


    public static class Rate {
        private String no;
        private String effectiveDate;
        private BigDecimal mid;


        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getEffectiveDate() {
            return effectiveDate;
        }

        public void setEffectiveDate(String effectiveDate) {
            this.effectiveDate = effectiveDate;
        }

        public BigDecimal getMid() {
            return mid;
        }

        public void setMid(BigDecimal mid) {
            this.mid = mid;
        }
    }
}