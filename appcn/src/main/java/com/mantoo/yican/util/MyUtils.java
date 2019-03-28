package com.mantoo.yican.util;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
/**
 * Created by Administrator on 2017/10/12.
 */

public class MyUtils
{

    /*
     * 获取版本号
     * return 版本号
     */
    public static String getVersion(Context context){
        PackageManager manager = context.getPackageManager();//PackageManager可以获取文件的信息
        Log.i("VersionUpdateUtils", "manager：" + manager);

        try {
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            Log.i("VersionUpdateUtils", "packageInfo：" + packageInfo);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("VersionUpdateUtils", "MyUtilsTryAbnormal ：" + e.getMessage());
            return "";
        }
    }

    /*
     * 安装新版本
     */
    public static void installApk(Activity activity){
        Log.i("VersionUpdateUtils", "install activity："+activity);
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"mobilesafe"+File.separator+"mobilesafe.apk";
            Intent intent = new Intent();

            intent.setAction(Intent.ACTION_VIEW);
            //设置数据类型
            intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");/* Uri.fromFile(new File(path))*///Uri.parse("file://"+path)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            activity.startActivity(intent);
            activity.finish();

        } catch (Exception e) {
            Log.i("VersionUpdateUtils", "install Excepyion："+e.getMessage());
        }

    }




}
