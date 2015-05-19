package com.sporthenon.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.sporthenon.android.R;
import com.sporthenon.android.adapter.ItemListAdapter;
import com.sporthenon.android.async.AsyncSports;
import com.sporthenon.android.data.DataItem;
import com.sporthenon.android.data.IDataItem;

import java.util.ArrayList;

public class SportActivity extends Activity implements AdapterView.OnItemClickListener {

	private ArrayList<IDataItem> sports;
    private ListView list;
    private EditText search;

    public ArrayList<IDataItem> getSports() {
        if (sports == null)
            sports = new ArrayList<IDataItem>();
        return sports;
    }

    public void setSports(ArrayList<IDataItem> sports) {
        this.sports = sports;
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
		search.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
                filter();
			}
		});
        search.setVisibility(View.INVISIBLE);

        AsyncSports task = new AsyncSports();
        task.execute(this);
	}

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        DataItem sp = (DataItem) list.getItemAtPosition(position);
        Intent i = new Intent(this, ChampionshipActivity.class);
        Bundle b = new Bundle();
        b.putInt("spid", sp.getId());
        i.putExtras(b);
        startActivity(i);
        finish();
    }

	public void filter() {
		String name = search.getText().toString();
		ArrayList<IDataItem> listSportNew = new ArrayList<IDataItem>();
		for (IDataItem sport : sports)
			if (sport.getName().toLowerCase().toString().startsWith(name))
				listSportNew.add(sport);
		list.setAdapter(null);
		//if (listSportNew.size() == 0)
		//	listSportNew.add(new Sport(100, "ERREUR", getResources().getDrawable(R.drawable.cake)));
		list.setAdapter(new ItemListAdapter(getApplicationContext(), listSportNew));
	}

}