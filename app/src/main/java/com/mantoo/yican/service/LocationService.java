package com.mantoo.yican.service;


import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.MainUrl;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;


/**
 * Created by Administrator on 2017/11/11.
 */

public class LocationService extends Service {
    private LocationManager locationManager;
    private Location location;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 获取location对象，优先以GPS_PROVIDER获取location对象，当以GPS_PROVIDER获取到的locaiton为null时
     * ，则以NETWORK_PROVIDER获取location对象，这样可保证在室内开启网络连接的状态下获取到的location对象不为空
     *
     * @param locationManager
     * @return
     */
    private Location getBestLocation(LocationManager locationManager) {
        Location result = null;
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            result = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (result != null) {
                return result;
            } else {
                result = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                return result;
            }
        }
        return result;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // 获取location对象
        location = getBestLocation(locationManager);
        updateView(location);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 1000 * 30;
        Log.e("", "============================================");
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }


    private void updateView(Location location) {
        if (location != null) {
            Log.e("", location.getLongitude() + "============================================" + location.getLatitude());
            //"经度" location.getLongitude() " 纬度"location.getLatitude()
            //请求开始
            AjaxParams params=new AjaxParams();
            params.put("zlng", String.valueOf(location.getLongitude()));
            params.put("zlat", String.valueOf(location.getLatitude()));
            params.put("clng", String.valueOf(location.getLongitude()));
            params.put("clat", String.valueOf(location.getLatitude()));
            params.put("driverid", AppCache.getString(AppCacheKey.driverid));
            String url = MainUrl.URL + MainUrl.UPLOADLBS;
            PDAApplication.http.configTimeout(8000);
            PDAApplication.http.post(url,params,new AjaxCallBack<Object>() {
                public void onSuccess(Object t) {
                };
                public void onFailure(Throwable t, String strMsg) {
                };
            } );
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}