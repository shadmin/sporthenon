package com.sporthenon.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.R;
import com.sporthenon.android.async.AsyncChampionships;
import com.sporthenon.android.data.DataItem;

import java.util.ArrayList;

public class ChampionshipActivity extends AbstractActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        title.setText(R.string.championship);
        Bundle b = getIntent().getExtras();
        setSportId(b.getInt("spid"));
        setSportName(b.getString("spname"));
        AsyncChampionships task = new AsyncChampionships();
        task.execute(getSportId(), this);
        setPath(getSportName());
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        DataItem cp = (DataItem) list.getItemAtPosition(position);
        Intent i = new Intent(this, EventActivity.class);
        Bundle b = new Bundle();
        b.putInt("spid", getSportId());
        b.putString("spname", getSportName());
        b.putInt("cpid", cp.getId());
        b.putString("cpname", cp.getName());
        i.putExtras(b);
        startActivity(i);
    }

}