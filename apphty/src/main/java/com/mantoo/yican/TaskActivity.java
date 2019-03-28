package com.mantoo.yican;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.mantoo.yican.adapter.CompletionInfoAdapter;
import com.mantoo.yican.adapter.GoodInfoAdapter;
import com.mantoo.yican.adapter.TaskExpressInfoAdapter;
import com.mantoo.yican.cn.hty.R;
import com.mantoo.yican.adapter.ExpressPaiDanAdapter;
import com.mantoo.yican.adapter.TaskExpressAdapter;
import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.model.ExpressStatus;
import com.mantoo.yican.model.GoodInfo;
import com.mantoo.yican.model.TaskInfo;
import com.mantoo.yican.view.SwipeMenu;
import com.mantoo.yican.view.SwipeMenuCreator;
import com.mantoo.yican.view.SwipeMenuItem;
import com.mantoo.yican.view.SwipeMenuListView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 任务模块
 * Created by Administrator on 2017/10/12.
 */

public class TaskActivity extends BaseActivity implements View.OnClickListener {

    List<ExpressStatus> dataList = new ArrayList<ExpressStatus>();

    List<GoodInfo> goodsList = new ArrayList<GoodInfo>();

    List<GoodInfo> showList = new ArrayList<GoodInfo>();

    private SwipeMenuListView taskListView;

    private Configuration config;
    private Resources resources;

