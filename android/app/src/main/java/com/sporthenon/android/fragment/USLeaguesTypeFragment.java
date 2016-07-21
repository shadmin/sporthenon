package com.sporthenon.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sporthenon.android.R;
import com.sporthenon.android.activity.AbstractActivity;

public class USLeaguesTypeFragment extends Fragment {

    protected static Activity activity;

    public static USLeaguesTypeFragment newInstance(int n, Activity activity_) {
        USLeaguesTypeFragment fragment = new USLeaguesTypeFragment();
        Bundle args = new Bundle();
        args.putInt("section_number", n);
        fragment.setArguments(args);
        activity = activity_;
        return fragment;
    }

    public USLeaguesTypeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_usleaguestype, container, false);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((AbstractActivity) activity).onSectionAttached(getArguments().getInt("section_number"));
    }

}