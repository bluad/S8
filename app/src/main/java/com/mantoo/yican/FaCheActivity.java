package com.mantoo.yican;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mantoo.yican.adapter.FaCheAdapter;
import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.model.ExpressStatus;
import com.mantoo.yican.view.SwipeMenu;
import com.mantoo.yican.view.SwipeMenuCreator;
import com.mantoo.yican.view.SwipeMenuItem;
import com.mantoo.yican.view.SwipeMenuListView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 发车activity
 * Created by Administrator on 2017/10/12.
 */

public class FaCheActivity extends BaseActivity implements View.OnClickListener {

        List<ExpressStatus> dataList = new ArrayList<ExpressStatus>();
        private SwipeMenuListView faCheListView;

        private Configuration config;
        private Resources resources;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_fache);
                resources = getResources();// 获得res资源对象
                config = resources.getConfiguration();// 获得设置对象
                initView();
        }

        private void initView() {
                findViewById(R.id.fache_express_return).setOnClickListener(this);
                faCheListView = (SwipeMenuListView) findViewById(R.id.fache_express_list);

                //请求开始
                AjaxParams params = new AjaxParams();
                params.put("driverid", AppCache.getString(AppCacheKey.driverid));
                params.put("missionType", "3");
                String url = MainUrl.URL + MainUrl.PIELIST;
                System.out.println(params + url);
                PDAApplication.http.configTimeout(8000);
                showLoadingDialog();
                PDAApplication.http.post(url, params, new AjaxCallBack<Object>() {
                        public void onSuccess(Object t) {
                                hideLoadingDialog();
                                JSONObject object;
                                try {
                                        object = new JSONObject(t.toString());
                                        if (object.optInt(Constants.RESULT_CODE) == 0) {
                                                JSONObject data = object.optJSONObject(Constants.DATA);
                                                JSONArray array = data.getJSONArray("arr");
                                                if (array.length() > 0) {
                                                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
                                                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                                                        for (int i = 0; i < array.length(); i++) {
                                                                JSONObject tablelist = array.getJSONObject(i);
                                                                String time = tablelist.optString("time");
                                                                ExpressStatus status = new ExpressStatus();
                                                                status.setMissionNo(tablelist.optString("missionNo"));
                                                                status.setWeight(tablelist.optString("weight") + "KG");
                                                                status.setExpressDate(dateFormat.format(new Date(Long.parseLong(time))));
                                                                status.setExpressTime(timeFormat.format(new Date(Long.parseLong(time))));
                                                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                                                {
                                                                        status.setJianShu(tablelist.optString("number") + "件");
                                                                }
                                                                else
                                                                {
                                                                        status.setJianShu(tablelist.optString("number") + "Pieces");
                                                                }

                                                                status.setLine(tablelist.optString("route"));
                                                                status.setPay(tablelist.optString("amount"));
                                                                status.setTiJi(tablelist.optString("volumn") + "m³");
                                                                status.setNumber(tablelist.optString("waybillNumber"));
                                                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                                                {
                                                                        status.setWay((tablelist.optString("type").equals("提货点到仓库")) ? "中转" : "直送");
                                                                }
                                                                else
                                                                {
                                                                        status.setWay((tablelist.optString("type").equals("Take point To Storage")) ? "Transfer" : "Direct");
                                                                }
                                                                status.setSendAddr(tablelist.optString("sendAddr"));
                                                                status.setComplayName(tablelist.optString("companyName"));
                                                                status.setReceiveAddr(tablelist.optString("receiveAddr"));
                                                                status.setMissionStatus(tablelist.optString("missionStatus"));

                                                                dataList.add(status);
                                                        }
                                                        FaCheAdapter adapter = new FaCheAdapter(FaCheActivity.this, R.layout.item_listview_fache, dataList,config);
                                                        faCheListView.setAdapter(adapter);
                                                        faCheListView.setMenuCreator(creator);
                                                }
                                        } else {
                                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                                {
                                                        showStringToastMsg("无数据！");
                                                }
                                                else
                                                {
                                                        showStringToastMsg("No Data！");
                                                }
                                        }
                                } catch (JSONException e) {
                                        e.printStackTrace();
                                }
                        }

                        ;

                        public void onFailure(Throwable t, String strMsg) {
                                hideLoadingDialog();
                                showToastMsg(R.string.error_network);
                        }

                        ;
                });
                //请求结束

                /**
                 * 滑动点击事件
                 */
                faCheListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                                ExpressStatus task = dataList.get(position);
                                String missonStatus = task.getMissionStatus();
                                switch (index) {
                                        case 0:
                                                // 接单
                                                if(missonStatus.equals("3"))
                                                {
                                                        daoda(task.getMissionNo());
                                                }
                                                else
                                                {
                                                        qianShou(task.getWay(), task.getMissionNo());
                                                }

                                                break;
                                }
                                return false;
                        }
                });

        }

        private SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {
                        // Create different menus depending on the view type
                        switch (menu.getViewType()) {
                                case 1:
                                        createMenu1(menu);
                                        break;
                                case 3:
                                        createMenu3(menu);
                                        break;
                        }
                }

                private void createMenu1(SwipeMenu menu) {
                        SwipeMenuItem item1 = new SwipeMenuItem(
                                getApplicationContext());
                        SwipeMenuItem openItem = new SwipeMenuItem(
                                getApplicationContext());
                        openItem.setBackground(new ColorDrawable(Color.rgb(255,
                                170, 0)));
                        openItem.setWidth(dp2px(100));
                        openItem.setTitle(R.string.sign);
                        openItem.setTitleSize(18);
                        openItem.setTitleColor(Color.WHITE);
                        menu.addMenuItem(openItem);
                }

                private void createMenu2(SwipeMenu menu) {
                        SwipeMenuItem item2 = new SwipeMenuItem(
                                getApplicationContext());
                        SwipeMenuItem openItem = new SwipeMenuItem(
                                getApplicationContext());
                        openItem.setBackground(new ColorDrawable(Color.rgb(255,
                                170, 0)));
                        openItem.setWidth(dp2px(100));
                        openItem.setTitle(R.string.daozhan);
                        openItem.setTitleSize(18);
                        openItem.setTitleColor(Color.WHITE);
                        menu.addMenuItem(openItem);
                }
                private void createMenu3(SwipeMenu menu) {
                        SwipeMenuItem item3 = new SwipeMenuItem(
                                getApplicationContext());
                        SwipeMenuItem openItem = new SwipeMenuItem(
                                getApplicationContext());
                        openItem.setBackground(new ColorDrawable(Color.rgb(255,
                                170, 0)));
                        openItem.setWidth(dp2px(100));
                        openItem.setTitle(R.string.daoda);
                        openItem.setTitleSize(18);
                        openItem.setTitleColor(Color.WHITE);
                        menu.addMenuItem(openItem);
                }
        };

        private int dp2px(int dp) {
                return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                        getResources().getDisplayMetrics());
        }


        @Override
        protected void onResume() {
                super.onResume();
                // item点击
                faCheListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ExpressStatus task = dataList.get(position);
                                if(task.getMissionStatus().equals("3"))
                                {
                                        if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                        {
                                                Intent intent = new Intent(FaCheActivity.this, FaCheInfoActivity.class);
                                                intent.putExtra("mission_no", task.getMissionNo());
                                                intent.putExtra("type", "到达");
                                                startActivity(intent);
                                                finish();
                                        }
                                        else
                                        {
                                                Intent intent = new Intent(FaCheActivity.this, FaCheInfoActivity.class);
                                                intent.putExtra("mission_no", task.getMissionNo());
                                                intent.putExtra("type", "Arrive");
                                                startActivity(intent);
                                                finish();
                                        }
                                        return;
                                }
                                if(task.getWay().equals("中转"))
                                {
                                        Intent intent = new Intent(FaCheActivity.this, FaCheInfoActivity.class);
                                        intent.putExtra("mission_no", task.getMissionNo());
                                        intent.putExtra("type", "到站");
                                        startActivity(intent);
                                        finish();
                                }
                                else if(task.getWay().equals("直送"))
                                {
                                        Intent intent = new Intent(FaCheActivity.this, FaCheInfoActivity.class);
                                        intent.putExtra("mission_no", task.getMissionNo());
                                        intent.putExtra("type", "签收");
                                        startActivity(intent);
                                        finish();
                                }
                                else if(task.getWay().equals("Transfer"))
                                {
                                        Intent intent = new Intent(FaCheActivity.this, FaCheInfoActivity.class);
                                        intent.putExtra("mission_no", task.getMissionNo());
                                        intent.putExtra("type", "Arrive");
                                        startActivity(intent);
                                        finish();
                                }
                                else if(task.getWay().equals("Direct"))
                                {
                                        Intent intent = new Intent(FaCheActivity.this, FaCheInfoActivity.class);
                                        intent.putExtra("mission_no", task.getMissionNo());
                                        intent.putExtra("type", "Sign");
                                        startActivity(intent);
                                        finish();
                                }

                        }
                });
        }

        /**
         * 签收按钮 全部到站签收
         */
        public void qianShou(String way, String missionNo) {
                if(way.equals("中转"))
                {
                        // 中转到站
                        AjaxParams params=new AjaxParams();
                        params.put("type", "2");
                        params.put("missionNo", missionNo);
                        params.put("waybillNo", "-1");
                        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
                        String url = MainUrl.URL + MainUrl.TASKORDERSIGN;
                        PDAApplication.http.configTimeout(8000);
                        showLoadingDialog();
                        PDAApplication.http.post(url,params,new AjaxCallBack<Object>() {
                                public void onSuccess(Object t) {
                                        hideLoadingDialog();
                                        JSONObject object;
                                        try {
                                                object = new JSONObject(t.toString());
                                                if (object.optInt(Constants.RESULT_CODE) == 0) {
                                                        if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                                        {
                                                                showStringToastMsg("到站成功！");
                                                        }
                                                        else
                                                        {
                                                                showStringToastMsg("Arrive Success！");
                                                        }
                                                }
                                                else
                                                {
                                                        if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                                        {
                                                                showStringToastMsg("到站失败！");
                                                        }
                                                        else
                                                        {
                                                                showStringToastMsg("Arrive failed！");
                                                        }
                                                }
                                                Intent intent = new Intent(FaCheActivity.this, FaCheActivity.class);
                                                startActivity(intent);
                                                finish();
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                        }
                                };
                                public void onFailure(Throwable t, String strMsg) {
                                        hideLoadingDialog();
                                        showToastMsg(R.string.error_network);
                                };
                        } );
                        //请求结束
                }
                else
                {
                        //跳转到签收上传图片页面
                        Intent intent = new Intent(FaCheActivity.this, QianShouAllActivity.class);
                        intent.putExtra("mission_no", missionNo);
                        intent.putExtra("waybillNo", "-1");
                        intent.putExtra("activity", "FaCheActivity");
                        startActivity(intent);
                        finish();
                }

        }

        /**
         * 发车按钮 全部发车
         */
        public void daoda(String missonNo) {
                //请求开始
                AjaxParams params=new AjaxParams();
                params.put("driverid", AppCache.getString(AppCacheKey.driverid));
                params.put("missionNo", missonNo);
                params.put("type", "2");
                params.put("waybillNo", "-1");
                String url = MainUrl.URL + MainUrl.TASKORDERARRIVE;
                PDAApplication.http.configTimeout(8000);
                PDAApplication.http.post(url,params,new AjaxCallBack<Object>() {
                        public void onSuccess(Object t) {
                                hideLoadingDialog();
                                JSONObject object;
                                try {
                                        object = new JSONObject(t.toString());
                                        if (object.optInt(Constants.RESULT_CODE) == 0) {
                                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                                {
                                                        showStringToastMsg("到达成功！");
                                                }
                                                else
                                                {
                                                        showStringToastMsg("Arrive successful！");
                                                }
                                        }
                                        else
                                        {
                                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                                {
                                                        showStringToastMsg("到达失败！");
                                                }
                                                else
                                                {
                                                        showStringToastMsg("Arrive Failed！");
                                                }
                                        }
                                        Intent intent = new Intent(FaCheActivity.this, FaCheActivity.class);
                                        startActivity(intent);
                                        finish();
                                } catch (JSONException e) {
                                        e.printStackTrace();
                                }
                        };
                        public void onFailure(Throwable t, String strMsg) {
                                hideLoadingDialog();
                                showToastMsg(R.string.error_network);
                        };
                } );
                //请求结束
        }


        @Override
        public void onClick(View v) {
                switch (v.getId()) {
                        case R.id.fache_express_return:
                                Intent intent = new Intent(FaCheActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                }
        }

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                        Intent intent = new Intent(FaCheActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        return false;
                }else {
                        return super.onKeyDown(keyCode, event);
                }

        }

}
