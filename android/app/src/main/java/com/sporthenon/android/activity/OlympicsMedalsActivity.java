package com.sporthenon.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.async.AsyncMedals;

public class OlympicsMedalsActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle state) {
        index = INDEX_OLYMPICS;
        super.onCreate(state);
        Bundle b = getIntent().getExtras();
        setOlId(b.getInt("olid"));
        AsyncMedals task = new AsyncMedals(b.getString("olname"));
        task.execute(this, getOlId());
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
    }

}