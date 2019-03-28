package com.mantoo.yican;

import com.mantoo.yican.s8.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.mantoo.yican.customview.QRCodeView;
import com.mantoo.yican.customview.ZBarView;

/**
 * 二维�?/条码扫描界面，扫描结果将在返回时传�?�到前一个界�?
 * @author LENOVO
 *
 */
public class LongnerScanActivity extends Activity implements QRCodeView.Delegate {

	private final String TAG = "LongnerScanActivity";
	/**
	 * 镜头显示
	 */
	private QRCodeView mQRCodeView;
	/**
	 * 闪光灯控�?
	 */
	private ImageView iv_flash_light;
	/**
	 * 闪光灯是否开�?
	 */
	private boolean flashLightOpeng = false;

    private String activityType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_longner_scan_layout);
		
		mQRCodeView = (ZBarView) findViewById(R.id.zbarview);
        mQRCodeView.setDelegate(this);
        iv_flash_light = (ImageView) findViewById(R.id.iv_flash_light);
        //监听闪光灯图片被点击
        iv_flash_light.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(flashLightOpeng){
					flashLightOpeng = false;
					mQRCodeView.closeFlashlight();
					iv_flash_light.setImageResource(R.mipmap.ic_flash_off_white_24dp);
				}else{
					flashLightOpeng = true;
					mQRCodeView.openFlashlight();
					iv_flash_light.setImageResource(R.mipmap.ic_flash_on_white_24dp);
				}
			}
		});

        Intent intent = getIntent();
        activityType = intent.getStringExtra("activityType");
	}
	
	@Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);

        mQRCodeView.showScanRect();
        mQRCodeView.startSpot();
    }


    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
        vibrate();
        //解析结果不为空，将结果返�?
        if(!TextUtils.isEmpty(result)){
            // 提报异常
            if(activityType.equals("YiChangSubmitActivity"))
            {
                Intent intent = new Intent(LongnerScanActivity.this, YiChangSubmitActivity.class);
                intent.putExtra("result", result);
                startActivity(intent);
                finish();
            }
            else if(activityType.equals("MainActivity"))
            {
                Intent intent = new Intent(LongnerScanActivity.this, MainActivity.class);
                intent.putExtra("result", result);
                startActivity(intent);
                finish();
            }
            else if(activityType.equals("TongJiActivity"))
            {
                Intent intent = new Intent(LongnerScanActivity.this, TongJiActivity.class);
                intent.putExtra("result", result);
                startActivity(intent);
                finish();
            }
            else if(activityType.equals("SequenceActivity"))
            {
                Intent intent = new Intent(LongnerScanActivity.this, SequenceActivity.class);
                intent.putExtra("result", result);
                startActivity(intent);
                finish();
            }
            else
            {
                finish();
            }
        }else{
        	//否则重新扫描
        	mQRCodeView.startSpot();
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "error");
    }
	
}
