package com.wisea.service;

import com.wisea.entity.ItemDetail;
import com.wisea.entity.ItemIndex;
import com.wisea.entity.ItemIndexPage;
import com.wisea.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class ItemService {

    @Autowired
    ItemMapper itemMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW) //为每一页列表数据开启一个事务
    public String itemIndex(ItemIndexPage itemIndexPage, List<Map<String, String>> dataMapList) {
        List<ItemIndex> list = new ArrayList<>();
        for (Map<String, String> dataMap : dataMapList) {
            ItemIndex itemIndex = new ItemIndex();
            itemIndex.setId(Math.abs(new Random().nextLong()));
            itemIndex.setItemId(dataMap.get("ITEM_ID"));
            itemIndex.setItemCountry(dataMap.get("ITEM_COUNTRY"));
            itemIndex.setStatus("0");
            list.add(itemIndex);
        }

        itemMapper.batchInsertItemIndex(list);

        itemIndexPage.setStatus("1");
        itemMapper.updateItemIndexPage(itemIndexPage);
        return "success";
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String itemDetail(List<ItemDetail> itemDetailList, List<ItemIndex> itemIndexList) {
        itemMapper.batchInsertItemDetail(itemDetailList);
        itemMapper.batchUpdateItemIndex(itemIndexList);
        return "success";
    }
}
