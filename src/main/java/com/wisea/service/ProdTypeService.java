package com.wisea.service;

import com.wisea.entity.FcTreeNode;
import com.wisea.entity.TreeNode;
import com.wisea.mapper.ProdTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdTypeService {

    @Autowired
    ProdTypeMapper prodTypeMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW) //为每一页列表数据开启一个事务
    public String save(List<TreeNode> dataList) {
        prodTypeMapper.batchInsertProdType(dataList);
        return "success";
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW) //为每一页列表数据开启一个事务
    public String saveFcType(List<FcTreeNode> dataList) {
        prodTypeMapper.batchInsertFcProdType(dataList);
        return "success";
    }
}
