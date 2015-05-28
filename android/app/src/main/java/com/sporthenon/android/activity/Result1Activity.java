package com.sporthenon.android.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.sporthenon.android.R;
import com.sporthenon.android.async.AsyncEvents;
import com.sporthenon.android.async.AsyncResult1;

public class Result1Activity extends Activity {

    protected String lang;
    protected TextView year;
    protected TextView sport;
    protected TextView championship;
    protected TextView event;
    protected TextView subevent;
    protected TextView subevent2;
    protected TextView date;
    protected TextView place1;
    protected TextView place2;

    public String getLang() {
        return lang;
    }

    public TextView getYear() {
        return year;
    }

    public TextView getSport() {
        return sport;
    }

    public TextView getChampionship() {
        return championship;
    }

    public TextView getEvent() {
        return event;
    }

    public TextView getSubevent() {
        return subevent;
    }

    public TextView getSubevent2() {
        return subevent2;
    }

    public TextView getDate() {
        return date;
    }

    public TextView getPlace1() {
        return place1;
    }

    public TextView getPlace2() {
        return place2;
    }

    protected void onCreate(Bundle state) {
        super.onCreate(state);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_result1);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        ((TextView) findViewById(R.id.title)).setText(R.string.result1);
        findViewById(R.id.search_icon).setVisibility(View.GONE);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        lang = prefs.getString("lang", null);

        year = (TextView) findViewById(R.id.rs_year);
        sport = (TextView) findViewById(R.id.rs_sport);
        championship = (TextView) findViewById(R.id.rs_championship);
        event = (TextView) findViewById(R.id.rs_event);
        subevent = (TextView) findViewById(R.id.rs_subevent);
        subevent2 = (TextView) findViewById(R.id.rs_subevent2);
        date = (TextView) findViewById(R.id.rs_date);
        place1 = (TextView) findViewById(R.id.rs_place1);
        place2 = (TextView) findViewById(R.id.rs_place2);

        Bundle b = getIntent().getExtras();
        AsyncResult1 task = new AsyncResult1();
        task.execute(this, b.getInt("rsid"), b.getString("rsyr"));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void onBackClick(View v) {
        finish();
    }

}