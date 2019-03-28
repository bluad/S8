package com.mantoo.yican;

import android.*;
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
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    private String type;

    private Button task_express_info_button;

    /*
	 * 定位
	 */
    private TextView locationCity;
    private LocationManager locationManager;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_express_info);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        initView();

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if(type != null && !type.equals(""))
        {
            task_express_info_button.setText(type);
        }

    }

    private void initView() {
        task_express_info_button = (Button) findViewById(R.id.task_express_info_button);
        findViewById(R.id.task_express_info_button).setOnClickListener(this);
        findViewById(R.id.task_express_info_return).setOnClickListener(this);
        taskInfoList = (ListView)findViewById(R.id.task_express_info_list);

        Intent intent = getIntent();
        missionNo = intent.getStringExtra("mission_no");
        //请求开始
        AjaxParams params=new AjaxParams();
        params.put("missionNo", missionNo);
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        String url = MainUrl.URL + MainUrl.TASKLIST;
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
                startActivity(intent);
            }
        });
    }


    // 发车
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.task_express_info_button:
                if(task_express_info_button.getText().equals("到达") || task_express_info_button.getText().equals("Arrive"))
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
                Intent intent1 = new Intent(TaskExpressInfoActivity.this, RemarkActivity.class);
                intent1.putExtra("missonNo", missionNo);
                intent1.putExtra("waybillNos", sb.toString());
                startActivity(intent1);
                break;
            case R.id.task_express_info_return:
                Intent intent = new Intent(TaskExpressInfoActivity.this, TaskActivity.class);
                startActivity(intent);
                finish();
                break;
        }
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


    /**
     * 发车按钮 全部发车
     */
    public void daoda(String missonNo) {
        //请求开始
        AjaxParams params=new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        params.put("missionNo", missonNo);
        params.put("type", "1");
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
                    Intent intent = new Intent(TaskExpressInfoActivity.this, TaskExpressInfoActivity.class);
                    intent.putExtra("mission_no", missionNo);
                    if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                    {
                        intent.putExtra("type", "提货发车");
                    }
                    else
                    {
                        intent.putExtra("type", "Send Out");
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

}
