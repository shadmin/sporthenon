package com.sporthenon.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.sporthenon.R;
import com.sporthenon.android.async.AsyncChampionships;
import com.sporthenon.android.data.DataItem;
import com.sporthenon.android.data.IDataItem;

import java.util.ArrayList;

public class ChampionshipActivity extends Activity implements AdapterView.OnItemClickListener {

	private ArrayList<IDataItem> championships;
    private ListView list;
    private EditText search;
    private Integer sportId;

    public ArrayList<IDataItem> getChampionships() {
        if (championships == null)
            championships = new ArrayList<IDataItem>();
        return championships;
    }

    public void setChampionships(ArrayList<IDataItem> championships) {
        this.championships = championships;
    }

    public Integer getSportId() {
        return sportId;
    }

    public void setSportId(Integer sportId) {
        this.sportId = sportId;
    }

    public ListView getList() {
        return list;
    }

    public void setList(ListView list) {
        this.list = list;
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(this);

        search = (EditText) findViewById(R.id.search);
        search.setVisibility(View.INVISIBLE);

        Bundle b = getIntent().getExtras();
        setSportId(b.getInt("spid"));
        AsyncChampionships task = new AsyncChampionships();
        task.execute(getSportId(), this);
	}

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        DataItem cp = (DataItem) list.getItemAtPosition(position);
        Intent i = new Intent(this, EventActivity.class);
        Bundle b = new Bundle();
        b.putInt("spid", getSportId());
        b.putInt("cpid", cp.getId());
        i.putExtras(b);
        startActivity(i);
        finish();
    }

}