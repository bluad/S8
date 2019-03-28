package com.mantoo.yican;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.mantoo.yican.cn.hty.R;
import com.mantoo.yican.adapter.TaskInfoAdapter;
import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.model.TaskInfo;
import com.mantoo.yican.util.ValidateUtil;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class LanguageActivity extends BaseActivity  implements View.OnClickListener{

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;

    private CheckBox Chinese,English;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        findViewById(R.id.language_setting_return).setOnClickListener(this);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();

        Chinese = (CheckBox) findViewById(R.id.Chinese);
        English = (CheckBox) findViewById(R.id.English);

        if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
        {
            Chinese.setChecked(true);
            English.setChecked(false);
        }
        else
        {
            Chinese.setChecked(false);
            English.setChecked(true);
        }

        Chinese.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    config.locale = Locale.SIMPLIFIED_CHINESE;
                    resources.updateConfiguration(config, dm);

                    //司机id
                    AppCache.putString(AppCacheKey.language,ValidateUtil.isCacheNotEmpty("cn"));
                    //请求开始
                    AjaxParams params=new AjaxParams();
                    params.put("driverid", AppCache.getString(AppCacheKey.driverid));
                    params.put("language", "cn");
                    String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.LANGUAGE;
                    System.out.println(params+url);
                    PDAApplication.http.configTimeout(8000);
                    PDAApplication.http.post(url,params,new AjaxCallBack<Object>() {
                        public void onSuccess(Object t) {
                            hideLoadingDialog();
                        };
                        public void onFailure(Throwable t, String strMsg) {
                            hideLoadingDialog();
                            showToastMsg(R.string.error_network);
                        };
                    } );
                }

                Intent intent = new Intent(LanguageActivity.this, LanguageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        English.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    config.locale = Locale.US;
                    resources.updateConfiguration(config, dm);
                    AppCache.putString(AppCacheKey.language,ValidateUtil.isCacheNotEmpty("en"));
                    //请求开始
                    AjaxParams params=new AjaxParams();
                    params.put("language", "en");
                    params.put("driverid", AppCache.getString(AppCacheKey.driverid));
                    String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.LANGUAGE;
                    System.out.println(params+url);
                    PDAApplication.http.configTimeout(8000);
                    PDAApplication.http.post(url,params,new AjaxCallBack<Object>() {
                        public void onSuccess(Object t) {
                            hideLoadingDialog();
                        };
                        public void onFailure(Throwable t, String strMsg) {
                            hideLoadingDialog();
                            showToastMsg(R.string.error_network);
                        };
                    } );
                }

                Intent intent = new Intent(LanguageActivity.this, LanguageActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.language_setting_return:
                Intent intent = new Intent(LanguageActivity.this, PersionalInfoActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent2 = new Intent(LanguageActivity.this, PersionalInfoActivity.class);
            startActivity(intent2);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

}
