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
        Boolean rateList = rateService.rateList();
        if (rateList != null) {
            map.put("date", rateList);
            map.put("message", "success");
        } else {
            map.put("message", "error");
        }
        return map;

    }
}
