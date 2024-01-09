package com.rate.demo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rate.demo.dao.RateDao;
import com.rate.demo.model.CurrencyName;
import com.rate.demo.model.Rate;
import com.fasterxml.jackson.core.StreamReadConstraints.Builder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

@Service
public class RateServiceImpl implements RateService {
    @Autowired
    private RateDao rateDao;
    @Autowired
    private Rate rate;

    @Override
    public Boolean rateList() throws IOException, CsvException {
        rateDao.deleteDate();
        getcsv();
        CSVReader readerCsvout = new CSVReader(new FileReader("/Users/ben/Documents/RateProgram/rate/outputline.csv"));
        List<String[]> linesout = readerCsvout.readAll();
        List<String> rateString = new ArrayList<>();
        // Assuming the first row is header, start from index 1
        for (String[] array : linesout) {
            // 遍历每个 String[]
            for (String element : array) {
                rateString.add(element);
            }

        }

        System.out.println("------------");
        System.out.println(rateString.size());

        // insert 用for 一筆一筆打;
        // if (rateDao.insertRate(rateString)) {
        // System.out.println("true");
        // return true;
        // } else {
        // System.out.println("false");
        // return false;
        // }

        // 把rateString 轉換成 List<Rate> insertdata 供jdbcTemplate.batchUpdate 使用
        List<Rate> insertdata = converToRAaetData(rateString);

        if (rateDao.bathInsert(insertdata)) {
            System.out.println("success");
            return true;
        } else {
            System.err.println("error");
            return false;
        }
    }

    public List<Rate> converToRAaetData(List<String> rateString) {
        List<Rate> ratedata = new ArrayList<>();
        for (int i = 11; i < rateString.size(); i += 11) {
            Rate rate = new Rate();
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
            ratedata.add(rate);
        }

        return ratedata;
    }

    public void getcsv() {
        try {
            disableSSLVerification();
            URL url = new URL(
                    "https://openapi.taifex.com.tw/v1/DailyForeignExchangeRates");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                    // System.out.println(line);
                }

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsontree = objectMapper.readTree(response.toString());

                CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder();
                JsonNode firstObject = jsontree.elements().next();
                firstObject.fieldNames().forEachRemaining(fieldName -> {
                    csvSchemaBuilder.addColumn(fieldName);
                });
                CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();
                CsvMapper csvMapper = new CsvMapper();
                csvMapper.writerFor(JsonNode.class).with(csvSchema)
                        .writeValue(new File("/Users/ben/Documents/RateProgram/rate/orderline.csv"), jsontree);

                CSVReader readerCsv = new CSVReader(
                        new FileReader("/Users/ben/Documents/RateProgram/rate/orderline.csv"));
                List<String[]> lines = readerCsv.readAll();
                readerCsv.close();

                String[] newHeadre = { "Date", "USD_NTD", "RMB_USD", "EUR_USD", "USD_JPY", "GBP_USD", "AUD_USD",
                        "USD_HKD", "USD_RMB", "USD_ZAR", "NZD_USD" };
                lines.set(0, newHeadre);

                CSVWriter csvWriter = new CSVWriter(
                        new FileWriter("/Users/ben/Documents/RateProgram/rate/outputline.csv"));
                csvWriter.writeAll(lines);
                csvWriter.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private static void disableSSLVerification() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }
        } };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }

    @Override
    public Boolean getTask() {

        Timer timer = new Timer();
        MyTaks task = new MyTaks();

        try {
            timer.schedule(task, 5000, 10000);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

    }

    // count 計算次數，可控制taks跑幾次
    int count = 0;

    class MyTaks extends TimerTask {
        @Override
        public void run() {
            try {
                rateList();
                count++;
                if (count > 5) {
                    cancel();
                    System.out.println("超過五次停止");
                } else {
                    System.out.println("跑了幾次count:" + count);
                }
            } catch (IOException | CsvException e) {
                e.printStackTrace();
                System.out.println(e);
            }
        }
    }

    @Override
    public List<CurrencyName> currencyList() {
        return rateDao.currencyList();
    }

    @Override
    public List converRate(String downSelectValue, String targetCurrencyValue) {
        String unit = "";
        if (downSelectValue.equals("1") && targetCurrencyValue.equals("10")) {
            unit = "USD_NTD";
        } else {
            return null;
        }

        List currency = rateDao.converRate(unit);
        return currency;
    }

    @Override
    public List getRateList() {
        return rateDao.getRateList();
    }
}
