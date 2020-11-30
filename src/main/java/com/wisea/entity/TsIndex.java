package com.wisea.entity;

public class TsIndex {

    private Long id;

    private String tsId;

    private String tsNo;

    private String tsValidity;

    private String tsName;

    private String tsPubDept;

    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTsId() {
        return tsId;
    }

    public void setTsId(String tsId) {
        this.tsId = tsId;
    }

    public String getTsNo() {
        return tsNo;
    }

    public void setTsNo(String tsNo) {
        this.tsNo = tsNo;
    }

    public String getTsValidity() {
        return tsValidity;
    }

    public void setTsValidity(String tsValidity) {
        this.tsValidity = tsValidity;
    }

    public String getTsName() {
        return tsName;
    }

    public void setTsName(String tsName) {
        this.tsName = tsName;
    }

    public String getTsPubDept() {
        return tsPubDept;
    }

    public void setTsPubDept(String tsPubDept) {
        this.tsPubDept = tsPubDept;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
