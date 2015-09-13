package com.sporthenon.android.data;

import android.graphics.drawable.Drawable;

public class ResultItem {

	private int id;
    private String year;
    private Drawable img1;
    private String imgURL1;
    private String txt1;
    private Drawable img2;
    private String imgURL2;
    private String txt2;
    private Drawable img3;
    private String imgURL3;
    private String txt3;

	public ResultItem(int id, String year, String imgURL1, Drawable img1, String txt1) {
		super();
		this.id = id;
		this.year = year;
        this.imgURL1 = imgURL1;
		this.img1 = img1;
        this.txt1 = txt1;
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

    public Drawable getImg1() {
        return img1;
    }

    public void setImg1(Drawable img1) {
        this.img1 = img1;
    }

    public String getImgURL1() {
        return imgURL1;
    }

    public void setImgURL1(String imgURL1) {
        this.imgURL1 = imgURL1;
    }

    public String getTxt1() {
        return txt1;
    }

    public void setTxt1(String txt1) {
        this.txt1 = txt1;
    }

    public Drawable getImg2() {
        return img2;
    }

    public void setImg2(Drawable img2) {
        this.img2 = img2;
    }

    public String getImgURL2() {
        return imgURL2;
    }

    public void setImgURL2(String imgURL2) {
        this.imgURL2 = imgURL2;
    }

    public String getTxt2() {
        return txt2;
    }

    public void setTxt2(String txt2) {
        this.txt2 = txt2;
    }

    public Drawable getImg3() {
        return img3;
    }

    public void setImg3(Drawable img3) {
        this.img3 = img3;
    }

    public String getImgURL3() {
        return imgURL3;
    }

    public void setImgURL3(String imgURL3) {
        this.imgURL3 = imgURL3;
    }

    public String getTxt3() {
        return txt3;
    }

    public void setTxt3(String txt3) {
        this.txt3 = txt3;
    }

}