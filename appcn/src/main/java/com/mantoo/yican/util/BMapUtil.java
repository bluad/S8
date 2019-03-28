package com.mantoo.yican.util;

import java.math.BigDecimal;

import android.graphics.Bitmap;
import android.view.View;

public class BMapUtil {
    	
	/**
	 * 从view 得到图片
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
	}
	
	
	/**
     * 计算两点之间距离
     * @return 米
     */
	public static String getDistance(double lat_a, double lng_a, double lat_b, double lng_b){
        double pk = 180 / Math.PI;
        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;
        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);
        
        BigDecimal   b  =   new BigDecimal(6371000 * tt/1000f);  
		 float   f1   =  b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue(); 
        return String.valueOf(f1);
    }
	
}
