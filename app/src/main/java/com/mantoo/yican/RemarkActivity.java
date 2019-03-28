package com.mantoo.yican;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;


public class RemarkActivity extends BaseActivity  implements View.OnClickListener{

    private Configuration config;
    private Resources resources;

    private EditText remark_content;
    private String missonNo;
    private String waybillNos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象

        findViewById(R.id.remark_content_return).setOnClickListener(this);
        findViewById(R.id.remark_commit).setOnClickListener(this);
        remark_content = (EditText)findViewById(R.id.remark_content);

        Intent intent = getIntent();
        missonNo = intent.getStringExtra("missonNo");
        waybillNos = intent.getStringExtra("waybillNos");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remark_content_return:
                finish();
                break;
            case R.id.remark_commit:
                String content = remark_content.getText().toString();
                if(waybillNos != null && !waybillNos.equals(""))
                {
                    tiHuoAll(content);
                }
                else
                {
                    tiHuo(content);
                }
                break;
        }
    }

    private void tiHuo(String content)
    {
        //请求开始
        AjaxParams params=new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        params.put("missionNo", missonNo);
        params.put("waybillNo", "-1");
        params.put("note",content);
        String url = MainUrl.URL + MainUrl.TASKSHEETDEPARTURE;
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
                    Intent intent = new Intent(RemarkActivity.this, TaskActivity.class);
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

    private void tiHuoAll(String content)
    {
        //请求开始
        AjaxParams params=new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        params.put("missionNo", missonNo);
        params.put("note",content);
        params.put("waybillNo", waybillNos);
        String url = MainUrl.URL + MainUrl.TASKSHEETDEPARTURE;
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
                        Intent intent = new Intent(RemarkActivity.this, TaskActivity.class);
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
        //请求结束
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

}
