package com.sporthenon.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.sporthenon.android.R;
import com.sporthenon.android.activity.AbstractActivity;

public class USLeaguesTypeFragment extends Fragment {

    protected static Activity activity;
    protected static TextView path;

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

    public static TextView getPath() {
        return path;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_usleaguestype, container, false);
        path = (TextView) view.findViewById(R.id.ustype_path);
        path.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((AbstractActivity) activity).onSectionAttached(getArguments().getInt("section_number"));
    }

}