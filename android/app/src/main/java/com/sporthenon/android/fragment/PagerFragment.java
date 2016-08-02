package com.sporthenon.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
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
        pager.setAdapter(new MyAdapter(getChildFragmentManager()));

        /*TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);*/
        /*
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            actionBar.setHomeButtonEnabled(true);
            actionBar.setSubtitle(getString(R.string.app_name));
            actionBar.setDisplayShowTitleEnabled(true);*/
        return view;
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return new ListFragment();
                case 1 : return new ListFragment();
                case 2 : return new ListFragment();
            }
            return null;
        }

        @Override
        public int getCount() {

            return 3;

        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "Primary";
                case 1 :
                    return "Social";
                case 2 :
                    return "Updates";
            }
            return null;
        }
    }

}