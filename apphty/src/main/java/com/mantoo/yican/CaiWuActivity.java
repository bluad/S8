package com.mantoo.yican;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;

import com.mantoo.yican.adapter.CaiWuAdapter;
import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.model.ExpressStatus;

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
import com.mantoo.yican.cn.hty.R;

/**
 * Created by Administrator on 2017/10/12.
 */

public class CaiWuActivity  extends BaseActivity {

    private List<ExpressStatus> dataList = new ArrayList<ExpressStatus>();
    private ListView caiwuListView;
    private EditText yearSelect;
    private TextView account;
    private String yearAndMonth;
    private TextView January,February,March,April,May,June,July,August,September,October,November,December;
    private Configuration config;
    private Resources resources;
    private HorizontalScrollView textview_ScrollView;
    private List<String> year_list = new ArrayList<String>();
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caiwu);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象

        findViewById(R.id.caiwu_express_return).setOnClickListener(this);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM");
        yearAndMonth = formatter.format(new Date(System.currentTimeMillis()));

        January = (TextView)findViewById(R.id.January);
        February = (TextView)findViewById(R.id.February);
        March = (TextView)findViewById(R.id.March);
        April = (TextView)findViewById(R.id.April);
        May = (TextView)findViewById(R.id.May);
        June = (TextView)findViewById(R.id.June);
        July = (TextView)findViewById(R.id.July);
        August = (TextView)findViewById(R.id.August);
        September = (TextView)findViewById(R.id.September);
        October = (TextView)findViewById(R.id.October);
        November = (TextView)findViewById(R.id.November);
        December = (TextView)findViewById(R.id.December);

        initMonth();
        initView();
    }


    /**
     * 初始化月份
     */
    private void initMonth(){

        // 一月
        January.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                January.setBackgroundColor(Color.parseColor("#c8defc"));
                February.setBackgroundColor(Color.TRANSPARENT);
                March.setBackgroundColor(Color.TRANSPARENT);
                April.setBackgroundColor(Color.TRANSPARENT);
                May.setBackgroundColor(Color.TRANSPARENT);
                June.setBackgroundColor(Color.TRANSPARENT);
                July.setBackgroundColor(Color.TRANSPARENT);
                August.setBackgroundColor(Color.TRANSPARENT);
                September.setBackgroundColor(Color.TRANSPARENT);
                October.setBackgroundColor(Color.TRANSPARENT);
                November.setBackgroundColor(Color.TRANSPARENT);
                December.setBackgroundColor(Color.TRANSPARENT);

                String year = yearSelect.getText().toString();
                String month = January.getText().toString();
                if(month.equals("Jan."))
                {
                    month = "1月";
                }

                requestHttp(year, month);

            }
        });



        // 2月
        February.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                January.setBackgroundColor(Color.TRANSPARENT);
                February.setBackgroundColor(Color.parseColor("#c8defc"));
                March.setBackgroundColor(Color.TRANSPARENT);
                April.setBackgroundColor(Color.TRANSPARENT);
                May.setBackgroundColor(Color.TRANSPARENT);
                June.setBackgroundColor(Color.TRANSPARENT);
                July.setBackgroundColor(Color.TRANSPARENT);
                August.setBackgroundColor(Color.TRANSPARENT);
                September.setBackgroundColor(Color.TRANSPARENT);
                October.setBackgroundColor(Color.TRANSPARENT);
                November.setBackgroundColor(Color.TRANSPARENT);
                December.setBackgroundColor(Color.TRANSPARENT);

                String year = yearSelect.getText().toString();
                String month = February.getText().toString();
                if(month.equals("Feb."))
                {
                    month = "2月";
                }

                requestHttp(year, month);
            }
        });


        // 3月
        March.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                January.setBackgroundColor(Color.TRANSPARENT);
                February.setBackgroundColor(Color.TRANSPARENT);
                March.setBackgroundColor(Color.parseColor("#c8defc"));
                April.setBackgroundColor(Color.TRANSPARENT);
                May.setBackgroundColor(Color.TRANSPARENT);
                June.setBackgroundColor(Color.TRANSPARENT);
                July.setBackgroundColor(Color.TRANSPARENT);
                August.setBackgroundColor(Color.TRANSPARENT);
                September.setBackgroundColor(Color.TRANSPARENT);
                October.setBackgroundColor(Color.TRANSPARENT);
                November.setBackgroundColor(Color.TRANSPARENT);
                December.setBackgroundColor(Color.TRANSPARENT);

                String year = yearSelect.getText().toString();
                String month = March.getText().toString();
                if(month.equals("Mar."))
                {
                    month = "3月";
                }
                requestHttp(year, month);
            }
        });



        // 4月
        April.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                January.setBackgroundColor(Color.TRANSPARENT);
                February.setBackgroundColor(Color.TRANSPARENT);
                March.setBackgroundColor(Color.TRANSPARENT);
                April.setBackgroundColor(Color.parseColor("#c8defc"));
                May.setBackgroundColor(Color.TRANSPARENT);
                June.setBackgroundColor(Color.TRANSPARENT);
                July.setBackgroundColor(Color.TRANSPARENT);
                August.setBackgroundColor(Color.TRANSPARENT);
                September.setBackgroundColor(Color.TRANSPARENT);
                October.setBackgroundColor(Color.TRANSPARENT);
                November.setBackgroundColor(Color.TRANSPARENT);
                December.setBackgroundColor(Color.TRANSPARENT);

                String year = yearSelect.getText().toString();
                String month = April.getText().toString();

                if(month.equals("Apr."))
                {
                    month = "4月";
                }

                requestHttp(year, month);
            }
        });


        // 5月
        May.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                January.setBackgroundColor(Color.TRANSPARENT);
                February.setBackgroundColor(Color.TRANSPARENT);
                March.setBackgroundColor(Color.TRANSPARENT);
                April.setBackgroundColor(Color.TRANSPARENT);
                May.setBackgroundColor(Color.parseColor("#c8defc"));
                June.setBackgroundColor(Color.TRANSPARENT);
                July.setBackgroundColor(Color.TRANSPARENT);
                August.setBackgroundColor(Color.TRANSPARENT);
                September.setBackgroundColor(Color.TRANSPARENT);
                October.setBackgroundColor(Color.TRANSPARENT);
                November.setBackgroundColor(Color.TRANSPARENT);
                December.setBackgroundColor(Color.TRANSPARENT);

                String year = yearSelect.getText().toString();
                String month = May.getText().toString();

                if(month.equals("May."))
                {
                    month = "5月";
                }

                requestHttp(year, month);
            }
        });


        // 6月
        June.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                January.setBackgroundColor(Color.TRANSPARENT);
                February.setBackgroundColor(Color.TRANSPARENT);
                March.setBackgroundColor(Color.TRANSPARENT);
                April.setBackgroundColor(Color.TRANSPARENT);
                May.setBackgroundColor(Color.TRANSPARENT);
                June.setBackgroundColor(Color.parseColor("#c8defc"));
                July.setBackgroundColor(Color.TRANSPARENT);
                August.setBackgroundColor(Color.TRANSPARENT);
                September.setBackgroundColor(Color.TRANSPARENT);
                October.setBackgroundColor(Color.TRANSPARENT);
                November.setBackgroundColor(Color.TRANSPARENT);
                December.setBackgroundColor(Color.TRANSPARENT);

                String year = yearSelect.getText().toString();
                String month = June.getText().toString();

                if(month.equals("June."))
                {
                    month = "6月";
                }
                requestHttp(year, month);
            }
        });


        // 7月
        July.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                January.setBackgroundColor(Color.TRANSPARENT);
                February.setBackgroundColor(Color.TRANSPARENT);
                March.setBackgroundColor(Color.TRANSPARENT);
                April.setBackgroundColor(Color.TRANSPARENT);
                May.setBackgroundColor(Color.TRANSPARENT);
                June.setBackgroundColor(Color.TRANSPARENT);
                July.setBackgroundColor(Color.parseColor("#c8defc"));
                August.setBackgroundColor(Color.TRANSPARENT);
                September.setBackgroundColor(Color.TRANSPARENT);
                October.setBackgroundColor(Color.TRANSPARENT);
                November.setBackgroundColor(Color.TRANSPARENT);
                December.setBackgroundColor(Color.TRANSPARENT);

                String year = yearSelect.getText().toString();
                String month = July.getText().toString();
                if(month.equals("July."))
                {
                    month = "7月";
                }

                requestHttp(year, month);
            }
        });


        // 8月
        August.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                January.setBackgroundColor(Color.TRANSPARENT);
                February.setBackgroundColor(Color.TRANSPARENT);
                March.setBackgroundColor(Color.TRANSPARENT);
                April.setBackgroundColor(Color.TRANSPARENT);
                May.setBackgroundColor(Color.TRANSPARENT);
                June.setBackgroundColor(Color.TRANSPARENT);
                July.setBackgroundColor(Color.TRANSPARENT);
                August.setBackgroundColor(Color.parseColor("#c8defc"));
                September.setBackgroundColor(Color.TRANSPARENT);
                October.setBackgroundColor(Color.TRANSPARENT);
                November.setBackgroundColor(Color.TRANSPARENT);
                December.setBackgroundColor(Color.TRANSPARENT);

                String year = yearSelect.getText().toString();
                String month = August.getText().toString();
                if(month.equals("Aug."))
                {
                    month = "8月";
                }
                requestHttp(year, month);
            }
        });


        // 9月
        September.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                January.setBackgroundColor(Color.TRANSPARENT);
                February.setBackgroundColor(Color.TRANSPARENT);
                March.setBackgroundColor(Color.TRANSPARENT);
                April.setBackgroundColor(Color.TRANSPARENT);
                May.setBackgroundColor(Color.TRANSPARENT);
                June.setBackgroundColor(Color.TRANSPARENT);
                July.setBackgroundColor(Color.TRANSPARENT);
                August.setBackgroundColor(Color.TRANSPARENT);
                September.setBackgroundColor(Color.parseColor("#c8defc"));
                October.setBackgroundColor(Color.TRANSPARENT);
                November.setBackgroundColor(Color.TRANSPARENT);
                December.setBackgroundColor(Color.TRANSPARENT);

                String year = yearSelect.getText().toString();
                String month = September.getText().toString();
                if(month.equals("Sept."))
                {
                    month = "9月";
                }
                requestHttp(year, month);
            }
        });

        // 10月
        October.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                January.setBackgroundColor(Color.TRANSPARENT);
                February.setBackgroundColor(Color.TRANSPARENT);
                March.setBackgroundColor(Color.TRANSPARENT);
                April.setBackgroundColor(Color.TRANSPARENT);
                May.setBackgroundColor(Color.TRANSPARENT);
                June.setBackgroundColor(Color.TRANSPARENT);
                July.setBackgroundColor(Color.TRANSPARENT);
                August.setBackgroundColor(Color.TRANSPARENT);
                September.setBackgroundColor(Color.TRANSPARENT);
                October.setBackgroundColor(Color.parseColor("#c8defc"));
                November.setBackgroundColor(Color.TRANSPARENT);
                December.setBackgroundColor(Color.TRANSPARENT);

                String year = yearSelect.getText().toString();
                String month = October.getText().toString();
                if(month.equals("Oct."))
                {
                    month = "10月";
                }
                requestHttp(year, month);
            }
        });


        // 11月
        November.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                January.setBackgroundColor(Color.TRANSPARENT);
                February.setBackgroundColor(Color.TRANSPARENT);
                March.setBackgroundColor(Color.TRANSPARENT);
                April.setBackgroundColor(Color.TRANSPARENT);
                May.setBackgroundColor(Color.TRANSPARENT);
                June.setBackgroundColor(Color.TRANSPARENT);
                July.setBackgroundColor(Color.TRANSPARENT);
                August.setBackgroundColor(Color.TRANSPARENT);
                September.setBackgroundColor(Color.TRANSPARENT);
                October.setBackgroundColor(Color.TRANSPARENT);
                November.setBackgroundColor(Color.parseColor("#c8defc"));
                December.setBackgroundColor(Color.TRANSPARENT);

                String year = yearSelect.getText().toString();
                String month = November.getText().toString();
                if(month.equals("Nov."))
                {
                    month = "11月";
                }
                requestHttp(year, month);
            }
        });


        // 12月
        December.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                January.setBackgroundColor(Color.TRANSPARENT);
                February.setBackgroundColor(Color.TRANSPARENT);
                March.setBackgroundColor(Color.TRANSPARENT);
                April.setBackgroundColor(Color.TRANSPARENT);
                May.setBackgroundColor(Color.TRANSPARENT);
                June.setBackgroundColor(Color.TRANSPARENT);
                July.setBackgroundColor(Color.TRANSPARENT);
                August.setBackgroundColor(Color.TRANSPARENT);
                September.setBackgroundColor(Color.TRANSPARENT);
                October.setBackgroundColor(Color.TRANSPARENT);
                November.setBackgroundColor(Color.TRANSPARENT);
                December.setBackgroundColor(Color.parseColor("#c8defc"));

                String year = yearSelect.getText().toString();
                String month = December.getText().toString();
                if(month.equals("Dec."))
                {
                    month = "12月";
                }
                requestHttp(year, month);
            }
        });

    }

    private void initData()
    {
        for(int i=2000; i < 2101; i++)
        {
            year_list.add(i+"");
        }

    }

    /**
     * 选择年
     */
    public void showDialog()
    {
        initData();
        Context context = CaiWuActivity.this;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.activity_year_select, null);
        ListView myListView = (ListView)layout.findViewById(R.id.formcustomspinner_list);
        MyAdapter adapter = new MyAdapter(year_list,context);
        myListView.setAdapter(adapter);
        myListView.setDivider(new ColorDrawable(Color.rgb(0, 206, 209)));
        myListView.setDividerHeight(5);
        myListView.setSelection(year_list.indexOf(yearAndMonth.split("/")[0].toString()));
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                {
                    yearSelect.setText(year_list.get(position).toString() + "年");
                }
                else
                {
                    yearSelect.setText(year_list.get(position).toString() + "Year");
                }
                if(alertDialog != null)
                {
                    alertDialog.dismiss();
                }
            }
        });
        builder = new AlertDialog.Builder(context);
        builder.setView(layout);
        alertDialog = builder.create();
        alertDialog.show();
    }


    private void initView() {
        account = (TextView) findViewById(R.id.caiwu_express_account);
        yearSelect = (EditText) findViewById(R.id.year_select);
        yearSelect.setInputType(InputType.TYPE_NULL);
        caiwuListView = (ListView) findViewById(R.id.caiwu_express_list);
        if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
        {
            yearSelect.setText(yearAndMonth.split("/")[0].toString() + "年");
        }
        else
        {
            yearSelect.setText(yearAndMonth.split("/")[0].toString() + "Year");
        }

        // 加载年
        yearSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        //请求开始
        AjaxParams params = new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        params.put("year", yearAndMonth.split("/")[0]);
        params.put("month", yearAndMonth.split("/")[1]);
        String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.OBTAINFINANCIALINFOMATION;
        System.out.println(params + url);
        showLoadingDialog();
        PDAApplication.http.configTimeout(8000);
        PDAApplication.http.post(url, params, new AjaxCallBack<Object>() {
            public void onSuccess(Object t) {
                hideLoadingDialog();
                JSONObject object;
                try {
                    object = new JSONObject(t.toString());
                    if (object.optInt(Constants.RESULT_CODE) == 0) {
                        JSONObject data = object.optJSONObject(Constants.DATA);
                        account.setText(data.optString("totalAmount"));
                        JSONArray array = data.getJSONArray("missionList");
                        if (array.length() > 0) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject tablelist = array.getJSONObject(i);
                                String time = tablelist.optString("time");
                                ExpressStatus status = new ExpressStatus();
                                status.setMissionNo(tablelist.optString("missionNo"));
                                status.setWeight(tablelist.optString("weight") + "KG");
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
                                status.setTiJi(tablelist.optString("volumn") + "m³");
                                status.setNumber(tablelist.optString("waybillNumber"));
                                status.setStatus(tablelist.optString("status"));
                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                {
                                    status.setWay((tablelist.optString("type").equals("提货点到仓库")) ? "中转" : "直送");
                                }
                                else
                                {
                                    status.setWay((tablelist.optString("type").equals("提货点到仓库")) ? "Transfer" : "Direct");
                                }
                                dataList.add(status);
                            }
                            CaiWuAdapter adapter = new CaiWuAdapter(CaiWuActivity.this, R.layout.item_listview_caiwu, dataList);
                            caiwuListView.setAdapter(adapter);
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
            };
        });
        //请求结束

    }


    @Override
    protected void onResume() {
        super.onResume();
        // item点击
        caiwuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExpressStatus task = dataList.get(position);
                Intent intent = new Intent(CaiWuActivity.this, CaiWuInfoActivity.class);
                intent.putExtra("mission_no", task.getMissionNo());
                startActivity(intent);
                finish();
            }
        });
    }


    public void requestHttp(String year, String month)
    {
        //请求开始
        dataList.clear();
        account.setText("0");
        AjaxParams params = new AjaxParams();
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
        {
            params.put("year", year.substring(0, year.length() -1));
        }
        else
        {
            params.put("year", year.substring(0, year.length() -4));
        }
        params.put("month", month.substring(0, month.length() - 1));
        String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.OBTAINFINANCIALINFOMATION;
        System.out.println(params + url);
        showLoadingDialog();
        PDAApplication.http.configTimeout(8000);
        PDAApplication.http.post(url, params, new AjaxCallBack<Object>() {
            public void onSuccess(Object t) {
                hideLoadingDialog();
                JSONObject object;
                try {
                    object = new JSONObject(t.toString());
                    if (object.optInt(Constants.RESULT_CODE) == 0) {
                        JSONObject data = object.optJSONObject(Constants.DATA);
                        account.setText(data.optString("totalAmount"));
                        JSONArray array = data.getJSONArray("missionList");
                        if (array.length() > 0) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject tablelist = array.getJSONObject(i);
                                String time = tablelist.optString("time");
                                ExpressStatus status = new ExpressStatus();
                                status.setMissionNo(tablelist.optString("missionNo"));
                                status.setWeight(tablelist.optString("weight") + "KG");
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
                                status.setTiJi(tablelist.optString("volumn") + "m³");
                                status.setNumber(tablelist.optString("waybillNumber"));
                                status.setStatus(tablelist.optString("status"));
                                status.setMissionNo(tablelist.optString("missionNo"));
                                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                {
                                    status.setWay((tablelist.optString("type").equals("提货点到仓库")) ? "中转" : "直送");
                                }
                                else
                                {
                                    status.setWay((tablelist.optString("type").equals("提货点到仓库")) ? "Transfer" : "Direct");
                                }
                                dataList.add(status);
                            }
                            CaiWuAdapter adapter = new CaiWuAdapter(CaiWuActivity.this, R.layout.item_listview_caiwu, dataList);
                            caiwuListView.setAdapter(adapter);
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
            };
        });
        //请求结束
    }



    /**
     * 选择年
     */
   /* private void selectYear() {

        final Calendar calendar = Calendar.getInstance();
        new MonPickerDialog(CaiWuActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                {
                    yearSelect.setText(DateUtil.clanderTodatetime(calendar, "yyyy")+"年");
                }
                else
                {
                    yearSelect.setText(DateUtil.clanderTodatetime(calendar, "yyyy")+"Year");
                }


            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();

    }*/

    class MyAdapter extends BaseAdapter
    {
        private List<String> mlist;
        private Context mContext;

        public MyAdapter(List<String> mlist, Context mContext) {
            this.mlist = mlist;
            mlist = new ArrayList<String>();
            this.mContext = mContext;
        }


        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public Object getItem(int position) {
            return mlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Year year = null;
            if(convertView == null)
            {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(R.layout.item_listview_year, null);
                year = new Year();
                year.name = (TextView)convertView.findViewById(R.id.tv_name);
                convertView.setTag(year);
            }
            else
            {
                year = (Year)convertView.getTag();
            }
            year.name.setText(mlist.get(position).toString());
            return convertView;
        }

        class Year{private TextView name;}
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.caiwu_express_return:
                Intent intent = new Intent(CaiWuActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

}
