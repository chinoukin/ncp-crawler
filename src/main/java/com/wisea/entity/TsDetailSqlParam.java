package com.wisea.entity;

import java.util.List;

public class TsDetailSqlParam {

    private FcProdType fcProdType;

    private String linkName;

    private List<String> tsIds;

    public FcProdType getFcProdType() {
        return fcProdType;
    }

    public void setFcProdType(FcProdType fcProdType) {
        this.fcProdType = fcProdType;
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
