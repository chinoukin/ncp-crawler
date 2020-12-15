package com.wisea.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
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

    @GetMapping(value = {"/env", "/env1", "/env2"})
    public Map<String, Object> env() {
        System.out.println(environment.getDefaultProfiles()[0]);

        StandardEnvironment standardEnvironment = (StandardEnvironment) environment;
        MutablePropertySources mutablePropertySources = standardEnvironment.getPropertySources();
        Iterator<PropertySource<?>> iterator = mutablePropertySources.iterator();
        //Map<String, Object> map = new HashMap<>();
        Map<String, Object> map = new TreeMap<>();

        OriginTrackedMapPropertySource originTrackedMapPropertySource = null;
        SimpleCommandLinePropertySource simpleCommandLinePropertySource = null;

        while (iterator.hasNext()) {
            PropertySource<?> propertySource = iterator.next();
            if (propertySource instanceof OriginTrackedMapPropertySource) {
                originTrackedMapPropertySource = (OriginTrackedMapPropertySource) propertySource;
            }
            // 命令行参数，判断有没有覆盖配置文件参数
            if (propertySource instanceof SimpleCommandLinePropertySource) {
                simpleCommandLinePropertySource = (SimpleCommandLinePropertySource) propertySource;
            }
        }

        Map<String, Object> source = originTrackedMapPropertySource.getSource();
        for (String key : source.keySet()) {
            OriginTrackedValue originTrackedValue = (OriginTrackedValue) source.get(key);
            map.put(key, originTrackedValue.getValue());
        }

        String[] names = simpleCommandLinePropertySource.getPropertyNames();
        for (String name : names) {
            String value = simpleCommandLinePropertySource.getProperty(name);
            if (map.containsKey(name)) {
                System.out.println("参数[" + name + "]被命令行参数覆盖,原值:" + map.get(name) + ";新值:" + value);
                map.put(name, value);
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
