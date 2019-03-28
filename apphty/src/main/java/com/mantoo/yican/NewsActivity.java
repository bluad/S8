package com.mantoo.yican;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mantoo.yican.adapter.NewsAdapter;
import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.model.ExpressStatus;
import com.mantoo.yican.model.News;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import com.mantoo.yican.cn.hty.R;
import com.mantoo.yican.model.TaskInfo;
import com.mantoo.yican.view.SwipeMenu;
import com.mantoo.yican.view.SwipeMenuCreator;
import com.mantoo.yican.view.SwipeMenuItem;
import com.mantoo.yican.view.SwipeMenuListView;

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
    private SwipeMenuListView newsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        initView();

        // 只有系统消息才可以进去
        newsAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = dataList.get(position);
                if(news.getNewsType().equals("1"))
                {
                    Intent intent = new Intent(NewsActivity.this, NewsContentActivity.class);
                    intent.putExtra("news", news);
                    startActivity(intent);
                }

            }
        });
    }

    private void initView() {
        findViewById(R.id.messages_return).setOnClickListener(this);
        newsAdapter = (SwipeMenuListView) findViewById(R.id.news_lists);

        //请求开始
        AjaxParams params = new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.GETALISTOFMESSAGES;
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
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject tablelist = array.getJSONObject(i);
                                String time = tablelist.optString("createdAt");
                                News news = new News();
                                news.setNewsDate(dateFormat.format(new Date(Long.parseLong(time))));
                                news.setNewsTime(timeFormat.format(new Date(Long.parseLong(time))));
                                news.setNewContent(tablelist.optString("newContent"));
                                news.setNewsType(tablelist.optString("newsType"));
                                news.setNewsTitle(tablelist.optString("newsTitle"));
                                news.setNewsNumber(tablelist.optString("newsNumber"));
                                JSONArray pictureList = tablelist.getJSONArray("pictureList");
                                if(pictureList != null)
                                {
                                    List<String> piclists = new ArrayList<String>();
                                    for (int j = 0; j < pictureList.length(); j++) {
                                        piclists.add(pictureList.getJSONObject(j).optString("pictureLink"));
                                    }
                                    news.setPictureList(piclists);
                                }

                                dataList.add(news);
                            }
                            NewsAdapter adapter = new NewsAdapter(NewsActivity.this, R.layout.item_listview_news, dataList);
                            newsAdapter.setAdapter(adapter);
                            newsAdapter.setMenuCreator(creator);
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

        /**
         * 滑动点击事件
         */
        newsAdapter.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                News news = dataList.get(position);
                switch (index) {
                    case 0:
                        AjaxParams params = new AjaxParams();
                        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
                        params.put("newsNumber", news.getNewsNumber());
                        String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.DELETEMESSAGES;
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
                                        Intent intent = new Intent(NewsActivity.this, NewsActivity.class);
                                        startActivity(intent);
                                        finish();
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
                        break;
                }
                return false;
            }
        });

    }


    private SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            SwipeMenuItem openItem = new SwipeMenuItem(
                    getApplicationContext());
            openItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                    0x3F, 0x25)));
            openItem.setWidth(dp2px(100));
            openItem.setTitle("删除");
            openItem.setTitleSize(18);
            openItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(openItem);

        }
    };

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
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
