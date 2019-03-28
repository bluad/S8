package com.mantoo.yican;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mantoo.yican.adapter.ExpressPaiDanAdapter;
import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.model.ExpressStatus;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/10/12.
 */

public class PaidanTaskRefuseActivity extends BaseActivity implements View.OnClickListener {

    private EditText refuseContent;
    private Configuration config;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paidan_task_refuse);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        findViewById(R.id.paidan_task_refuse_submit).setOnClickListener(this);
        findViewById(R.id.paidan_task_refuse_return).setOnClickListener(this);
        refuseContent = (EditText)findViewById(R.id.paidan_task_refuse_content);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 提交
            case R.id.paidan_task_refuse_submit:
                String contents = refuseContent.getText().toString();
                if(contents == null || contents.equals(""))
                {
                    if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                    {
                        Toast.makeText(PaidanTaskRefuseActivity.this, "原因不能为空！", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(PaidanTaskRefuseActivity.this, "The reason can not be empty！", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                Intent intent = getIntent();
                String missionNo = intent.getStringExtra("mission_no");
                //请求开始
                AjaxParams params=new AjaxParams();
                params.put("missionNo", missionNo);
                params.put("reason", contents);
                params.put("driverid", AppCache.getString(AppCacheKey.driverid));
                String url = MainUrl.URL + MainUrl.SENDASINGLEREJECT;
                showLoadingDialog();
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
                                    showStringToastMsg("拒单成功！");
                                }
                                else
                                {
                                    showStringToastMsg("Refused to single success！");
                                }
                                Intent intent = new Intent(PaidanTaskRefuseActivity.this, PaiDanActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                {
                                    showStringToastMsg("拒单失败！");
                                }
                                else
                                {
                                    showStringToastMsg("Refused to single Failed！");
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
            case R.id.paidan_task_refuse_return:
                Intent intent1 = new Intent(PaidanTaskRefuseActivity.this, PaiDanActivity.class);
                startActivity(intent1);
                finish();
                break;

        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent1 = new Intent(PaidanTaskRefuseActivity.this, PaiDanActivity.class);
            startActivity(intent1);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }
}
