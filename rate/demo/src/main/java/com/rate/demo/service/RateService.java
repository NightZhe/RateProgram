package com.rate.demo.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.opencsv.exceptions.CsvException;
import com.rate.demo.model.Rate;

public interface RateService {

    public Boolean rateList() throws FileNotFoundException, IOException, CsvException;

    public Boolean getTask();
}
