package com.wisea.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SecurityController {
    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/auth_code")
    public String authCode(@RequestParam("code") String code) {
        return code;
    }

    /**
     * 受限于【当https访问时，前端ajax无法重定向至http的请求上来】,不直接访问/oauth/authorize,用/getAuthorizationCode代替
     * @param request
     * @param clientId
     * @return
     */
    @RequestMapping("/getAuthorizationCode")
    public String getAuthorizationCode(HttpServletRequest request, @RequestParam("clientId") String clientId) {
        System.out.println(request.getRequestURL().toString().replace("/getAuthorizationCode", "/oauth/authorize"));
        HttpHeaders httpHeaders = new HttpHeaders();
        // 登录信息从浏览器上登录后复制下来
        httpHeaders.add("Authorization", "Basic Y2hpbm91a2luOjEyMzQ1Ng==");// chinoukin:123456
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.put("response_type", Collections.singletonList("code"));
//        map.put("client_id", Collections.singletonList("cli2"));
        HttpEntity httpEntity = new HttpEntity(map, httpHeaders);

        String params = String.format("?response_type=code&client_id=%s", clientId);
        ResponseEntity<String> exchange = restTemplate.
                exchange(request.getRequestURL().toString().replace("/getAuthorizationCode", "/oauth/authorize")+ params,
                        HttpMethod.GET, httpEntity, String.class);
        String responseBody = exchange.getBody();
        return responseBody;
    }

}
