package com.sporthenon.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.R;
import com.sporthenon.android.async.AsyncMedals;

public class OlympicsMedalsActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle state) {
        index = INDEX_OLYMPICS;
        super.onCreate(state);
        Bundle b = getIntent().getExtras();
        setOlId(b.getInt("olid"));
        setOlName(b.getString("olname"));
        setOlType(b.getInt("oltype"));
        AsyncMedals task = new AsyncMedals(getString(getOlType() == 0 ? R.string.ol_winter : R.string.ol_summer) + "\r\n" + getOlName());
        task.execute(this, getOlId());
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
    }

}