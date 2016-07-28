package com.sporthenon.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.async.AsyncHallOfFame;
import com.sporthenon.android.async.AsyncRetiredNumbers;
import com.sporthenon.android.async.AsyncTeamStadiums;
import com.sporthenon.android.async.AsyncUSChampionships;

public class USLeaguesRequestActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle state) {
        index = INDEX_USLEAGUES;
        super.onCreate(state);
        Bundle b = getIntent().getExtras();
        setLeagueId(b.getInt("lgid"));
        setUsltype(b.getInt("usltype"));
        if (getUsltype() == USTYPE_CHAMPIONSHIPS)
            new AsyncUSChampionships().execute(this, getLeagueId());
        else if (getUsltype() == USTYPE_HOF) {
            Integer yrid = b.getInt("year");
            new AsyncHallOfFame().execute(this, getLeagueId(), yrid);
        }
        else if (getUsltype() == USTYPE_RETIRED_NUM) {
            Integer tmid = b.getInt("tmid");
            new AsyncRetiredNumbers().execute(this, getLeagueId(), tmid);
        }
        else if (getUsltype() == USTYPE_TEAM_STADIUMS) {
            Integer tmid = b.getInt("tmid");
            new AsyncTeamStadiums().execute(this, getLeagueId(), tmid);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
    }

}