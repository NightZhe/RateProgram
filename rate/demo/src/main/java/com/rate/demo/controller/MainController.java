package com.rate.demo.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.opencsv.exceptions.CsvException;
import com.rate.demo.service.RateService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @Autowired
    RateService rateService;

    @RequestMapping("/home")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping("/getRate")
    @ResponseBody
    public Map<String, Object> getRate() throws FileNotFoundException, IOException, CsvException {
        Map<String, Object> map = new HashMap<String, Object>();
        Boolean rateList = rateService.getTask();
        // Boolean rateList = rateService.rateList();
        if (rateList != null) {
            map.put("date", rateList);
            map.put("message", "success");
        } else {
            map.put("message", "error");
        }
        System.out.println(map.get("message"));
        return map;

    }

    @RequestMapping("/currencyid")
    @ResponseBody
    public Map<String, Object> getCurrencyDownList() {
        Map<String, Object> map = new HashMap<String, Object>();
        List currencyList = rateService.currencyList();

        if (currencyList != null) {
            map.put("data", currencyList);
            map.put("messaage", "sueccess");
        } else {
            map.put("message", "error");
        }

        return map;
    }

    @RequestMapping("/convertRate")
    @ResponseBody
    public Map<String, Object> convertRate(@RequestParam String downSelectValue, String targetCurrencyValue) {
        System.out.println("downSelectValue: " + downSelectValue);
        System.out.println("targetCurrencyValue: " + targetCurrencyValue);
        Map<String, Object> map = new HashMap<String, Object>();
        List rateList = rateService.converRate(downSelectValue, targetCurrencyValue);
        if (rateList != null) {
            map.put("data", rateList);
            map.put("message", "sucess");
        } else {
            map.put("message", "error");
        }
        return map;

    }

    @RequestMapping("/getRateList")
    @ResponseBody
    public Map<String, Object> getRateList() {
        Map<String, Object> map = new HashMap<String, Object>();
        List rateList = rateService.getRateList();
        if (rateList != null) {
            map.put("data", rateList);
            map.put("message", "success");

        } else {
            map.put("message", "error");
        }
        return map;
    }

}
