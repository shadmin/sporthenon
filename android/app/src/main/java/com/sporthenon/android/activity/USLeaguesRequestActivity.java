package com.sporthenon.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.R;
import com.sporthenon.android.async.AsyncHallOfFame;
import com.sporthenon.android.async.AsyncRetiredNumbers;
import com.sporthenon.android.async.AsyncStatsLeaders;
import com.sporthenon.android.async.AsyncTeamStadiums;
import com.sporthenon.android.async.AsyncUSChampionships;
import com.sporthenon.android.async.AsyncUSRecords;

public class USLeaguesRequestActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle state) {
        index = INDEX_USLEAGUES;
        super.onCreate(state);
        Bundle b = getIntent().getExtras();
        setLeagueId(b.getInt("lgid"));
        setLeagueName(b.getString("lgname"));
        setUsltype(b.getInt("usltype"));
        setRctype(b.getString("rctype"));
        String path = getLeagueName() + "|";
        if (getUsltype() == USTYPE_CHAMPIONSHIPS)
            new AsyncUSChampionships(path + getString(R.string.us_type1)).execute(this, getLeagueId());
        else if (getUsltype() == USTYPE_RECORDS) {
            Integer evid = b.getInt("ev1id");
            new AsyncUSRecords(path + getString(R.string.us_type2) + "|" + b.getString("ev1name")).execute(this, getLeagueId(), evid, getRctype());
        }
        else if (getUsltype() == USTYPE_STATS) {
            Integer evid = b.getInt("ev1id");
            new AsyncStatsLeaders(path + getString(R.string.us_type3) + "|" + b.getString("ev1name")).execute(this, getLeagueId(), evid);
        }
        else if (getUsltype() == USTYPE_HOF) {
            Integer yrid = b.getInt("yrid");
            new AsyncHallOfFame(path + getString(R.string.us_type4) + "|" + b.getString("yrname")).execute(this, getLeagueId(), yrid);
        }
        else if (getUsltype() == USTYPE_RETIRED_NUM) {
            Integer tmid = b.getInt("tmid");
            new AsyncRetiredNumbers(path + getString(R.string.us_type5) + "|" + b.getString("tmname")).execute(this, getLeagueId(), tmid);
        }
        else if (getUsltype() == USTYPE_TEAM_STADIUMS) {
            Integer tmid = b.getInt("tmid");
            new AsyncTeamStadiums(path + getString(R.string.us_type6) + "|" + b.getString("tmname")).execute(this, getLeagueId(), tmid);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
    }

}