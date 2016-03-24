package com.sporthenon.android.data;

import android.graphics.drawable.Drawable;

public class CalendarItem {

	private int id;
    private String sport;
    private Drawable sportImg;
    private String event;
    private String dates;

	public CalendarItem(int id, String sport, Drawable sportImg, String event, String dates) {
		super();
		this.id = id;
		this.sport = sport;
        this.sportImg = sportImg;
		this.event = event;
        this.dates = dates;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public Drawable getSportImg() {
        return sportImg;
    }

    public void setSportImg(Drawable sportImg) {
        this.sportImg = sportImg;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

}