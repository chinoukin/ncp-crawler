package com.wisea.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class AbstractController {

    protected String getMatchValue(String regex, String text) {
        String[] aroundTexts = regex.split("\\.\\*\\?");
        Pattern r = Pattern.compile(regex);
        Matcher m = r.matcher(text);
        if (m.find()) {
            String res = m.group();
            for (String around : aroundTexts) {
                res = res.replace(around, "");
            }
            return res;
        }
        return "";
    }

    @Autowired
    Environment environment;

    @GetMapping("/env")
    public Map<String, Object> env() {
        System.out.println(environment.getDefaultProfiles()[0]);
        StandardEnvironment standardEnvironment = (StandardEnvironment) environment;
        MutablePropertySources mutablePropertySources = standardEnvironment.getPropertySources();
        Iterator<PropertySource<?>> iterator = mutablePropertySources.iterator();
        //Map<String, Object> map = new HashMap<>();
        Map<String, Object> map = new TreeMap<>();
        while (iterator.hasNext()) {
            PropertySource<?> propertySource = iterator.next();
            if (propertySource instanceof OriginTrackedMapPropertySource) {
                OriginTrackedMapPropertySource originTrackedMapPropertySource = (OriginTrackedMapPropertySource) propertySource;
                Map<String, Object> source = originTrackedMapPropertySource.getSource();
                for (String key : source.keySet()) {
                    OriginTrackedValue originTrackedValue = (OriginTrackedValue) source.get(key);
                    map.put(key, originTrackedValue.getValue());
                }
            }
        }
        StringBuffer textBuff = new StringBuffer();
        // 格式输出
        for (Map.Entry entry : map.entrySet()) {
            String text = "--" + entry.getKey() + "=" + entry.getValue() + " ";
            System.out.println(text + "\\");
            textBuff.append(text);
        }
        map.put("text", textBuff.toString());
        return map;
    }


    @RequestMapping("/createException")
    public String exceptionTest() {
        throw new RuntimeException("Got a exception for test");
    }
}
