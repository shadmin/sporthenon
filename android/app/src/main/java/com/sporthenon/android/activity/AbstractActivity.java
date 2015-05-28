package com.sporthenon.android.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.sporthenon.android.R;
import com.sporthenon.android.adapter.ItemListAdapter;
import com.sporthenon.android.adapter.ResultListAdapter;
import com.sporthenon.android.data.DataItem;
import com.sporthenon.android.data.ResultItem;

import java.util.ArrayList;

public abstract class AbstractActivity extends Activity implements AdapterView.OnItemClickListener {

    protected String lang;
    protected TextView title;
    private ArrayList<Object> itemList;
    protected ListView list;
    protected EditText search;
    protected Integer sportId;
    protected Integer championshipId;
    protected Integer event1Id;
    protected Integer event2Id;
    protected Integer event3Id;

    public ArrayList<Object> getItemList() {
        if (itemList == null)
            itemList = new ArrayList<>();
        return itemList;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public ListView getList() {
        return list;
    }

    public void setList(ListView list) {
        this.list = list;
    }

    public Integer getSportId() {
        return sportId;
    }

    public void setSportId(Integer sportId) {
        this.sportId = sportId;
    }

    public Integer getChampionshipId() {
        return championshipId;
    }

    public void setChampionshipId(Integer championshipId) {
        this.championshipId = championshipId;
    }

    public Integer getEvent1Id() {
        return event1Id;
    }

    public void setEvent1Id(Integer event1Id) {
        this.event1Id = event1Id;
    }

    public Integer getEvent2Id() {
        return event2Id;
    }

    public void setEvent2Id(Integer event2Id) {
        this.event2Id = event2Id;
    }

    public Integer getEvent3Id() {
        return event3Id;
    }

    public void setEvent3Id(Integer event3Id) {
        this.event3Id = event3Id;
    }

    protected void onCreate(Bundle state) {
        super.onCreate(state);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        title = (TextView) findViewById(R.id.title);

        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(this);

        search = (EditText) findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {filter();}
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
            @Override
            public void afterTextChanged(Editable arg0) { }
        });
        search.setVisibility(View.GONE);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        lang = prefs.getString("lang", null);
    }

    @Override
    public abstract void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3);

    @Override
    public void onBackPressed() {
        finish();
    }

    public void onBackClick(View v) {
        finish();
    }

    public void onSearchClick(View v) {
        search.setVisibility(search.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        if (search.getVisibility() == View.VISIBLE)
            search.requestFocus();
    }

	public void filter() {
        String text = search.getText().toString().toLowerCase();
        list.setAdapter(null);
        if (this instanceof ResultActivity) {
            ArrayList<ResultItem> list_ = new ArrayList<>();
            for (Object o : itemList) {
                ResultItem item = (ResultItem) o;
                if (text.length() == 0 || item.getRank1().toLowerCase().matches(".*" + text + ".*"))
                    list_.add(item);
            }
            if (list_.size() == 0)
                list_.add(new ResultItem(0, null, getResources().getDrawable(R.drawable.error), String.valueOf(R.string.error)));
            list.setAdapter(new ResultListAdapter(getApplicationContext(), list_));
        }
        else {
            ArrayList<DataItem> list_ = new ArrayList<>();
            for (Object o : itemList) {
                DataItem item = (DataItem) o;
                if (text.length() == 0 || item.getName().toLowerCase().matches(".*" + text + ".*"))
                    list_.add(item);
            }
            if (list_.size() == 0)
                list_.add(new DataItem(0, String.valueOf(R.string.error), getResources().getDrawable(R.drawable.error)));
            list.setAdapter(new ItemListAdapter(getApplicationContext(), list_));
        }
    }

}