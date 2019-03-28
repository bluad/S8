package com.mantoo.yican.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.common.StringUtils;
import com.mantoo.yican.cn.R;
import com.mantoo.yican.model.TaskInfo;

import java.util.List;

/**
 * 派单任务详情adapter
 * Created by Administrator on 2017/10/14.
 */

public class TaskInfoAdapter extends ArrayAdapter<TaskInfo>  implements View.OnClickListener {

    private int resourceId;
    private Callback mCallback;

    public TaskInfoAdapter(Context context, int textViewResourceId,
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
        TextView waybillNo = (TextView) view.findViewById(R.id.paidan_task_info_waybillNo);
        TextView sendAddress = (TextView) view.findViewById(R.id.paidan_task_info_sendAddress);
        TextView receviceAddress = (TextView) view.findViewById(R.id.paidan_task_info_receviceAddress);
        TextView number = (TextView) view.findViewById(R.id.paidan_task_info_number);
        TextView volumn = (TextView) view.findViewById(R.id.paidan_task_info_volumn);
        TextView weight = (TextView) view.findViewById(R.id.paidan_task_info_weight);
        TextView distance = (TextView) view.findViewById(R.id.paidan_distance);
        ImageView route_plan = (ImageView) view.findViewById(R.id.route_plan);

        // 到货时间和代收款
        LinearLayout daishoukuaiL = (LinearLayout)view.findViewById(R.id.daishoujineL);
        LinearLayout daohuoshijianL = (LinearLayout)view.findViewById(R.id.songhuoshijianL);
        TextView daishoukuaiT = (TextView) view.findViewById(R.id.daishoujineT);
        TextView daohuoTime = (TextView) view.findViewById(R.id.songhuoshijianT);

        if(express.getDaishouAmount() != null && !express.getDaishouAmount().equals("") && !express.getDaishouAmount().equals("元"))
        {
            if(express.getPaytype().equals("3"))
            {
                daishoukuaiT.setText(String.valueOf(Double.parseDouble(express.getDaishouAmount()) + Double.parseDouble(express.getAccount())) + "元");
            }
            else
            {
                daishoukuaiT.setText(express.getDaishouAmount() + "元");
            }
            daishoukuaiL.setVisibility(View.VISIBLE);
        }

        if(express.getSendDate() != null && !express.getSendDate().equals(""))
        {
            daohuoTime.setText(express.getSendDate());
            daohuoshijianL.setVisibility(View.VISIBLE);
        }

        waybillNo.setText(express.getWaybillNo());
        sendAddress.setText(express.getSendAddress());
        receviceAddress.setText(express.getReceviceAddress());
        number.setText(express.getNumber());
        volumn.setText(express.getVolumn());
        weight.setText(express.getWeight());
        if(distance != null)
        {
            distance.setText(express.getDistence());
        }
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

        return view;
    }

    /**
     * 自定义接口，用于回调按钮点击事件到Activity
     */
    public interface Callback {
        public void click(View v);
    }

}
