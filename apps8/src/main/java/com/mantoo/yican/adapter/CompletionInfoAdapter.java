package com.mantoo.yican.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mantoo.yican.s8.R;
import com.mantoo.yican.model.TaskInfo;

import java.util.List;

/**
 * 完成 任务详情adapter
 * Created by Administrator on 2017/10/14.
 */

public class CompletionInfoAdapter extends ArrayAdapter<TaskInfo> implements View.OnClickListener{

    private int resourceId;
    private Callback mCallback;

    public CompletionInfoAdapter(Context context, int textViewResourceId,
                                 List<TaskInfo> objects, Callback callback) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        mCallback = callback;
    }

    @Override
    public void onClick(View v) {
        mCallback.click(v);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskInfo express = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }

        TextView waybillNo = (TextView) view.findViewById(R.id.completion_express_info_waybillNo);
        TextView sendAddress = (TextView) view.findViewById(R.id.completion_express_info_sendAddress);
        TextView receviceAddress = (TextView) view.findViewById(R.id.completion_express_info_receviceAddress);
        TextView number = (TextView) view.findViewById(R.id.completion_express_info_number);
        TextView volumn = (TextView) view.findViewById(R.id.completion_express_info_volumn);
        TextView weight = (TextView) view.findViewById(R.id.completion_express_info_weight);
        ImageView waybillStatus = (ImageView)view.findViewById(R.id.completion_waybill_status);
        ImageView route_plan = (ImageView) view.findViewById(R.id.route_plan);

        // 到货时间和代收款
        LinearLayout daishouL = (LinearLayout)view.findViewById(R.id.wanchengdaishou);
        LinearLayout daoTimeL = (LinearLayout)view.findViewById(R.id.wanchengshijian);
        TextView daishouT = (TextView) view.findViewById(R.id.wanchengdaishouT);
        TextView daoTimeT = (TextView) view.findViewById(R.id.wanchengshijianT);

        if(express.getDaishouAmount() != null && !express.getDaishouAmount().equals("") && !express.getDaishouAmount().equals("元"))
        {
            if(express.getPaytype().equals("3"))
            {
                daishouT.setText(String.valueOf(Double.parseDouble(express.getDaishouAmount()) + Double.parseDouble(express.getAccount())) + "元");
            }
            else
            {
                daishouT.setText(express.getDaishouAmount() + "元");
            }
            daishouL.setVisibility(View.VISIBLE);

        }

        if(express.getSendDate() != null && !express.getSendDate().equals(""))
        {
            daoTimeT.setText(express.getSendDate());
            daoTimeL.setVisibility(View.VISIBLE);
        }

        waybillNo.setText(express.getWaybillNo());
        sendAddress.setText(express.getSendAddress());
        receviceAddress.setText(express.getReceviceAddress());
        number.setText(express.getNumber());
        volumn.setText(express.getVolumn());
        weight.setText(express.getWeight());
        if(waybillStatus != null)
        {
            String status = express.getWaybillStatus();
            if(status.equals("部分完成"))
            {
                waybillStatus.setImageResource(R.mipmap.bufenwancheng);
            }
            else if(status.equals("待派送"))
            {
                waybillStatus.setImageResource(R.mipmap.daipaisong);
            }
            else if(status.equals("待配送"))
            {
                waybillStatus.setImageResource(R.mipmap.daipeisong);
            }
            else if(status.equals("待确认"))
            {
                waybillStatus.setImageResource(R.mipmap.daiqueren);
            }
            else if(status.equals("未处理"))
            {
                waybillStatus.setImageResource(R.mipmap.weichuli);
            }
            else if(status.equals("已处理"))
            {
                waybillStatus.setImageResource(R.mipmap.yichuli);
            }
            else if(status.equals("已签收"))
            {
                waybillStatus.setImageResource(R.mipmap.yiqianshou);
            }
            else if(status.equals("已完成"))
            {
                waybillStatus.setImageResource(R.mipmap.yiwancheng);
            }
            else if(status.equals("运输中"))
            {
                waybillStatus.setImageResource(R.mipmap.yunshuzhong);
            }
            else if(status.equals("直送"))
            {
                waybillStatus.setImageResource(R.mipmap.zhisong);
            }
            else if(status.equals("中转"))
            {
                waybillStatus.setImageResource(R.mipmap.zhongzhuan);
            }
        }

        if(route_plan != null)
        {
            if(express.getStartLat() == null || express.getStartLat().equals("")
                    || express.getStartLng() == null || express.getStartLng().equals("")
                    || express.getEndLat()== null || express.getEndLat().equals("")
                    || express.getEndLng() == null || express.getEndLng().equals(""))
            {
                route_plan.setVisibility(View.GONE);
            }
            else
            {
                route_plan.setOnClickListener(this);
                route_plan.setTag(position);
            }
        }

        return view;
    }

    /**
     * 自定义接口，用于回调按钮点击事件到Activity
     */
    public interface Callback {
        public void click(View v);
    }

}
