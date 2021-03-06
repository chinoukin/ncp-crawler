package com.wisea.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.wisea.JsonMapper;
import com.wisea.TreeUtils;
import com.wisea.entity.TreeNode;
import com.wisea.service.ItemTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemTypeController {

    public final static Logger logger = LoggerFactory.getLogger(ItemTypeController.class);
    public final static String LOGIN_INFO_KEY = "JSESSIONID=%s";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ItemTypeService itemTypeService;

    @RequestMapping("/type")
    public String listAll(@RequestParam(value = "loginSessionId") String loginSessionId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        // 登录信息从浏览器上登录后复制下来
        httpHeaders.add("Cookie", String.format(LOGIN_INFO_KEY, loginSessionId));
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("treeType", Collections.singletonList("itemType"));
        map.put("fcLibType", Collections.singletonList("SHOWIN_ITEM_LIB"));

        HttpEntity httpEntity = new HttpEntity(map, httpHeaders);

        ResponseEntity<String> exchange = restTemplate.exchange("https://www.sdtdata.com/fx/fc/selTreeType.jsp", HttpMethod.POST, httpEntity, String.class);
        String responseBody = exchange.getBody();

        String tmpDom = "<textarea name=\"treeNodeJson\" id=\"treeNodeJson\" style=\"display: none\">";
        String jsonData = responseBody.substring(responseBody.indexOf(tmpDom)).replace(tmpDom, "");
        jsonData = jsonData.substring(0, jsonData.indexOf("</textarea>"));

        JsonMapper jsonMapper = new JsonMapper();
        JavaType collectionType = jsonMapper.createCollectionType(List.class, TreeNode.class);
        List<TreeNode> tree = jsonMapper.fromJson(jsonData, collectionType);
        List<TreeNode> treeNodes = TreeUtils.structureTree(tree);

        int fromIndex = 0;
        int toIndex = 100;
        while (true) {
            List<TreeNode> dataList = treeNodes.subList(fromIndex, toIndex);
            itemTypeService.save(dataList);
            fromIndex += 100;
            toIndex += 100;
            if (toIndex > treeNodes.size()) {
                toIndex = treeNodes.size();
            }
            if (fromIndex >= treeNodes.size()) {
                break;
            }
        }

        return jsonData;
    }


}
