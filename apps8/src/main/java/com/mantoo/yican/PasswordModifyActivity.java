package com.mantoo.yican;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.mantoo.yican.s8.R;
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

/**
 * 密码修改
 * Created by Administrator on 2017/11/4.
 */

public class PasswordModifyActivity extends BaseActivity implements View.OnClickListener {

    private EditText modifyOldPassword, modifyNewPassword, modifyNewPassword1;
    private Configuration config;
    private Resources resources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_modify);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        initView();

    }

    private void initView() {
        findViewById(R.id.password_modify_return).setOnClickListener(this);
        findViewById(R.id.modify_password_button).setOnClickListener(this); //修改密码

        modifyOldPassword = (EditText) findViewById(R.id.modify_old_password);
        modifyNewPassword = (EditText) findViewById(R.id.modify_new_password);
        modifyNewPassword1 = (EditText) findViewById(R.id.modify_new_password1);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.password_modify_return:
                Intent intent = new Intent(PasswordModifyActivity.this, PersionalInfoActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.modify_password_button:
                String oldPassword = modifyOldPassword.getText().toString().trim();
                String newPassword = modifyNewPassword.getText().toString().trim();
                String newPassword1 = modifyNewPassword1.getText().toString().trim();

                if(oldPassword == null || oldPassword.equals(""))
                {
                    if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                    {
                        showStringToastMsg("请填写旧密码！");
                    }
                    else
                    {
                        showStringToastMsg("Please fill in the old password！");
                    }
                    return;
                }
                if(newPassword == null || newPassword.equals(""))
                {
                    if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                    {
                        showStringToastMsg("请填写新密码！");
                    }
                    else
                    {
                        showStringToastMsg("Please fill in the new password！");
                    }
                    return;
                }
                if(newPassword1 == null || newPassword1.equals(""))
                {
                    if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                    {
                        showStringToastMsg("请填写新密码！");
                    }
                    else
                    {
                        showStringToastMsg("Please fill in the new password！");
                    }
                    return;
                }

                if(!oldPassword.equals(AppCache.getString(AppCacheKey.key_password)))
                {
                    if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                    {
                        showStringToastMsg("旧密码不正确！");
                    }
                    else
                    {
                        showStringToastMsg("The old password is incorrect！");
                    }
                    return;
                }

                if(!newPassword.equals(newPassword1))
                {
                    if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                    {
                        showStringToastMsg("两次输入的新密码不一样！");
                    }
                    else
                    {
                        showStringToastMsg("The new password entered twice is not the same！");
                    }

                    return;
                }


                AjaxParams params=new AjaxParams();
                params.put("driverid", AppCache.getString(AppCacheKey.driverid));
                params.put("username", AppCache.getString(AppCacheKey.key_username));
                params.put("oldpassword", oldPassword);
                params.put("newpassword", newPassword);
                String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.MODIFYPASSWORD;
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
                                    showStringToastMsg("密码修改成功！");
                                }
                                else
                                {
                                    showStringToastMsg("Password reset complete！");
                                }
                                Intent intent1 = new Intent(PasswordModifyActivity.this, PersionalInfoActivity.class);
                                startActivity(intent1);
                                finish();
                            } else {
                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                {
                                    showStringToastMsg("密码修改失败！");
                                }
                                else
                                {
                                    showStringToastMsg("Password change failed！");
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
                break;

        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(PasswordModifyActivity.this, PersionalInfoActivity.class);
            startActivity(intent);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }


}
