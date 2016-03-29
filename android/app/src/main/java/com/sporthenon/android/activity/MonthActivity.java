package com.sporthenon.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.async.AsyncMonths;
import com.sporthenon.android.data.DataItem;

public class MonthActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle state) {
        index = INDEX_CALENDAR;
        super.onCreate(state);
        Bundle b = getIntent().getExtras();
        setClYear(b.getInt("year"));
        String path = String.valueOf(getClYear());
        AsyncMonths task = new AsyncMonths(path);
        task.execute(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        DataItem mt = (DataItem) getList().getItemAtPosition(position);
        Intent i = new Intent(this, CalendarActivity.class);
        Bundle b = new Bundle();
        b.putInt("year", getClYear());
        b.putInt("month", mt.getId());
        i.putExtras(b);
        startActivity(i);
    }

}