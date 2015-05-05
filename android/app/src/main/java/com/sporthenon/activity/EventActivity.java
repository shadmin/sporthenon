package com.sporthenon.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.sporthenon.R;
import com.sporthenon.async.AsyncEvents;
import com.sporthenon.data.DataItem;
import com.sporthenon.data.IDataItem;

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
        AsyncEvents task = new AsyncEvents();
        task.execute(b.getInt("spid"), this, b.getInt("cpid"), b.getInt("ev1id"));
	}

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        DataItem ev = (DataItem) list.getItemAtPosition(position);
        Intent i = new Intent(this, EventActivity.class);
        Bundle b = new Bundle();
        b.putInt("spid", getSportId());
        b.putInt("cpid", getChampionshipId());
        b.putInt("ev1id", ev.getId());
        //b.putInt("ev2id", ev.getId());
        //b.putInt("ev3id", ev.getId());
        i.putExtras(b);
        startActivity(i);
        finish();
    }

}