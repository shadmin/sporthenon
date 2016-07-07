package com.sporthenon.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.R;
import com.sporthenon.android.fragment.OlympicsModeFragment;

public class OlympicsModeActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle state) {
        index = INDEX_OLYMPICS;
        super.onCreate(state);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, OlympicsModeFragment.newInstance(index + 1, this)).commit();
    }

    public void onPodiumClick(View v) {
        nextActivity(OLMODE_PODIUM);
    }

    public void onMedalsClick(View v) {
        nextActivity(OLMODE_MEDALS);
    }

    public void nextActivity(Integer n) {
        Intent i = new Intent(this, OlympicsActivity.class);
        Bundle b = new Bundle();
        b.putInt("oltype", n);
        i.putExtras(b);
        startActivity(i);
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
    }

}