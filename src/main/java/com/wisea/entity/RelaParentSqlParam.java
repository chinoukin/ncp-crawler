package com.wisea.entity;

import java.util.List;

public class RelaParentSqlParam {

    private String fcProdTypeCode;

    private String linkName;

    private List<String> tsIds;

    public String getFcProdTypeCode() {
        return fcProdTypeCode;
    }

    public void setFcProdTypeCode(String fcProdTypeCode) {
        this.fcProdTypeCode = fcProdTypeCode;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public List<String> getTsIds() {
        return tsIds;
    }

    public void setTsIds(List<String> tsIds) {
        this.tsIds = tsIds;
    }
}
