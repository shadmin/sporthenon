package com.sporthenon.android.data;

import android.graphics.drawable.Drawable;

public class Result1Item {

	private int id;
    private String sport;
    private String championship;
    private Drawable event;
    private String subevent;
    private String subevent2;
    private String year;

	public Result1Item(int id, String year) {
		super();
		this.id = id;
        this.year = year;
	}

	public Result1Item() {
		super();
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

    public String getChampionship() {
        return championship;
    }

    public void setChampionship(String championship) {
        this.championship = championship;
    }

    public Drawable getEvent() {
        return event;
    }

    public void setEvent(Drawable event) {
        this.event = event;
    }

    public String getSubevent() {
        return subevent;
    }

    public void setSubevent(String subevent) {
        this.subevent = subevent;
    }

    public String getSubevent2() {
        return subevent2;
    }

    public void setSubevent2(String subevent2) {
        this.subevent2 = subevent2;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

}