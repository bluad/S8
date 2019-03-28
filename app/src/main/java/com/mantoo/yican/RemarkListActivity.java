package com.mantoo.yican;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.mantoo.yican.adapter.NewsAdapter;
import com.mantoo.yican.adapter.RemarkAdapter;
import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.model.News;
import com.mantoo.yican.model.Remark;

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

public class RemarkListActivity extends BaseActivity implements View.OnClickListener {

    ListView newsAdapter;

    private List<Remark> sendCustomerNote = new ArrayList<Remark>();
    private List<Remark> receiveCustomerNote = new ArrayList<Remark>();

    private String flag = "";
    private Configuration config;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark_list);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        initView();
    }

    private void initView() {
        findViewById(R.id.remarks_return).setOnClickListener(this);
        newsAdapter = (ListView) findViewById(R.id.remarks_list);

        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        if(flag.equals("send"))
        {
            sendCustomerNote = (ArrayList<Remark>) getIntent().getSerializableExtra("sendCustomerNote");
            RemarkAdapter adapter = new RemarkAdapter(RemarkListActivity.this, R.layout.item_listview_remarks, sendCustomerNote);
            newsAdapter.setAdapter(adapter);
        }
        else
        {
            receiveCustomerNote = (ArrayList<Remark>) getIntent().getSerializableExtra("receiveCustomerNote");
            RemarkAdapter adapter = new RemarkAdapter(RemarkListActivity.this, R.layout.item_listview_remarks, receiveCustomerNote);
            newsAdapter.setAdapter(adapter);
        }

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remarks_return:
                finish();
                break;
        }
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
