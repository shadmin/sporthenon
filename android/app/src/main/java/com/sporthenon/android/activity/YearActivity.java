package com.sporthenon.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.async.AsyncYears;
import com.sporthenon.android.data.DataItem;

public class YearActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle state) {
        index = INDEX_CALENDAR;
        super.onCreate(state);
        AsyncYears task = new AsyncYears();
        task.execute(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        DataItem sp = (DataItem) getList().getItemAtPosition(position);
        Intent i = new Intent(this, MonthActivity.class);
        Bundle b = new Bundle();
        b.putInt("yrid", sp.getId());
        b.putString("yrname", sp.getName());
        i.putExtras(b);
        startActivity(i);
    }

}