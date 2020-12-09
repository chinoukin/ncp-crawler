package com.wisea.service;

import com.wisea.entity.TreeNode;
import com.wisea.mapper.ItemTypeMapper;
import com.wisea.mapper.ProdTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemTypeService {

    @Autowired
    ItemTypeMapper itemTypeMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW) //为每一页列表数据开启一个事务
    public String save(List<TreeNode> dataList) {
       itemTypeMapper.batchInsertItemType(dataList);
        return "success";
    }
}
