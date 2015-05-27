package com.sporthenon.android.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.sporthenon.android.R;
import com.sporthenon.android.async.AsyncEvents;
import com.sporthenon.android.async.AsyncResult1;

public class Result1Activity extends Activity {

    protected String lang;
    protected TextView sport;
    protected TextView championship;
    protected TextView year;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public TextView getSport() {
        return sport;
    }

    public void setSport(TextView sport) {
        this.sport = sport;
    }

    public TextView getChampionship() {
        return championship;
    }

    public void setChampionship(TextView championship) {
        this.championship = championship;
    }

    public TextView getYear() {
        return year;
    }

    public void setYear(TextView year) {
        this.year = year;
    }

    protected void onCreate(Bundle state) {
        super.onCreate(state);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_lang);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        ((TextView) findViewById(R.id.title)).setText(R.string.result1);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        lang = prefs.getString("lang", null);

        sport = (TextView) findViewById(R.id.sport);
        championship = (TextView) findViewById(R.id.championship);
        year = (TextView) findViewById(R.id.year);

        Bundle b = getIntent().getExtras();
        AsyncResult1 task = new AsyncResult1();
        task.execute(this, b.getInt("rsid"), b.getString("rsyr"));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}