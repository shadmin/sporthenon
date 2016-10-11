package com.sporthenon.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

public class USLeaguesRecordTypeActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle state) {
        index = INDEX_USLEAGUES;
        super.onCreate(state);
        Bundle b = getIntent().getExtras();
        setLeagueId(b.getInt("lgid"));
        setLeagueName(b.getString("lgname"));
        setUsltype(b.getInt("usltype"));
    }

    @Override
    protected void onPostCreate(Bundle state) {
        super.onPostCreate(state);
        setPath(getLeagueName() + "|Records");
    }

    public void onRcType1Click(View v) {
        nextActivity("i");
    }

    public void onRcType2Click(View v) {
        nextActivity("t");
    }

    public void onRcType3Click(View v) {
        nextActivity("it");
    }

    public void nextActivity(String s) {
        Bundle b = new Bundle();
        b.putInt("lgid", getLeagueId());
        b.putString("lgname", getLeagueName());
        b.putInt("usltype", getUsltype());
        b.putString("rctype", s);
        Intent i = new Intent(this, EventActivity.class);
        i.putExtras(b);
        startActivity(i);
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
    }

}