package com.sporthenon.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.R;
import com.sporthenon.android.fragment.USLeaguesTypeFragment;

public class USLeaguesTypeActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle state) {
        index = INDEX_USLEAGUES;
        super.onCreate(state);
        Bundle b = getIntent().getExtras();
        setLeagueId(b.getInt("lgid"));
        getSupportFragmentManager().beginTransaction().replace(R.id.container, USLeaguesTypeFragment.newInstance(index + 1, this)).commit();
    }

    public void onUSType1Click(View v) {
        nextActivity(USTYPE_CHAMPIONSHIPS);
    }

    public void onUSType2Click(View v) {
        nextActivity(USTYPE_RECORDS);
    }

    public void onUSType3Click(View v) {
        nextActivity(USTYPE_STATS);
    }

    public void onUSType4Click(View v) {
        nextActivity(USTYPE_HOF);
    }

    public void onUSType5Click(View v) {
        nextActivity(USTYPE_RETIRED_NUM);
    }

    public void onUSType6Click(View v) {
        nextActivity(USTYPE_TEAM_STADIUMS);
    }

    public void nextActivity(Integer n) {
        Bundle b = new Bundle();
        b.putInt("lgid", getLeagueId());
        b.putInt("usltype", n);
        Intent i = null;
        if (n == USTYPE_HOF)
            i = new Intent(this, YearActivity.class);
        else if (n == USTYPE_RECORDS || n == USTYPE_STATS)
            i = new Intent(this, EventActivity.class);
        else if (n == USTYPE_CHAMPIONSHIPS)
            i = new Intent(this, USLeaguesRequestActivity.class);
        else
            i = new Intent(this, TeamActivity.class);
        i.putExtras(b);
        startActivity(i);
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
    }

}