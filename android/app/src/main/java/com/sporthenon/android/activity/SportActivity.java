package com.sporthenon.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.async.AsyncSports;
import com.sporthenon.android.data.DataItem;

public class SportActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        AsyncSports task = new AsyncSports();
        task.execute(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        DataItem sp = (DataItem) getList().getItemAtPosition(position);
        Intent i = new Intent(this, ChampionshipActivity.class);
        Bundle b = new Bundle();
        b.putInt("spid", sp.getId());
        b.putString("spname", sp.getName());
        i.putExtras(b);
        startActivity(i);
    }

}