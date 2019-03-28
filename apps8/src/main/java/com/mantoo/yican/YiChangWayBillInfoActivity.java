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
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.mantoo.yican.s8.R;
import com.mantoo.yican.adapter.TableAdapter;
import com.mantoo.yican.adapter.TablePicAdapter;
import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.model.OrderSku;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 异常运单详情
 * Created by Administrator on 2017/10/14.
 */

public class YiChangWayBillInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView sender,sendPhone,sendAddress;
    private TextView receiver,receivePhone,receiveAddress;
    private TextView fee,goodsFormat;

    private List<Bitmap> picList = new ArrayList<Bitmap>();
    private List<OrderSku> dataList = new ArrayList<OrderSku>();
    private Configuration config;
    private Resources resources;


    private ListView waybillListView;
    private ListView waybillListViewPic;
    private String waybillNo;
    private EditText yichangContent,yichangResult;

    private ImageView problem_pic1,problem_pic2,problem_pic3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yichang_yundan_info);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        initView();
    }

    private void initView() {

        findViewById(R.id.yichang_info_return).setOnClickListener(this);
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
        yichangContent = (EditText)findViewById(R.id.yichang_content);
        yichangResult = (EditText)findViewById(R.id.yichang_result_content);
        waybillListViewPic.setVisibility(View.GONE);

        problem_pic1 = (ImageView) findViewById(R.id.problem_pic1);
        problem_pic2 = (ImageView) findViewById(R.id.problem_pic2);
        problem_pic3 = (ImageView) findViewById(R.id.problem_pic3);


        Intent intent = getIntent();
        waybillNo = intent.getStringExtra("waybillNo");
        String missionNo = intent.getStringExtra("missionNo");
        //请求开始
        AjaxParams params=new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        params.put("waybillNo", waybillNo);
        String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.EXCEPTIONWAYBILLDETAILS;
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
                        goodsFormat.setText(data.optString("number")+(config.locale.equals(Locale.SIMPLIFIED_CHINESE) ? "件/" : "P/")+data.optString("weight")+"KG/"+data.optString("volumn")+"m³");
                        yichangContent.setText(data.optString("problemReason"));
                        yichangResult.setText(data.optString("problemResult"));
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
                            TableAdapter adapter = new TableAdapter(YiChangWayBillInfoActivity.this, dataList);
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

                        final JSONArray picArray = data.getJSONArray("problemPictures");
                        //异步展示图片
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                picList.clear();
                                for (int i = 0; i < picArray.length(); i++) {
                                    Bitmap bitmap = null;
                                    try
                                    {
                                        bitmap = getBitmap(picArray.getJSONObject(i).optString("pictureLink"));
                                    } catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                    picList.add(bitmap);
                                }

                                Message message = new Message();
                                message.obj = picList;
                                handlerP.sendMessage(message);
                            }
                        }).start();
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
            TablePicAdapter adapter = new TablePicAdapter(YiChangWayBillInfoActivity.this, R.layout.item_listview_goodsinfo_pic, lists);
            waybillListViewPic.setAdapter(adapter);
            waybillListViewPic.setVisibility(View.VISIBLE);
        }
    };


    private Handler handlerP = new Handler() {
        public void handleMessage(Message msg) {
            List<Bitmap> lists = (List<Bitmap>)msg.obj;
            int ll = lists.size();
            switch (ll)
            {
                case 1:
                    problem_pic1.setImageBitmap(lists.get(0));
                    problem_pic1.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    problem_pic1.setImageBitmap(lists.get(0));
                    problem_pic1.setVisibility(View.VISIBLE);
                    problem_pic2.setImageBitmap(lists.get(1));
                    problem_pic2.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    problem_pic1.setImageBitmap(lists.get(0));
                    problem_pic1.setVisibility(View.VISIBLE);
                    problem_pic2.setImageBitmap(lists.get(1));
                    problem_pic2.setVisibility(View.VISIBLE);
                    problem_pic3.setImageBitmap(lists.get(2));
                    problem_pic3.setVisibility(View.VISIBLE);
                    break;
            }


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
            case R.id.yichang_info_return:
                Intent intent = new Intent(YiChangWayBillInfoActivity.this, YiChangActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(YiChangWayBillInfoActivity.this, YiChangActivity.class);
            startActivity(intent);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }
}
