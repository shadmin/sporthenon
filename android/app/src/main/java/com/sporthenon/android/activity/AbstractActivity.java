package com.sporthenon.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.sporthenon.android.R;
import com.sporthenon.android.adapter.ItemListAdapter;
import com.sporthenon.android.async.AsyncSports;
import com.sporthenon.android.data.DataItem;
import com.sporthenon.android.data.IDataItem;

import java.util.ArrayList;

public abstract class AbstractActivity extends Activity implements AdapterView.OnItemClickListener {

    protected ListView list;
    protected EditText search;
    protected Integer sportId;
    protected Integer championshipId;
    protected Integer event1Id;
    protected Integer event2Id;
    protected Integer event3Id;

    public ListView getList() {
        return list;
    }

    public void setList(ListView list) {
        this.list = list;
    }

    public Integer getSportId() {
        return sportId;
    }

    public void setSportId(Integer sportId) {
        this.sportId = sportId;
    }

    public Integer getChampionshipId() {
        return championshipId;
    }

    public void setChampionshipId(Integer championshipId) {
        this.championshipId = championshipId;
    }

    public Integer getEvent1Id() {
        return event1Id;
    }

    public void setEvent1Id(Integer event1Id) {
        this.event1Id = event1Id;
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

    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);

        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(this);

        search = (EditText) findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
            @Override
            public void afterTextChanged(Editable arg0) {
                //filter();
            }
        });
        search.setVisibility(View.GONE);
    }

    @Override
    public abstract void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3);

    @Override
    public void onBackPressed() {
        finish();
    }

	/*public void filter() {
        String name = search.getText().toString();
        ArrayList<IDataItem> listSportNew = new ArrayList<IDataItem>();
        for (IDataItem sport : sports)
            if (sport.getName().toLowerCase().toString().startsWith(name))
                listSportNew.add(sport);
        list.setAdapter(null);
        //if (listSportNew.size() == 0)
        //	listSportNew.add(new Sport(100, "ERREUR", getResources().getDrawable(R.drawable.cake)));
        list.setAdapter(new ItemListAdapter(getApplicationContext(), listSportNew));
    }*/

}