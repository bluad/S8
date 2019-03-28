package com.mantoo.yican.adapter;

import java.util.ArrayList;
import com.mantoo.yican.model.AddresseeData;
import com.mantoo.yican.model.RunningRecord;
import com.mantoo.yican.util.ValidateUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.mantoo.yican.cn.R;

public class WuliuAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<RunningRecord> myList = new ArrayList<RunningRecord>();
	private LayoutInflater mInflater;
	RunningRecord info = new RunningRecord();

	public WuliuAdapter(Context context, ArrayList<RunningRecord> list) {
		this.myList = list;
		this.context = context;
		this.mInflater = LayoutInflater.from(this.context);
	}

	public int getCount() {
		return myList.size();
	}

	public Object getItem(int position) {
		return myList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, final ViewGroup parent) {
		ViewHolder holder = null;
		info = myList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_listview_wuliu, null);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.note = (TextView) convertView.findViewById(R.id.note);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == 0) {
			holder.image.setImageResource(R.mipmap.flag);
			holder.time.setTextColor(context.getResources().getColor(R.color.yellowdeep));
			holder.note.setTextColor(context.getResources().getColor(R.color.yellowdeep));
		}
		holder.time.setText(info.getTime());
		holder.note.setText(info.getContext());

		return convertView;

	}

	final class ViewHolder {
		public TextView time;
		public TextView note;
		public ImageView image;

	}

}
