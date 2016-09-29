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
import com.sporthenon.android.activity.ResultActivity;
import com.sporthenon.android.adapter.ItemListAdapter;
import com.sporthenon.android.adapter.ResultListAdapter;
import com.sporthenon.android.data.DataItem;
import com.sporthenon.android.data.ResultItem;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private View view;
    protected static Activity activity;
    private static ArrayList<Object> itemList;
    protected static ProgressBar progress;
    protected static ListView list;
    protected static EditText search;
    protected static TextView path;
    protected static TextView notice;

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

    public ListView getList() {
        return list;
    }

    public void setList(ListView list_) {
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

    public static TextView getNotice() {
        return notice;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        list = (ListView) view.findViewById(R.id.list);
        list.setOnItemClickListener((AbstractActivity) this.getActivity());
        progress = (ProgressBar) view.findViewById(R.id.progress);
        path = (TextView) view.findViewById(R.id.path);
        path.setVisibility(path.getText().length() > 0 ? View.VISIBLE : View.GONE);
        notice = (TextView) view.findViewById(R.id.notice);
        notice.setVisibility(View.GONE);
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
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((AbstractActivity) activity).onSectionAttached(getArguments().getInt("section_number"));
    }

    public void filter() {
        String text = search.getText().toString().toLowerCase();
        list.setAdapter(null);
        if (activity instanceof ResultActivity) {
            ArrayList<ResultItem> list_ = new ArrayList<>();
            for (Object o : itemList) {
                ResultItem item = (ResultItem) o;
                if (text.length() == 0 || item.getYear().matches(".*" + text + ".*") || item.getTxt1().toLowerCase().matches(".*" + text + ".*"))
                    list_.add(item);
            }
            list.setAdapter(new ResultListAdapter(activity.getApplicationContext(), list_));
        }
        else {
            ArrayList<DataItem> list_ = new ArrayList<>();
            for (Object o : itemList) {
                DataItem item = (DataItem) o;
                if (text.length() == 0 || item.getName().toLowerCase().matches(".*" + text + ".*"))
                    list_.add(item);
            }
            list.setAdapter(new ItemListAdapter(activity.getApplicationContext(), list_));
        }
    }

    public static void onSearchClick() {
        search.setVisibility(search.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        if (search.getVisibility() == View.VISIBLE)
            search.requestFocus();
    }

    public static void hideProgress() {
        progress.setVisibility(View.GONE);
    }

}