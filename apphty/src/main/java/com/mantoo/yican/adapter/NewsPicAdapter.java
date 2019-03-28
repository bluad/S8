package com.mantoo.yican.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.mantoo.yican.cn.hty.R;

import java.util.List;


public class NewsPicAdapter extends ArrayAdapter<Bitmap> {

	private int resourceId;
	private Context context;

	public NewsPicAdapter(Context context, int textViewResourceId,
                          List<Bitmap> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
		this.context = context;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        Bitmap bitmap = getItem(position);
		View view;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		} else {
			view = convertView;
		}

		ImageView picStr = (ImageView) view.findViewById(R.id.news_pic);
		ViewGroup.LayoutParams vgl = picStr.getLayoutParams();

		//获取bitmap的宽度
		float bitWidth = bitmap.getWidth();
		//获取bitmap的宽度
		float bithight = bitmap.getHeight();

		//计算出图片的宽高比，然后按照图片的比列去缩放图片
		float bitScalew = bitWidth / bithight;

		float imgWidth = getScreenWith(context);
		//如果图片宽度大于高度
		vgl.width = (int) imgWidth;
		vgl.height = (int) (imgWidth / bitScalew);

		//设置图片充满ImageView控件
		picStr.setScaleType(ImageView.ScaleType.CENTER_CROP);
		//等比例缩放
		picStr.setAdjustViewBounds(true);
		picStr.setLayoutParams(vgl);

		picStr.setImageBitmap(bitmap);
		return view;
	}

	public int getScreenWith(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	public int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}

}
