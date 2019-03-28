package com.mantoo.yican;

import android.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.mantoo.yican.adapter.TaskExpressInfoAdapter;
import com.mantoo.yican.adapter.TaskInfoAdapter;
import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.model.ExpressStatus;
import com.mantoo.yican.model.TaskInfo;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.mantoo.yican.cn.R;

/**
 * 任务（任务详情）
 * Created by Administrator on 2017/10/14.
 */

public class TaskExpressInfoActivity extends BaseActivity implements View.OnClickListener, TaskExpressInfoAdapter.Callback {

    private List<TaskInfo> dataList = new ArrayList<TaskInfo>();

    private List<String> waybillNos = new ArrayList<String>();

    private Configuration config;
    private Resources resources;

    private ListView taskInfoList;
    private String missionNo;
    private Double pickLng, pickLat;

    /*
	 * 定位
	 */
    private LocationManager locationManager;
    private Location location;
    private static final double EARTH_RADIUS = 6378.137;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_express_info);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        initView();
    }

    private void initView() {
        findViewById(R.id.task_express_info_button).setOnClickListener(this);
        findViewById(R.id.task_express_info_return).setOnClickListener(this);
        taskInfoList = (ListView)findViewById(R.id.task_express_info_list);

        Intent intent = getIntent();
        missionNo = intent.getStringExtra("mission_no");
        //请求开始
        AjaxParams params=new AjaxParams();
        params.put("missionNo", missionNo);
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
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        if (array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject tablelist = array.getJSONObject(i);
                                TaskInfo taskInfo = new TaskInfo();
                                taskInfo.setWaybillNo(tablelist.optString("waybillNo"));
                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                {
                                    taskInfo.setNumber(tablelist.optString("number")+"件");
                                }
                                else
                                {
                                    taskInfo.setNumber(tablelist.optString("number")+"Pieces");
                                }
                                taskInfo.setSendAddress(tablelist.optString("sendAddress"));
                                taskInfo.setReceviceAddress(tablelist.optString("receviceAddress"));
                                taskInfo.setVolumn(tablelist.optString("volumn")+"m³");
                                taskInfo.setWeight(tablelist.optString("weight")+"KG");
                                taskInfo.setDistence(tablelist.optString("distance")+"km");
                                taskInfo.setStartLat(tablelist.optString("startLat"));
                                taskInfo.setStartLng(tablelist.optString("startLng"));
                                taskInfo.setEndLat(tablelist.optString("endLat"));
                                taskInfo.setEndLng(tablelist.optString("endLng"));
                                taskInfo.setDaishouAmount(tablelist.optString("daishouAmount"));
                                if(!tablelist.isNull("sendDate"))
                                {
                                    taskInfo.setSendDate(dateFormat.format(new Date(Long.parseLong(tablelist.optString("sendDate")))));
                                }
                                taskInfo.setPaytype(tablelist.optString("paytype"));
                                taskInfo.setAccount(tablelist.optString("account"));
                                dataList.add(taskInfo);
                            }
                            TaskExpressInfoAdapter adapter = new TaskExpressInfoAdapter(TaskExpressInfoActivity.this, R.layout.item_listview_task_express_info, dataList, TaskExpressInfoActivity.this);
                            taskInfoList.setAdapter(adapter);
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


    @Override
    protected void onResume() {
        super.onResume();
        // item点击
        taskInfoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskInfo task = dataList.get(position);
                Intent intent = new Intent(TaskExpressInfoActivity.this, WayBillInfoActivity.class);
                intent.putExtra("waybillNo", task.getWaybillNo());
                intent.putExtra("missionNo", missionNo);
                intent.putExtra("activityType", "TaskExpressInfoActivity");
                startActivity(intent);
            }
        });
    }


    // 发车
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.task_express_info_button:
                int waybillnumbers = waybillNos.size();
                if(waybillnumbers == 0)
                {
                    if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                    {
                        showStringToastMsg("至少选择一个！");
                    }
                    else
                    {
                        showStringToastMsg("Choose at least one！");
                    }
                    return;
                }

                StringBuffer sb = new StringBuffer(); //[“W001”,”W002”]
                sb.append("[");
                for(int i=0;i<waybillnumbers;i++)
                {
                    if(i != (waybillnumbers - 1))
                    {
                        sb.append("\"" + waybillNos.get(i) + "\",");
                    }
                    else
                    {
                        sb.append("\"" + waybillNos.get(i) + "\"");
                    }

                }

                sb.append("]");


                // 当前位置到提货点的距离
                String lng = "";
                String lat = "";
                for(int i=0; i < dataList.size(); i++)
                {
                    if(dataList.get(i).getWaybillNo().equals(waybillNos.get(0)))
                    {
                        lng = dataList.get(i).getStartLng();
                        lat = dataList.get(i).getStartLat();
                    }
                }

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

                LatLng wgLoc = transformFromWGSToGCJ(new LatLng(Double.parseDouble(currentLat), Double.parseDouble(currentLng)));
                double distance;
                try
                {
                    distance = distanceOfTwoPoints(wgLoc.latitude,wgLoc.longitude,Double.parseDouble(lat),Double.parseDouble(lng));
                }
                catch (Exception e)
                {
                    distance = 0.0;
                }
                pickLng = location.getLongitude();
                pickLat = location.getLatitude();

                if(distance > 500)
                {
                    dialog(sb.toString());
                }
                else
                {
                    //请求开始
                    AjaxParams params=new AjaxParams();
                    params.put("driverid", AppCache.getString(AppCacheKey.driverid));
                    params.put("missionNo", missionNo);
                    params.put("waybillNo", sb.toString());
                    params.put("pickLng", String.valueOf(pickLng));
                    params.put("pickLat", String.valueOf(pickLat));
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
                                    Intent intent = new Intent(TaskExpressInfoActivity.this, TaskActivity.class);
                                    startActivity(intent);
                                    finish();
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

                //请求结束
                break;
            case R.id.task_express_info_return:
                Intent intent = new Intent(TaskExpressInfoActivity.this, TaskActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }


    public void dialog(final String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TaskExpressInfoActivity.this);
        builder.setMessage("未到达指定提货地点，是否继续提货？");

        builder.setTitle("提示");

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //请求开始
                AjaxParams params=new AjaxParams();
                params.put("driverid", AppCache.getString(AppCacheKey.driverid));
                params.put("missionNo", missionNo);
                params.put("waybillNo", str);
                params.put("pickLng", String.valueOf(pickLng));
                params.put("pickLat", String.valueOf(pickLat));
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
                                Intent intent = new Intent(TaskExpressInfoActivity.this, TaskActivity.class);
                                startActivity(intent);
                                finish();
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
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(TaskExpressInfoActivity.this, TaskActivity.class);
            startActivity(intent);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public void click(View v) {
        TaskInfo task = dataList.get((Integer) v.getTag());
        switch (v.getId()) {
            case R.id.route_plan:
                // 提货的起始点为当前司机的位置
                String startLng = "";
                String startLat = "";
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                // 获取location对象
                location = getBestLocation(locationManager);
                if (location != null) {
                    //"经度" location.getLongitude() " 纬度"location.getLatitude()
                    startLng = String.valueOf(location.getLongitude());
                    startLat = String.valueOf(location.getLatitude());
                }
                Intent intent = new Intent(TaskExpressInfoActivity.this, BaiduMapActivity.class);
                intent.putExtra("activity", "TaskExpressInfoActivity");
                intent.putExtra("mission_no", missionNo);
                intent.putExtra("startLng", startLng);
                intent.putExtra("startLat", startLat);
                intent.putExtra("endLng", task.getStartLng());
                intent.putExtra("endLat", task.getStartLat());
                startActivity(intent);
                finish();
                break;

            case R.id.task_express_info_select_express:
                // 如果集合中有运单号，说明是第二次点击，那就是取消
                String waybillno = task.getWaybillNo();
                if(waybillNos.contains(waybillno))
                {
                    waybillNos.remove(waybillno);
                }
                else
                {
                    waybillNos.add(waybillno);
                }
                break;
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
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
