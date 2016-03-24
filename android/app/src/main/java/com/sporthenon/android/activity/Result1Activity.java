package com.sporthenon.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.R;
import com.sporthenon.android.async.AsyncResult1;
import com.sporthenon.android.fragment.Result1Fragment;

public class Result1Activity extends AbstractActivity {

    protected void onCreate(Bundle state) {
        index = INDEX_RESULTS;
        super.onCreate(state);
        Bundle b = getIntent().getExtras();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, Result1Fragment.newInstance(index + 1, this)).commit();
        AsyncResult1 task = new AsyncResult1();
        task.execute(this, b.getInt("rsid"), b.getString("rsyr"));
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void onBackClick(View v) {
        finish();
    }

}