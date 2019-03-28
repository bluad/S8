package com.mantoo.yican;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.mantoo.yican.adapter.ChatAdapter;
import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.model.Msg;

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

import butterknife.ButterKnife;

public class MessagingActivity extends BaseActivity  implements View.OnClickListener{

    private List<Msg> mMsgs = new ArrayList<>();
    private ChatAdapter mAdapter;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    RecyclerView mRvChatList;
    EditText mEtContent;
    Button mBtSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        ButterKnife.bind(this);

        mRvChatList = (RecyclerView) findViewById(R.id.rv_chatList);
        mEtContent = (EditText) findViewById(R.id.et_content);
        mBtSend = (Button) findViewById(R.id.bt_send);
        findViewById(R.id.bt_send).setOnClickListener(this);
        findViewById(R.id.message_back).setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvChatList.setLayoutManager(linearLayoutManager);
        mAdapter = new ChatAdapter(this, mMsgs);
        mRvChatList.setAdapter(mAdapter);
        //初试加载历史记录呈现最新消息
        mRvChatList.scrollToPosition(mAdapter.getItemCount() - 1);

        //设置下滑隐藏软键盘
        mRvChatList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < -10) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEtContent.getWindowToken(), 0);
                }
            }
        });


        queryNumber();

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.mantoo.yican.service.CHANGE_STATUS");
        registerReceiver(mReceiver, filter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message_back:
                Intent intent = new Intent(MessagingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bt_send:
                String content = mEtContent.getText().toString();
                mAdapter.addItem(new Msg(AppCache.getString(AppCacheKey.name),content,df.format(new Date()), Msg.TYPE_PHONE));
                //铺满屏幕后呈现最新消息
                mRvChatList.scrollToPosition(mAdapter.getItemCount() - 1);
                mEtContent.setText("");

                //发送到后台
                AjaxParams params=new AjaxParams();
                params.put("driverId", AppCache.getString(AppCacheKey.driverid));
                params.put("content", content);
                String url = MainUrl.URL + MainUrl.REPALY;
                PDAApplication.http.configTimeout(8000);
                PDAApplication.http.post(url,params,new AjaxCallBack<Object>() {
                    public void onSuccess(Object t) {
                    };
                    public void onFailure(Throwable t, String strMsg) {
                        showToastMsg(R.string.error_network);
                    };
                } );
                //请求结束
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent2 = new Intent(MessagingActivity.this, MainActivity.class);
            startActivity(intent2);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }


    private void queryNumber()
    {
        AjaxParams params=new AjaxParams();
        params.put("driverId", AppCache.getString(AppCacheKey.driverid));
        String url = MainUrl.URL + MainUrl.SEARCHMESSAGE;
        PDAApplication.http.configTimeout(8000);
        PDAApplication.http.post(url,params,new AjaxCallBack<Object>() {
            public void onSuccess(Object t) {
                JSONObject object;
                try {
                    object = new JSONObject(t.toString());
                    JSONArray array = object.getJSONArray("data");
                    int size = array.length();
                    if(size > 0)
                    {
                        if(mMsgs.size() > 0)
                        {
                            mMsgs.clear();
                        }
                        for(int i=0; i<size; i++)
                        {
                            JSONObject tablelist = array.getJSONObject(i);
                            Msg msg = new Msg();
                            String time = tablelist.optString("sendTime");
                            msg.setName(tablelist.optString("createdBy"));
                            msg.setTime(df.format(new Date(Long.parseLong(time))));
                            msg.setContent(tablelist.optString("content"));
                            if(tablelist.optString("sendType").equals("1"))
                            {
                                msg.setType(Msg.TYPE_BLE);
                            }
                            else
                            {
                                msg.setType(Msg.TYPE_PHONE);
                            }

                            mMsgs.add(msg);
                        }

                        mAdapter.notifyDataSetChanged();
                        mRvChatList.scrollToPosition(mAdapter.getItemCount() - 1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            };
            public void onFailure(Throwable t, String strMsg) {
                showToastMsg(R.string.error_network);
            };
        } );
        //请求结束
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.mantoo.yican.service.CHANGE_STATUS")) {
                queryNumber();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

}
