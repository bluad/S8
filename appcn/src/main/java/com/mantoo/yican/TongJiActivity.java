package com.mantoo.yican;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.mantoo.yican.cn.R;
import com.mantoo.yican.adapter.TongJiExpressAdapter;
import com.mantoo.yican.adapter.WuliuAdapter;
import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.model.ExpressStatus;
import com.mantoo.yican.model.RunningRecord;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/10/12.
 */

public class TongJiActivity extends BaseActivity {

    private RadioGroup MyrGroup;
    private RadioButton yeJi; //业绩
    private RadioButton daipaidan;// 派单
    private RadioButton taskdan;// 任务单
    private RadioButton yifache;// 已发车
    private RadioButton finishdan;// 已完成
    private RadioButton wuliu;// 物流跟踪
    private TongJiExpressAdapter paiJianAdapter = null;// 统计-派单
    private TongJiExpressAdapter renWUAdapter = null;// 任务单
    private TongJiExpressAdapter faCheAdapter = null;// 已发车
    private TongJiExpressAdapter finishAdapter = null;// 已完成
    private WuliuAdapter wlAdapter = null; // 物流跟踪
    private TextView note;
    private LinearLayout ll_wuliu; // 物流跟踪
    private LinearLayout lYeJi; //业绩

    private ListView mListView;

    private TextView number;// 运单编号
    private ListView wlListview; //

    private EditText wayBillNoSearch;//运单号

    private String resultNo; // 扫描返回值

    private Configuration config;
    private Resources resources;

    // 业绩
    private TextView the_number_of_annual_orders,refund_orders,amount_completed;
    private TextView to_be_completed_the_number,number_of_exceptions;
    private TextView order_rate,exception_rate;

