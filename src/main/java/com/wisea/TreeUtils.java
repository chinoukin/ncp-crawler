package com.wisea;

import com.wisea.entity.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TreeUtils {

    public static List<TreeNode> structureTree(TreeNode tree) {
        List<TreeNode> structureTreeList = new ArrayList<>();

        tree.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        tree.setParentId("");
        tree.setParentIds("");
        tree.setNames(tree.getName());
        // 递归当前node
        resursionNode(tree, structureTreeList);
        structureTreeList.add(tree);

        return structureTreeList;
    }

    public static List<TreeNode> structureTree(List<TreeNode> tree) {
        List<TreeNode> structureTreeList = new ArrayList<>();
        // 一级
        for (TreeNode treeNode : tree) {
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

    public static void resursionNode(TreeNode treeNode, List<TreeNode> structureTreeList) {
        List<TreeNode> children = treeNode.getChildren();
        if (children != null && children.size() > 0) {
            for (TreeNode childrenNode : children) {
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
