package com.sporthenon.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.R;

public class OlympicsModeActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle state) {
        index = INDEX_OLYMPICS;
        super.onCreate(state);
        Bundle b = getIntent().getExtras();
        setOlId(b.getInt("olid"));
        setOlName(b.getString("olname"));
        setOlType(b.getInt("oltype"));
    }

    @Override
    protected void onPostCreate(Bundle state) {
        super.onPostCreate(state);
        setPath(getString(getOlType() == 0 ? R.string.ol_winter : R.string.ol_summer) + "|" + getOlName());
    }

    public void onPodiumClick(View v) {
        nextActivity(OLMODE_PODIUM);
    }

    public void onMedalsClick(View v) {
        nextActivity(OLMODE_MEDALS);
    }

    public void nextActivity(Integer n) {
        if (n == OLMODE_PODIUM) {
            Intent i = new Intent(this, SportActivity.class);
            Bundle b = new Bundle();
            b.putInt("olid", getOlId());
            b.putInt("oltype", getOlType());
            i.putExtras(b);
            startActivity(i);
        }
        else if (n == OLMODE_MEDALS) {
            Intent i = new Intent(this, OlympicsMedalsActivity.class);
            Bundle b = new Bundle();
            b.putInt("olid", getOlId());
            b.putString("olname", getOlName());
            b.putInt("oltype", getOlType());
            i.putExtras(b);
            startActivity(i);
        }
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
    }

}