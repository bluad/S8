package com.mantoo.yican.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.location.Location;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * 
 * @date 2015-7-24 上午9:06:55
 * 
 * @author LiangShiNian
 * 
 * @Description:
 * 
 */
public class ValidateUtil {

	/**
	 * 判断URL格式是否正确
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isUrlValidate(String url) {
		if (url == null || !(url.startsWith("http") || url.startsWith("https"))) {
			return false;
		}
		return true;
	}

	/**
	 * 判断list集合是否为空
	 * 
	 * @param list
	 * @return
	 */
	public static boolean isEmptyList(List<String> list) {
		if (list == null || list.size() == 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.trim().equals("") || str.trim().equals("null")) {
			return true;
		}
		return false;
	}

	/**
	 * 判断AppCache不为空
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isAppCacheNotEmpty(String value) {
		if (!isEmpty(value) && !value.equalsIgnoreCase("null")) {
			return true;
		}
		return false;
	}

	/**
	 * 判断AppCache不为空 null则返回""
	 * 
	 * @param value
	 * @return
	 */
	public static String isCacheNotEmpty(String value) {
		if (!isEmpty(value) && !value.equalsIgnoreCase("null")) {
			return value;
		}
		return "";
	}
	/**
	 * 判断AppCache不为空 null则返回"0"
	 * 
	 * @param value
	 * @return
	 */
	public static String isCacheNotEmpty0(String value) {
		if (!isEmpty(value) && !value.equalsIgnoreCase("null")) {
			return value;
		}
		return "0";
	}
	/**
	 * 判断AppCache不为空 null则返回"1"
	 * 
	 * @param value
	 * @return
	 */
	public static String isCacheNotEmpty1(String value) {
		if (!isEmpty(value) && !value.equalsIgnoreCase("null")) {
			return value;
		}
		return "1";
	}

	/**
	 * 将加密好的字节编码变为字符编码。
	 */
	public static String MD5(String str) {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(str.getBytes());
			return toHex(m.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 将字节变为字符
	 */
	private static String toHex(byte buffer[]) {
		StringBuffer sb = new StringBuffer(buffer.length * 2);
		for (int i = 0; i < buffer.length; i++) {
			sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
			sb.append(Character.forDigit(buffer[i] & 15, 16));
		}
		return sb.toString();
	}

	public static String getJsonString(String data) {
		if (isEmpty(data)) {
			return "";
		} else if ("null".equals(data)) {
			return "";
		}
		return data;
	}

	/**
	 * 点击键盘外自动隐藏键盘
	 */

	public static boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			// 获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
				// 点击的是输入框区域，保留点击EditText的事件
				return false;
			} else {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * String 是否为空
	 */
	public static boolean isNotEmpty(String str) {
		if (str.trim().length() == 0 && str.trim().equals("")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * String 是否为中文
	 */
	public static boolean isChinese(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		m = p.matcher(str);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static double getDouble(String str) {
		if (str == null || str.trim().equals("") || str.trim().equals("null")) {
			return 0d;
		}
		return Double.parseDouble(str);
	}

	public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
		float[] results = new float[1];
		Location.distanceBetween(lat1, lon1, lat2, lon2, results);
		return results[0];
	}
}
