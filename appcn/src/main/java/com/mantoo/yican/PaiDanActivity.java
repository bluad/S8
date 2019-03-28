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
import com.mantoo.yican.cn.R;
import com.mantoo.yican.adapter.ExpressPaiDanAdapter;
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
 * 派单页面
 * Created by Administrator on 2017/10/12.
 */

public class PaiDanActivity extends BaseActivity implements View.OnClickListener {

    List<ExpressStatus> dataList = new ArrayList<ExpressStatus>();

    private SwipeMenuListView taskListView;

    private Configuration config;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paidan);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        initView();

    }

    private void initView() {
        taskListView = (SwipeMenuListView)findViewById(R.id.paidan_express_list);
        findViewById(R.id.paidan_express_return).setOnClickListener(this);

        //请求开始
        AjaxParams params=new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        params.put("missionType", "1");
        String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.PIELIST;
        System.out.println(params+url);
        showLoadingDialog();
        PDAApplication.http.configTimeout(8000);
        PDAApplication.http.post(url,params,new AjaxCallBack<Object>() {
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
                                status.setWeight(tablelist.optString("weight")+"KG");
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
                                status.setTiJi(tablelist.optString("volumn")+"m³");
                                status.setNumber(tablelist.optString("waybillNumber"));
                                status.setTotalAcount(tablelist.optString("totalAmount"));
                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                {
                                    status.setWay((tablelist.optString("type").equals("提货点到仓库")) ? "中转" : "直送");
                                }
                                else
                                {
                                    status.setWay((tablelist.optString("type").equals("提货点到仓库")) ? "Transfer" : "Direct");
                                }
                                dataList.add(status);
                            }
                            ExpressPaiDanAdapter adapter = new ExpressPaiDanAdapter(PaiDanActivity.this, R.layout.item_listview_paidan_task, dataList);
                            taskListView.setAdapter(adapter);
                            taskListView.setMenuCreator(creator);
                        }
                    }
                    else
                    {
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
            };
            public void onFailure(Throwable t, String strMsg) {
                hideLoadingDialog();
                showToastMsg(R.string.error_network);
            };
        } );
        //请求结束


        /**
         * 滑动点击事件
         */
        taskListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                ExpressStatus task = dataList.get(position);
                switch (index) {
                    case 0:
                        // 接单
                        accepitWayBill(task.getMissionNo());
                        break;
                    case 1:
                        // 拒单
                        Intent intent = new Intent(PaiDanActivity.this, PaidanTaskRefuseActivity.class);
                        intent.putExtra("mission_no", task.getMissionNo());
                        startActivity(intent);
                        finish();
                        break;
                }
                return false;
            }
        });
    }


    private SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            SwipeMenuItem openItem = new SwipeMenuItem(
                    getApplicationContext());
            openItem.setBackground(new ColorDrawable(Color.rgb(255,
                    170, 0)));
            openItem.setWidth(dp2px(100));
            openItem.setTitle(R.string.Take_order);
            openItem.setTitleSize(18);
            openItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(openItem);

            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                    0x3F, 0x25)));
            deleteItem.setWidth(dp2px(100));
            deleteItem.setTitle(R.string.Refuse);
            deleteItem.setTitleSize(18);
            deleteItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(deleteItem);
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
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExpressStatus task = dataList.get(position);
                Intent intent = new Intent(PaiDanActivity.this, TaskInfoActivity.class);
                intent.putExtra("mission_no", task.getMissionNo());
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.paidan_express_return:
                Intent intent = new Intent(PaiDanActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(PaiDanActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }


    /**
     * 接单
     * @param missionNo
     */
    private void accepitWayBill(String missionNo)
    {
        //请求开始
        AjaxParams params=new AjaxParams();
        params.put("missionNo", missionNo);
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.DISPATCHORDER;
        System.out.println(params+url);
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
                            showStringToastMsg("接单成功！");
                        }
                        else
                        {
                            showStringToastMsg("Order successful！");
                        }
                        Intent intent = new Intent(PaiDanActivity.this, PaiDanActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                        {
                            showStringToastMsg("接单失败！");
                        }
                        else
                        {
                            showStringToastMsg("Order failed！");
                        }
                    }
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

}