package com.sporthenon.android.data;

import android.graphics.drawable.Drawable;

public class ResultItem {

	private int id;
    private String year;
    private Drawable img;
    private String imgURL;
    private String rank1;

	public ResultItem(int id, String year, String imgURL, Drawable img, String rank1) {
		super();
		this.id = id;
		this.year = year;
        this.imgURL = imgURL;
		this.img = img;
        this.rank1 = rank1;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }

    public String getRank1() {
        return rank1;
    }

    public void setRank1(String rank1) {
        this.rank1 = rank1;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

}