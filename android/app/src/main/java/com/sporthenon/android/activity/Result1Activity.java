package com.sporthenon.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.sporthenon.R;
import com.sporthenon.android.async.AsyncResults;
import com.sporthenon.android.data.IResultItem;

import java.util.ArrayList;

public class Result1Activity extends Activity implements AdapterView.OnItemClickListener {

	private ArrayList<IResultItem> results;
    private ListView list;
    private EditText search;

    public ArrayList<IResultItem> getResults() {
        if (results == null)
            results = new ArrayList<IResultItem>();
        return results;
    }

    public void setResults(ArrayList<IResultItem> results) {
        this.results = results;
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
        AsyncResults task = new AsyncResults();
        task.execute(b.getInt("spid"), this, b.getInt("cpid"), b.getInt("ev1id"), b.getInt("ev2id"));
	}

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        /*DataItem cp = (DataItem) list.getItemAtPosition(position);
        Intent i = new Intent(this, EventActivity.class);
        Bundle b = new Bundle();
        b.putInt("spid", getSportId());
        b.putInt("cpid", cp.getId());
        i.putExtras(b);
        startActivity(i);
        finish();*/
    }

}