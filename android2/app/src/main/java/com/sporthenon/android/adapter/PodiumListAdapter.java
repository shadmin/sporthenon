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
import com.sporthenon.android.data.PodiumItem;
import com.sporthenon.android.utils.AndroidUtils;

import java.util.List;

public class PodiumListAdapter extends BaseAdapter {

	private List<PodiumItem> list = null;
	LayoutInflater layoutInflater;
	Context context;

	public PodiumListAdapter(Context context, List<PodiumItem> list) {
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
		TextView event;
		LinearLayout line1;
		ImageView img1;
        TextView txt1;
		LinearLayout line2;
		ImageView img2;
		TextView txt2;
		LinearLayout line3;
		ImageView img3;
		TextView txt3;
		TextView venue;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_podium, null);
			holder = new ViewHolder();
			holder.event = (TextView) convertView.findViewById(R.id.pmevent);
			holder.line1 = (LinearLayout) convertView.findViewById(R.id.pmline1);
			holder.img1 = (ImageView) convertView.findViewById(R.id.pmimg1);
			holder.txt1 = (TextView) convertView.findViewById(R.id.pmtxt1);
			holder.line2 = (LinearLayout) convertView.findViewById(R.id.pmline2);
			holder.img2 = (ImageView) convertView.findViewById(R.id.pmimg2);
			holder.txt2 = (TextView) convertView.findViewById(R.id.pmtxt2);
			holder.line3 = (LinearLayout) convertView.findViewById(R.id.pmline3);
			holder.img3 = (ImageView) convertView.findViewById(R.id.pmimg3);
			holder.txt3 = (TextView) convertView.findViewById(R.id.pmtxt3);
			holder.venue = (TextView) convertView.findViewById(R.id.pmvenue);
			convertView.setTag(holder);
		}
        else
			holder = (ViewHolder) convertView.getTag();
		PodiumItem pi = list.get(position);
		holder.event.setText(pi.getEvent());
		holder.venue.setText(pi.getVenue());
		if (!AndroidUtils.notEmpty(pi.getVenue()))
			holder.venue.setVisibility(View.GONE);
		// Line 1
		Drawable img1 = pi.getImg1();
		String imgURL1 = pi.getImgURL1();
		String txt1 = pi.getTxt1();
		if (img1 != null) {
			holder.img1.setImageDrawable(img1);
			holder.img1.setLayoutParams(AndroidUtils.getImageSize(convertView.getContext(), imgURL1));
			holder.img1.setVisibility(View.VISIBLE);
		}
		else
			holder.img1.setVisibility(View.GONE);
        holder.txt1.setText(txt1);
		// Line 2
		String txt2 = pi.getTxt2();
		if (AndroidUtils.notEmpty(txt2)) {
			Drawable img2 = pi.getImg2();
			String imgURL2 = pi.getImgURL2();
			if (img2 != null) {
				holder.img2.setImageDrawable(img2);
				holder.img2.setLayoutParams(AndroidUtils.getImageSize(convertView.getContext(), imgURL2));
				holder.img2.setVisibility(View.VISIBLE);
			}
			else
				holder.img2.setVisibility(View.GONE);
			holder.txt2.setText(txt2);
			holder.line2.setVisibility(View.VISIBLE);
		}
		else
			holder.line2.setVisibility(View.GONE);
		// Line 3
		String txt3 = pi.getTxt3();
		if (AndroidUtils.notEmpty(txt3)) {
			Drawable img3 = pi.getImg3();
			String imgURL3 = pi.getImgURL3();
			if (img3 != null) {
				holder.img3.setImageDrawable(img3);
				holder.img3.setLayoutParams(AndroidUtils.getImageSize(convertView.getContext(), imgURL3));
				holder.img3.setVisibility(View.VISIBLE);
			}
			else
				holder.img3.setVisibility(View.GONE);
			holder.txt3.setText(txt3);
			holder.line3.setVisibility(View.VISIBLE);
		}
		else
			holder.line3.setVisibility(View.GONE);
		return convertView;
	}

}