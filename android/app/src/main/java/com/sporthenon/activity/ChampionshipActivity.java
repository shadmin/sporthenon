package com.sporthenon.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.sporthenon.R;
import com.sporthenon.async.AsyncChampionships;
import com.sporthenon.data.DataItem;
import com.sporthenon.data.IDataItem;

import java.util.ArrayList;

public class ChampionshipActivity extends Activity implements AdapterView.OnItemClickListener {

	private ArrayList<IDataItem> championships;
    private ListView list;
    private EditText search;

    public ArrayList<IDataItem> getChampionships() {
        if (championships == null)
            championships = new ArrayList<IDataItem>();
        return championships;
    }

    public void setChampionships(ArrayList<IDataItem> championships) {
        this.championships = championships;
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
        AsyncChampionships task = new AsyncChampionships();
        task.execute(b.getInt("spid"), this);
	}

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        DataItem sp = (DataItem) list.getItemAtPosition(position);
        Intent i = new Intent(this, ChampionshipActivity.class);
        startActivity(i);
    }

}