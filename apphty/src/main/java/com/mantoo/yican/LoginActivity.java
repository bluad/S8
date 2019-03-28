package com.mantoo.yican;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import com.mantoo.yican.cn.hty.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import com.hit.library.http.AppException;
import com.hit.library.http.Request;
import com.hit.library.http.callback.Callback;
import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.service.LocationService;
import com.mantoo.yican.util.FileInfo;
import com.mantoo.yican.util.ValidateUtil;
import com.mantoo.yican.widget.AbstractSpinerAdapter.IOnItemSelectListener;
import com.mantoo.yican.widget.SpinerPopWindow;

/**
 * Created by Administrator on 2017/10/12.
 */

public class LoginActivity extends BaseActivity implements OnClickListener,
        IOnItemSelectListener {

    private Configuration config;
    private Resources resources;

    // 输入账号
    private EditText userEdit;
    // 输入密码
    private EditText passEdit;
    private TextView changeView;
    private Button submitView;
    private ArrayList<String> lists;
    // 站点选择器
    private SpinerPopWindow mSpinerPopWindow;
    private ArrayList<String> nameList = new ArrayList<String>();
    private String[] names;
    // 当前何种登录
    private int pos = 0;

    //车牌号
    private String vehicleNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        startService(new Intent(this, LocationService.class));
        initView(this);
        initData(this);
        getCode();

    }
    protected void initView(Context context) {
        userEdit = (EditText) findViewById(R.id.user_id);
        passEdit = (EditText) findViewById(R.id.password);
        submitView = (Button) findViewById(R.id.submit);
        submitView.setOnClickListener(this);
        changeView = (TextView) findViewById(R.id.spiner);
        changeView.setOnClickListener(this);
        mSpinerPopWindow = new SpinerPopWindow(this);
        names = getResources().getStringArray(R.array.site);
        for (int i = 0; i < names.length; i++) {
            nameList.add(names[i]);
        }
        mSpinerPopWindow.refreshData(nameList, 0);
        mSpinerPopWindow.setItemListener(this);
    }

    protected void initData(Context context) {
        changeView.setText(nameList.get(0));
        lists = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            lists.add(i + "");
        }

        String userText = AppCache.getString(AppCacheKey.key_username, "");
        String pwdText = AppCache.getString(AppCacheKey.key_password, "");
        userEdit.setText(userText);
        passEdit.setText(pwdText);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (ValidateUtil.isShouldHideInput(v, ev)) {
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                        0);
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (userEdit.getText().toString().trim().equals("")) {
                    if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                    {
                        showStringToastMsg("账号不能为空");
                    }
                    else
                    {
                        showStringToastMsg("Account can not be empty");
                    }
                    return;
                }
                if (passEdit.getText().toString().trim().equals("")) {
                    if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                    {
                        showStringToastMsg("密码不能为空");
                    }
                    else
                    {
                        showStringToastMsg("Password can not be blank");
                    }
                    return;
                }

                String userText = userEdit.getText().toString().trim();
                String pwdText = passEdit.getText().toString().trim();

                submit(userText, pwdText);
                break;
            case R.id.spiner:
                mSpinerPopWindow.setWidth(changeView.getWidth());
                mSpinerPopWindow.showAsDropDown(changeView);
                break;
        }

    }

    /**
     * 请求登录
     * @param userText
     * @param pwdText
     */
    private void submit(final String userText, final String pwdText) {
        showLoadingDialog();
        AjaxParams params=new AjaxParams();
        params.put("username", userText);
        params.put("password", pwdText);
        if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
        {
            params.put("language", "cn");
        }
        else
        {
            params.put("language", "en");
        }
        String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.LOGIN;
        System.out.println(params+url);
        PDAApplication.http.configTimeout(8000);
        PDAApplication.http.post(url,params,new AjaxCallBack<Object>() {
            public void onSuccess(Object t) {
                hideLoadingDialog();
                JSONObject object;
                try {
                    object = new JSONObject(t.toString());

                    if (object.optInt(Constants.RESULT_CODE) == 0) {
                        JSONObject obj = object.optJSONObject(Constants.DATA);
                        AppCache.putString(AppCacheKey.key_username, userText);
                        AppCache.putString(AppCacheKey.key_password, pwdText);
                        JPushInterface.setAlias(LoginActivity.this, AppCache.getString(AppCacheKey.registerId)+obj.optString("driverid"), new TagAliasCallback() {
                                    public void gotResult(int arg0, String arg1, Set<String> arg2) {
                                        System.out.print(arg0);
                                        System.out.print(arg1);
                                    }
                         });
                        //司机id
                        AppCache.putString(AppCacheKey.driverid,
                                ValidateUtil.isCacheNotEmpty(obj.optString("driverid")));
                        //账号（工号）
                        AppCache.putString(AppCacheKey.username,
                                ValidateUtil.isCacheNotEmpty(obj.optString("username")));
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
                        //司机类型
                        AppCache.putString(AppCacheKey.driverType,
                                ValidateUtil.isCacheNotEmpty(obj.optString("driverType")));
                        //上班状态
                        AppCache.putString(AppCacheKey.isWork,
                                ValidateUtil.isCacheNotEmpty(obj.optString("isWork")));

                        Editor editor = sp.edit();
                        editor.commit();
                        if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                        {
                            showStringToastMsg("登录成功！");
                        }
                        else
                        {
                            showStringToastMsg("Login Success！");
                        }
                        intentToMain();
                    } else if (object.optInt(Constants.RESULT_CODE) == 1) {
                        if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                        {
                            showStringToastMsg("用户名或密码不正确！");
                        }
                        else
                        {
                            showStringToastMsg("User name or password is incorrect!");
                        }
                    } else {
                        if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                        {
                            showStringToastMsg("登录失败");
                        }
                        else
                        {
                            showStringToastMsg("Login Failed!");
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

    private void intentToMain() {
        // 获取车牌号
        AjaxParams params=new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        String url = HTTP_URL+MainUrl.GETVEHICLENO;
        PDAApplication.http.configTimeout(8000);
        PDAApplication.http.post(url,params,new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                JSONObject object;
                try {
                    object = new JSONObject(t.toString());
                    if (object.optInt(Constants.RESULT_CODE) == 0) {
                        JSONObject ob=object.getJSONObject("data");
                        vehicleNo = ob.getString("vehicleNos");
                        Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);
                        mIntent.putExtra("vehicleNo", vehicleNo);
                        startActivity(mIntent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onItemClick(int pos) {
        this.pos = pos;
        changeView.setText(nameList.get(pos));
        if (!userEdit.getText().toString().trim().equals("")
                && !passEdit.getText().toString().trim().equals("")) {
        }
    }
    private void getCode() {
        AjaxParams params=new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        String url = HTTP_URL+MainUrl.getUsersInfo;
        PDAApplication.http.configTimeout(8000);
        PDAApplication.http.post(url,params,new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                JSONObject object;
                try {
                    object = new JSONObject(t.toString());
                    if (object.optInt(Constants.RESULT_CODE) == 0) {
                        final JSONObject ob=object.getJSONObject("data");

                        AppCache.putString("erweima", ob.getString("erweima"));
                        AppCache.putString("encryption", ob.getString("encryption"));
                        new Thread()
                        {
                            public void run()
                            {
                                URL url = null;
                                HttpURLConnection con = null;
                                try {
                                    url = new URL(ob.getString("printlogo"));
                                    con = (HttpURLConnection) url.openConnection();
                                    con.setRequestMethod("GET");
                                    con.setReadTimeout(5000);
                                    con.setDoInput(true);
                                    File parent = Environment.getExternalStorageDirectory();
                                    File file = new File(parent,"logo.png");
                                    FileOutputStream fos = new FileOutputStream(file);
                                    InputStream in = con.getInputStream();
                                    byte ch[] = new byte[2 * 1024];
                                    int len;
                                    if (fos != null){
                                        while ((len = in.read(ch)) != -1){
                                            fos.write(ch,0,len);
                                        }
                                        in.close();
                                        fos.close();
                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });}
    @Override
    protected void onResume() {
        super.onResume();

    }
    private long exitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exitApp();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    protected void exitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            exitTime = System.currentTimeMillis();
        } else {
            PDAApplication.getInstance().exit();
        }
    }

}
