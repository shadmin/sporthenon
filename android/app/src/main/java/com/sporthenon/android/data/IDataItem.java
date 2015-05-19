package com.sporthenon.android.data;

import android.graphics.drawable.Drawable;

public interface IDataItem {

    public int getId();

    public void setId(int id);

    public String getName();

    public void setName(String name);

    public Drawable getPicture();

    public void setPicture(Drawable picture);

}