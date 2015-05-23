package com.sporthenon.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sporthenon.android.R;
import com.sporthenon.android.data.ResultItem;

import java.util.List;

public class ResultListAdapter extends BaseAdapter {

	private List<ResultItem> list = null;
	LayoutInflater layoutInflater;
	Context context;
	private int lastPosition = -1;

	public ResultListAdapter(Context context, List<ResultItem> list) {
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
		TextView year;
		ImageView img;
        TextView rank1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.list_row2, null);
			holder = new ViewHolder();
			holder.year = (TextView) convertView.findViewById(R.id.year);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.rank1 = (TextView) convertView.findViewById(R.id.rank1);
			convertView.setTag(holder);
		}
        else
			holder = (ViewHolder) convertView.getTag();
		holder.year.setText(list.get(position).getYear());
		holder.img.setBackgroundDrawable(list.get(position).getImg());
        holder.rank1.setText(list.get(position).getRank1());
		lastPosition = position;
		return convertView;
	}

}