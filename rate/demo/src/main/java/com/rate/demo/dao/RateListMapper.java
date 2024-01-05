package com.rate.demo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.rate.demo.model.CurrencyName;
import com.rate.demo.model.Rate;

import io.micrometer.common.lang.Nullable;

public class RateListMapper implements RowMapper {
    @Override
    @Nullable
    public Rate mapRow(ResultSet rs, int rowNum) throws SQLException {
        Rate rate = new Rate();
        rate.setId(rs.getInt("id"));
        rate.setDate(rs.getString("unit"));
        rate.setDate(rs.getString("unit"));
        return rate;
    }

}
