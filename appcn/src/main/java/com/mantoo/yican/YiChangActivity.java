package com.mantoo.yican;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.mantoo.yican.cn.R;
import com.mantoo.yican.adapter.YiChangAdapter;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/10/14.
 */

public class YiChangActivity extends BaseActivity implements View.OnClickListener {

    List<TaskInfo> dataList = new ArrayList<TaskInfo>();
    ListView yiChangListView;
    private Configuration config;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yichang_yundan);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        initView();
    }

    private void initView() {
        findViewById(R.id.yichang_button).setOnClickListener(this);
        findViewById(R.id.yichang_return).setOnClickListener(this);

        yiChangListView = (ListView)findViewById(R.id.yichang_yundan_list);

        //请求开始
        AjaxParams params=new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.LISTOFABNORMALWAYBILL;
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
                        JSONArray array = data.getJSONArray("waybillProblemArr");
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
                                taskInfo.setReceviceAddress(tablelist.optString("receiveAddress"));
                                taskInfo.setVolumn(tablelist.optString("volumn")+"m³");
                                taskInfo.setWeight(tablelist.optString("weight")+"KG");
                                taskInfo.setDaishouAmount(tablelist.optString("daishouAmount") + "元");
                                if(!tablelist.isNull("sendDate"))
                                {
                                    taskInfo.setSendDate(dateFormat.format(new Date(Long.parseLong(tablelist.optString("sendDate")))));
                                }
                                String isHandle = (tablelist.optString("ishandled").equals("0") ? "已处理" : "未处理");
                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                {
                                    if(isHandle.equals("已处理"))
                                    {
                                        taskInfo.setIsHandle("已处理");
                                    }
                                    else
                                    {
                                        taskInfo.setIsHandle("未处理");
                                    }
                                }
                                else
                                {
                                    if(isHandle.equals("已处理"))
                                    {
                                        taskInfo.setIsHandle("Handled");
                                    }
                                    else
                                    {
                                        taskInfo.setIsHandle("unHandle");
                                    }
                                }

                                dataList.add(taskInfo);
                            }
                            YiChangAdapter adapter = new YiChangAdapter(YiChangActivity.this, R.layout.item_listview_yichang, dataList);
                            yiChangListView.setAdapter(adapter);
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
                            showStringToastMsg("No Data!");
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
        yiChangListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskInfo task = dataList.get(position);
                Intent intent = new Intent(YiChangActivity.this, YiChangWayBillInfoActivity.class);
                intent.putExtra("waybillNo", task.getWaybillNo());
                intent.putExtra("activityType", "YiChangActivity");
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 提报异常
            case R.id.yichang_button:
                Intent intent = new Intent(YiChangActivity.this, YiChangSubmitActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.yichang_return:
                Intent intent1 = new Intent(YiChangActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
                break;

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent1 = new Intent(YiChangActivity.this, MainActivity.class);
            startActivity(intent1);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }
}
