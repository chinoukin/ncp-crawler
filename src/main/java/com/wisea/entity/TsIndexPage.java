package com.wisea.entity;

public class TsIndexPage {

    private Long id;

    private Integer pageNum;

    /**
     * 页面状态,0-未处理，1-索引已处理，2-详细数据已处理
     */
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        // Integer的比较
        // -128-127之间的用“==”返回true,大于等于128的则返回false
        return this.pageNum.equals(((TsIndexPage)obj).getPageNum())
                && this.status.equals(((TsIndexPage)obj).getStatus());
    }

}
