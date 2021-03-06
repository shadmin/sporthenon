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

public class OlympicsModeFragment extends Fragment {

    protected static Activity activity;
    protected static TextView path;

    public static OlympicsModeFragment newInstance(int n, Activity activity_) {
        OlympicsModeFragment fragment = new OlympicsModeFragment();
        Bundle args = new Bundle();
        args.putInt("section_number", n);
        fragment.setArguments(args);
        activity = activity_;
        return fragment;
    }

    public OlympicsModeFragment() {
    }

    public static TextView getPath() {
        return path;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_olympicsmode, container, false);
        path = (TextView) view.findViewById(R.id.olmode_path);
        path.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((AbstractActivity) activity).onSectionAttached(getArguments().getInt("section_number"));
    }

}