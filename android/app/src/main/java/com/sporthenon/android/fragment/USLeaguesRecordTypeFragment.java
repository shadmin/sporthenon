package com.sporthenon.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sporthenon.android.R;
import com.sporthenon.android.activity.AbstractActivity;

public class USLeaguesRecordTypeFragment extends Fragment {

    protected static Activity activity;
    protected static TextView path;

    public static USLeaguesRecordTypeFragment newInstance(int n, Activity activity_) {
        USLeaguesRecordTypeFragment fragment = new USLeaguesRecordTypeFragment();
        Bundle args = new Bundle();
        args.putInt("section_number", n);
        fragment.setArguments(args);
        activity = activity_;
        return fragment;
    }

    public USLeaguesRecordTypeFragment() {
    }

    public static TextView getPath() {
        return path;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_usleaguesrctype, container, false);
        path = (TextView) view.findViewById(R.id.rctype_path);
        path.setVisibility(View.GONE);
        Button both = (Button) view.findViewById(R.id.us_both);
        if (!((AbstractActivity) activity).getUsltype().equals(((AbstractActivity) activity).USTYPE_RECORDS))
            both.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((AbstractActivity) activity).onSectionAttached(getArguments().getInt("section_number"));
    }

}