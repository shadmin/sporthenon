package com.sporthenon.android.data;

import android.graphics.drawable.Drawable;

public class MedalItem {

    private String country;
    private Drawable img;
    private String imgURL;
    private Integer gold;
    private Integer silver;
    private Integer bronze;

	public MedalItem(String country, Integer gold, Integer silver, Integer bronze) {
		super();
		this.country = country;
        this.gold = gold;
        this.silver = silver;
        this.bronze = bronze;
	}

	public MedalItem() {
		super();
	}

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }

    public Integer getGold() {
        return gold;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }

    public Integer getSilver() {
        return silver;
    }

    public void setSilver(Integer silver) {
        this.silver = silver;
    }

    public Integer getBronze() {
        return bronze;
    }

    public void setBronze(Integer bronze) {
        this.bronze = bronze;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}