package com.sporthenon.android.data;

import android.graphics.drawable.Drawable;

public class Result1Item {

	private int id;
    private String year;
    private String sport;
    private Drawable sportImg;
    private String championship;
    private Drawable championshipImg;
    private String event;
    private Drawable eventImg;
    private String subevent;
    private Drawable subeventImg;
    private String subevent2;
    private Drawable subevent2Img;
    private String date;
    private String place1;
    private Drawable place1Img;
    private String place2;
    private Drawable place2Img;

	public Result1Item(int id, String year) {
		super();
		this.id = id;
        this.year = year;
	}

	public Result1Item() {
		super();
	}

    public Drawable getSportImg() {
        return sportImg;
    }

    public void setSportImg(Drawable sportImg) {
        this.sportImg = sportImg;
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

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getChampionship() {
        return championship;
    }

    public void setChampionship(String championship) {
        this.championship = championship;
    }

    public Drawable getChampionshipImg() {
        return championshipImg;
    }

    public void setChampionshipImg(Drawable championshipImg) {
        this.championshipImg = championshipImg;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Drawable getEventImg() {
        return eventImg;
    }

    public void setEventImg(Drawable eventImg) {
        this.eventImg = eventImg;
    }

    public String getSubevent() {
        return subevent;
    }

    public void setSubevent(String subevent) {
        this.subevent = subevent;
    }

    public Drawable getSubeventImg() {
        return subeventImg;
    }

    public void setSubeventImg(Drawable subeventImg) {
        this.subeventImg = subeventImg;
    }

    public String getSubevent2() {
        return subevent2;
    }

    public void setSubevent2(String subevent2) {
        this.subevent2 = subevent2;
    }

    public Drawable getSubevent2Img() {
        return subevent2Img;
    }

    public void setSubevent2Img(Drawable subevent2Img) {
        this.subevent2Img = subevent2Img;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace1() {
        return place1;
    }

    public void setPlace1(String place1) {
        this.place1 = place1;
    }

    public Drawable getPlace1Img() {
        return place1Img;
    }

    public void setPlace1Img(Drawable place1Img) {
        this.place1Img = place1Img;
    }

    public String getPlace2() {
        return place2;
    }

    public void setPlace2(String place2) {
        this.place2 = place2;
    }

    public Drawable getPlace2Img() {
        return place2Img;
    }

    public void setPlace2Img(Drawable place2Img) {
        this.place2Img = place2Img;
    }

}