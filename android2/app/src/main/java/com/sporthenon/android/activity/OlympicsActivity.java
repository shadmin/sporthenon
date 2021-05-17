package com.sporthenon.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.R;
import com.sporthenon.android.async.AsyncOlympics;
import com.sporthenon.android.data.DataItem;

public class OlympicsActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle state) {
        index = INDEX_OLYMPICS;
        super.onCreate(state);
        Bundle b = getIntent().getExtras();
        setOlType(b.getInt("oltype"));
        String path = getString(getOlType() == 0 ? R.string.ol_winter : R.string.ol_summer);
        AsyncOlympics task = new AsyncOlympics(path);
        task.execute(this, getOlType());
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        DataItem ol = (DataItem) getList().getItemAtPosition(position);
        Intent i = new Intent(this, OlympicsModeActivity.class);
        Bundle b = new Bundle();
        b.putInt("olid", ol.getId());
        b.putString("olname", ol.getName());
        b.putInt("oltype", getOlType());
        i.putExtras(b);
        startActivity(i);
    }

}