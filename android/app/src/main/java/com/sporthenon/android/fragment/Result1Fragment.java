package com.sporthenon.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sporthenon.android.R;
import com.sporthenon.android.activity.AbstractActivity;

public class Result1Fragment extends Fragment {

    protected static Activity activity;
    protected static ProgressBar progress;
    protected static ListView rankList;
    protected static TextView year;
    protected static TextView sport;
    protected static TextView championship;
    protected static TextView event;
    protected static TextView subevent;
    protected static TextView subevent2;
    protected static TextView date;
    protected static TextView place1;
    protected static TextView place2;
    protected static TextView labelDate;
    protected static TextView labelPlace;
    protected static TextView labelResult;

    public static Result1Fragment newInstance(int n, Activity activity_) {
        Result1Fragment fragment = new Result1Fragment();
        Bundle args = new Bundle();
        args.putInt("section_number", n);
        fragment.setArguments(args);
        activity = activity_;
        return fragment;
    }

    public Result1Fragment() {
    }

    public static TextView getLabelResult() {
        return labelResult;
    }

    public static ListView getRankList() {
        return rankList;
    }

    public static TextView getYear() {
        return year;
    }

    public static TextView getSport() {
        return sport;
    }

    public static TextView getChampionship() {
        return championship;
    }

    public static TextView getEvent() {
        return event;
    }

    public static TextView getSubevent() {
        return subevent;
    }

    public static TextView getSubevent2() {
        return subevent2;
    }

    public static TextView getDate() {
        return date;
    }

    public static TextView getPlace1() {
        return place1;
    }

    public static TextView getPlace2() {
        return place2;
    }

    public static TextView getLabelDate() {
        return labelDate;
    }

    public static TextView getLabelPlace() {
        return labelPlace;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_result1, container, false);

        progress = (ProgressBar) view.findViewById(R.id.progress);
        year = (TextView) view.findViewById(R.id.rs_year);
        sport = (TextView) view.findViewById(R.id.rs_sport);
        championship = (TextView) view.findViewById(R.id.rs_championship);
        event = (TextView) view.findViewById(R.id.rs_event);
        subevent = (TextView) view.findViewById(R.id.rs_subevent);
        subevent2 = (TextView) view.findViewById(R.id.rs_subevent2);
        date = (TextView) view.findViewById(R.id.rs_date);
        place1 = (TextView) view.findViewById(R.id.rs_place1);
        place2 = (TextView) view.findViewById(R.id.rs_place2);
        labelDate = (TextView) view.findViewById(R.id.rs_label_date);
        labelPlace = (TextView) view.findViewById(R.id.rs_label_place);
        labelResult = (TextView) view.findViewById(R.id.rs_label_result);
        rankList = (ListView) view.findViewById(R.id.rank_list);

        labelDate.setText(R.string.date);
        labelPlace.setText(R.string.place);
        labelResult.setText(R.string.result);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((AbstractActivity) activity).onSectionAttached(getArguments().getInt("section_number"));
    }

    public static void hideProgress() {
        progress.setVisibility(View.GONE);
    }

}