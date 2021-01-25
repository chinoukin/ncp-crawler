package com.wisea.mapper;

import com.wisea.entity.FcTreeNode;
import com.wisea.entity.TreeNode;

import java.util.List;

public interface ProdTypeMapper {

    int batchInsertProdType(List<TreeNode> list);

    int batchInsertFcProdType(List<FcTreeNode> list);
}
