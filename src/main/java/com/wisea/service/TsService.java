package com.wisea.service;

import com.wisea.entity.FcProdType;
import com.wisea.entity.FcProdTypeTsSqlParam;
import com.wisea.entity.RelaParentSqlParam;
import com.wisea.entity.TsDetail;
import com.wisea.entity.TsDetailSqlParam;
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
import java.util.UUID;

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

    /**
     * 查询fc产品分类数据，分类级别排序，级别小的靠前
     * @return
     */
    public List<FcProdType> findRankByLevelList(){
        List<FcProdType> list = new ArrayList<>();
        int level = 1;
        while(true) {
            List<FcProdType> fcProdTypeList = tsMapper.findFcProdTypeList(level-1);
            if (fcProdTypeList == null || fcProdTypeList.size() == 0) {
                break;
            }
            list.addAll(fcProdTypeList);
            level++;
        }
        return list;
    }

//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public String updateTsDetail(List<Map<String, String>> dataMapList, FcProdType fcProdType, String linkName) {
//        List<String> tsIds = new ArrayList<>();
//        for (Map<String, String> dataMap : dataMapList) {
//            tsIds.add(dataMap.get("TS_ID"));
//        }
//        TsDetailSqlParam tsDetailSqlParam = new TsDetailSqlParam();
//        tsDetailSqlParam.setFcProdType(fcProdType);
//        tsDetailSqlParam.setLinkName(linkName);
//        tsDetailSqlParam.setTsIds(tsIds);
//
//        tsMapper.batchUpdateTsDetail(tsDetailSqlParam);
//
//        return "success";
//    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String addTypeTs(List<Map<String, String>> dataMapList, FcProdType fcProdType, String linkName) {
        List<String> tsIds = new ArrayList<>();
        for (Map<String, String> dataMap : dataMapList) {
            tsIds.add(dataMap.get("TS_ID"));
        }
        RelaParentSqlParam relaParentSqlParam = new RelaParentSqlParam();
        String fcTypeCode = fcProdType.getFcTypeCode();
        relaParentSqlParam.setFcProdTypeCode(fcTypeCode.substring(0, fcTypeCode.length()-2));
        relaParentSqlParam.setLinkName(linkName);
        relaParentSqlParam.setTsIds(tsIds);
        // 删除已记录的父分类
        tsMapper.deleteFcProdTypeTsByRelaParent(relaParentSqlParam);

        FcProdTypeTsSqlParam fcProdTypeTsSqlParam = new FcProdTypeTsSqlParam();
        fcProdTypeTsSqlParam.setFcProdType(fcProdType);
        fcProdTypeTsSqlParam.setLinkName(linkName);
        List<TsDetail> list = new ArrayList<>();
        for (Map<String, String> dataMap : dataMapList) {
            TsDetail tsDetail = new TsDetail();
            tsDetail.setFcProdTypeTsId(UUID.randomUUID().toString().replaceAll("-", ""));
            tsDetail.setTsId(dataMap.get("TS_ID"));
            tsDetail.setTsName(dataMap.get("TS_NAME"));
            list.add(tsDetail);
        }
        fcProdTypeTsSqlParam.setList(list);

        tsMapper.batchInsertFcProdTypeTs(fcProdTypeTsSqlParam);
        return "success";
    }
}
