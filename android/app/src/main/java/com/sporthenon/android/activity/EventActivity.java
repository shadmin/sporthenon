package com.sporthenon.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.R;
import com.sporthenon.android.async.AsyncEvents;
import com.sporthenon.android.data.DataItem;

public class EventActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle state) {
        index = INDEX_RESULTS;
        Bundle b = getIntent().getExtras();
        setSportId(b.getInt("spid"));
        setSportName(b.getString("spname"));
        setChampionshipId(b.getInt("cpid"));
        setChampionshipName(b.getString("cpname"));
        setEvent1Id(b.getInt("ev1id"));
        setEvent1Name(b.getString("ev1name"));
        setEvent2Id(b.getInt("ev2id"));
        setEvent2Name(b.getString("ev2name"));
        setEvent3Id(b.getInt("ev3id"));
        setEvent3Name(b.getString("ev3name"));
        setOlId(b.getInt("olid"));
        if (getOlId() != null && getOlId() > 0) {
            index = INDEX_OLYMPICS;
            setChampionshipId(1);
            setChampionshipName(getString(R.string.olympic_games));
        }
        else {
            setLeagueId(b.getInt("lgid"));
            setUsltype(b.getInt("usltype"));
            if (getUsltype() != null && (getUsltype() == USTYPE_RECORDS || getUsltype() == USTYPE_STATS))
                index = INDEX_USLEAGUES;
        }
        super.onCreate(state);
        String path = getSportName() + "\r\n" + getChampionshipName() + (getEvent1Name() != null ? "\r\n" + getEvent1Name() : "") + (getEvent2Name() != null ? "\r\n" + getEvent2Name() : "");
        AsyncEvents task = new AsyncEvents(path);
        task.execute(this, getSportId(), getChampionshipId(), getEvent1Id(), getEvent2Id(), getEvent3Id());
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        DataItem ev = (DataItem) getList().getItemAtPosition(position);
        boolean isResults = false;
        Bundle b = new Bundle();
        b.putInt("spid", getSportId());
        b.putString("spname", getSportName());
        b.putInt("cpid", getChampionshipId());
        b.putString("cpname", getChampionshipName());
        b.putInt("olid", getOlId());
        if (getEvent1Id() != null && getEvent1Id() > 0) {
            b.putInt("ev1id", getEvent1Id());
            b.putString("ev1name", getEvent1Name());
            if (getEvent2Id() != null && getEvent2Id() > 0) {
                b.putInt("ev2id", getEvent2Id());
                b.putString("ev2name", getEvent2Name());
                b.putInt("ev3id", ev.getId());
                b.putString("ev3name", ev.getName());
                isResults = true;
            }
            else {
                b.putInt("ev2id", ev.getId());
                b.putString("ev2name", ev.getName());
                if (ev.getParam() != null && ev.getParam() == 0)
                    isResults = true;
            }
        }
        else {
            b.putInt("ev1id", ev.getId());
            b.putString("ev1name", ev.getName());
            if (ev.getParam() != null && ev.getParam() == 0)
                isResults = true;
        }
        Intent i = null;
        if (isResults)
            i = new Intent(this, ResultActivity.class);
        else
            i = new Intent(this, EventActivity.class);
        i.putExtras(b);
        startActivity(i);
    }

}