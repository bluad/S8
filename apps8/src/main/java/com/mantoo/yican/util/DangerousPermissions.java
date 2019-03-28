package com.mantoo.yican.util;

import android.Manifest;

/**
 * Created by Administrator on 2017/11/13.
 */

public class DangerousPermissions {


    /**
     *  permission group : CALENDAR
     *  READ_CALENDAR
     *  WRITE_CALENDAR
     */
    public static final String CALENDAR= Manifest.permission.READ_CALENDAR;

    /**
     *  permission group : CAMERA
     *  CAMERA
     */
    public static final String CAMERA= Manifest.permission.CAMERA;

    /**
     *  permission group : LOCATION
     *  ACCESS_FINE_LOCATION
     *  ACCESS_COARSE_LOCATION
     */
    public static final String LOCATION= Manifest.permission.ACCESS_FINE_LOCATION;

    /**
     *  permission group : MICROPHONE
     *  RECORD_AUDIO
     */
    public static final String MICROPHONE= Manifest.permission.RECORD_AUDIO;

    /**
     *  permission group : SENSORS
     *  BODY_SENSORS
     */
    public static final String SENSORS= Manifest.permission.BODY_SENSORS;

    /**
     *  permission group : SMS
     *  SEND_SMS
     *  RECEIVE_SMS
     *  READ_SMS
     *  RECEIVE_WAP_PUSH
     *  RECEIVE_MMS
     */
    public static final String SMS= Manifest.permission.SEND_SMS;

    /**
     *  permission group : STORAGE
     *  READ_EXTERNAL_STORAGE
     *  WRITE_EXTERNAL_STORAGE
     */
    public static final String STORAGE= Manifest.permission.WRITE_EXTERNAL_STORAGE;


    public static final String COARSE= Manifest.permission.ACCESS_COARSE_LOCATION;

}
