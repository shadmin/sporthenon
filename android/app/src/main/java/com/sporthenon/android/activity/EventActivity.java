package com.sporthenon.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.sporthenon.R;
import com.sporthenon.android.async.AsyncEvents;
import com.sporthenon.android.data.DataItem;
import com.sporthenon.android.data.IDataItem;

import java.util.ArrayList;

public class EventActivity extends Activity implements AdapterView.OnItemClickListener {

	private ArrayList<IDataItem> events;
    private ListView list;
    private EditText search;
    private Integer sportId;
    private Integer championshipId;
    private Integer event1Id;
    private Integer event2Id;
    private Integer event3Id;

    public ArrayList<IDataItem> getEvents() {
        if (events == null)
            events = new ArrayList<IDataItem>();
        return events;
    }

    public void setEvents(ArrayList<IDataItem> events) {
        this.events = events;
    }

    public Integer getSportId() {
        return sportId;
    }

    public void setSportId(Integer sportId) {
        this.sportId = sportId;
    }

    public Integer getEvent1Id() {
        return event1Id;
    }

    public void setEvent1Id(Integer event1Id) {
        this.event1Id = event1Id;
    }

    public Integer getChampionshipId() {
        return championshipId;
    }

    public void setChampionshipId(Integer championshipId) {
        this.championshipId = championshipId;
    }

    public Integer getEvent2Id() {
        return event2Id;
    }

    public void setEvent2Id(Integer event2Id) {
        this.event2Id = event2Id;
    }

    public Integer getEvent3Id() {
        return event3Id;
    }

    public void setEvent3Id(Integer event3Id) {
        this.event3Id = event3Id;
    }

    public ListView getList() {
        return list;
    }

    public void setList(ListView list) {
        this.list = list;
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(this);

        search = (EditText) findViewById(R.id.search);
        search.setVisibility(View.INVISIBLE);

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
        finish();
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
        finish();
    }

}