package com.mantoo.yican.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.mantoo.yican.LoginActivity;
import com.mantoo.yican.SequenceActivity;
import com.mantoo.yican.model.VersionEntity;

/**
 * Created by Administrator on 2017/10/12.
 */

public class VersionUpdateUtils {

    private static final String TAG = "VersionUpdateUtils";
    public SharedPreferences sp;

    private static final int MESSAGE_NET_ERROR = 101;
    private static final int MESSAGE_IO_ERROR = 102;
    private static final int MESSAGE_JSON_ERROR = 103;
    private static final int MESSAGE_SHOW_DIALOG = 104;
    private static final int MESSAGE_ENTERHOME = 105;
    /* 本地版本号 */
    private String mVersion;
    private Activity context;
    private VersionEntity versionEntity;
    private ProgressDialog mProgressDialog;

    // private Version
    public VersionUpdateUtils(String Version, Activity activity) {
        mVersion = Version;
        this.context = activity;
        Log.i(TAG, "VersionUpdateUtils已重构");
    }

    /* 用于更新的UI */
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Log.i(TAG, "handleMessage启动");
            switch (msg.what) {
                case MESSAGE_IO_ERROR: //102
                    Toast.makeText(context, "IO异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_JSON_ERROR: //103
                    Toast.makeText(context, "JSON异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_NET_ERROR: //101
                    Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_SHOW_DIALOG: // app更新 104
                    Log.i(TAG, "接收到message："+MESSAGE_SHOW_DIALOG);
                    showUpdateDialog(versionEntity);
                    break;
                case 106:
                    Intent intent = new Intent(context,LoginActivity.class);//LoginActivity
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    context.finish();
                    break;
                case 107:
                    Intent intent2 = new Intent(context,SequenceActivity.class);//LoginActivity
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent2);
                    context.finish();
                    break;
                case MESSAGE_ENTERHOME: //105
                    sp = context.getSharedPreferences("cpnfig", 0);
                    //检查序列号
                    if(sp.getString("ports", "") != ""){
                        handler.sendEmptyMessageDelayed(106, 1000);
                    }else{
                        handler.sendEmptyMessageDelayed(106, 1000);
                    }
                    break;

                default:
                    break;
            }
        }

        /**
         * 弹出提示框
         *
         * @param versionEntity
         */
        private void showUpdateDialog(final VersionEntity versionEntity) {
            Log.i(TAG, "showUpdateDialog启动构建");
            /**
             * 创建dialog
             */
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("检测到新版本：" + versionEntity.versioncode);
            builder.setMessage(versionEntity.descripting);
            /** 服务器返回的描述信息 */
            builder.setCancelable(false);
            /** 设置手机不能点击返回键 */
            builder.setPositiveButton("立即升级",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            initProgressDialog();
                            downloadNewAPK(versionEntity.apkurl);
                        }

                        /** 下载APK */
                        @SuppressLint("SdCardPath") private void downloadNewAPK(String apkurl) {
                            Log.i(TAG, "downloadApkurl:"+apkurl);
                            String fileName = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"mobilesafe"+File.separator+"mobilesafe.apk";//getExternalStorageDirectory
                            Log.i(TAG, "fileName:"+fileName);
                            /*DownLoadUtils dowLoadUtils = new DownLoadUtils();
                            dowLoadUtils.downapk(apkurl,
                                    fileName,
                                    new MyCallBack() {

                                        @Override
                                        public void onSuccess(
                                                ResponseInfo<File> arg0) {
                                            mProgressDialog.dismiss();
                                            Log.i(TAG, "Dialog关闭");
                                            MyUtils.installApk(context);
                                        }

                                        @Override
                                        public void onLoadding(long total,
                                                               long current,
                                                               boolean isUploading) {
                                            mProgressDialog.setMax((int) total);
                                            mProgressDialog
                                                    .setMessage("正在下载...");
                                            mProgressDialog
                                                    .setProgress((int) current);
                                            Log.i(TAG, "onLoadding");
                                        }

                                        @Override
                                        public void onFailure(
                                                HttpException arg0, String arg1) {
                                            mProgressDialog.setMessage("下载失败");
                                            mProgressDialog.dismiss();
                                            enterHome();
                                            Log.i(TAG, "onFailure->arg0:"+arg0+" arg1:"+ arg1);
                                        }
                                    });*/

                        }

                        private void initProgressDialog() {
                            Log.i(TAG, "initProgressDialog开始运行");
                            mProgressDialog = new ProgressDialog(context);
                            mProgressDialog.setMessage("准备下载...");
                            mProgressDialog
                                    .setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            mProgressDialog.show();
                        }
                    });
            builder.setNegativeButton("暂不升级",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            enterHome();
                        }
                    });
            builder.show();

        };
    };

    /*
     * 获取服务器版本
     */
    public void getCloudVersion() {
        enterHome();
        /*try {
            new Thread(postThread).start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "线程异常：" + e.getMessage());
        }*/
    }

    /*private Thread postThread = new Thread() {
        public void run() {
            Log.i(TAG, "postThread线程启动");
            String target = "http://121.42.179.235:8081/YiChanPlatform.asmx/GetVersion";
            URL url;
            try {
                url = new URL(target);
                HttpURLConnection urlConn = (HttpURLConnection) url
                        .openConnection();
                urlConn.setRequestMethod("POST");
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setUseCaches(true);// 禁止缓存
                urlConn.setInstanceFollowRedirects(true);
                urlConn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                DataOutputStream out = new DataOutputStream(
                        urlConn.getOutputStream());
                String param = "screatStr="
                        + URLEncoder.encode("850DB0F5F6B89EB75798076CE9A5A56B",
                        "UTF-8") + "&" + "phone="
                        + URLEncoder.encode("zk244", "UTF-8");
                out.writeBytes(param);
                out.flush();
                out.close();

                if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader in = new InputStreamReader(
                            urlConn.getInputStream(), "UTF-8");
                    BufferedReader buffer = new BufferedReader(in);
                    String inputLine = null;
                    while ((inputLine = buffer.readLine()) != null) {
                        Log.i(TAG, "inputLine:" + inputLine);
                        try {
                            JSONObject jsonObject = new JSONObject(inputLine);
                            Log.i(TAG,
                                    "result_code:"
                                            + jsonObject
                                            .getString("result_code"));
                            if ((jsonObject.getString("result_code"))
                                    .equalsIgnoreCase("2")) {*//* 服务器是否返回成功 *//*
                                JSONObject request = jsonObject
                                        .getJSONObject("data");
                                versionEntity = new VersionEntity();
                                String code = request.getString("code");
                                Log.i(TAG, "code:"+code);
                                versionEntity.versioncode = code;
                                String des = request.getString("des");
                                Log.i(TAG, "des:"+des);
                                versionEntity.descripting = des;
                                String apkurl = request.getString("apkurl");
                                Log.i(TAG, "apkurl:"+apkurl);
                                versionEntity.apkurl = apkurl;
                                *//*if (!mVersion
                                        .equalsIgnoreCase(versionEntity.versioncode)) {
									*//**//* 版本号不一致的时候 *//**//*
                                    handler.sendEmptyMessage(MESSAGE_SHOW_DIALOG);
                                    Log.i(TAG, "进入版本不一致模式");
                                } else {
                                    enterHome();
                                }*//*
                                enterHome();
                            }else{
                                enterHome();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        };
    };*/

    /* end */
    private void enterHome() {
        handler.sendEmptyMessageDelayed(MESSAGE_ENTERHOME, 2000);
    }

}
