package com.sporthenon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sporthenon.R;
import com.sporthenon.data.IDataItem;

import java.util.List;

public class ListAdapter extends BaseAdapter {

	private List<IDataItem> list = null;
	LayoutInflater layoutInflater;
	Context context;
	private int lastPosition = -1;

	public ListAdapter(Context context, List<IDataItem> list) {
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
		TextView name;
		ImageView picture;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.list_row, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.picture = (ImageView) convertView.findViewById(R.id.picture);

			convertView.setTag(holder);
		}
        else
			holder = (ViewHolder) convertView.getTag();
		holder.name.setText(list.get(position).getName());
		holder.picture.setBackgroundDrawable(list.get(position).getPicture());
		lastPosition = position;
		return convertView;
	}

}