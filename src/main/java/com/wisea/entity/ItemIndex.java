package com.wisea.entity;

public class ItemIndex {

    private Long id;

    private String itemId;

    private String itemCountry;

    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemCountry() {
        return itemCountry;
    }

    public void setItemCountry(String itemCountry) {
        this.itemCountry = itemCountry;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
