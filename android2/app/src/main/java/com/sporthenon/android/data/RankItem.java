package com.sporthenon.android.data;

import android.graphics.drawable.Drawable;

public class RankItem {

    private String rank;
    private Drawable img;
    private String imgURL;
    private String text;
    private String result;

	public RankItem(String rank) {
		super();
		this.rank = rank;
	}

	public RankItem() {
		super();
	}

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}