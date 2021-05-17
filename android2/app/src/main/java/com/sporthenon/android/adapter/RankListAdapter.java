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
import com.sporthenon.android.data.RankItem;
import com.sporthenon.android.utils.AndroidUtils;

import java.util.List;

public class RankListAdapter extends BaseAdapter {

	private List<RankItem> list = null;
	LayoutInflater layoutInflater;
	Context context;

	public RankListAdapter(Context context, List<RankItem> list) {
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
		TextView rank;
		ImageView img;
        TextView text;
		TextView result;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_rank, null);
			holder = new ViewHolder();
			holder.rank = (TextView) convertView.findViewById(R.id.rank_index);
			holder.img = (ImageView) convertView.findViewById(R.id.rank_img);
			holder.text = (TextView) convertView.findViewById(R.id.rank_text);
			holder.result = (TextView) convertView.findViewById(R.id.rank_result);
			convertView.setTag(holder);
		}
        else
			holder = (ViewHolder) convertView.getTag();
		RankItem ri = list.get(position);
		holder.rank.setText(ri.getRank());
		holder.text.setText(ri.getText());
		holder.result.setText(ri.getResult());
		Drawable img = ri.getImg();
		String imgURL = ri.getImgURL();
		if (img != null) {
			holder.img.setImageDrawable(img);
			holder.img.setLayoutParams(AndroidUtils.getImageSize(convertView.getContext(), imgURL));
		}
		else
			holder.img.setVisibility(View.GONE);
		return convertView;
	}

}