package com.wisea;

import com.wisea.entity.FcTreeNode;
import com.wisea.entity.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FcTreeUtils {

    public static List<FcTreeNode> structureTree(FcTreeNode tree) {
        List<FcTreeNode> structureTreeList = new ArrayList<>();

        tree.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        tree.setParentId("");
        tree.setParentIds("");
        tree.setNames(tree.getName());
        // 递归当前node
        resursionNode(tree, structureTreeList);
        structureTreeList.add(tree);

        return structureTreeList;
    }

    public static List<FcTreeNode> structureTree(List<FcTreeNode> tree) {
        List<FcTreeNode> structureTreeList = new ArrayList<>();
        // 一级
        for (FcTreeNode treeNode : tree) {
            treeNode.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            treeNode.setParentId("");
            treeNode.setParentIds("");
            treeNode.setNames(treeNode.getName());
            // 递归当前node
            resursionNode(treeNode, structureTreeList);
            structureTreeList.add(treeNode);
        }
        return structureTreeList;
    }

    public static void resursionNode(FcTreeNode treeNode, List<FcTreeNode> structureTreeList) {
        List<FcTreeNode> children = treeNode.getChildren();
        if (children != null && children.size() > 0) {
            for (FcTreeNode childrenNode : children) {
                childrenNode.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                childrenNode.setParentId(treeNode.getId());
                childrenNode.setParentIds(treeNode.getParentIds() + "," + treeNode.getId());
                childrenNode.setNames(treeNode.getNames() + " > " + childrenNode.getName());
                structureTreeList.add(childrenNode);

                if (childrenNode.getChildren() != null && childrenNode.getChildren().size() > 0) {
                    resursionNode(childrenNode, structureTreeList);
                }
            }
        }
    }

}