    // 任务单集合
    private List<ExpressStatus> missionNoLists = new ArrayList<ExpressStatus>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongji);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        findViewById(R.id.tongji_return).setOnClickListener(this);
        findViewById(R.id.tongji_waybillno_scan).setOnClickListener(this);
        initView();
        intData();

        wayBillNoSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });

        // item点击
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExpressStatus task = missionNoLists.get(position);
                Intent intent = new Intent(TongJiActivity.this, TongJiInfoActivity.class);
                intent.putExtra("mission_no", task.getMissionNo());
                startActivity(intent);
                finish();
            }
        });

        Intent intent = getIntent();
        resultNo = intent.getStringExtra("result");
        if(resultNo != null)
        {
            wayBillNoSearch.setText(resultNo.toString());
            getWuliu(resultNo);
        }

    }

    private void intData() {
        getNumber();
    }

    private void search() {
        String searchContext = wayBillNoSearch.getText().toString().trim();
        getWuliu(searchContext);
    }

    private void initView() {

        // 业绩
        the_number_of_annual_orders = (TextView) findViewById(R.id.the_number_of_annual_orders);
        refund_orders = (TextView) findViewById(R.id.refund_orders);
        amount_completed = (TextView) findViewById(R.id.amount_completed);
        to_be_completed_the_number = (TextView) findViewById(R.id.to_be_completed_the_number);
        number_of_exceptions = (TextView) findViewById(R.id.number_of_exceptions);
        order_rate = (TextView) findViewById(R.id.order_rate);
        exception_rate = (TextView) findViewById(R.id.exception_rate);

        wayBillNoSearch = (EditText) findViewById(R.id.tongji_waybillno_search);

        number = (TextView) findViewById(R.id.number);
        wlListview = (ListView) findViewById(R.id.lv_wuliu);
        ll_wuliu = (LinearLayout) findViewById(R.id.ll_wuliu);
        mListView = (ListView) findViewById(R.id.lv);
        note = (TextView) findViewById(R.id.note);
        MyrGroup = (RadioGroup) findViewById(R.id.main_rb);
        yeJi = (RadioButton) findViewById(R.id.tongji_ye_ji);
        daipaidan = (RadioButton) findViewById(R.id.tongji_paidan);
        taskdan = (RadioButton) findViewById(R.id.tongji_renwudan);
        yifache = (RadioButton) findViewById(R.id.tongji_yifache);
        finishdan = (RadioButton) findViewById(R.id.tongji_yiwancheng);
        wuliu = (RadioButton) findViewById(R.id.wuliu);
        lYeJi = (LinearLayout) findViewById(R.id.l_ye_ji);
        mListView.setVisibility(View.GONE);
        ll_wuliu.setVisibility(View.GONE);
        note.setVisibility(View.VISIBLE);
        lYeJi.setVisibility(View.GONE);

        getYeJi();//业绩
        MyrGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                switch (arg1) {
                    case R.id.tongji_ye_ji:
                        // 业绩
                        getYeJi();
                        break;
                    case R.id.tongji_paidan:
                        // 派单
                        getPaiDanList();
                        break;
                    case R.id.tongji_yifache:
                        // 已发车
                        getYiFaChe();
                        break;
                    case R.id.tongji_renwudan:
                        // 任务单
                        getTaskExpress();
                        break;
                    case R.id.tongji_yiwancheng:
                        // 已完成
                        getCompletion();
                        break;
                    case R.id.wuliu:
                        getWuliu("");
                        break;

                    default:
                        break;
                }

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUESTCODE_ADDRESSEE_NEW && resultCode == RESULT_OK) {
            String code = data.getStringExtra("data");
        }
    }

    // 派单
    protected void getPaiDanList() {
        lYeJi.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        note.setVisibility(View.GONE);
        //请求开始
        AjaxParams params=new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        params.put("missionType", "1");
        String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.PIELIST;
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
                        missionNoLists.clear();
                        JSONObject data = object.optJSONObject(Constants.DATA);
                        JSONArray array = data.getJSONArray("arr");
                        if (array.length() > 0) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject tablelist = array.getJSONObject(i);
                                String time = tablelist.optString("time");
                                ExpressStatus status = new ExpressStatus();
                                status.setMissionNo(tablelist.optString("missionNo"));
                                status.setWeight(tablelist.optString("weight")+"KG");
                                status.setExpressDate(dateFormat.format(new Date(Long.parseLong(time))));
                                status.setExpressTime(timeFormat.format(new Date(Long.parseLong(time))));
                                status.setJianShu(tablelist.optString("number")+"件");
                                status.setLine(tablelist.optString("route"));
                                status.setPay(tablelist.optString("amount"));
                                status.setTiJi(tablelist.optString("volumn")+"m³");
                                status.setNumber(tablelist.optString("waybillNumber"));
                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                {
                                    status.setWay((tablelist.optString("type").equals("提货点到仓库")) ? "中转" : "直送");
                                }
                                else
                                {
                                    status.setWay((tablelist.optString("type").equals("提货点到仓库")) ? "Transfer" : "Direct");
                                }
                                missionNoLists.add(status);
                            }
                            mListView.setVisibility(View.VISIBLE);
                            note.setVisibility(View.GONE);
                            ll_wuliu.setVisibility(View.GONE);
                            paiJianAdapter = new TongJiExpressAdapter(TongJiActivity.this, R.layout.item_tongji_express, missionNoLists);
                            mListView.setAdapter(paiJianAdapter);
                        }
                    }
                    else
                    {
                        if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                        {
                            showStringToastMsg("无数据!");
                        }
                        else
                        {
                            showStringToastMsg("No data!");
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


    // 已发车
    private void getYiFaChe() {
        lYeJi.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        note.setVisibility(View.GONE);
        //请求开始
        AjaxParams params=new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        params.put("missionType", "3");
        String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.PIELIST;
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
                        missionNoLists.clear();
                        JSONObject data = object.optJSONObject(Constants.DATA);
                        JSONArray array = data.getJSONArray("arr");
                        if (array.length() > 0) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject tablelist = array.getJSONObject(i);
                                String time = tablelist.optString("time");
                                ExpressStatus status = new ExpressStatus();
                                status.setMissionNo(tablelist.optString("missionNo"));
                                status.setWeight(tablelist.optString("weight")+"KG");
                                status.setExpressDate(dateFormat.format(new Date(Long.parseLong(time))));
                                status.setExpressTime(timeFormat.format(new Date(Long.parseLong(time))));
                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                {
                                    status.setJianShu(tablelist.optString("number") + "件");
                                }
                                else
                                {
                                    status.setJianShu(tablelist.optString("number") + "Pieces");
                                }
                                status.setLine(tablelist.optString("route"));
                                status.setPay(tablelist.optString("amount"));
                                status.setTiJi(tablelist.optString("volumn")+"m³");
                                status.setNumber(tablelist.optString("waybillNumber"));
                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                {
                                    status.setWay((tablelist.optString("type").equals("提货点到仓库")) ? "中转" : "直送");
                                }
                                else
                                {
                                    status.setWay((tablelist.optString("type").equals("提货点到仓库")) ? "Transfer" : "Direct");
                                }
                                missionNoLists.add(status);
                            }
                            mListView.setVisibility(View.VISIBLE);
                            note.setVisibility(View.GONE);
                            ll_wuliu.setVisibility(View.GONE);
                            faCheAdapter = new TongJiExpressAdapter(TongJiActivity.this, R.layout.item_tongji_express, missionNoLists);
                            mListView.setAdapter(faCheAdapter);
                        }
                    }
                    else
                    {
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
            };
        } );
        //请求结束

    }

    // 任务单
    private void getTaskExpress() {
        lYeJi.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        note.setVisibility(View.GONE);
        //请求开始
        AjaxParams params=new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        params.put("missionType", "2");
        String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.PIELIST;
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
                        missionNoLists.clear();
                        JSONObject data = object.optJSONObject(Constants.DATA);
                        JSONArray array = data.getJSONArray("arr");
                        if (array.length() > 0) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject tablelist = array.getJSONObject(i);
                                String time = tablelist.optString("time");
                                ExpressStatus status = new ExpressStatus();
                                status.setMissionNo(tablelist.optString("missionNo"));
                                status.setWeight(tablelist.optString("weight")+"KG");
                                status.setExpressDate(dateFormat.format(new Date(Long.parseLong(time))));
                                status.setExpressTime(timeFormat.format(new Date(Long.parseLong(time))));
                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                {
                                    status.setJianShu(tablelist.optString("number") + "件");
                                }
                                else
                                {
                                    status.setJianShu(tablelist.optString("number") + "Pieces");
                                }
                                status.setLine(tablelist.optString("route"));
                                status.setPay(tablelist.optString("amount"));
                                status.setTiJi(tablelist.optString("volumn")+"m³");
                                status.setNumber(tablelist.optString("waybillNumber"));
                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                {
                                    status.setWay((tablelist.optString("type").equals("提货点到仓库")) ? "中转" : "直送");
                                }
                                else
                                {
                                    status.setWay((tablelist.optString("type").equals("提货点到仓库")) ? "Transfer" : "Direct");
                                }
                                missionNoLists.add(status);
                            }
                            mListView.setVisibility(View.VISIBLE);
                            note.setVisibility(View.GONE);
                            ll_wuliu.setVisibility(View.GONE);
                            renWUAdapter = new TongJiExpressAdapter(TongJiActivity.this, R.layout.item_tongji_express, missionNoLists);
                            mListView.setAdapter(renWUAdapter);
                        }
                    }
                    else
                    {
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
            };
        } );
        //请求结束

    }

    // 已完成
    protected void getCompletion() {
        lYeJi.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        note.setVisibility(View.GONE);
        //请求开始
        AjaxParams params=new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.COMPLETElIST;
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
                        missionNoLists.clear();
                        JSONObject data = object.optJSONObject(Constants.DATA);
                        JSONArray array = data.getJSONArray("arr");
                        if (array.length() > 0) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject tablelist = array.getJSONObject(i);
                                String time = tablelist.optString("time");
                                ExpressStatus status = new ExpressStatus();
                                status.setMissionNo(tablelist.optString("missionNo"));
                                status.setWeight(tablelist.optString("weight")+"KG");
                                status.setExpressDate(dateFormat.format(new Date(Long.parseLong(time))));
                                status.setExpressTime(timeFormat.format(new Date(Long.parseLong(time))));
                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                {
                                    status.setJianShu(tablelist.optString("number") + "件");
                                }
                                else
                                {
                                    status.setJianShu(tablelist.optString("number") + "Pieces");
                                }
                                status.setLine(tablelist.optString("route"));
                                status.setPay(tablelist.optString("amount"));
                                status.setTiJi(tablelist.optString("volumn")+"m³");
                                status.setNumber(tablelist.optString("waybillNumber"));
                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                {
                                    status.setWay((tablelist.optString("type").equals("提货点到仓库")) ? "中转" : "直送");
                                }
                                else
                                {
                                    status.setWay((tablelist.optString("type").equals("提货点到仓库")) ? "Transfer" : "Direct");
                                }
                                missionNoLists.add(status);
                            }
                            mListView.setVisibility(View.VISIBLE);
                            note.setVisibility(View.GONE);
                            ll_wuliu.setVisibility(View.GONE);
                            finishAdapter = new TongJiExpressAdapter(TongJiActivity.this, R.layout.item_tongji_express, missionNoLists);
                            mListView.setAdapter(finishAdapter);
                        }
                    }
                    else
                    {
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
            };
        } );
        //请求结束
    }

    // 物流
    protected void getWuliu(String waybillNo) {
        lYeJi.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        String wayBillNoText = wayBillNoSearch.getText().toString();
        if((waybillNo == null || waybillNo.equals(""))
                && (wayBillNoText == null || wayBillNoText.equals("")))
        {
            note.setText(R.string.input_or_scan_express_no);
            note.setVisibility(View.VISIBLE);
            return;
        }

        if((waybillNo == null || waybillNo.equals("")) && !wayBillNoText.equals(""))
        {
            waybillNo = wayBillNoText;
        }

        //请求开始
        AjaxParams params=new AjaxParams();
        params.put("waybillNo", waybillNo);
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.LOGISTICSTRACKIING;
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
                        ArrayList<RunningRecord> list = new ArrayList<RunningRecord>();
                        list.clear();
                        JSONObject data = object.optJSONObject(Constants.DATA);
                        JSONArray array = data.getJSONArray("logistics_record");
                        if (array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject tablelist = array.getJSONObject(i);
                                RunningRecord record = new RunningRecord();
                                record.setTime(tablelist.optString("time"));
                                record.setContext(tablelist.optString("status"));
                                list.add(record);
                            }
                            ll_wuliu.setVisibility(View.VISIBLE);
                            note.setVisibility(View.GONE);
                            wlAdapter = new WuliuAdapter(TongJiActivity.this, list);
                            wlListview.setAdapter(wlAdapter);
                        }
                    }
                    else
                    {
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
            };
        } );
        //请求结束
    }

    // 业绩
    protected void getYeJi() {
        mListView.setVisibility(View.GONE);
        lYeJi.setVisibility(View.VISIBLE);
        note.setVisibility(View.GONE);
        ll_wuliu.setVisibility(View.GONE);
        //请求开始
        AjaxParams params=new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.GETSTATISTICS;
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
                        the_number_of_annual_orders.setText(data.optString("missionNumber"));
                        refund_orders.setText(data.optString("refuzeNumber"));
                        amount_completed.setText(data.optString("finishNumber"));
                        to_be_completed_the_number.setText(data.optString("unfinishedNumber"));
                        number_of_exceptions.setText(data.optString("problemNumber"));
                        order_rate.setText(Float.parseFloat(data.optString("missionPercent")) + "%");
                        exception_rate.setText(Float.parseFloat(data.optString("problemPercent")) + "%");
                    }
                    else
                    {
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
            };
        } );
        //请求结束
    }


    //派单
    private void getNumber() {

        //请求开始
        AjaxParams params=new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.GETSTATISTICS;
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
                        if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                        {
                            daipaidan.setText("接单\n"+"("+data.optString("dispatchNumber")+")");
                            taskdan.setText("提货\n"+"("+data.optString("deliverNumber")+")");
                            yifache.setText("到货\n"+"("+data.optString("transportNumber")+")");
                            finishdan.setText("已完成\n"+"("+data.optString("finishNumber")+")");
                        }
                        else
                        {
                            daipaidan.setText("Take order\n"+"("+data.optString("dispatchNumber")+")");
                            taskdan.setText("Send\n"+"("+data.optString("deliverNumber")+")");
                            yifache.setText("Arrive\n"+"("+data.optString("transportNumber")+")");
                            finishdan.setText("Finished\n"+"("+data.optString("finishNumber")+")");
                        }
                    }
                    else
                    {
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
            };
        } );
        //请求结束

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tongji_return:
                Intent intent = new Intent(TongJiActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tongji_waybillno_scan:
                Intent intent1 = new Intent(TongJiActivity.this, LongnerScanActivity.class);
                intent1.putExtra("activityType", "TongJiActivity");
                startActivity(intent1);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(TongJiActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

}
