package com.sporthenon.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.R;
import com.sporthenon.android.async.AsyncSports;
import com.sporthenon.android.data.DataItem;

public class SportActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle state) {
        index = INDEX_RESULTS;

        String path = null;
        int olid = -1;
        int oltype = -1;
        Bundle b = getIntent().getExtras();
        if (b != null) {
            index = INDEX_OLYMPICS;
            olid = b.getInt("olid");
            oltype = b.getInt("oltype");
            setOlId(olid);
            setOlName(b.getString("olname"));
            setOlType(b.getInt("oltype"));
            path = getString(getOlType() == 0 ? R.string.ol_winter : R.string.ol_summer) + "|" + getOlName() + "|" + getString(R.string.results);
        }

        super.onCreate(state);
        AsyncSports task = new AsyncSports(path);
        task.execute(this, olid, oltype);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        DataItem sp = (DataItem) getList().getItemAtPosition(position);
        Bundle b = new Bundle();
        b.putInt("spid", sp.getId());
        b.putString("spname", sp.getName());
        if (getOlId() != null) {
            Intent i = new Intent(this, OlympicsPodiumsActivity.class);
            b.putInt("olid", getOlId());
            b.putString("olname", getOlName());
            b.putInt("oltype", getOlType());
            i.putExtras(b);
            startActivity(i);
        }
        else {
            Intent i = new Intent(this, ChampionshipActivity.class);
            i.putExtras(b);
            startActivity(i);
        }
    }

}