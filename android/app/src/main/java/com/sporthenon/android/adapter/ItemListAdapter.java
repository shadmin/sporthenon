package com.sporthenon.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sporthenon.android.R;
import com.sporthenon.android.data.DataItem;

import java.util.List;

public class ItemListAdapter extends BaseAdapter {

	private List<DataItem> list = null;
	LayoutInflater layoutInflater;
	Context context;
	private int lastPosition = -1;

	public ItemListAdapter(Context context, List<DataItem> list) {
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
			convertView = layoutInflater.inflate(R.layout.item_data, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.picture = (ImageView) convertView.findViewById(R.id.picture);
			convertView.setTag(holder);
		}
        else
			holder = (ViewHolder) convertView.getTag();
		String text = list.get(position).getName();
		if (text.startsWith("+")) {
			text = text.replaceAll("^\\+", "");
			holder.name.setTextColor(Color.GRAY);
		}
		holder.name.setText(text);
		Drawable img = list.get(position).getPicture();
		holder.picture.setImageDrawable(img);
		holder.picture.setVisibility(img != null ? View.VISIBLE : View.GONE);
		lastPosition = position;
		return convertView;
	}

}