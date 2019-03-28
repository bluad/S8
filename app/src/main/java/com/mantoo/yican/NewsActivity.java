package com.mantoo.yican;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.mantoo.yican.adapter.NewsAdapter;
import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.model.News;

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
 * Created by Administrator on 2017/10/12.
 */

public class NewsActivity extends BaseActivity implements View.OnClickListener {

    private Configuration config;
    private Resources resources;

    List<News> dataList = new ArrayList<News>();
    ListView newsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        initView();

    }

    private void initView() {
        findViewById(R.id.messages_return).setOnClickListener(this);
        newsAdapter = (ListView) findViewById(R.id.fache_express_list);

        //请求开始
        AjaxParams params = new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        String url = MainUrl.URL + MainUrl.GETALISTOFMESSAGES;
        System.out.println(params + url);
        PDAApplication.http.configTimeout(8000);
        showLoadingDialog();
        PDAApplication.http.post(url, params, new AjaxCallBack<Object>() {
            public void onSuccess(Object t) {
                hideLoadingDialog();
                JSONObject object;
                try {
                    object = new JSONObject(t.toString());
                    if (object.optInt(Constants.RESULT_CODE) == 0) {
                        JSONObject data = object.optJSONObject(Constants.DATA);
                        JSONArray array = data.getJSONArray("newsArr");
                        if (array.length() > 0) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject tablelist = array.getJSONObject(i);
                                String time = tablelist.optString("time");
                                News news = new News();
                                Date d = null ;
                                try{d = sdf1.parse(time);}catch(Exception e){};
                                news.setNewsDate(dateFormat.format(d));
                                news.setNewsTime(timeFormat.format(d));
                                news.setNewContent(tablelist.optString("content"));
                                dataList.add(news);
                            }
                            NewsAdapter adapter = new NewsAdapter(NewsActivity.this, R.layout.item_listview_news, dataList);
                            newsAdapter.setAdapter(adapter);
                        }
                    } else {
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
            }

            ;
        });
        //请求结束

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.messages_return:
                Intent intent = new Intent(NewsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(NewsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }
}
