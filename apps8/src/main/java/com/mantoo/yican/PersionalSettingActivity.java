package com.mantoo.yican;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.util.ValidateUtil;
import com.mantoo.yican.s8.R;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by Administrator on 2017/10/14.
 */

public class PersionalSettingActivity extends BaseActivity implements View.OnClickListener {

    private EditText persionEditName, persionEditUsername, persionEditVehicleNo;
    private EditText persionEditVehicleType, persionEditPhone, persionEditLength;
    private EditText persionEditWidth, persionEditHeight, persionEditAddress;
    private EditText persionEditBankCardNum;
    private Button buttonPersionModify;

    private Configuration config;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysel_info);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        initView();

    }

    private void initView() {
        findViewById(R.id.setting_back).setOnClickListener(this);
        findViewById(R.id.button_persion_modify).setOnClickListener(this);


        persionEditName = (EditText)findViewById(R.id.persion_edit_name);
        persionEditUsername = (EditText)findViewById(R.id.persion_edit_username);
        persionEditVehicleNo = (EditText)findViewById(R.id.persion_edit_vehicleNo);
        persionEditVehicleType = (EditText)findViewById(R.id.persion_edit_vehicleType);
        persionEditPhone = (EditText)findViewById(R.id.persion_edit_phone);
        persionEditLength = (EditText)findViewById(R.id.persion_edit_length);
        persionEditWidth = (EditText)findViewById(R.id.persion_edit_width);
        persionEditHeight = (EditText)findViewById(R.id.persion_edit_height);
        persionEditAddress = (EditText)findViewById(R.id.persion_edit_address);
        persionEditBankCardNum = (EditText)findViewById(R.id.persion_edit_bankCardNum);

        persionEditName.setText(AppCache.getString(AppCacheKey.name));
        persionEditUsername.setText(AppCache.getString(AppCacheKey.username));
        persionEditVehicleNo.setText(AppCache.getString(AppCacheKey.vehicleNo));
        persionEditVehicleType.setText(AppCache.getString(AppCacheKey.vehicleType));
        persionEditPhone.setText(AppCache.getString(AppCacheKey.phone));
        persionEditLength.setText(AppCache.getString(AppCacheKey.length));
        persionEditWidth.setText(AppCache.getString(AppCacheKey.width));
        persionEditHeight.setText(AppCache.getString(AppCacheKey.height));
        persionEditAddress.setText(AppCache.getString(AppCacheKey.address));
        persionEditBankCardNum.setText(AppCache.getString(AppCacheKey.bankCardNum));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 个人中心
            case R.id.setting_back:
                Intent intent = new Intent(PersionalSettingActivity.this, PersionalInfoActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.button_persion_modify:
                AjaxParams params=new AjaxParams();
                params.put("driverid", AppCache.getString(AppCacheKey.driverid));
                params.put("name", persionEditName.getText().toString());
                //params.put("vehicleNo", persionEditVehicleNo.getText().toString());
                //params.put("vehicleType", persionEditVehicleType.getText().toString());
                params.put("phone", persionEditPhone.getText().toString());
                //params.put("length", persionEditLength.getText().toString());
                //params.put("width", persionEditWidth.getText().toString());
                //params.put("height", persionEditHeight.getText().toString());
                params.put("address", persionEditAddress.getText().toString());
                params.put("bankCardNum", persionEditBankCardNum.getText().toString());
                String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.MODIFYACCOUNTINFORMATION;
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
                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                {
                                    showStringToastMsg("保存成功！");
                                }
                                else
                                {
                                    showStringToastMsg("Save Success！");
                                }
                                intentToBack();
                            } else if (object.optInt(Constants.RESULT_CODE) == 1) {
                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                {
                                    showStringToastMsg("保存失败！");
                                }
                                else
                                {
                                    showStringToastMsg("Save Failed！");
                                }
                            } else {
                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                {
                                    showStringToastMsg("保存失败！");
                                }
                                else
                                {
                                    showStringToastMsg("Save Failed！");
                                }
                                System.out.println(object.optString(Constants.MESSAGE));
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
                break;

        }

    }

    private void intentToBack() {
        Intent mIntent = new Intent(this, PersionalInfoActivity.class);
        startActivity(mIntent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(PersionalSettingActivity.this, PersionalInfoActivity.class);
            startActivity(intent);
            this.finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

}
