package com.sporthenon.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.R;
import com.sporthenon.android.adapter.ItemListAdapter;
import com.sporthenon.android.adapter.ResultListAdapter;
import com.sporthenon.android.async.AsyncResults;
import com.sporthenon.android.data.DataItem;
import com.sporthenon.android.data.ResultItem;
import com.sporthenon.android.utils.AndroidUtils;

import java.util.ArrayList;

public class ResultActivity extends AbstractActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        title.setText(R.string.results);
        Bundle b = getIntent().getExtras();
        AsyncResults task = new AsyncResults();
        task.execute(this, b.getInt("spid"), b.getInt("cpid"), b.getInt("ev1id"), b.getInt("ev2id"), b.getInt("ev3id"));
        setPath(b.getString("spname") + "\r\n" + b.getString("cpname") + (AndroidUtils.notEmpty(b.getString("ev1name")) ? "\r\n" + b.getString("ev1name") : "") + (AndroidUtils.notEmpty(b.getString("ev2name")) ? "\r\n" + b.getString("ev2name") : "") + (AndroidUtils.notEmpty(b.getString("ev3name")) ? "\r\n" + b.getString("ev3name") : ""));
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