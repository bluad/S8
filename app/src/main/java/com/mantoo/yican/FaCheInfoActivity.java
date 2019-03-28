package com.mantoo.yican;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mantoo.yican.adapter.FaCheInfoAdapter;
import com.mantoo.yican.adapter.TaskExpressInfoAdapter;
import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.model.TaskInfo;
import com.mantoo.yican.widget.RotateTextView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 发车（任务详情）
 * Created by Administrator on 2017/10/14.
 */

public class FaCheInfoActivity extends BaseActivity implements View.OnClickListener, FaCheInfoAdapter.Callback {

    private List<TaskInfo> dataList = new ArrayList<TaskInfo>();
    private Button fache_express_info_button;
    private ListView taskInfoList;
    private String missionNo;
    private String type;
    private List<String> waybillNos = new ArrayList<String>();

    private Configuration config;
    private Resources resources;

    private LocationManager locationManager;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fache_info);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        initView();
    }

    private void initView() {
        findViewById(R.id.fache_express_info_button).setOnClickListener(this);
        fache_express_info_button = (Button)findViewById(R.id.fache_express_info_button);
        taskInfoList = (ListView)findViewById(R.id.fache_express_info_list);
        findViewById(R.id.fache_express_info_return).setOnClickListener(this);

        Intent intent = getIntent();
        missionNo = intent.getStringExtra("mission_no");
        type = intent.getStringExtra("type");
        fache_express_info_button.setText(type);
        //请求开始
        AjaxParams params=new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        params.put("missionNo", missionNo);
        String url = MainUrl.URL + MainUrl.FACHETASKLIST;
        System.out.println(params+url);
        PDAApplication.http.configTimeout(8000);
        showLoadingDialog();
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
                                TaskInfo taskInfo = new TaskInfo();
                                taskInfo.setWaybillNo(tablelist.optString("waybillNo"));
                                taskInfo.setNumber(tablelist.optString("number")+"件");
                                taskInfo.setSendAddress(tablelist.optString("sendAddress"));
                                taskInfo.setReceviceAddress(tablelist.optString("receviceAddress"));
                                taskInfo.setVolumn(tablelist.optString("volumn")+"m³");
                                taskInfo.setWeight(tablelist.optString("weight")+"KG");
                                taskInfo.setWaybillStatus(tablelist.optString("waybillStatus"));
                                taskInfo.setDistence(tablelist.optString("distance")+"km");
                                taskInfo.setStartLat(tablelist.optString("startLat"));
                                taskInfo.setStartLng(tablelist.optString("startLng"));
                                taskInfo.setEndLat(tablelist.optString("endLat"));
                                taskInfo.setEndLng(tablelist.optString("endLng"));
                                dataList.add(taskInfo);
                            }
                            FaCheInfoAdapter adapter = new FaCheInfoAdapter(FaCheInfoActivity.this, R.layout.item_listview_fache_info, dataList, FaCheInfoActivity.this);
                            taskInfoList.setAdapter(adapter);
                        }
                    }
                    else
                    {
                        if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                        {
                            showStringToastMsg("获取数据失败");
                        }
                        else
                        {
                            showStringToastMsg("Failed to get data");
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


    //  按钮 到站或签收
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fache_express_info_button:
                if(fache_express_info_button.getText().equals("到达") || fache_express_info_button.getText().equals("Arrive"))
                {
                    daoda(missionNo);
                    return;
                }
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

                // 如果是签收的话，一个记录跳转到签收详情，多个记录跳转到批量签收页面
                if(fache_express_info_button.getText().toString().equals("签收") ||
                        fache_express_info_button.getText().toString().equals("Sign"))
                {
                    if(waybillnumbers == 1)
                    {
                        Intent intent = new Intent(FaCheInfoActivity.this, QianShouActivity.class);
                        intent.putExtra("mission_no", missionNo);
                        intent.putExtra("waybillNo", waybillNos.get(0));
                        intent.putExtra("type", type);
                        startActivity(intent);
                        finish();
                        return;
                    }
                    else
                    {
                        Intent intent = new Intent(FaCheInfoActivity.this, QianShouAllActivity.class);
                        intent.putExtra("mission_no", missionNo);
                        intent.putExtra("waybillNo", sb.toString());
                        intent.putExtra("activity", "FaCheInfoActivity");
                        intent.putExtra("type", type);
                        startActivity(intent);
                        finish();
                        return;
                    }
                }

                //请求开始
                AjaxParams params=new AjaxParams();
                params.put("missionNo", missionNo);
                params.put("waybillNo", sb.toString());
                params.put("type", "2");
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
                                Intent intent = new Intent(FaCheInfoActivity.this, FaCheActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                {
                                    showStringToastMsg("到站失败！");
                                }
                                else
                                {
                                    showStringToastMsg("Arrive Failed！");
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
                break;
            case R.id.fache_express_info_return:
                Intent intent = new Intent(FaCheInfoActivity.this, FaCheActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(FaCheInfoActivity.this, FaCheActivity.class);
            startActivity(intent);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // item点击
        taskInfoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskInfo task = dataList.get(position);
                Intent intent = new Intent(FaCheInfoActivity.this, WayBillInfoActivity.class);
                intent.putExtra("waybillNo", task.getWaybillNo());
                intent.putExtra("missionNo", missionNo);
                startActivity(intent);
            }
        });
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
                    Intent intent = new Intent(FaCheInfoActivity.this, FaCheInfoActivity.class);
                    intent.putExtra("mission_no", missionNo);
                    if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                    {
                        intent.putExtra("type", "签收");
                    }
                    else
                    {
                        intent.putExtra("type", "Sign");
                    }
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
                Intent intent = new Intent(FaCheInfoActivity.this, BaiduMapActivity.class);
                intent.putExtra("activity", "FaCheInfoActivity");
                intent.putExtra("mission_no", missionNo);
                intent.putExtra("type", type);
                intent.putExtra("startLng", startLng);
                intent.putExtra("startLat", startLat);
                intent.putExtra("endLng", task.getEndLng());
                intent.putExtra("endLat", task.getEndLat());
                startActivity(intent);
                finish();
                break;

            case R.id.fache_express_info_select_express:
                // 如果集合中有运单号，说明是第二次点击，那就是取消
                String waybillno = task.getWaybillNo();
                if (waybillNos.contains(waybillno)) {
                    waybillNos.remove(waybillno);
                } else {
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
}
