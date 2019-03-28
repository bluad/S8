package com.mantoo.yican.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mantoo.yican.cn.hty.R;
import com.mantoo.yican.model.OrderSku;


public class TableAdapter extends BaseAdapter {
	
	private List<OrderSku> list;
	private LayoutInflater inflater;
	
	public TableAdapter(Context context, List<OrderSku> list){
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		int ret = 0;
		if(list!=null){
			ret = list.size();
		}
		return ret;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		OrderSku goods = (OrderSku) this.getItem(position);
		
		ViewHolder viewHolder;
		
		if(convertView == null){
			
			viewHolder = new ViewHolder();
			
			convertView = inflater.inflate(R.layout.item_listview_goodsinfo, null);
			viewHolder.goodsId = (TextView) convertView.findViewById(R.id.paidan_goods_no);
			viewHolder.goodsName = (TextView) convertView.findViewById(R.id.paidan_goods_name);
			viewHolder.goodsNumber = (TextView) convertView.findViewById(R.id.paidan_goods_number);
			viewHolder.goodsRemark = (TextView) convertView.findViewById(R.id.paidan_goods_remark);

			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.goodsId.setText(position+"");
		viewHolder.goodsName.setText(goods.getSkuName());
		viewHolder.goodsNumber.setText(goods.getSkuNum());
		viewHolder.goodsRemark.setText(goods.getRemarks());

		/*if (position == 0) {
			viewHolder.goodsId.setText("NO.");
			viewHolder.goodsId.setBackgroundColor(Color.rgb(150, 215, 229));
			viewHolder.goodsName.setBackgroundColor(Color.rgb(150, 215, 229));
			viewHolder.goodsNumber.setBackgroundColor(Color.rgb(150, 215, 229));
			viewHolder.goodsRemark.setBackgroundColor(Color.rgb(150, 215, 229));
		}*/

		return convertView;
	}
	
	public static class ViewHolder{
		public TextView goodsId;
		public TextView goodsName;
		public TextView goodsNumber;
		public TextView goodsRemark;
	}
	
}
