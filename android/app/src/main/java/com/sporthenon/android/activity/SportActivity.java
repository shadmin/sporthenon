package com.sporthenon.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.async.AsyncSports;

public class SportActivity extends AbstractActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        AsyncSports task = new AsyncSports();
        task.execute(this);
        //getPath().setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
       /* DataItem sp = (DataItem) list.getItemAtPosition(position);
        Intent i = new Intent(this, ChampionshipActivity.class);
        Bundle b = new Bundle();
        b.putInt("spid", sp.getId());
        b.putString("spname", sp.getName());
        i.putExtras(b);
        startActivity(i);*/
    }

}