package com.wisea.entity;

import java.util.List;

public class FcProdTypeTsSqlParam {

    private FcProdType fcProdType;

    private String linkName;

    private List<TsDetail> list;

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

    public List<TsDetail> getList() {
        return list;
    }

    public void setList(List<TsDetail> list) {
        this.list = list;
    }
}
