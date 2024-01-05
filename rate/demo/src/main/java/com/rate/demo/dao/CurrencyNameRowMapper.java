package com.rate.demo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.rate.demo.model.CurrencyName;

import io.micrometer.common.lang.Nullable;

public class CurrencyNameRowMapper implements RowMapper {

    @Override
    @Nullable
    public CurrencyName mapRow(ResultSet rs, int rowNum) throws SQLException {
        CurrencyName cn = new CurrencyName();
        cn.setId(rs.getInt("id"));
        cn.setUnit(rs.getString("unit"));
        return cn;
    }

}
