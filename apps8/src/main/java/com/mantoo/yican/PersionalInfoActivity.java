package com.mantoo.yican;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.model.Vehicle;
import com.mantoo.yican.util.ValidateUtil;
import com.mantoo.yican.s8.R;
import com.mantoo.yican.widget.CustomDialog;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/10/12.
 */

public class
PersionalInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView personalName, personalVehicleNo, personalUsername;
    private TextView personalVehicleType, personalIncome, personalAccount;
    private TextView personalMissionNumber, personalUnfinishedNumber;
    private TextView personalModifyPassword, personalVersion;
    private RelativeLayout personalSysSet;


    private Configuration config;
    private Resources resources;

    private Button personalExit;

    private ImageView settingImg, personalJiedan, personalDriverPic;

    private LinearLayout personalTopInfo;
    private String isWork;//上班状态

    private String vehicleNo; //车牌号
    private boolean isSelected;//是否选中车牌号
    private List<Vehicle> datas = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_setting);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        findViewById(R.id.per_setting_back).setOnClickListener(this);
        findViewById(R.id.personal_sys_set).setOnClickListener(this);
        findViewById(R.id.suggest_back).setOnClickListener(this);

        personalJiedan = (ImageView) findViewById(R.id.personal_jiedan); //上班接单

        initView();

    }

    private void initView() {
        findViewById(R.id.setting_img).setOnClickListener(this);
        findViewById(R.id.personal_modify_password).setOnClickListener(this); //修改密码
        findViewById(R.id.personal_exit).setOnClickListener(this);
        findViewById(R.id.personal_jiedan).setOnClickListener(this);

        personalTopInfo = (LinearLayout) findViewById(R.id.personal_top_info); //司机名称
        personalName = (TextView) findViewById(R.id.personal_name); //司机名称
        personalVehicleNo = (TextView) findViewById(R.id.personal_vehicleNo); //车牌号
        personalUsername = (TextView) findViewById(R.id.personal_username); //账号（工号）
        personalVehicleType = (TextView) findViewById(R.id.personal_vehicleType); //车型
        personalIncome = (TextView) findViewById(R.id.personal_income); //收入
        personalAccount = (TextView) findViewById(R.id.personal_account); //待结算金额
        personalMissionNumber = (TextView) findViewById(R.id.personal_missionNumber); //本月接单数量
        personalUnfinishedNumber = (TextView) findViewById(R.id.personal_unfinishedNumber); //待完成数量
        personalSysSet = (RelativeLayout) findViewById(R.id.personal_sys_set); //系统设置
        personalVersion = (TextView) findViewById(R.id.personal_version); //当前版本号
        settingImg = (ImageView) findViewById(R.id.setting_img); //个人设置

        personalDriverPic = (ImageView) findViewById(R.id.personal_driver_pic); //司机头像

        // 先隐藏，数据加载后再显示
        personalTopInfo.setVisibility(View.INVISIBLE);

        AjaxParams params=new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        params.put("driverType", AppCache.getString(AppCacheKey.driverType));
        String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.toPersonalCenter;
        System.out.println(params+url);
        showLoadingDialog();
        PDAApplication.http.configTimeout(8000);
        PDAApplication.http.post(url,params, new AjaxCallBack<Object>() {
            public void onSuccess(Object t) {
                hideLoadingDialog();
                JSONObject object;
                try {
                    object = new JSONObject(t.toString());

                    if (object.optInt(Constants.RESULT_CODE) == 0) {
                        JSONObject obj = object.optJSONObject(Constants.DATA);
                        System.out.println("obj"+obj.toString());
                        personalIncome.setText(obj.optString("income")); //收入
                        personalAccount.setText(obj.optString("account")); //待结算金额
                        personalMissionNumber.setText(obj.optString("missionNumber")); //本月接单数量
                        personalUnfinishedNumber.setText(obj.optString("unfinishedNumber")); //待完成数量
                        //司机名称
                        AppCache.putString(AppCacheKey.name,
                                ValidateUtil.isCacheNotEmpty(obj.optString("name")));
                        //司机电话
                        AppCache.putString(AppCacheKey.phone,
                                ValidateUtil.isCacheNotEmpty(obj.optString("phone")));
                        //车牌号
                        AppCache.putString(AppCacheKey.vehicleNo,
                                ValidateUtil.isCacheNotEmpty(obj.optString("vehicleNo")));
                        //车型
                        AppCache.putString(AppCacheKey.vehicleType,
                                ValidateUtil.isCacheNotEmpty(obj.optString("vehicleType")));
                        //车长
                        AppCache.putString(AppCacheKey.length,
                                ValidateUtil.isCacheNotEmpty(obj.optString("length")));
                        //车宽
                        AppCache.putString(AppCacheKey.width,
                                ValidateUtil.isCacheNotEmpty(obj.optString("width")));
                        //车高
                        AppCache.putString(AppCacheKey.height,
                                ValidateUtil.isCacheNotEmpty(obj.optString("height")));
                        //地址
                        AppCache.putString(AppCacheKey.address,
                                ValidateUtil.isCacheNotEmpty(obj.optString("address")));
                        //银行卡号
                        AppCache.putString(AppCacheKey.bankCardNum,
                                ValidateUtil.isCacheNotEmpty(obj.optString("bankCardNum")));
                        //上班状态
                        AppCache.putString(AppCacheKey.isWork,
                                ValidateUtil.isCacheNotEmpty(obj.optString("isWork")));
                        SharedPreferences.Editor editor = sp.edit();
                        editor.commit();

                        isWork = obj.optString("isWork"); //上班状态
                        if(isWork.equals("0"))
                        {
                            personalJiedan.setImageResource(R.mipmap.switch_on);
                        }
                        else
                        {
                            personalJiedan.setImageResource(R.mipmap.switch_off);
                        }

                        personalName.setText(obj.optString("name")); //司机名称
                        personalVehicleNo.setText(obj.optString("vehicleNo")); //车牌号
                        personalUsername.setText(AppCache.getString(AppCacheKey.username)); //账号（工号）
                        personalVehicleType.setText(obj.optString("vehicleType")); //车型
                        personalTopInfo.setVisibility(View.VISIBLE);

                    } else if (object.optInt(Constants.RESULT_CODE) == 1) {
                        if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                        {
                            showStringToastMsg("个人信息有误！");
                        }
                        else
                        {
                            showStringToastMsg("Personal information is incorrect！");
                        }

                    } else {
                        if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                        {
                            showStringToastMsg("个人信息初始化失败");
                        }
                        else
                        {
                            showStringToastMsg("Initialization of personal information failed！");
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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 个人中心
            case R.id.setting_img:
                Intent intent = new Intent(PersionalInfoActivity.this, PersionalSettingActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.suggest_back:
                Intent intent4 = new Intent(PersionalInfoActivity.this, SuggestedFeedbackActivity.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.personal_jiedan: //上班
                if(AppCache.getString(AppCacheKey.isWork).equals("0"))
                {
                    AjaxParams params=new AjaxParams();
                    params.put("driverid", AppCache.getString(AppCacheKey.driverid));
                    params.put("isWork", "1");
                    params.put("vehicleNo", AppCache.getString(AppCacheKey.vehicleNo));
                    String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.MODIFYWORKSTATUS;
                    System.out.println(params+url);
                    PDAApplication.http.configTimeout(8000);
                    PDAApplication.http.post(url,params,new AjaxCallBack<Object>() {
                        public void onSuccess(Object t) {
                            hideLoadingDialog();
                            JSONObject object;
                            try {
                                object = new JSONObject(t.toString());
                                if (object.optInt(Constants.RESULT_CODE) == 0) {
                                    AppCache.putString(AppCacheKey.isWork,ValidateUtil.isCacheNotEmpty("1"));
                                    personalJiedan.setImageResource(R.mipmap.switch_off);
                                } else {
                                    if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                    {
                                        showStringToastMsg("修改失败！");
                                    }
                                    else
                                    {
                                        showStringToastMsg("fail to edit！");
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
                else
                {
                    // 获取车牌号
                    AjaxParams params1=new AjaxParams();
                    params1.put("driverid", AppCache.getString(AppCacheKey.driverid));
                    String url1 = HTTP_URL+MainUrl.GETVEHICLENO;
                    PDAApplication.http.configTimeout(8000);
                    PDAApplication.http.post(url1,params1,new AjaxCallBack<Object>() {
                        @Override
                        public void onSuccess(Object t) {
                            super.onSuccess(t);
                            JSONObject object;
                            try {
                                object = new JSONObject(t.toString());
                                if (object.optInt(Constants.RESULT_CODE) == 0) {
                                    JSONObject ob=object.getJSONObject("data");
                                    vehicleNo = ob.getString("vehicleNos");
                                    vehicleNo = vehicleNo.replace("[", "");
                                    vehicleNo = vehicleNo.replace("]", "");
                                    vehicleNo = vehicleNo.replace("\"", "");
                                    String[] arr = vehicleNo.split(",");
                                    for(String s : arr)
                                    {
                                        datas.add(new Vehicle(s));
                                    }
                                    final CustomDialog.Builder builder = new CustomDialog.Builder(PersionalInfoActivity.this);
                                    builder.setTitle("车牌号");
                                    builder.setMessage(datas);
                                    builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            datas.clear();
                                            vehicleNo = null;
                                            dialog.dismiss();
                                        }
                                    });

                                    builder.setNegativeButton("上班",
                                            new android.content.DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    AjaxParams params=new AjaxParams();
                                                    params.put("driverid", AppCache.getString(AppCacheKey.driverid));
                                                    params.put("vehicleNo", builder.getSelectV());
                                                    params.put("isWork", "0");
                                                    String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.MODIFYWORKSTATUS;
                                                    System.out.println(params+url);
                                                    showLoadingDialog();
                                                    PDAApplication.http.configTimeout(8000);
                                                    PDAApplication.http.post(url,params,new AjaxCallBack<Object>() {
                                                        public void onSuccess(Object t) {
                                                            hideLoadingDialog();
                                                            AppCache.putString(AppCacheKey.vehicleNo,builder.getSelectV());
                                                            Intent intent2 = new Intent(PersionalInfoActivity.this, PersionalInfoActivity.class);
                                                            startActivity(intent2);
                                                            finish();
                                                        };
                                                        public void onFailure(Throwable t, String strMsg) {
                                                            hideLoadingDialog();
                                                            showToastMsg(R.string.error_network);
                                                        };
                                                    } );
                                                    datas.clear();
                                                    vehicleNo = null;
                                                    //请求结束
                                                    dialog.dismiss();
                                                }
                                            });
                                    builder.create().show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
                break;
            case R.id.personal_modify_password:
                Intent intent1 = new Intent(PersionalInfoActivity.this, PasswordModifyActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.personal_exit:
                Intent intent2 = new Intent(PersionalInfoActivity.this, LoginActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.per_setting_back:
                Intent intent3 = new Intent(PersionalInfoActivity.this, MainActivity.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.personal_sys_set:
                Intent intent7 = new Intent(PersionalInfoActivity.this, LanguageActivity.class);
                startActivity(intent7);
                finish();
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent3 = new Intent(PersionalInfoActivity.this, MainActivity.class);
            startActivity(intent3);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

}
