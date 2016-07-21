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
        if (n == USTYPE_CHAMPIONSHIPS) {
            Intent i = new Intent(this, YearActivity.class);
            Bundle b = new Bundle();
            //b.putInt("lgid", getOlId());
            //b.putInt("oltype", getOlType());
            i.putExtras(b);
            startActivity(i);
        }
        /*else if (n == OLMODE_MEDALS) {
            Intent i = new Intent(this, OlympicsMedalsActivity.class);
            Bundle b = new Bundle();
            b.putInt("olid", getOlId());
            b.putString("olname", getOlName());
            i.putExtras(b);
            startActivity(i);
        }*/
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
    }

}