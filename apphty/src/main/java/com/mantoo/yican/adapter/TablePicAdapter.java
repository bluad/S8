package com.mantoo.yican.adapter;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mantoo.yican.cn.hty.R;
import com.mantoo.yican.model.ExpressStatus;
import com.mantoo.yican.model.OrderSku;

import java.util.List;


public class TablePicAdapter extends ArrayAdapter<Bitmap> {

	private int resourceId;

	public TablePicAdapter(Context context, int textViewResourceId,
						  List<Bitmap> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        Bitmap pic = getItem(position);
		View view;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		} else {
			view = convertView;
		}

		ImageView picStr = (ImageView) view.findViewById(R.id.paidan_goods_pic);

        picStr.setImageBitmap(pic);
		return view;
	}

}
