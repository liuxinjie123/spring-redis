package com.dream.springredis.controller;

import com.dream.springredis.util.JedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class HelloController {
    @Autowired
    private JedisPoolUtil cacheUtil;

    @RequestMapping("/")
    public String hello() {
        String key = "greeting";
        try {
            String value = cacheUtil.getStr(key);
            return value;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
