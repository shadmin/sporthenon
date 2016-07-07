package com.sporthenon.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sporthenon.android.R;
import com.sporthenon.android.activity.AbstractActivity;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    protected static Activity activity;
    private static ArrayList<Object> itemList;
    protected static ProgressBar progress;
    protected static ListView list;
    protected static EditText search;
    protected static TextView path;

    public static ListFragment newInstance(int n, Activity activity_) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt("section_number", n);
        fragment.setArguments(args);
        activity = activity_;
        return fragment;
    }

    public ListFragment() {
    }

    public static ListView getList() {
        return list;
    }

    public static void setList(ListView list_) {
        list = list_;
    }

    public static ArrayList<Object> getItemList() {
        if (itemList == null)
            itemList = new ArrayList<>();
        return itemList;
    }

    public static TextView getPath() {
        return path;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        list = (ListView) view.findViewById(R.id.list);
        list.setOnItemClickListener((AbstractActivity) this.getActivity());
        progress = (ProgressBar) view.findViewById(R.id.progress);
        path = (TextView) view.findViewById(R.id.path1);
        path.setVisibility(View.GONE);
        search = (EditText) view.findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                filter();
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
        search.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((AbstractActivity) activity).onSectionAttached(getArguments().getInt("section_number"));
    }

    public void filter() {
       /* String text = search.getText().toString().toLowerCase();
        list.setAdapter(null);
        if (this instanceof ResultActivity) {
            ArrayList<ResultItem> list_ = new ArrayList<>();
            for (Object o : itemList) {
                ResultItem item = (ResultItem) o;
                if (text.length() == 0 || item.getTxt1().toLowerCase().matches(".*" + text + ".*"))
                    list_.add(item);
            }
            list.setAdapter(new ResultListAdapter(getApplicationContext(), list_));
        }
        else {
            ArrayList<DataItem> list_ = new ArrayList<>();
            for (Object o : itemList) {
                DataItem item = (DataItem) o;
                if (text.length() == 0 || item.getName().toLowerCase().matches(".*" + text + ".*"))
                    list_.add(item);
            }
            list.setAdapter(new ItemListAdapter(getApplicationContext(), list_));
        }*/
    }

    public void onSearchClick(View v) {
        search.setVisibility(search.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        if (search.getVisibility() == View.VISIBLE)
            search.requestFocus();
    }

    public static void hideProgress() {
        progress.setVisibility(View.GONE);
    }

}