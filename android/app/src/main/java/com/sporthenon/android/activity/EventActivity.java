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
        setChampionshipId(b.getInt("cpid"));
        setEvent1Id(b.getInt("ev1id"));
        setEvent2Id(b.getInt("ev2id"));
        setEvent3Id(b.getInt("ev3id"));
        if (getEvent3Id() != null && getEvent3Id() > 0)
            loadResults(getSportId(), getChampionshipId(), getEvent1Id(), getEvent2Id(), getEvent3Id());
        else {
            AsyncEvents task = new AsyncEvents();
            task.execute(this, b.getInt("spid"), b.getInt("cpid"), b.getInt("ev1id"), b.getInt("ev2id"), b.getInt("ev3id"));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        DataItem ev = (DataItem) list.getItemAtPosition(position);
        Intent i = new Intent(this, EventActivity.class);
        Bundle b = new Bundle();
        b.putInt("spid", getSportId());
        b.putInt("cpid", getChampionshipId());
        if (getEvent1Id() != null && getEvent1Id() > 0) {
            b.putInt("ev1id", getEvent1Id());
            if (getEvent2Id() != null && getEvent2Id() > 0) {
                b.putInt("ev2id", getEvent2Id());
                b.putInt("ev3id", ev.getId());
            }
            else
                b.putInt("ev2id", ev.getId());
        }
        else
            b.putInt("ev1id", ev.getId());
        i.putExtras(b);
        startActivity(i);
    }

    public void loadResults(Integer sp, Integer cp, Integer ev1, Integer ev2, Integer ev3) {
        Intent i = new Intent(this, ResultActivity.class);
        Bundle b = new Bundle();
        b.putInt("spid", getSportId());
        b.putInt("cpid", getChampionshipId());
        b.putInt("ev1id", getEvent1Id());
        b.putInt("ev2id", getEvent2Id());
        b.putInt("ev3id", getEvent3Id());
        i.putExtras(b);
        startActivity(i);
    }

}