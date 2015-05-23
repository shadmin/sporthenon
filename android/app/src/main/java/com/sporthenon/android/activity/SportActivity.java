package com.sporthenon.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.async.AsyncSports;
import com.sporthenon.android.data.DataItem;

import java.util.ArrayList;

public class SportActivity extends AbstractActivity implements AdapterView.OnItemClickListener {

	private ArrayList<DataItem> sports;

    public ArrayList<DataItem> getSports() {
        if (sports == null)
            sports = new ArrayList<DataItem>();
        return sports;
    }

    public void setSports(ArrayList<DataItem> sports) {
        this.sports = sports;
    }

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        AsyncSports task = new AsyncSports();
        task.execute(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        DataItem sp = (DataItem) list.getItemAtPosition(position);
        Intent i = new Intent(this, ChampionshipActivity.class);
        Bundle b = new Bundle();
        b.putInt("spid", sp.getId());
        i.putExtras(b);
        startActivity(i);
    }

}