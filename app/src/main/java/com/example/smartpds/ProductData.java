package com.example.smartpds;

public class ProductData {
    private String name,quota,price;
    public ProductData(String name,String quota, String price) {

        this.quota = quota;
        this.name = name;
        this.price = price;

    }
    public String getQuota() {
        return quota;
    }
    public void setQuota(String quota) {
        this.quota = quota;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPrice() {
        return price;
    }
    public void setImgId(String price) {
        this.price = price;
    }
}
