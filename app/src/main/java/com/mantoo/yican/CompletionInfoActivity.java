package com.mantoo.yican;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mantoo.yican.adapter.CompletionInfoAdapter;
import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.model.TaskInfo;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 完成（任务详情）
 * Created by Administrator on 2017/10/14.
 */

public class CompletionInfoActivity extends BaseActivity implements View.OnClickListener,CompletionInfoAdapter.Callback{

    List<TaskInfo> dataList = new ArrayList<TaskInfo>();
    private Configuration config;
    private Resources resources;
    ListView completionListView;
    String missionNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completion_info);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        initView();
    }

    private void initView() {
        findViewById(R.id.completion_express_info_return).setOnClickListener(this);
        completionListView = (ListView)findViewById(R.id.completion_express_info_list);

        Intent intent = getIntent();
        missionNo = intent.getStringExtra("mission_no");
        //请求开始
        AjaxParams params=new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        params.put("missionNo", missionNo);
        String url = MainUrl.URL + MainUrl.TASKLIST;
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
                                taskInfo.setWaybillStatus(tablelist.optString("waybillStatus"));
                                taskInfo.setStartLat(tablelist.optString("startLat"));
                                taskInfo.setStartLng(tablelist.optString("startLng"));
                                taskInfo.setEndLat(tablelist.optString("endLat"));
                                taskInfo.setEndLng(tablelist.optString("endLng"));
                                dataList.add(taskInfo);
                            }
                            CompletionInfoAdapter adapter = new CompletionInfoAdapter(CompletionInfoActivity.this, R.layout.item_listview_completion_info, dataList, CompletionInfoActivity.this);
                            completionListView.setAdapter(adapter);
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
        completionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskInfo task = dataList.get(position);
                Intent intent = new Intent(CompletionInfoActivity.this, WayBillInfoActivity.class);
                intent.putExtra("waybillNo", task.getWaybillNo());
                intent.putExtra("missionNo", missionNo);
                startActivity(intent);
            }
        });
    }


    // 地图跳转
    @Override
    public void click(View v) {
        TaskInfo task = dataList.get((Integer) v.getTag());
        Intent intent = new Intent(CompletionInfoActivity.this, BaiduMapActivity.class);
        intent.putExtra("activity", "CompletionInfoActivity");
        intent.putExtra("mission_no", missionNo);
        intent.putExtra("startLng", task.getStartLng());
        intent.putExtra("startLat", task.getStartLat());
        intent.putExtra("endLng", task.getEndLng());
        intent.putExtra("endLat", task.getEndLat());
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.completion_express_info_return:
                Intent intent = new Intent(CompletionInfoActivity.this, CompletionActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(CompletionInfoActivity.this, CompletionActivity.class);
            startActivity(intent);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

}
