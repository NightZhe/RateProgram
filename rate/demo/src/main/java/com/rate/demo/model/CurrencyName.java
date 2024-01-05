package com.rate.demo.model;

public class CurrencyName {
    private int id;
    private String unit;

    public CurrencyName() {
        super();
    }

    public CurrencyName(int id, String unit) {
        this.id = id;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}
