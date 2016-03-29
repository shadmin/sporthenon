package com.sporthenon.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sporthenon.android.R;
import com.sporthenon.android.data.CalendarItem;

import java.util.List;

public class CalendarListAdapter extends BaseAdapter {

    private List<CalendarItem> list = null;
    LayoutInflater layoutInflater;
    Context context;

    public CalendarListAdapter(Context context, List<CalendarItem> list) {
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    static class ViewHolder {
        TextView dates;
        TextView sport_event;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_calendar, null);
            holder = new ViewHolder();
            holder.dates = (TextView) convertView.findViewById(R.id.cl_dates);
            holder.sport_event = (TextView) convertView.findViewById(R.id.cl_sport_event);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();
        CalendarItem ci = list.get(position);
        holder.dates.setText(ci.getDates());
        holder.sport_event.setText("\n" + ci.getSport() + "\n" + ci.getEvent());
        return convertView;
	}

}