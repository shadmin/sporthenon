package com.sporthenon.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.R;
import com.sporthenon.android.async.AsyncEvents;
import com.sporthenon.android.data.DataItem;

import java.util.ArrayList;

public class EventActivity extends AbstractActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        title.setText(R.string.event);
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
        AsyncEvents task = new AsyncEvents();
        task.execute(this, b.getInt("spid"), b.getInt("cpid"), b.getInt("ev1id"), b.getInt("ev2id"), b.getInt("ev3id"));
        setPath(getSportName() + "\r\n" + getChampionshipName() + (getEvent1Name() != null ? "\r\n" + getEvent1Name() : "") + (getEvent2Name() != null ? "\r\n" + getEvent2Name() : ""));
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        DataItem ev = (DataItem) list.getItemAtPosition(position);
        Intent i = new Intent(this, EventActivity.class);
        Bundle b = new Bundle();
        b.putInt("spid", getSportId());
        b.putString("spname", getSportName());
        b.putInt("cpid", getChampionshipId());
        b.putString("cpname", getChampionshipName());
        if (getEvent1Id() != null && getEvent1Id() > 0) {
            b.putInt("ev1id", getEvent1Id());
            b.putString("ev1name", getEvent1Name());
            if (getEvent2Id() != null && getEvent2Id() > 0) {
                b.putInt("ev2id", getEvent2Id());
                b.putString("ev2name", getEvent2Name());
                b.putInt("ev3id", ev.getId());
                b.putString("ev3name", ev.getName());
                setEvent3Id(ev.getId());
                setEvent3Name(ev.getName());
                loadResults();
            }
            else {
                b.putInt("ev2id", ev.getId());
                b.putString("ev2name", ev.getName());
            }
        }
        else {
            b.putInt("ev1id", ev.getId());
            b.putString("ev1name", ev.getName());
        }
        i.putExtras(b);
        startActivity(i);
    }

    public void loadResults() {
        Intent i = new Intent(this, ResultActivity.class);
        Bundle b = new Bundle();
        b.putInt("spid", getSportId());
        b.putString("spname", getSportName());
        b.putInt("cpid", getChampionshipId());
        b.putString("cpname", getChampionshipName());
        b.putInt("ev1id", getEvent1Id());
        b.putString("ev1name", getEvent1Name());
        b.putInt("ev2id", getEvent2Id());
        b.putString("ev2name", getEvent2Name());
        b.putInt("ev3id", getEvent3Id());
        b.putString("ev3name", getEvent3Name());
        i.putExtras(b);
        startActivity(i);
    }

}