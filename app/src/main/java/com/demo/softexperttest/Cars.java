package com.demo.softexperttest;

import com.google.gson.annotations.SerializedName;

public class Cars {

    @SerializedName("id")
    private String id;
    @SerializedName("brand")
    private String brand;
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("isUsed")
    private String isUsed;
    @SerializedName("constructionYear")
    private String constructionYear;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    public String getConstructionYear() {
        return constructionYear;
    }

    public void setConstructionYear(String constructionYear) {
        this.constructionYear = constructionYear;
    }
}
