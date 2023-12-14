package com.rate.demo.model;

import org.springframework.stereotype.Component;

@Component
public class Rate {
    private int id;
    private String Date;
    private String USD_NTD;
    private String RMB_USD;
    private String EUR_USD;
    private String USD_JPY;
    private String GBP_USD;
    private String AUD_USD;
    private String USD_HKD;
    private String USD_RMB;
    private String USD_ZAR;
    private String NZD_USD;

    public Rate() {
        super();
    }

    public Rate(int id, String Date, String USD_NTD, String RMB_USD, String EUR_USD, String USD_JPY, String GBP_USD,
            String AUD_USD,
            String USD_HKD, String USD_RMB, String USD_ZAR, String NZD_USD) {
        this.id = id;
        this.Date = Date;
        this.USD_NTD = USD_NTD;
        this.RMB_USD = RMB_USD;
        this.EUR_USD = EUR_USD;
        this.USD_JPY = USD_JPY;
        this.GBP_USD = GBP_USD;
        this.AUD_USD = AUD_USD;
        this.USD_HKD = USD_HKD;
        this.USD_RMB = USD_RMB;
        this.USD_ZAR = USD_ZAR;
        this.NZD_USD = NZD_USD;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getUSD_NTD() {
        return USD_NTD;
    }

    public void setUSD_NTD(String USD_NTD) {
        this.USD_NTD = USD_NTD;
    }

    public String getRMB_USD() {
        return RMB_USD;
    }

    public void setRMB_USD(String RMB_USD) {
        this.RMB_USD = RMB_USD;
    }

    public String getEUR_USD() {
        return EUR_USD;
    }

    public void setEUR_USD(String EUR_USD) {
        this.EUR_USD = EUR_USD;
    }

    public String getUSD_JPY() {
        return USD_JPY;
    }

    public void setUSD_JPY(String USD_JPY) {
        this.USD_JPY = USD_JPY;
    }

    public String getGBP_USD() {
        return GBP_USD;
    }

    public void setGBP_USD(String GBP_USD) {
        this.GBP_USD = GBP_USD;
    }

    public String getAUD_USD() {
        return AUD_USD;
    }

    public void setAUD_USD(String AUD_USD) {
        this.AUD_USD = AUD_USD;
    }

    public String getUSD_HKD() {
        return USD_HKD;
    }

    public void setUSD_HKD(String USD_HKD) {
        this.USD_HKD = USD_HKD;
    }

    public String getUSD_RMB() {
        return USD_RMB;
    }

    public void setUSD_RMB(String USD_RMB) {
        this.USD_RMB = USD_RMB;
    }

    public String getUSD_ZAR() {
        return USD_ZAR;
    }

    public void setUSD_ZAR(String USD_ZAR) {
        this.USD_ZAR = USD_ZAR;
    }

    public String getNZD_USD() {
        return NZD_USD;
    }

    public void setNZD_USD(String NZD_USD) {
        this.NZD_USD = NZD_USD;
    }

}
