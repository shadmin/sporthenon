package com.sporthenon.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.R;
import com.sporthenon.android.fragment.LeagueFragment;

public class LeagueActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle state) {
        index = INDEX_USLEAGUES;
        super.onCreate(state);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, LeagueFragment.newInstance(index + 1, this)).commit();
    }

    public void onUSLeague1Click(View v) {
        nextActivity(USLEAGUE_NFL, "NFL");
    }

    public void onUSLeague2Click(View v) {
        nextActivity(USLEAGUE_NBA, "NBA");
    }

    public void onUSLeague3Click(View v) {
        nextActivity(USLEAGUE_NHL, "NHL");
    }

    public void onUSLeague4Click(View v) {
        nextActivity(USLEAGUE_MLB, "MLB");
    }

    public void nextActivity(Integer n, String s) {
        Intent i = new Intent(this, USLeaguesTypeActivity.class);
        Bundle b = new Bundle();
        b.putInt("lgid", n);
        b.putString("lgname", s);
        i.putExtras(b);
        startActivity(i);
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
    }

}