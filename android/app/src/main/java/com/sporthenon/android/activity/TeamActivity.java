package com.sporthenon.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.async.AsyncTeams;
import com.sporthenon.android.data.DataItem;

public class TeamActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle state) {
        index = INDEX_USLEAGUES;
        super.onCreate(state);
        Bundle b = getIntent().getExtras();
        setLeagueId(b.getInt("lgid"));
        setUsltype(b.getInt("usltype"));
        AsyncTeams task = new AsyncTeams();
        task.execute(this, getLeagueId());
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        DataItem tm = (DataItem) getList().getItemAtPosition(position);
        Intent i = new Intent(this, USLeaguesRequestActivity.class);
        Bundle b = new Bundle();
        b.putInt("lgid", getLeagueId());
        b.putInt("tmid", tm.getId());
        b.putInt("usltype", getUsltype());
        i.putExtras(b);
        startActivity(i);
    }

}