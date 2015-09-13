package com.sporthenon.android.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sporthenon.android.R;
import com.sporthenon.android.data.ResultItem;
import com.sporthenon.android.utils.AndroidUtils;

import java.util.List;

public class ResultListAdapter extends BaseAdapter {

	private List<ResultItem> list = null;
	LayoutInflater layoutInflater;
	Context context;

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
		LinearLayout line1;
		ImageView img1;
        TextView txt1;
		LinearLayout line2;
		ImageView img2;
		TextView txt2;
		LinearLayout line3;
		ImageView img3;
		TextView txt3;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_result, null);
			holder = new ViewHolder();
			holder.year = (TextView) convertView.findViewById(R.id.year);
			holder.line1 = (LinearLayout) convertView.findViewById(R.id.line1);
			holder.img1 = (ImageView) convertView.findViewById(R.id.img1);
			holder.txt1 = (TextView) convertView.findViewById(R.id.txt1);
			holder.line2 = (LinearLayout) convertView.findViewById(R.id.line2);
			holder.img2 = (ImageView) convertView.findViewById(R.id.img2);
			holder.txt2 = (TextView) convertView.findViewById(R.id.txt2);
			holder.line3 = (LinearLayout) convertView.findViewById(R.id.line3);
			holder.img3 = (ImageView) convertView.findViewById(R.id.img3);
			holder.txt3 = (TextView) convertView.findViewById(R.id.txt3);
			convertView.setTag(holder);
		}
        else
			holder = (ViewHolder) convertView.getTag();
		ResultItem ri = list.get(position);
		holder.year.setText(ri.getYear());
		// Line 1
		Drawable img1 = ri.getImg1();
		String imgURL1 = ri.getImgURL1();
		String txt1 = ri.getTxt1();
		if (img1 != null) {
			holder.img1.setImageDrawable(img1);
			holder.img1.setLayoutParams(AndroidUtils.getImageSize(convertView.getContext(), imgURL1));
			holder.img1.setVisibility(View.VISIBLE);
		}
		else
			holder.img1.setVisibility(View.GONE);
        holder.txt1.setText(txt1);
		// Line 2
		String txt2 = ri.getTxt2();
		if (AndroidUtils.notEmpty(txt2)) {
			Drawable img2 = ri.getImg2();
			String imgURL2 = ri.getImgURL2();
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
		String txt3 = ri.getTxt3();
		if (AndroidUtils.notEmpty(txt3)) {
			Drawable img3 = ri.getImg3();
			String imgURL3 = ri.getImgURL3();
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