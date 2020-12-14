package com.wisea.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class SecurityController {
    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/auth_code")
    public String authCode(@RequestParam("code") String code) {
        return code;
    }

}
