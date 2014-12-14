package com.myweather;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {

	private ArrayList<NewsItem> listData;

	private LayoutInflater layoutInflater;

	public CustomListAdapter(Context context, ArrayList<NewsItem> listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.activity_hours, null);
			holder = new ViewHolder();
			holder.temperatureView = (TextView) convertView.findViewById(R.id.temperature);
			holder.iconView = (TextView) convertView.findViewById(R.id.icon);
			holder.dateView = (TextView) convertView.findViewById(R.id.date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.temperatureView.setText(listData.get(position).gettemperature());
		holder.iconView.setText("By, " + listData.get(position).geticon());
		holder.dateView.setText(listData.get(position).getDate());

		return convertView;
	}

	static class ViewHolder {
		TextView temperatureView;
		TextView iconView;
		TextView dateView;
	}

}
