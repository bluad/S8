package com.mantoo.yican.service;
import android.app.ActivityManager;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;

import com.mantoo.yican.LoginActivity;
import com.mantoo.yican.MainActivity;
import com.mantoo.yican.PaiDanActivity;

import java.util.List;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	private boolean push, sound;
	private boolean address = false;
	public SharedPreferences sp;
	private MyReceiver myReceiver;


	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();

		sp = context.getSharedPreferences("cpnfig", 0);
		push = sp.getBoolean("push", false);
		sound = sp.getBoolean("sound", false);
		System.out.println("pushed" + push);
		System.out.println("sound" + sound);

		address = sp.getBoolean("AddresseeActivity", false);
		Log.i(TAG, "titile:" + bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
		Log.i(TAG, "content:" + bundle.getString(JPushInterface.EXTRA_ALERT));
		Editor editor = sp.edit();
		editor.putString("title", bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
		editor.putString("content", bundle.getString(JPushInterface.EXTRA_ALERT));
		editor.commit();
		Log.i(TAG, "消息存储完成");

		/*
		 * 静默时间 如果静默时间是开启的则就不需要在设置声音提醒
		 */
		if (!push) {
			System.out.println(000);
			setNotification4(context);
		}
		if (push) {
			JPushInterface.setSilenceTime(context, 5, 00, 4, 00);// 静默时间是从今天的5点整到第二天的4点。
		} else {
		}



		if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {


		} else if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			//send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {

			Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
			Log.e("推送消息啦", "DDD" + bundle.getString(JPushInterface.EXTRA_EXTRA) + "DDD");

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			if(isAppaLive(context))
			{
				Intent i=new Intent(context,PaiDanActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}
			else
			{
				Intent i=new Intent(context,LoginActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}


		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
			boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
		} else {
		}

	}

	// *****

	// 声音提醒
	// 自定义报警通知（震动铃声都要）
	public void setNotification1(Context context) {
		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL; // 设置为自动消失
		builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE
				| Notification.DEFAULT_LIGHTS;// 设置为铃声与震动都要
		JPushInterface.setDefaultPushNotificationBuilder(builder);
	}

	// 自定义报警通知（铃声）
	public void setNotification2(Context context) {
		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL; // 设置为自动消失
		builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS;// 设置为铃声与震动都要
		JPushInterface.setDefaultPushNotificationBuilder(builder);
	}

	// 自定义报警通知（震动）
	public void setNotification3(Context context) {
		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL; // 设置为自动消失
		builder.notificationDefaults = Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;// 震动
		JPushInterface.setDefaultPushNotificationBuilder(builder);
	}

	// 自定义报警通知（震动铃声都不要）
	public void setNotification4(Context context) {
		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL; // 设置为自动消失
		builder.notificationDefaults = Notification.DEFAULT_LIGHTS;// 设置为铃声与震动都不要
		JPushInterface.setDefaultPushNotificationBuilder(builder);
	}

	/*
	 * 判断app是否存活
	 */

	public boolean isAppaLive(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
		boolean isAppRunning = false;
		String MY_PKG_NAME = "com.mantoo.yican.cn";
		for (ActivityManager.RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(MY_PKG_NAME)
					|| info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
				isAppRunning = true;
				Log.i(TAG, info.topActivity.getPackageName() + " info.baseActivity.getPackageName()="
						+ info.baseActivity.getPackageName());
				break;
			}
		}
		return isAppRunning;
	}


}
