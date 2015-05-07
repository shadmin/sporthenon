package com.sporthenon.data;

import android.graphics.drawable.Drawable;

public class ResultItem implements IResultItem {

	private int id;
    private String year;
    private Drawable img;
    private String rank1;

	public ResultItem(int id, String year, Drawable img, String rank1) {
		super();
		this.id = id;
		this.year = year;
		this.img = img;
        this.rank1 = rank1;
	}

	public ResultItem() {
		super();
	}

    @Override
    public int getId() {
        return id;
    }

    @Override
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

}