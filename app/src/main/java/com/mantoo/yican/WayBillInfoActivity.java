package com.mantoo.yican;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mantoo.yican.adapter.ExpressPaiDanAdapter;
import com.mantoo.yican.adapter.TableAdapter;
import com.mantoo.yican.adapter.TablePicAdapter;
import com.mantoo.yican.adapter.TaskInfoAdapter;
import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.model.OrderSku;
import com.mantoo.yican.model.Remark;
import com.mantoo.yican.model.TaskInfo;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 运单详情
 * Created by Administrator on 2017/10/14.
 */

public class WayBillInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView sender,sendPhone,sendAddress;
    private TextView receiver,receivePhone,receiveAddress;
    private TextView fee,goodsFormat,yundan_info_remark;

    private Configuration config;
    private Resources resources;

    private List<Bitmap> picList = new ArrayList<Bitmap>();
    private List<OrderSku> dataList = new ArrayList<OrderSku>();

    private List<Remark> sendCustomerNote = new ArrayList<Remark>();
    private List<Remark> receiveCustomerNote = new ArrayList<Remark>();

    private ListView waybillListView;
    private ListView waybillListViewPic;
    private String waybillNo;

    private SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yundan_info);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        initView();
    }

    private void initView() {

        findViewById(R.id.sumbmit_yichang_button).setOnClickListener(this);
        findViewById(R.id.receive_remark_id).setOnClickListener(this);
        findViewById(R.id.rend_remark_id).setOnClickListener(this);

        sender = (TextView)findViewById(R.id.yundan_info_sender);
        sendPhone = (TextView)findViewById(R.id.yundan_info_sendPhone);
        sendAddress = (TextView)findViewById(R.id.yundan_info_sendAddress);
        receiver = (TextView)findViewById(R.id.yundan_info_receiver);
        receivePhone = (TextView)findViewById(R.id.yundan_info_receivePhone);
        receiveAddress = (TextView)findViewById(R.id.yundan_info_receiveAddress);
        fee = (TextView)findViewById(R.id.yundan_info_order_fee);
        goodsFormat = (TextView)findViewById(R.id.yundan_info_goods_format);
        waybillListView = (ListView)findViewById(R.id.goods_info_list);
        waybillListViewPic = (ListView)findViewById(R.id.goods_info_list_for_pic);
        waybillListViewPic.setVisibility(View.GONE);
        yundan_info_remark = (TextView)findViewById(R.id.yundan_info_remark);

        Intent intent = getIntent();
        waybillNo = intent.getStringExtra("waybillNo");
        String missionNo = intent.getStringExtra("missionNo");
        //请求开始
        AjaxParams params=new AjaxParams();
        params.put("waybillNo", waybillNo);
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        params.put("missionNo", missionNo);
        String url = MainUrl.URL + MainUrl.WAYBILLDETAILS;
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
                        JSONObject data = object.optJSONObject(Constants.DATA);
                        sender.setText(data.optString("sender"));
                        sendPhone.setText(data.optString("sendPhone"));
                        sendAddress.setText(data.optString("sendAddress"));
                        receiver.setText(data.optString("receiver"));
                        receivePhone.setText(data.optString("receivePhone"));
                        receiveAddress.setText(data.optString("receiveAddress"));

                        //发件人备注信息
                        JSONArray sendNotes = data.getJSONArray("sendCustomerNote");
                        int size = sendNotes.length();
                        if(size > 0)
                        {
                            for(int i=0; i<size; i++)
                            {
                                JSONObject tablelist = sendNotes.getJSONObject(i);
                                Remark remark = new Remark();
                                remark.setName(tablelist.optString("name"));
                                remark.setCreatedAt(sdft.format(new Date(Long.parseLong(tablelist.optString("createdAt")))));
                                remark.setNote(tablelist.optString("note"));
                                sendCustomerNote.add(remark);
                            }
                        }

                        //收件人备注信息
                        JSONArray receiveNotes = data.getJSONArray("receiveCustomerNote");
                        int sizes = receiveNotes.length();
                        if(sizes > 0)
                        {
                            for(int i=0; i<sizes; i++)
                            {
                                JSONObject tablelist = sendNotes.getJSONObject(i);
                                Remark remark = new Remark();
                                remark.setName(tablelist.optString("name"));
                                remark.setCreatedAt(sdft.format(new Date(Long.parseLong(tablelist.optString("createdAt")))));
                                remark.setNote(tablelist.optString("note"));
                                receiveCustomerNote.add(remark);
                            }
                        }


                        String note = data.optString("note");
                        if(note == null || note.equals(""))
                        {
                            yundan_info_remark.setText("无备注信息");
                        }
                        else {
                            yundan_info_remark.setText(note);
                        }

                        goodsFormat.setText(data.optString("number")+(config.locale.equals(Locale.SIMPLIFIED_CHINESE) ? "件/" : "P/")+data.optString("weight")+"KG/"+data.optString("volumn")+"m³");
                        fee.setText(data.optString("account"));
                        if(data.optString("isPic").equals("1"))
                        {
                            OrderSku o = new OrderSku();
                            if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                            {
                                o.setSkuName("货品信息");
                                o.setSkuNum("货品数量");
                                o.setRemarks("货品备注");
                                dataList.add(o);
                            }
                            else
                            {
                                o.setSkuName("Info");
                                o.setSkuNum("Number");
                                o.setRemarks("Mark");
                                dataList.add(o);
                            }

                            JSONArray array = data.getJSONArray("goodsDetail");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject tablelist = array.getJSONObject(i);
                                o = new OrderSku();
                                o.setSkuName(tablelist.optString("skuName"));
                                o.setSkuNum(tablelist.optString("skuNumber"));
                                o.setRemarks(tablelist.optString("remark"));
                                dataList.add(o);
                            }
                            TableAdapter adapter = new TableAdapter(WayBillInfoActivity.this, dataList);
                            waybillListView.setAdapter(adapter);
                        }
                        else
                        {
                            final JSONArray array = data.getJSONArray("pictureList");
                            waybillListView.setVisibility(View.GONE);
                            //异步展示图片
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < array.length(); i++) {
                                        Bitmap bitmap = null;
                                        try
                                        {
                                            bitmap = getBitmap(array.getJSONObject(i).optString("pictureLink"));
                                        } catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                        picList.add(bitmap);
                                    }

                                    Message message = new Message();
                                    message.obj = picList;
                                    handler.sendMessage(message);
                                }
                            }).start();
                        }
                    }
                    else
                    {
                        if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                        {
                            showStringToastMsg("获取数据失败");
                        }
                        else
                        {
                            showStringToastMsg("Failed to get data");
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
        //请求结束
    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Bitmap> lists = (List<Bitmap>)msg.obj;
            TablePicAdapter adapter = new TablePicAdapter(WayBillInfoActivity.this, R.layout.item_listview_goodsinfo_pic, lists);
            waybillListViewPic.setAdapter(adapter);
            waybillListViewPic.setVisibility(View.VISIBLE);
        }
    };

    public Bitmap getBitmap(String path) throws IOException {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sumbmit_yichang_button:
                Intent intent = new Intent(WayBillInfoActivity.this, YiChangSubmitActivity.class);
                intent.putExtra("waybillNo", waybillNo);
                startActivity(intent);
                finish();
                break;
            case R.id.rend_remark_id:
                Intent intent1 = new Intent(WayBillInfoActivity.this, RemarkListActivity.class);
                intent1.putExtra("sendCustomerNote", (Serializable)sendCustomerNote);
                intent1.putExtra("flag", "send");
                startActivity(intent1);
                break;
            case R.id.receive_remark_id:
                Intent intent2 = new Intent(WayBillInfoActivity.this, RemarkListActivity.class);
                intent2.putExtra("receiveCustomerNote", (Serializable)receiveCustomerNote);
                intent2.putExtra("flag", "receive");
                startActivity(intent2);
                break;
        }
    }
}
