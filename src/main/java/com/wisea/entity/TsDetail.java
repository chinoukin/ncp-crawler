package com.wisea.entity;

public class TsDetail {

    private Long id;

    private String tsId;

    private String tsNo;

    private String tsCountry;

    private String tsValidity;

    private String tsName;

    private String tsEnsName;

    private String tsPubDept;

    private String tsClass;

    private String icsClass;

    private String pubDate;

    private String implDate;

    private String pubNo;

    private String officialSource;

    private String tsSummary;

    private String insteadTs;

    private String currentTs;

    private String modifList;

    // 用于sql查询主键临时存放
    private String fcProdTypeTsId;

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

    public String getTsCountry() {
        return tsCountry;
    }

    public void setTsCountry(String tsCountry) {
        this.tsCountry = tsCountry;
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

    public String getTsEnsName() {
        return tsEnsName;
    }

    public void setTsEnsName(String tsEnsName) {
        this.tsEnsName = tsEnsName;
    }

    public String getTsPubDept() {
        return tsPubDept;
    }

    public void setTsPubDept(String tsPubDept) {
        this.tsPubDept = tsPubDept;
    }

    public String getTsClass() {
        return tsClass;
    }

    public void setTsClass(String tsClass) {
        this.tsClass = tsClass;
    }

    public String getIcsClass() {
        return icsClass;
    }

    public void setIcsClass(String icsClass) {
        this.icsClass = icsClass;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getImplDate() {
        return implDate;
    }

    public void setImplDate(String implDate) {
        this.implDate = implDate;
    }

    public String getPubNo() {
        return pubNo;
    }

    public void setPubNo(String pubNo) {
        this.pubNo = pubNo;
    }

    public String getOfficialSource() {
        return officialSource;
    }

    public void setOfficialSource(String officialSource) {
        this.officialSource = officialSource;
    }

    public String getTsSummary() {
        return tsSummary;
    }

    public void setTsSummary(String tsSummary) {
        this.tsSummary = tsSummary;
    }

    public String getInsteadTs() {
        return insteadTs;
    }

    public void setInsteadTs(String insteadTs) {
        this.insteadTs = insteadTs;
    }

    public String getCurrentTs() {
        return currentTs;
    }

    public void setCurrentTs(String currentTs) {
        this.currentTs = currentTs;
    }

    public String getModifList() {
        return modifList;
    }

    public void setModifList(String modifList) {
        this.modifList = modifList;
    }

    public String getFcProdTypeTsId() {
        return fcProdTypeTsId;
    }

    public void setFcProdTypeTsId(String fcProdTypeTsId) {
        this.fcProdTypeTsId = fcProdTypeTsId;
    }
}
