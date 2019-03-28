package com.mantoo.yican;

import org.json.JSONException;
import org.json.JSONObject;

import com.hit.library.http.RequestManager;
import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.model.AddresseeData;
import com.mantoo.yican.util.JsonParser;
import com.mantoo.yican.widget.AlertDialog;
import com.mantoo.yican.widget.QQDialog;
import com.mantoo.yican.widget.QQDialog.Button1Listener;
import com.mantoo.yican.widget.QQDialog.Button2Listener;
import com.mantoo.yican.ExpressinfoDetailsActivity;
import com.mantoo.yican.cn.hty.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.Locale;


/**
 * Created by Administrator on 2017/10/12.
 */

public class BaseActivity extends BaseSetUpActivity implements OnClickListener {


    private Configuration config;
    private Resources resources;

    // private LoadingDialog loadingDialog = null;
    protected RequestManager requestManager;
    protected InputMethodManager inputMethodManager;
    private long exitTime;
    public boolean isExist = true;
    private ProgressDialog progressDialg = null;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        if(!getRunningActivityName().equals("LoginActivity")){

            PDAApplication.getInstance().addActivity(this);
        }
        requestManager = RequestManager.getInstance();
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }
    private String getRunningActivityName() {
        String contextString = this.toString();
        return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
    }
    public void showToastMsg(int stringId) {
        Toast.makeText(this, getResources().getString(stringId),
                Toast.LENGTH_LONG).show();
    }

    public void showStringToastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void exitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
            {
                showStringToastMsg("再按一次退出应用！");
            }
            else
            {
                showStringToastMsg("Press again to exit the app");
            }
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    /**
     * 显示加载Dialog
     */
    protected void showLoadingDialog() {
        // if (loadingDialog == null) {
        // loadingDialog = LoadingDialog.getInstance(this);
        // } else if (!loadingDialog.isShowing()) {
        // loadingDialog.show();
        // }

        if (progressDialg == null) {
            progressDialg = new ProgressDialog(this);
            progressDialg.setCancelable(false);
            if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
            {
                progressDialg.setMessage("加载中...");
            }
            else
            {
                progressDialg.setMessage("Loading...");
            }
        }
        progressDialg.show();

        // SVProgressHUD.showWithStatus(this, "加载中...");
        // if (!SVProgressHUD.isShowing(this)) {
        // SVProgressHUD.show(this);
        // }
    }

    /**
     * 隐藏加载Dialog
     */
    protected void hideLoadingDialog() {
        // if (loadingDialog != null) {
        // loadingDialog.dismiss();
        // }
        if (progressDialg != null) {
            progressDialg.dismiss();
        }
        // if (SVProgressHUD.isShowing(this)) {
        // SVProgressHUD.dismiss(this);
        // }
    }

    protected QQDialog qqdialog;

    protected void showQQdialog(String text1, String text2,
                                Button1Listener listener1, Button2Listener listener2) {
        if (qqdialog == null) {
            qqdialog = new QQDialog(this, text1, text2, listener1, listener2);
            qqdialog.show();
        } else if (!qqdialog.isShowing()) {
            qqdialog.show();
        }
    }

    protected AlertDialog iosDialog;

    protected void showIOSdialog(String title, String content, String right,
                                 OnClickListener rightListener, String left,
                                 OnClickListener leftListener) {
        if (iosDialog == null) {
            iosDialog = new AlertDialog(this).builder().setTitle(title)
                    .setMsg(content).setPositiveButton(right, rightListener)
                    .setNegativeButton(left, leftListener);
            iosDialog.setCancelable(false);
        }
        iosDialog.show();
    }

    @Override
    public void showNext() {
        // TODO Auto-generated method stub

    }

    @Override
    public void showPre() {
        // TODO Auto-generated method stub

    }


    @Override
    protected void onResume() {
        isExist = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isExist = false;
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    // 有单号，查询单号
    public void query(final String number) {

        AjaxParams params = new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        params.put("express", number);
        PDAApplication.http.post(HTTP_URL + MainUrl.GETMSG, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object t) {
                // TODO Auto-generated method stub
                super.onSuccess(t);
                System.out.println("单号信息" + t.toString());
                try {
                    JSONObject object = new JSONObject(t.toString());
                    if(object.getInt("result_code")==0){
                        JSONObject ob = object.getJSONObject("data");
                        AddresseeData addData = JsonParser.deserializeFromJson(ob.toString(),
                                AddresseeData.class);
                        Intent intent= new Intent(BaseActivity.this, ExpressinfoDetailsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("addData", addData);
                        intent.putExtra("express", number);
                        intent.putExtra("flag", "0");
                        startActivity(intent);
                    }else{
                        showStringToastMsg(object.getString("message"));
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Throwable t, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, strMsg);
            }
        });
    }


}
