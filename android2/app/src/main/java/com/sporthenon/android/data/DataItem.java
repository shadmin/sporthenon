package com.sporthenon.android.data;

import android.graphics.drawable.Drawable;

public class DataItem {

	private int id;
    private String name;
    private Integer param;
    private Drawable picture;

	public DataItem(int id, String name, Drawable picture) {
		super();
		this.id = id;
		this.name = name;
		this.picture = picture;
	}

    public DataItem(int id, String name, Integer param, Drawable picture) {
        super();
        this.id = id;
        this.name = name;
        this.param = param;
        this.picture = picture;
    }

	public DataItem() {
		super();
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParam() {
        return param;
    }

    public void setParam(int param) {
        this.param = param;
    }

    public Drawable getPicture() {
        return picture;
    }

    public void setPicture(Drawable picture) {
        this.picture = picture;
    }

}