package com.sporthenon.android.data;

import android.graphics.drawable.Drawable;

public class LastUpdateItem {

	private int idResult;
    private String year;
    private String sport;
    private Drawable imgSport;
    private String imgURLSport;
    private String event;
    private String pos1;
    private int idPos1;
    private Drawable imgPos1;
    private String imgURLPos1;
    private String pos2;
    private int idPos2;
    private Drawable imgPos2;
    private String imgURLPos2;
    private String pos3;
    private int idPos3;
    private Drawable imgPos3;
    private String imgURLPos3;
    private String pos4;
    private int idPos4;
    private Drawable imgPos4;
    private String imgURLPos4;
    private String score;
    private String date;

	public LastUpdateItem(int idResult, String year, String sport, String event) {
		super();
		this.idResult = idResult;
		this.year = year;
        this.sport = sport;
		this.event = event;
	}

    public int getIdResult() {
        return idResult;
    }

    public void setIdResult(int idResult) {
        this.idResult = idResult;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public Drawable getImgSport() {
        return imgSport;
    }

    public void setImgSport(Drawable imgSport) {
        this.imgSport = imgSport;
    }

    public String getImgURLSport() {
        return imgURLSport;
    }

    public void setImgURLSport(String imgURLSport) {
        this.imgURLSport = imgURLSport;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getPos1() {
        return pos1;
    }

    public void setPos1(String pos1) {
        this.pos1 = pos1;
    }

    public int getIdPos1() {
        return idPos1;
    }

    public void setIdPos1(int idPos1) {
        this.idPos1 = idPos1;
    }

    public Drawable getImgPos1() {
        return imgPos1;
    }

    public void setImgPos1(Drawable imgPos1) {
        this.imgPos1 = imgPos1;
    }

    public String getImgURLPos1() {
        return imgURLPos1;
    }

    public void setImgURLPos1(String imgURLPos1) {
        this.imgURLPos1 = imgURLPos1;
    }

    public String getPos2() {
        return pos2;
    }

    public void setPos2(String pos2) {
        this.pos2 = pos2;
    }

    public int getIdPos2() {
        return idPos2;
    }

    public void setIdPos2(int idPos2) {
        this.idPos2 = idPos2;
    }

    public Drawable getImgPos2() {
        return imgPos2;
    }

    public void setImgPos2(Drawable imgPos2) {
        this.imgPos2 = imgPos2;
    }

    public String getImgURLPos2() {
        return imgURLPos2;
    }

    public void setImgURLPos2(String imgURLPos2) {
        this.imgURLPos2 = imgURLPos2;
    }

    public String getPos3() {
        return pos3;
    }

    public void setPos3(String pos3) {
        this.pos3 = pos3;
    }

    public int getIdPos3() {
        return idPos3;
    }

    public void setIdPos3(int idPos3) {
        this.idPos3 = idPos3;
    }

    public Drawable getImgPos3() {
        return imgPos3;
    }

    public void setImgPos3(Drawable imgPos3) {
        this.imgPos3 = imgPos3;
    }

    public String getImgURLPos3() {
        return imgURLPos3;
    }

    public void setImgURLPos3(String imgURLPos3) {
        this.imgURLPos3 = imgURLPos3;
    }

    public String getPos4() {
        return pos4;
    }

    public void setPos4(String pos4) {
        this.pos4 = pos4;
    }

    public int getIdPos4() {
        return idPos4;
    }

    public void setIdPos4(int idPos4) {
        this.idPos4 = idPos4;
    }

    public Drawable getImgPos4() {
        return imgPos4;
    }

    public void setImgPos4(Drawable imgPos4) {
        this.imgPos4 = imgPos4;
    }

    public String getImgURLPos4() {
        return imgURLPos4;
    }

    public void setImgURLPos4(String imgURLPos4) {
        this.imgURLPos4 = imgURLPos4;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}