package com.sporthenon.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sporthenon.android.R;
import com.sporthenon.android.data.RecordItem;
import com.sporthenon.android.utils.AndroidUtils;

import java.util.List;

public class RecordListAdapter extends BaseAdapter {

	private List<RecordItem> list = null;
	LayoutInflater layoutInflater;
	Context context;

	public RecordListAdapter(Context context, List<RecordItem> list) {
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
		TextView label;
		TextView type;
		TextView ranks;
		TextView record;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_record, null);
			holder = new ViewHolder();
			holder.label = (TextView) convertView.findViewById(R.id.rclabel);
			holder.type = (TextView) convertView.findViewById(R.id.rctype);
			holder.ranks = (TextView) convertView.findViewById(R.id.rcranks);
			holder.record = (TextView) convertView.findViewById(R.id.rcrecord);
			convertView.setTag(holder);
		}
        else
			holder = (ViewHolder) convertView.getTag();
		RecordItem ri = list.get(position);
		holder.label.setText(ri.getLabel());
		holder.type.setText(ri.getType1() + ", " + ri.getType2());
        holder.record.setText(ri.getRecord());
		StringBuffer sb = new StringBuffer(ri.getRank1());
        if (AndroidUtils.notEmpty(ri.getRank2()))
            sb.append("\r\n").append(ri.getRank2());
        if (AndroidUtils.notEmpty(ri.getRank3()))
            sb.append("\r\n").append(ri.getRank3());
        if (AndroidUtils.notEmpty(ri.getRank4()))
            sb.append("\r\n").append(ri.getRank4());
        if (AndroidUtils.notEmpty(ri.getRank5()))
            sb.append("\r\n").append(ri.getRank5());
        holder.ranks.setText(sb.toString());
		return convertView;
	}

}