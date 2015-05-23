package com.sporthenon.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.sporthenon.android.R;
import com.sporthenon.android.async.AsyncEvents;
import com.sporthenon.android.async.AsyncResult1;

public class Result1Activity extends Activity {

    protected TextView sport;
    protected TextView championship;
    protected TextView year;

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
        setContentView(R.layout.activity_result1);

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