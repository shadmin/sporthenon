package com.sporthenon.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.async.AsyncChampionships;
import com.sporthenon.android.data.DataItem;

public class ChampionshipActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        Bundle b = getIntent().getExtras();
        setSportId(b.getInt("spid"));
        setSportName(b.getString("spname"));
        String path = getSportName();
        AsyncChampionships task = new AsyncChampionships(path);
        task.execute(getSportId(), this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        DataItem cp = (DataItem) getList().getItemAtPosition(position);
        Intent i = new Intent(this, EventActivity.class);
        Bundle b = new Bundle();
        b.putInt("spid", getSportId());
        b.putString("spname", getSportName());
        b.putInt("cpid", cp.getId());
        b.putString("cpname", cp.getName());
        i.putExtras(b);
        startActivity(i);
    }

}