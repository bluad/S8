package com.mantoo.yican;

import com.mantoo.yican.application.AppCache;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mantoo.yican.s8.R;

public class GuideActivity extends BaseActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private RelativeLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext, btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 在setContentView()前检查是否第一次运行
        if (AppCache.getString("isfirst").equals("true")) {
            // Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(GuideActivity.this,
                    WelcomeActivity.class);// LoginActivity
            startActivity(intent);
            finish();
//			launchHomeScreen();
        }


        // 让状态栏透明
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_guide);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (RelativeLayout) findViewById(R.id.layoutDots);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        // 添加欢迎页面
        layouts = new int[] { R.layout.page1, R.layout.page2, R.layout.page3,R.layout.page4};

        // 添加点
//		 addBottomDots(0);

        // 让状态栏透明
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current < layouts.length) {
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });

    }



    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
//		 prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(this,WelcomeActivity.class));
        finish();
    }

    /**
     * 让状态栏变透明
     */
    private void changeStatusBarColor() {
        // if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
        // Window window = getWindow();
        // window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // window.setStatusBarColor(Color.TRANSPARENT);
        // }
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            // addBottomDots(position);

            // 改变下一步按钮text “NEXT”或“GOT IT”
            if (position == layouts.length - 1) {
                btnNext.setText("start");
                btnSkip.setVisibility(View.GONE);
                btn = (Button) findViewById(R.id.btn);
                btn.setTag("enter");
                btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        System.out.println("kakaka");
                        AppCache.putString("isfirst", "true");
                        startActivity(new Intent(GuideActivity.this,WelcomeActivity.class));
                        finish();
                    }
                });

            } else {
                btnNext.setText("next");
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

}
