package com.sporthenon.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.sporthenon.android.async.AsyncCalendar;
import com.sporthenon.android.data.CalendarItem;
import com.sporthenon.android.utils.AndroidUtils;

public class CalendarActivity extends AbstractActivity {

    protected void onCreate(Bundle state) {
        index = INDEX_CALENDAR;
        super.onCreate(state);
        Bundle b = getIntent().getExtras();
        int y = b.getInt("year");
        int m = b.getInt("month");
        setClYear(y);
        setClMonth(m);
        String path = getString(AndroidUtils.getMonthRes(m)) + " " + y;
        String dt1 = y + (m < 10 ? "0" : "") + m + "01";
        m++;
        if (m > 12) {
            m = 1;
            y++;
        }
        String dt2 = y + (m < 10 ? "0" : "") + m + "01";
        AsyncCalendar task = new AsyncCalendar(path);
        task.execute(this, dt1, dt2);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        CalendarItem cl = (CalendarItem) getList().getItemAtPosition(position);
        Intent i = new Intent(this, Result1Activity.class);
        Bundle b = new Bundle();
        b.putInt("rsid", cl.getId());
        b.putString("rsyr", String.valueOf(getClYear()));
        b.putBoolean("cl", true);
        i.putExtras(b);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void onBackClick(View v) {
        finish();
    }

}