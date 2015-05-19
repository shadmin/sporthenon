package com.sporthenon.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.sporthenon.android.R;
import com.sporthenon.android.async.AsyncChampionships;
import com.sporthenon.android.async.AsyncResults;
import com.sporthenon.android.data.DataItem;
import com.sporthenon.android.data.IDataItem;
import com.sporthenon.android.data.IResultItem;

import java.util.ArrayList;

public class Result1Activity extends AbstractActivity implements AdapterView.OnItemClickListener {

    private ArrayList<IResultItem> results;

    public ArrayList<IResultItem> getResults() {
        if (results == null)
            results = new ArrayList<IResultItem>();
        return results;
    }

    public void setResults(ArrayList<IResultItem> results) {
        this.results = results;
    }

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        Bundle b = getIntent().getExtras();
        AsyncResults task = new AsyncResults();
        task.execute(b.getInt("spid"), this, b.getInt("cpid"), b.getInt("ev1id"), b.getInt("ev2id"));
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        /*DataItem cp = (DataItem) list.getItemAtPosition(position);
        Intent i = new Intent(this, EventActivity.class);
        Bundle b = new Bundle();
        b.putInt("spid", getSportId());
        b.putInt("cpid", cp.getId());
        i.putExtras(b);
        startActivity(i);
        finish();*/
    }

}