package com.sporthenon.android.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.sporthenon.android.R;
import com.sporthenon.android.activity.AbstractActivity;
import com.sporthenon.android.activity.CalendarActivity;
import com.sporthenon.android.activity.LastUpdatesActivity;
import com.sporthenon.android.activity.OlympicsMedalsActivity;
import com.sporthenon.android.activity.OlympicsPodiumsActivity;
import com.sporthenon.android.activity.ResultActivity;
import com.sporthenon.android.activity.USLeaguesRequestActivity;
import com.sporthenon.android.adapter.CalendarListAdapter;
import com.sporthenon.android.adapter.ItemListAdapter;
import com.sporthenon.android.adapter.LastUpdateListAdapter;
import com.sporthenon.android.adapter.MedalListAdapter;
import com.sporthenon.android.adapter.PodiumListAdapter;
import com.sporthenon.android.adapter.RecordListAdapter;
import com.sporthenon.android.adapter.ResultListAdapter;
import com.sporthenon.android.data.CalendarItem;
import com.sporthenon.android.data.DataItem;
import com.sporthenon.android.data.LastUpdateItem;
import com.sporthenon.android.data.MedalItem;
import com.sporthenon.android.data.PodiumItem;
import com.sporthenon.android.data.RecordItem;
import com.sporthenon.android.data.ResultItem;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private View view;
    protected static Activity activity;
    private static ArrayList<Object> itemList;
    protected static ProgressBar progress;
    protected static ListView list;
    protected static EditText filterText;
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
        filterText = (EditText) view.findViewById(R.id.filter_text);
        filterText.addTextChangedListener(new TextWatcher() {
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
        filterText.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((AbstractActivity) activity).onSectionAttached(getArguments().getInt("section_number"));
    }

    public void filter() {
        String text = filterText.getText().toString().toLowerCase();
        list.setAdapter(null);
        if (activity instanceof ResultActivity) {
            ArrayList<ResultItem> list_ = new ArrayList<>();
            for (Object o : itemList) {
                ResultItem item = (ResultItem) o;
                if (text.length() == 0 || item.getYear().matches(".*" + text + ".*") || item.getTxt1().toLowerCase().matches(".*" + text + ".*")) {
                    list_.add(item);
                }
            }
            list.setAdapter(new ResultListAdapter(activity.getApplicationContext(), list_));
        }
        else if (activity instanceof CalendarActivity) {
            ArrayList<CalendarItem> list_ = new ArrayList<>();
            for (Object o : itemList) {
                CalendarItem item = (CalendarItem) o;
                if (text.length() == 0 || item.getSport().toLowerCase().matches(".*" + text + ".*") || item.getEvent().toLowerCase().matches(".*" + text + ".*")) {
                    list_.add(item);
                }
            }
            list.setAdapter(new CalendarListAdapter(activity.getApplicationContext(), list_));
        }
        else if (activity instanceof OlympicsMedalsActivity) {
            ArrayList<MedalItem> list_ = new ArrayList<>();
            for (Object o : itemList) {
                MedalItem item = (MedalItem) o;
                if (text.length() == 0 || item.getCountry().toLowerCase().matches(".*" + text + ".*")) {
                    list_.add(item);
                }
            }
            list.setAdapter(new MedalListAdapter(activity.getApplicationContext(), list_));
        }
        else if (activity instanceof OlympicsPodiumsActivity) {
            ArrayList<PodiumItem> list_ = new ArrayList<>();
            for (Object o : itemList) {
                PodiumItem item = (PodiumItem) o;
                if (text.length() == 0 || item.getEvent().toLowerCase().matches(".*" + text + ".*") || item.getTxt1().toLowerCase().matches(".*" + text + ".*")) {
                    list_.add(item);
                }
            }
            list.setAdapter(new PodiumListAdapter(activity.getApplicationContext(), list_));
        }
        else if (activity instanceof USLeaguesRequestActivity && ((AbstractActivity) activity).getUsltype() == AbstractActivity.USTYPE_RECORDS) {
            ArrayList<RecordItem> list_ = new ArrayList<>();
            for (Object o : itemList) {
                RecordItem item = (RecordItem) o;
                if (text.length() == 0 || item.getLabel().toLowerCase().matches(".*" + text + ".*") || item.getType1().toLowerCase().matches(".*" + text + ".*") || item.getType2().toLowerCase().matches(".*" + text + ".*")) {
                    list_.add(item);
                }
            }
            list.setAdapter(new RecordListAdapter(activity.getApplicationContext(), list_));
        }
        else if (activity instanceof LastUpdatesActivity) {
            ArrayList<LastUpdateItem> list_ = new ArrayList<>();
            for (Object o : itemList) {
                LastUpdateItem item = (LastUpdateItem) o;
                if (text.length() == 0 || item.getYear().matches(".*" + text + ".*") || item.getSport().toLowerCase().matches(".*" + text + ".*") || item.getPos1().toLowerCase().matches(".*" + text + ".*")) {
                    list_.add(item);
                }
            }
            list.setAdapter(new LastUpdateListAdapter(activity.getApplicationContext(), list_));
        }
        else {
            ArrayList<DataItem> list_ = new ArrayList<>();
            for (Object o : itemList) {
                DataItem item = (DataItem) o;
                if (text.length() == 0 || item.getName().toLowerCase().matches(".*" + text + ".*")) {
                    list_.add(item);
                }
            }
            list.setAdapter(new ItemListAdapter(activity.getApplicationContext(), list_));
        }
    }

    public static void filterClick() {
        filterText.setVisibility(filterText.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) path.getLayoutParams();
        if (filterText.getVisibility() == View.VISIBLE) {
            params.addRule(RelativeLayout.BELOW, R.id.filter_text);
            params.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
            filterText.requestFocus();
            imm.showSoftInput(filterText, InputMethodManager.SHOW_IMPLICIT);
        }
        else {
            filterText.setText("");
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            imm.hideSoftInputFromWindow(filterText.getWindowToken(), 0);
        }
        path.setLayoutParams(params);
    }

    public static void hideProgress() {
        progress.setVisibility(View.GONE);
    }

}