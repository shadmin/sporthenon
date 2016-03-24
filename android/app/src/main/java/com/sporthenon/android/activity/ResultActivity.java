package com.sporthenon.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.async.AsyncResults;
import com.sporthenon.android.data.ResultItem;
import com.sporthenon.android.utils.AndroidUtils;

public class ResultActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle state) {
        index = INDEX_RESULTS;
        super.onCreate(state);
        Bundle b = getIntent().getExtras();
        String path = b.getString("spname") + "\r\n" + b.getString("cpname") + (AndroidUtils.notEmpty(b.getString("ev1name")) ? "\r\n" + b.getString("ev1name") : "") + (AndroidUtils.notEmpty(b.getString("ev2name")) ? "\r\n" + b.getString("ev2name") : "") + (AndroidUtils.notEmpty(b.getString("ev3name")) ? "\r\n" + b.getString("ev3name") : "");
        AsyncResults task = new AsyncResults(path);
        task.execute(this, b.getInt("spid"), b.getInt("cpid"), b.getInt("ev1id"), b.getInt("ev2id"), b.getInt("ev3id"));
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        ResultItem rs = (ResultItem) getList().getItemAtPosition(position);
        Intent i = new Intent(this, Result1Activity.class);
        Bundle b = new Bundle();
        b.putInt("rsid", rs.getId());
        b.putString("rsyr", rs.getYear());
        i.putExtras(b);
        startActivity(i);
    }

}