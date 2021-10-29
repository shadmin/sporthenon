package com.sporthenon.android.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sporthenon.android.R;
import com.sporthenon.android.data.LastUpdateItem;
import com.sporthenon.android.data.ResultItem;
import com.sporthenon.android.utils.AndroidUtils;

import java.util.List;

public class LastUpdateListAdapter extends BaseAdapter {

	private List<LastUpdateItem> list = null;
	LayoutInflater layoutInflater;
	Context context;

	public LastUpdateListAdapter(Context context, List<LastUpdateItem> list) {
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
		ImageView imgSport;
		TextView event;
		LinearLayout line1;
		ImageView imgPos1;
        TextView pos1;
		LinearLayout line1_bis;
		TextView score;
		LinearLayout line2;
		ImageView imgPos2;
		TextView pos2;
		LinearLayout line3;
		ImageView imgPos3;
		TextView pos3;
		TextView date;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_last_update, null);
			holder = new ViewHolder();
			holder.imgSport = (ImageView) convertView.findViewById(R.id.img_sport);
			holder.event = (TextView) convertView.findViewById(R.id.event);
			holder.line1 = (LinearLayout) convertView.findViewById(R.id.line1);
			holder.imgPos1 = (ImageView) convertView.findViewById(R.id.img_pos1);
			holder.pos1 = (TextView) convertView.findViewById(R.id.pos1);
			holder.line1_bis = (LinearLayout) convertView.findViewById(R.id.line1_bis);
			holder.score = (TextView) convertView.findViewById(R.id.score);
			holder.line2 = (LinearLayout) convertView.findViewById(R.id.line2);
			holder.imgPos2 = (ImageView) convertView.findViewById(R.id.img_pos2);
			holder.pos2 = (TextView) convertView.findViewById(R.id.pos2);
			holder.line3 = (LinearLayout) convertView.findViewById(R.id.line3);
			holder.imgPos3 = (ImageView) convertView.findViewById(R.id.img_pos3);
			holder.pos3 = (TextView) convertView.findViewById(R.id.pos3);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			convertView.setTag(holder);
		}
        else {
			holder = (ViewHolder) convertView.getTag();
		}
		LastUpdateItem item = list.get(position);
		holder.imgSport.setImageDrawable(item.getImgSport());
		holder.event.setText(item.getEvent());
		holder.date.setText(item.getDate());

		// Line 1
		String pos1 = item.getPos1();
		Drawable imgPos1 = item.getImgPos1();
		String imgURLPos1 = item.getImgURLPos1();
		if (imgPos1 != null) {
			holder.imgPos1.setImageDrawable(imgPos1);
			holder.imgPos1.setLayoutParams(AndroidUtils.getImageSize(convertView.getContext(), imgURLPos1));
			holder.imgPos1.setVisibility(View.VISIBLE);
		}
		else {
			holder.imgPos1.setVisibility(View.GONE);
		}
		holder.pos1.setText(pos1);

		//Score
		String score = item.getScore();
		if (AndroidUtils.notEmpty(score)) {
			holder.score.setText(score);
			holder.line1_bis.setVisibility(View.VISIBLE);
		}
		else {
			holder.line1_bis.setVisibility(View.GONE);
		}

		// Line 2
		String pos2 = item.getPos2();
		if (AndroidUtils.notEmpty(pos2)) {
			Drawable imgPos2 = item.getImgPos2();
			String imgURLPos2 = item.getImgURLPos2();
			if (imgPos2 != null) {
				holder.imgPos2.setImageDrawable(imgPos2);
				holder.imgPos2.setLayoutParams(AndroidUtils.getImageSize(convertView.getContext(), imgURLPos2));
				holder.imgPos2.setVisibility(View.VISIBLE);
			}
			else {
				holder.imgPos2.setVisibility(View.GONE);
			}
			holder.pos2.setText(pos2);
			holder.line2.setVisibility(View.VISIBLE);
		}
		else {
			holder.line2.setVisibility(View.GONE);
		}

		// Line 3
		String pos3 = item.getPos3();
		if (AndroidUtils.notEmpty(pos3)) {
			Drawable imgPos3 = item.getImgPos3();
			String imgURLPos3 = item.getImgURLPos3();
			if (imgPos3 != null) {
				holder.imgPos3.setImageDrawable(imgPos3);
				holder.imgPos3.setLayoutParams(AndroidUtils.getImageSize(convertView.getContext(), imgURLPos3));
				holder.imgPos3.setVisibility(View.VISIBLE);
			}
			else {
				holder.imgPos3.setVisibility(View.GONE);
			}
			holder.pos3.setText(pos3);
			holder.line3.setVisibility(View.VISIBLE);
		}
		else {
			holder.line3.setVisibility(View.GONE);
		}

		return convertView;
	}

}