package com.wisea.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class FcTreeNode {

    // id = typeId
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String id;

    private String name;

    private String fcTypeCode;

    private List<FcTreeNode> children;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private String parentId;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private String parentIds;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private String names;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFcTypeCode() {
        return fcTypeCode;
    }

    public void setFcTypeCode(String fcTypeCode) {
        this.fcTypeCode = fcTypeCode;
    }

    public List<FcTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<FcTreeNode> children) {
        this.children = children;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }
}
