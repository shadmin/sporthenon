package com.sporthenon.android.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sporthenon.android.R;
import com.sporthenon.android.async.AsyncResult1;

public class Result1Activity extends Activity {

    protected String lang;
    protected ProgressBar progress;
    protected TextView year;
    protected TextView sport;
    protected TextView championship;
    protected TextView event;
    protected TextView subevent;
    protected TextView subevent2;
    protected TextView date;
    protected TextView place1;
    protected TextView place2;
    protected ImageView rank1Img;
    protected TextView rank1Text;
    protected ImageView rank2Img;
    protected TextView rank2Text;
    protected ImageView rank3Img;
    protected TextView rank3Text;
    protected TextView score;

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

    public TextView getRank1Text() {
        return rank1Text;
    }

    public ImageView getRank1Img() {
        return rank1Img;
    }

    public ImageView getRank2Img() {
        return rank2Img;
    }

    public TextView getRank2Text() {
        return rank2Text;
    }

    public ImageView getRank3Img() {
        return rank3Img;
    }

    public TextView getRank3Text() {
        return rank3Text;
    }

    public TextView getScore() {
        return score;
    }

    public void setRank1Img(ImageView rank1Img) {
        this.rank1Img = rank1Img;
    }

    protected void onCreate(Bundle state) {
        super.onCreate(state);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_result1);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
        ((TextView) findViewById(R.id.title)).setText(R.string.details);
        findViewById(R.id.search_icon).setVisibility(View.GONE);
        progress = (ProgressBar) findViewById(R.id.progress);

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
        rank1Img = (ImageView) findViewById(R.id.rank1_img);
        rank1Text = (TextView) findViewById(R.id.rank1_text);
        rank2Img = (ImageView) findViewById(R.id.rank2_img);
        rank2Text = (TextView) findViewById(R.id.rank2_text);
        rank3Img = (ImageView) findViewById(R.id.rank3_img);
        rank3Text = (TextView) findViewById(R.id.rank3_text);
        score = (TextView) findViewById(R.id.score);

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

    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

}