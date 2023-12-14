package com.rate.demo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rate.demo.model.Rate;

import org.springframework.jdbc.core.JdbcTemplate;

@Repository
public class RateDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Boolean insertRate(List<String> rateString) {
        // Rate rateList = new Rate();
        Rate rate = new Rate();

        String sql = "INSERT INTO rate (Date, USD_NTD, RMB_USD,EUR_USD,USD_JPY,GBP_USD,AUD_USD,USD_HKD,USD_RMB,USD_ZAR,NZD_USD) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try {
            for (int i = 11; i <= rateString.size(); i += 11) {
                rate.setDate(rateString.get(i));
                rate.setUSD_NTD(rateString.get(i + 1));
                rate.setRMB_USD(rateString.get(i + 2));
                rate.setEUR_USD(rateString.get(i + 3));
                rate.setUSD_JPY(rateString.get(i + 4));
                rate.setGBP_USD(rateString.get(i + 5));
                rate.setAUD_USD(rateString.get(i + 6));
                rate.setUSD_HKD(rateString.get(i + 7));
                rate.setUSD_RMB(rateString.get(i + 8));
                rate.setUSD_ZAR(rateString.get(i + 9));
                rate.setNZD_USD(rateString.get(i + 10));

                jdbcTemplate.update(sql, rate.getDate(),
                        rate.getUSD_NTD(), rate.getRMB_USD(),
                        rate.getEUR_USD(), rate.getUSD_JPY(),
                        rate.getGBP_USD(), rate.getAUD_USD(),
                        rate.getUSD_HKD(), rate.getUSD_RMB(),
                        rate.getUSD_ZAR(), rate.getNZD_USD());
            }
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public void deleteDate() {
        String sql = "TRUNCATE TABLE RATE";
        jdbcTemplate.execute(sql);
    }
}
