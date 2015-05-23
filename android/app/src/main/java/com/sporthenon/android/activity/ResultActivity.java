package com.sporthenon.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.async.AsyncResults;
import com.sporthenon.android.data.ResultItem;

import java.util.ArrayList;

public class ResultActivity extends AbstractActivity implements AdapterView.OnItemClickListener {

    private ArrayList<ResultItem> results;

    public ArrayList<ResultItem> getResults() {
        if (results == null)
            results = new ArrayList<ResultItem>();
        return results;
    }

    public void setResults(ArrayList<ResultItem> results) {
        this.results = results;
    }

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        Bundle b = getIntent().getExtras();
        AsyncResults task = new AsyncResults();
        task.execute(this, b.getInt("spid"), b.getInt("cpid"), b.getInt("ev1id"), b.getInt("ev2id"), b.getInt("ev3id"));
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        ResultItem rs = (ResultItem) list.getItemAtPosition(position);
        Intent i = new Intent(this, Result1Activity.class);
        Bundle b = new Bundle();
        b.putInt("rsid", rs.getId());
        b.putString("rsyr", rs.getYear());
        i.putExtras(b);
        startActivity(i);
    }

}