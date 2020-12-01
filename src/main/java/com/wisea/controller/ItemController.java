package com.wisea.controller;

import com.wisea.JsonMapper;
import com.wisea.entity.ItemDetail;
import com.wisea.entity.ItemIndex;
import com.wisea.entity.ItemIndexPage;
import com.wisea.mapper.ItemMapper;
import com.wisea.service.ItemService;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 查限量
 */
@RestController
@RequestMapping("/item")
public class ItemController extends AbstractController {

    public final static Logger logger = LoggerFactory.getLogger(ItemController.class);
    public final static String LOGIN_INFO_KEY = "JSESSIONID=%s";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ItemService itemService;

    @Autowired
    ItemMapper itemMapper;


    /**
     * 查限量-索引入库
     * <p>
     * （需要登录信息，暂时登录信息从浏览器上登录后复制下来，后期改为在此处登录）
     *
     * @return
     */
    @RequestMapping("/index")
    public String itemIndex(@RequestParam(value = "loginSessionId") String loginSessionId,
                            @RequestParam(value = "isSmallRange", defaultValue = "false") boolean isSmallRange) {
        HttpHeaders httpHeaders = new HttpHeaders();
        // 登录信息从浏览器上登录后复制下来
        httpHeaders.add("Cookie", String.format(LOGIN_INFO_KEY, loginSessionId));

        int counts = -1;
        int pageNum = 1;
        List<ItemIndexPage> treatedPages = itemMapper.findTreatedPages();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        while (true) {
            if (isSmallRange) {
                if (pageNum == 11) {
                    break;
                }
            }
            // 判断当前页是否被处理过
            ItemIndexPage itemIndexPage = itemMapper.findPage(pageNum);
            if (itemIndexPage == null) {
                itemIndexPage = new ItemIndexPage();
                itemIndexPage.setId(Long.valueOf(pageNum));
                itemIndexPage.setPageNum(pageNum);
                itemIndexPage.setStatus("0");
                itemMapper.insertItemIndexPage(itemIndexPage);
            }
            if (treatedPages != null && treatedPages.contains(itemIndexPage)) {
                pageNum++;
                continue;
            }

            map.put("pageNum", Collections.singletonList(String.valueOf(pageNum)));
            map.put("groupType", Collections.singletonList("noGroup"));
            map.put("counts", Collections.singletonList(String.valueOf(counts)));
            // 查全部数据(可不指定此参数)或只查中国,若切换此参数的配置则需清空item_index_page表
            //"[{\"field\":\"a.ITEM_COUNTRY\",\"rule\":\"equal\",\"disp\":\"国别\",\"value\":\"中国,中国香港,中国台湾,中国澳门,CAC,日本,欧盟,美国,韩国,加拿大,澳大利亚,新西兰,其它\"}]"
            //if (chinaOnly) {
            //    map.put("searchs", Collections.singletonList("[{\"field\":\"a.ITEM_COUNTRY\",\"rule\":\"equal\",\"disp\":\"国别\",\"value\":\"中国\"}]"));
            //}

            HttpEntity httpEntity = new HttpEntity(map, httpHeaders);


            ResponseEntity<String> exchange = restTemplate.exchange("https://www.sdtdata.com/fx/foodcodex?p=itemLibIndex&s=fmoa&act=doSearchNoGroup", HttpMethod.POST, httpEntity, String.class);
            String responseBody = exchange.getBody();
            if (counts == -1) {
                String regex = "\"counts\":\"\\d+\"";
                Pattern r = Pattern.compile(regex);
                Matcher m = r.matcher(responseBody);
                if (m.find()) {
                    String res = m.group();
                    counts = Integer.parseInt(res.replace("counts", "").replace("\"", "").replace(":", ""));
                    System.out.println(counts);
                }
            }

            String substring = responseBody.substring(responseBody.indexOf("["), responseBody.lastIndexOf("]") + 1);
            if ("[]".equals(substring)) {
                itemMapper.deleteItemIndexPage(itemIndexPage);
                break;
            }

            logger.debug("当前页码:" + pageNum);
            List<Map<String, String>> dataMapList = (List<Map<String, String>>) JsonMapper.fromJsonString(substring, List.class);
            //System.out.println(dataMapList);

            itemService.itemIndex(itemIndexPage, dataMapList);
            pageNum++;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "success";

    }

    /**
     * 查限量-项目详情入库
     *
     * @return
     */
    @RequestMapping("/detail")
    public String itemDetail(@RequestParam(value = "loginSessionId") String loginSessionId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        // 登录信息从浏览器上登录后复制下来
        httpHeaders.add("Cookie", String.format(LOGIN_INFO_KEY, loginSessionId));
        // 查询全部的项目索引库数据,每次查100条
        long startRow = 0L;
        while (true) {
            List<ItemIndex> itemIndexList = itemMapper.findItemIndexList(startRow, 100);
            if (itemIndexList == null || itemIndexList.size() == 0) {
                break;
            }

            List<ItemDetail> itemDetailList = new ArrayList<>();
            for (ItemIndex itemIndex : itemIndexList) {
                if (itemIndex.getStatus().equals("1")) {
                    continue;
                }

                HttpEntity httpEntity = new HttpEntity(httpHeaders);

                String url = String.format("https://www.sdtdata.com/fx/fmoa/itemLibCard/%s.html", itemIndex.getItemId());
                logger.debug("Grab data from " + url);
                ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
                String html = exchange.getBody();

                // 格式化
                html = html.substring(html.indexOf("<div class=\"inner techDetail\">"));
                html = html.substring(0, html.indexOf("<form name=\"feedbackForm\">"));
                StringBuffer htmlBuff = new StringBuffer();
                String[] lines = html.split("\r\n");
                for (String line : lines) {
                    line = line.trim();
                    if (line.startsWith("<div") || line.startsWith("<ul") || line.startsWith("<li")
                            || line.startsWith("<!--") || line.startsWith("</div") || line.startsWith("</ul") || line.startsWith("</li")) {
                        continue;
                    }
                    htmlBuff.append(line);
                }
                html = htmlBuff.toString();
                //System.out.println(html);

                ItemDetail itemDetail = new ItemDetail();
                itemDetail.setId(Math.abs(new Random().nextLong()));
                itemDetail.setItemId(itemIndex.getItemId());
                itemDetail.setItemCountry(itemIndex.getItemCountry());
                // 发布日期
                itemDetail.setPubDate(getMatchValue("<span>发布日期：.*?</span>", html));
                //System.out.println("-------" + pubDate);
                // 实施日期
                itemDetail.setImplDate(getMatchValue("<span>实施日期：.*?</span>", html));
                // 指标
                itemDetail.setIndicators(getMatchValue("<h3>指标信息</h3>" + "<p>.*?</p>", html));
                // 注释
                itemDetail.setComments(getMatchValue("<h3>注释</h3>" + "<p>.*?</p>", html));
                // 产品名称
                itemDetail.setProdName(getMatchValue("<p>产品名称：.*?</p>", html));
                // 产品名称（英文）
                itemDetail.setProdEnsName(getMatchValue("<p>产品名称（英文）：.*?</p>", html));
                // 产品分类
                itemDetail.setProdClass(getMatchValue("<p>产品分类：.*?</p>", html));
                // 判定标准号
                itemDetail.setTsNo(getMatchValue("<p>判定标准号：.*?</p>", html));
                // 判定标准名称
                itemDetail.setTsName(getMatchValue("<p>判定标准名称：.*?</p>", html));
                // 判定标准名称（英文）
                itemDetail.setTsEnsName(getMatchValue("<p>判定标准名称（英文）：.*?</p>", html));
                // 项目名称
                itemDetail.setItemName(getMatchValue("<p>项目名称：.*?</p>", html)
                        .replace("<sup>", "").replace("</sup>", ""));
                // 项目名称（英文）
                itemDetail.setItemEnsName(getMatchValue("<p>项目名称（英文）：.*?</p>", html));
                // 项目分类
                itemDetail.setItemClass(getMatchValue("<p>项目分类：.*?</p>", html));
                // CAS号
                itemDetail.setCasNo(getMatchValue("<p>CAS号：.*?</p>", html));
                // 检测方法号
                itemDetail.setDetecMethodNo(getMatchValue("title=\"查看方法原文\">.*?</a>", html));

                itemDetailList.add(itemDetail);
            }
            if (itemDetailList.size() > 0) {
                itemService.itemDetail(itemDetailList, itemIndexList);
            }
            logger.debug("===========================completed!");
            startRow += 100;
        }
        logger.debug("===========================All completed!");
        return "success";
    }

}
