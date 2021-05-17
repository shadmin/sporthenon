package com.sporthenon.android.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sporthenon.android.R;
import com.sporthenon.android.data.MedalItem;
import com.sporthenon.android.utils.AndroidUtils;

import java.util.List;

public class MedalListAdapter extends BaseAdapter {

	private List<MedalItem> list = null;
	LayoutInflater layoutInflater;
	Context context;

	public MedalListAdapter(Context context, List<MedalItem> list) {
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
		TextView country;
		ImageView img;
        TextView gold;
		TextView silver;
		TextView bronze;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_medals, null);
			holder = new ViewHolder();
			holder.country = (TextView) convertView.findViewById(R.id.medal_text);
			holder.img = (ImageView) convertView.findViewById(R.id.medal_img);
			holder.gold = (TextView) convertView.findViewById(R.id.medal_gold);
			holder.silver = (TextView) convertView.findViewById(R.id.medal_silver);
			holder.bronze = (TextView) convertView.findViewById(R.id.medal_bronze);
			convertView.setTag(holder);
		}
        else
			holder = (ViewHolder) convertView.getTag();
		MedalItem mi = list.get(position);
		holder.country.setText(mi.getCountry());
		holder.gold.setText(String.valueOf(mi.getGold()));
		holder.silver.setText(String.valueOf(mi.getSilver()));
		holder.bronze.setText(String.valueOf(mi.getBronze()));
		Drawable img = mi.getImg();
		String imgURL = mi.getImgURL();
		if (img != null) {
			holder.img.setImageDrawable(img);
			holder.img.setLayoutParams(AndroidUtils.getImageSize(convertView.getContext(), imgURL));
		}
		else
			holder.img.setVisibility(View.GONE);
		return convertView;
	}

}