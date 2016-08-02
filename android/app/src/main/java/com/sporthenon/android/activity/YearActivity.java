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
        Bundle b = getIntent().getExtras();
        if (b != null) {
            setLeagueId(b.getInt("lgid"));
            setUsltype(b.getInt("usltype"));
        }
        if (getUsltype() != null && getUsltype() == USTYPE_HOF)
            index = INDEX_USLEAGUES;
        super.onCreate(state);
        AsyncYears task = new AsyncYears();
        task.execute(this, getLeagueId());
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        DataItem yr = (DataItem) getList().getItemAtPosition(position);
        Intent i = null;
        Bundle b = new Bundle();
        if (getUsltype() != null && getUsltype() == USTYPE_HOF) {
            i = new Intent(this, USLeaguesRequestActivity.class);
            b.putInt("year", yr.getId());
            b.putInt("lgid", getLeagueId());
            b.putInt("usltype", getUsltype());
        }
        else {
            i = new Intent(this, MonthActivity.class);
            b.putInt("year", Integer.parseInt(yr.getName()));
        }
        i.putExtras(b);
        startActivity(i);
    }

}