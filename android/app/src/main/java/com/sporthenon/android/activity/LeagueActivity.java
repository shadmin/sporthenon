package com.sporthenon.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.R;
import com.sporthenon.android.fragment.LeagueFragment;

public class LeagueActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle state) {
        index = INDEX_USLEAGUES;
        super.onCreate(state);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, LeagueFragment.newInstance(index + 1, this)).commit();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        /*DataItem sp = (DataItem) getList().getItemAtPosition(position);
        Intent i = new Intent(this, ChampionshipActivity.class);
        Bundle b = new Bundle();
        b.putInt("spid", sp.getId());
        b.putString("spname", sp.getName());
        i.putExtras(b);
        startActivity(i);*/
    }

}