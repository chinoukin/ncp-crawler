package com.wisea.service;

import com.wisea.entity.TsDetail;
import com.wisea.entity.TsIndex;
import com.wisea.entity.TsIndexPage;
import com.wisea.mapper.TsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class TsService {

    @Autowired
    TsMapper tsMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW) //为每一页列表数据开启一个事务
    public String tsIndex(TsIndexPage tsIndexPage, List<Map<String, String>> dataMapList) {
        List<TsIndex> list = new ArrayList<>();
        for (Map<String, String> dataMap : dataMapList) {
            TsIndex tsIndex = new TsIndex();
            tsIndex.setId(Math.abs(new Random().nextLong()));
            tsIndex.setTsId(dataMap.get("TS_ID"));
            tsIndex.setTsNo(dataMap.get("TS_NO"));
            tsIndex.setTsName(dataMap.get("TS_NAME"));
            tsIndex.setTsValidity(dataMap.get("TS_VALIDITY"));
            tsIndex.setTsPubDept(dataMap.get("TS_PUBLISH_DEPT"));
            tsIndex.setStatus("0");
            list.add(tsIndex);
        }

        tsMapper.batchInsertTsIndex(list);

        tsIndexPage.setStatus("1");
        tsMapper.updateTsIndexPage(tsIndexPage);
        return "success";
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW) //为每一页列表数据开启一个事务
    public int updateIndex(List<Map<String, String>> dataMapList) {
        // 检查是否在数据库中存在
        List<String> tsIds = new ArrayList<>();
        for (Map<String, String> dataMap : dataMapList) {
            tsIds.add(dataMap.get("TS_ID"));
        }
        List<TsIndex> list = tsMapper.findSubTsIndexList(tsIds);

        if (list.size() == dataMapList.size()) {
            return 0;
        }

        List<Map<String, String>> subDataMapList = new ArrayList<>();
        for (TsIndex tsIndex : list) {
            int index = tsIds.indexOf(tsIndex.getTsId());
            subDataMapList.add(dataMapList.get(index));
        }
        if (subDataMapList.size() > 0) {
            dataMapList.removeAll(subDataMapList);
        }

        list = new ArrayList<>();
        for (Map<String, String> dataMap : dataMapList) {
            TsIndex tsIndex = new TsIndex();
            tsIndex.setId(Math.abs(new Random().nextLong()));
            tsIndex.setTsId(dataMap.get("TS_ID"));
            tsIndex.setTsNo(dataMap.get("TS_NO"));
            tsIndex.setTsName(dataMap.get("TS_NAME"));
            tsIndex.setTsValidity(dataMap.get("TS_VALIDITY"));
            tsIndex.setTsPubDept(dataMap.get("TS_PUBLISH_DEPT"));
            tsIndex.setStatus("0");
            list.add(tsIndex);
        }
        return tsMapper.batchInsertTsIndex(list);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String tsDetail(List<TsDetail> tsDetailList, List<TsIndex> tsIndexList) {
        tsMapper.batchInsertTsDetail(tsDetailList);
        tsMapper.batchUpdateTsIndex(tsIndexList);
        return "success";
    }
}