    // 定位
    private LocationManager locationManager;
    private Location location;
    private static final double EARTH_RADIUS = 6378.137;
    private Double pickLng, pickLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        initView();

    }

    private void initView() {
        findViewById(R.id.task_express_return).setOnClickListener(this);
        taskListView = (SwipeMenuListView)findViewById(R.id.task_express_list);

        //请求开始
        AjaxParams params=new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        params.put("missionType", "2");
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

                                JSONArray arrayGood = tablelist.optJSONArray("basket");
                                if (arrayGood.length() > 0) {
                                    for (int j = 0; j < arrayGood.length(); j++) {
                                        JSONObject items = arrayGood.getJSONObject(j);
                                        GoodInfo good = new GoodInfo();
                                        good.setOrderNumber(items.optString("ORDER_NO"));
                                        good.setCustomerName(items.optString("CUSTOMER_NAME"));
                                        good.setMissonNumber(items.optString("MISSION_NO"));
                                        good.setWaybillNumber(items.optString("WAYBILL_NO"));
                                        goodsList.add(good);
                                    }
                                }
                            }
                            TaskExpressAdapter adapter = new TaskExpressAdapter(TaskActivity.this, R.layout.item_listview_task_express, dataList);
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
                        tiHuo(task.getMissionNo());
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
            openItem.setTitle(R.string.Pick_up);
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
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExpressStatus task = dataList.get(position);
                String mNumber = task.getMissionNo();
                List<GoodInfo> paramsList = new ArrayList<GoodInfo>();
                //过滤此任务单下面的订单集合
                for(GoodInfo temp : goodsList)
                {
                    if(temp.getMissonNumber().equals(mNumber))
                    {
                        GoodInfo goodinfo = new GoodInfo();
                        goodinfo.setMissonNumber(mNumber);
                        goodinfo.setCustomerName(temp.getCustomerName());
                        goodinfo.setOrderNumber(temp.getOrderNumber());
                        goodinfo.setWaybillNumber(temp.getWaybillNumber());
                        paramsList.add(goodinfo);
                    }
                }
                Intent intent = new Intent(TaskActivity.this, TaskExpressInfoActivity.class);
                intent.putExtra("mission_no", mNumber);
                intent.putExtra("paramsList", (Serializable) paramsList);
                startActivity(intent);
                finish();
            }
        });
    }


    private void showSetDeBugDialog(String missonNumber) {
        final String mNumber = missonNumber;
        AlertDialog.Builder setDeBugDialog = new AlertDialog.Builder(this);
        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.alert_input_number, null);
        setDeBugDialog.setView(dialogView);
        //初始化控件
        Button okButton = (Button) dialogView.findViewById(R.id.ensure_ok);
        Button cacelButton = (Button) dialogView.findViewById(R.id.cacelButton);

        //过滤此任务单下面的订单集合
        for(GoodInfo temp : goodsList)
        {
            if(temp.getMissonNumber().equals(mNumber))
            {
                GoodInfo goodinfo = new GoodInfo();
                goodinfo.setMissonNumber(mNumber);
                goodinfo.setCustomerName(temp.getCustomerName());
                goodinfo.setOrderNumber(temp.getOrderNumber());
                goodinfo.setWaybillNumber(temp.getWaybillNumber());
                showList.add(goodinfo);
            }
        }

        //订单列表
        ListView goodListView = (ListView) dialogView.findViewById(R.id.select_alert_listview);
        GoodInfoAdapter adapter = new GoodInfoAdapter(TaskActivity.this, R.layout.item_listview_good_info, showList);
        goodListView.setAdapter(adapter);
        //取消点击外部消失弹窗
        setDeBugDialog.setCancelable(false);
        setDeBugDialog.create();
        final AlertDialog customAlert = setDeBugDialog.show();
        customAlert.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //设置自定义界面的点击事件逻辑
        okButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray arrys = new JSONArray();
                for (Map.Entry<String, String> entry : MainUrl.goodMap.entrySet()) {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("orderNo",entry.getKey());
                        json.put("outNumber",entry.getValue());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    arrys.put(json);
                }
                submitHuo(mNumber,arrys.toString());
                customAlert.dismiss();
                MainUrl.goodMap.clear();
                showList.clear();
            }
        });
        cacelButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customAlert.dismiss();
                MainUrl.goodMap.clear();
                showList.clear();
            }
        });
    };




    /**
     * 提货
     */
    public void tiHuo(String missonNo) {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // 获取location对象
        location = getBestLocation(locationManager);

        final String param = missonNo;
        AjaxParams params=new AjaxParams();
        params.put("missionNo", missonNo);
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.TASKLIST;
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
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject tablelist = array.getJSONObject(i);

                                // 当前位置
                                String currentLng = "";
                                String currentLat = "";
                                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                                // 获取location对象
                                location = getBestLocation(locationManager);
                                if (location != null) {
                                    //"经度" location.getLongitude() " 纬度"location.getLatitude()
                                    currentLng = String.valueOf(location.getLongitude());
                                    currentLat = String.valueOf(location.getLatitude());
                                }

                                if(!(currentLng == null || currentLng.equals("") || currentLat == null || currentLat.equals("")))
                                {
                                    LatLng wgLoc = transformFromWGSToGCJ(new LatLng(Double.parseDouble(currentLat), Double.parseDouble(currentLng)));
                                    double distance;
                                    try
                                    {
                                        distance = distanceOfTwoPoints(wgLoc.latitude,wgLoc.longitude,Double.parseDouble(tablelist.optString("startLat")),Double.parseDouble(tablelist.optString("startLng")));
                                    }
                                    catch(Exception e)
                                    {
                                        distance = 0.0;
                                    }

                                    pickLng = location.getLongitude();
                                    pickLat = location.getLatitude();
                                    if(distance > 500)
                                    {
                                        dialog(param);
                                    }
                                    else
                                    {
                                        showSetDeBugDialog(param);
                                    }
                                }
                                else
                                {
                                    showSetDeBugDialog(param);
                                }

                            }
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
    }

    public void dialog(final String param) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TaskActivity.this);
        builder.setMessage("未到达指定提货地点，是否继续提货？");

        builder.setTitle("提示");

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                showSetDeBugDialog(param);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    //提货
    private void submitHuo(String missonNo, final String selectSers)
    {
        //请求开始
        AjaxParams params=new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        params.put("missionNo", missonNo);
        params.put("waybillNo", "-1");
        params.put("pickLng", String.valueOf(pickLng));
        params.put("pickLat", String.valueOf(pickLat));
        params.put("baskets", selectSers);
        String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.TASKSHEETDEPARTURE;
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
                            showStringToastMsg("提货成功！");
                        }
                        else
                        {
                            showStringToastMsg("Delivery successful！");
                        }
                    }
                    else
                    {
                        if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                        {
                            showStringToastMsg("提货失败！");
                        }
                        else
                        {
                            showStringToastMsg("Delivery Failed！");
                        }
                    }
                    Intent intent = new Intent(TaskActivity.this, TaskActivity.class);
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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.task_express_return:
                Intent intent = new Intent(TaskActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(TaskActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

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

    private double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public double distanceOfTwoPoints(double lat1,double lng1,
                                      double lat2,double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10;
        return s;
    }

    static double a = 6378245.0;
    static double ee = 0.00669342162296594323;

    public static LatLng transformFromWGSToGCJ(LatLng wgLoc) {

        //如果在国外，则默认不进行转换
        if (outOfChina(wgLoc.latitude, wgLoc.longitude)) {
            return new LatLng(wgLoc.latitude, wgLoc.longitude);
        }
        double dLat = transformLat(wgLoc.longitude - 105.0,
                wgLoc.latitude - 35.0);
        double dLon = transformLon(wgLoc.longitude - 105.0,
                wgLoc.latitude - 35.0);
        double radLat = wgLoc.latitude / 180.0 * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0)/ ((a * (1 - ee)) / (magic * sqrtMagic) * Math.PI);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * Math.PI);

        return new LatLng(wgLoc.latitude + dLat, wgLoc.longitude + dLon);
    }

    public static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(x > 0 ? x : -x);
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x
                * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0
                * Math.PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * Math.PI) + 320 * Math.sin(y
                * Math.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(x > 0 ? x : -x);
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x
                * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0
                * Math.PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x
                / 30.0 * Math.PI)) * 2.0 / 3.0;
        return ret;
    }

    public static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }
}
