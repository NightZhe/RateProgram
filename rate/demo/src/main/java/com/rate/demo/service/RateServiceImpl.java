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

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rate.demo.dao.RateDao;
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

    @Override
    public Boolean rateList() throws IOException, CsvException {
        rateDao.deleteDate();
        getcsv();
        CSVReader readerCsvout = new CSVReader(new FileReader("/Users/ben/Documents/rate/outputline.csv"));
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
        if (rateDao.insertRate(rateString)) {
            System.out.println("true");
            return true;
        } else {
            System.out.println("fales");
            return false;
        }
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
                    System.out.println(line);
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
                        .writeValue(new File("/Users/ben/Documents/rate/ordreline.csv"), jsontree);

                CSVReader readerCsv = new CSVReader(new FileReader("/Users/ben/Documents/rate/ordreline.csv"));
                List<String[]> lines = readerCsv.readAll();
                readerCsv.close();

                String[] newHeadre = { "Date", "USD_NTD", "RMB_USD", "EUR_USD", "USD_JPY", "GBP_USD", "AUD_USD",
                        "USD_HKD", "USD_RMB", "USD_ZAR", "NZD_USD" };
                lines.set(0, newHeadre);

                CSVWriter csvWriter = new CSVWriter(new FileWriter("/Users/ben/Documents/rate/outputline.csv"));
                csvWriter.writeAll(lines);
                csvWriter.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    // public static void main(String[] agrs) {
    // try {
    // disableSSLVerification();
    // URL url = new URL(
    // "https://openapi.taifex.com.tw/v1/DailyForeignExchangeRates");
    // HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    // connection.setRequestMethod("GET");

    // int responseCode = connection.getResponseCode();
    // if (responseCode == HttpURLConnection.HTTP_OK) {
    // BufferedReader reader = new BufferedReader(new
    // InputStreamReader(connection.getInputStream()));
    // StringBuilder response = new StringBuilder();
    // String line;

    // while ((line = reader.readLine()) != null) {
    // response.append(line);
    // }

    // System.out.println("Response:" + response.toString());

    // ObjectMapper objectMapper = new ObjectMapper();
    // JsonNode jsonNode = objectMapper.readTree(response.toString());

    // String date = jsonNode.get(0).get("Date").asText();
    // String usdTOnt = jsonNode.get(0).get("USD/NTD").toString();

    // Date today = new Date();
    // Calendar calendar = Calendar.getInstance();
    // calendar.setTime(today);
    // calendar.add(calendar.DAY_OF_MONTH, -1);// 取得今日-1(yesterday);
    // Date yesterday = calendar.getTime();
    // String newNowDate = String.format("%1$tY%1$tm%1$td", yesterday);

    // for (int i = 0; i < jsonNode.size(); i++) {
    // String getDate = (jsonNode.get(i).get("Date").asText()).substring(0, 8);
    // if (getDate.equals(newNowDate)) {
    // System.out.println("getDate:" + jsonNode.get(i).get("Date").asText());
    // String getusdToNtd = jsonNode.get(i).get("USD/NTD").asText();
    // System.out.println("getusdToNtd:" + getusdToNtd);
    // } else {
    // System.out.println("notMatch");
    // }
    // }

    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // }

    // }

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

}
