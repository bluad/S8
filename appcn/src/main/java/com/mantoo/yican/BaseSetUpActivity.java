package com.mantoo.yican;

import android.content.Intent;
import android.content.SharedPreferences;
import com.mantoo.yican.cn.R;
import com.zhy.autolayout.AutoLayoutActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
/**
 * Created by Administrator on 2017/10/12.
 */

public abstract class BaseSetUpActivity extends AutoLayoutActivity {
    public SharedPreferences sp;
    private GestureDetector mGestureDetector;
    public static String HTTP_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //AutoUtils.setSize(this, false, 720, 1280);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        sp = getSharedPreferences("cpnfig", MODE_PRIVATE);
        HTTP_URL = sp.getString("ports", "");
        initView();
    }

    @SuppressWarnings("deprecation")
    private void initView() {
        // TODO Auto-generated method stub
        // 初始化手势识别器
        mGestureDetector = new GestureDetector(
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2,
                                           float velocityX, float velocityY) {
                        if (Math.abs(velocityX) < 200) {
                            // Toast.makeText(getApplicationContext(),
                            // "滑动的有点慢哦！",
                            // Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        if ((e2.getRawX() - e1.getRawX()) > 200) {
                            showPre();
                            overridePendingTransition(R.anim.pre_in,
                                    R.anim.pre_out);// 加载动画
                            return true;
                        }
                        if ((e1.getRawX() - e2.getRawX()) > 200) {
                            showNext();
                            overridePendingTransition(R.anim.next_in,
                                    R.anim.next_out);
                            return true;
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });
    }

    public abstract void showNext();

    public abstract void showPre();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void startActivityAndFinishSelf(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        finish();
    }

	/*
	 * 调用机制拦截
	 */

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        mGestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }


}
