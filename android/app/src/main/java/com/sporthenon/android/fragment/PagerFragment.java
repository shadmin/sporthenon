package com.sporthenon.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sporthenon.android.R;

public class PagerFragment extends Fragment {

    protected static Activity activity;
    protected static ViewPager pager;

    public static PagerFragment newInstance(int n, Activity activity_) {
        PagerFragment fragment = new PagerFragment();
        /*Bundle args = new Bundle();
        args.putInt("section_number", n);
        fragment.setArguments(args);*/
        activity = activity_;
        return fragment;
    }

    public PagerFragment() {
    }

    public static ViewPager getPager() {
        return pager;
    }

    public static void setPager(ViewPager pager) {
        PagerFragment.pager = pager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager, container, false);
        pager = (ViewPager) view.findViewById(R.id.view_pager);

        return view;
    }

}