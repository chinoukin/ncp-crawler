package com.wisea.controller;

import com.wisea.JsonMapper;
import com.wisea.entity.TsDetail;
import com.wisea.entity.TsIndex;
import com.wisea.entity.TsIndexPage;
import com.wisea.mapper.TsMapper;
import com.wisea.service.TsService;
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

/**
 * 查标准
 */
@RestController
@RequestMapping("/ts")
public class TsController extends AbstractController {
    public final static Logger logger = LoggerFactory.getLogger(TsController.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    TsService tsService;

    @Autowired
    TsMapper tsMapper;


    /**
     * 标准-索引入库
     * <p>
     * （需要登录信息，暂时登录信息从浏览器上登录后复制下来，后期改为在此处登录）
     *
     * @return
     */
    @RequestMapping("/index")
    public String tsIndex(@RequestParam(value = "isSmallRange", defaultValue = "false") boolean isSmallRange) {
        HttpHeaders httpHeaders = new HttpHeaders();
        int pageNum = 1;
        List<TsIndexPage> treatedPages = tsMapper.findTreatedPages();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        while (true) {
            if (isSmallRange) {
                if (pageNum == 11) {
                    break;
                }
            }
            // 判断当前页是否被处理过
            TsIndexPage tsIndexPage = tsMapper.findPage(pageNum);
            if (tsIndexPage == null) {
                tsIndexPage = new TsIndexPage();
                tsIndexPage.setId(Long.valueOf(pageNum));
                tsIndexPage.setPageNum(pageNum);
                tsIndexPage.setStatus("0");
                tsMapper.insertTsIndexPage(tsIndexPage);
            }
            if (treatedPages != null && treatedPages.contains(tsIndexPage)) {
                pageNum++;
                continue;
            }

            map.put("pageNum", Collections.singletonList(String.valueOf(pageNum)));
            map.put("isCurrent", Collections.singletonList("2"));
            map.put("isCompulsive", Collections.singletonList("2"));

            HttpEntity httpEntity = new HttpEntity(map, httpHeaders);

            ResponseEntity<String> exchange = restTemplate.exchange("https://www.sdtdata.com/fx/foodcodex?p=tsLibList&s=fmoa&act=doSearch", HttpMethod.POST, httpEntity, String.class);
            String responseBody = exchange.getBody();

            String substring = responseBody.substring(responseBody.indexOf("["), responseBody.lastIndexOf("]") + 1);
            if ("[]".equals(substring)) {
                tsMapper.deleteTsIndexPage(tsIndexPage);
                break;
            }
            logger.debug("当前页码:" + pageNum);

            List<Map<String, String>> dataMapList = (List<Map<String, String>>) JsonMapper.fromJsonString(substring, List.class);
            //System.out.println(dataMapList);

            tsService.tsIndex(tsIndexPage, dataMapList);
            pageNum++;

//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        return "success";

    }

    /**
     * 远程有更新的时候,跟新索引，此时不再使用page检查
     * @param diffCount
     * @return
     */
    @RequestMapping("/updateIndex")
    public String updateIndex(@RequestParam(value = "diffCount", defaultValue = "0") Integer diffCount) {
        HttpHeaders httpHeaders = new HttpHeaders();
        int pageNum = 1;

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        int counter = 0;// 计数器，当counter等于diffCount时程序终止
        while (true) {

            map.put("pageNum", Collections.singletonList(String.valueOf(pageNum)));
            map.put("isCurrent", Collections.singletonList("2"));
            map.put("isCompulsive", Collections.singletonList("2"));

            HttpEntity httpEntity = new HttpEntity(map, httpHeaders);

            ResponseEntity<String> exchange = restTemplate.exchange("https://www.sdtdata.com/fx/foodcodex?p=tsLibList&s=fmoa&act=doSearch", HttpMethod.POST, httpEntity, String.class);
            String responseBody = exchange.getBody();

            String substring = responseBody.substring(responseBody.indexOf("["), responseBody.lastIndexOf("]") + 1);
            if ("[]".equals(substring)) {
                break;
            }
            logger.debug("当前页码:" + pageNum);

            List<Map<String, String>> dataMapList = (List<Map<String, String>>) JsonMapper.fromJsonString(substring, List.class);

            int count = tsService.updateIndex(dataMapList);
            counter += count;
            if (counter == diffCount) {
                break;
            }
            pageNum++;

//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        return "success";
    }

    /**
     * 标准-详情入库
     *
     * @return
     */
    @RequestMapping("/detail")
    public String tsDetail(@RequestParam(value = "startRow", defaultValue = "0") long startRow) {
        HttpHeaders httpHeaders = new HttpHeaders();
        // 查询全部的项目索引库数据,每次查100条
        //long startRow = 0L;
        if (startRow == 0L) {
            startRow = tsMapper.findTreatedTsIndexMaxRow();
        }
        while (true) {
            List<TsIndex> tsIndexList = tsMapper.findTsIndexList(startRow, 100);
            if (tsIndexList == null || tsIndexList.size() == 0) {
                break;
            }

            List<TsDetail> tsDetailList = new ArrayList<>();
            for (TsIndex tsIndex : tsIndexList) {
                if (tsIndex.getStatus().equals("1")) {
                    continue;
                }

                HttpEntity httpEntity = new HttpEntity(httpHeaders);

                String url = String.format("https://www.sdtdata.com/fx/fmoa/tsLibCard/%s.html", tsIndex.getTsId());
                logger.debug("Grab data from " + url);
                ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
                String html = exchange.getBody();

                // 格式化
                html = html.substring(html.indexOf("<div class=\"con conStandard\">"));
                html = html.substring(0, html.indexOf("<div class=\"conTatal\">"));
                StringBuffer htmlBuff = new StringBuffer();
                String[] lines = html.split("\r\n");
                for (String line : lines) {
                    line = line.trim();
                    if (line.startsWith("<div") || line.startsWith("<ul") || line.startsWith("<li ") || line.startsWith("<form")
                            || line.startsWith("<input") || line.startsWith("<textarea") || line.startsWith("<a")
                            || line.startsWith("<!--") || line.startsWith("</div") || line.startsWith("</ul") || line.startsWith("</li")
                            || line.startsWith("</form")) {
                        continue;
                    }
                    htmlBuff.append(line);
                }
                html = htmlBuff.toString().replaceAll("_tsId=\"\\d*\"", "")
                        .replaceAll("href=\"javascript:void\\(0\\)\"", "")
                        .replaceAll("style=\"color:#333;cursor:default\"", "")
                        .replaceAll("_tsPublishFile=\".*?\"", "")
                        .replaceAll("_tsSourcUrl=\".*?\"", "");
//                System.out.println(html);

                TsDetail tsDetail = new TsDetail();
                tsDetail.setId(Math.abs(new Random().nextLong()));
                tsDetail.setTsId(tsIndex.getTsId());
                tsDetail.setTsNo(tsIndex.getTsNo());
                tsDetail.setTsName(tsIndex.getTsName());
                tsDetail.setTsValidity(tsIndex.getTsValidity());
                tsDetail.setTsPubDept(tsIndex.getTsPubDept());
                // 标准摘要
                tsDetail.setTsSummary(getMatchValue("<h3>标准摘要</h3><p>.*?</p>", html));
                // 替代标准
                tsDetail.setInsteadTs(getMatchValue("<h3>代替标准</h3><li><a   >.*?</a>", html));
                // 现行标准
                tsDetail.setCurrentTs(getMatchValue("<h3 class=\"mt20\">现行标准</h3><li><a   >.*?</a>", html));
                // 修改单
                tsDetail.setModifList(getMatchValue("<h3 class=\"mt20\">修改单</h3><li><a   >.*?</a>", html));
                // 国别
                tsDetail.setTsCountry(getMatchValue("<dt>国别:</dt><dd>.*?</dd>", html));
                // 英文名称  getMatchValue("", html)
                tsDetail.setTsEnsName(getMatchValue("<dt>英文名称:</dt><dd>.*?</dd>", html));
                // 中标分类
                tsDetail.setTsClass(getMatchValue("<dt>中标分类:</dt><dd>.*?</dd>", html));
                // ICS分类
                tsDetail.setIcsClass(getMatchValue("<dt>ICS分类:</dt><dd>.*?</dd>", html));
                // 发布日期
                tsDetail.setPubDate(getMatchValue("<dt>发布日期:</dt><dd>.*?</dd>", html));
                // 实施日期
                tsDetail.setImplDate(getMatchValue("<dt>实施日期:</dt><dd>.*?</dd>", html).replaceAll("<span class=\"tag\">.*?</span>", ""));
                // 发布部门
                tsDetail.setTsPubDept(getMatchValue("<dt>发布部门:</dt><dd>.*?</dd>", html));
                // 发布号
                tsDetail.setPubNo(getMatchValue("<dt>发布号:</dt><dd><span class=\"blue\" >.*?</span>", html));
                // 官方来源
                tsDetail.setOfficialSource(getMatchValue("<dt>官方来源:</dt><dd><span class=\"blue\" >.*?</span>", html));

                tsDetailList.add(tsDetail);
            }
            if (tsDetailList.size() > 0) {
                tsService.tsDetail(tsDetailList, tsIndexList);
            }
            logger.debug("===========================completed!");
            startRow += 100;
        }
        logger.debug("===========================All completed!");
        return "success";
    }

}
