package com.mantoo.yican;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import cn.jpush.android.api.JPushInterface;
import com.mantoo.yican.cn.R;
import com.mantoo.yican.util.MyUtils;
import com.mantoo.yican.util.VersionUpdateUtils;
/**
 * Created by Administrator on 2017/10/12.
 */

public class WelcomeActivity extends BaseActivity {

    private static final int ACCESS_FINE_LOCATION = 1;

    String Version;

    // android6.0 权限申请
    String[] permissions = new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION,
                                            android.Manifest.permission.CAMERA,
                                            android.Manifest.permission.READ_CALENDAR,
                                            android.Manifest.permission.SEND_SMS,
                                            android.Manifest.permission.CALL_PHONE,
                                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(this, permissions, ACCESS_FINE_LOCATION);
        }
        else
        {
            Version = MyUtils.getVersion(getApplicationContext());
            init();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == ACCESS_FINE_LOCATION)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Version = MyUtils.getVersion(getApplicationContext());
                init();
            }
            else
            {

            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void init() {
        final VersionUpdateUtils updateUtils = new VersionUpdateUtils(Version,
                WelcomeActivity.this);
        new Thread() {
            public void run() {
                Log.i("VersionUpdateUtils", "子线程运行");
                updateUtils.getCloudVersion();
            };
        }.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(WelcomeActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(WelcomeActivity.this);
    }


}
