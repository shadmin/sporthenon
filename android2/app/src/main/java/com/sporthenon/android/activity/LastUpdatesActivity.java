package com.sporthenon.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.R;
import com.sporthenon.android.async.AsyncLastUpdates;
import com.sporthenon.android.async.AsyncSports;
import com.sporthenon.android.data.DataItem;
import com.sporthenon.android.data.LastUpdateItem;
import com.sporthenon.android.data.ResultItem;

public class LastUpdatesActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle state) {
        index = INDEX_LAST_UPDATES;

        super.onCreate(state);
        AsyncLastUpdates task = new AsyncLastUpdates(null);
        task.execute(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        LastUpdateItem item = (LastUpdateItem) getList().getItemAtPosition(position);
        Intent i = new Intent(this, Result1Activity.class);
        Bundle b = new Bundle();
        b.putInt("rsid", item.getIdResult());
        b.putString("rsyr", item.getYear());
        b.putBoolean("cl", false);
        i.putExtras(b);
        startActivity(i);
    }

}