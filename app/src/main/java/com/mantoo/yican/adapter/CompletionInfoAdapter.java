package com.mantoo.yican.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mantoo.yican.R;
import com.mantoo.yican.model.TaskInfo;
import com.mantoo.yican.widget.RotateTextView;

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
        RotateTextView waybillstatis = (RotateTextView)view.findViewById(R.id.completion_waybill_status);
        ImageView route_plan = (ImageView) view.findViewById(R.id.route_plan);

        waybillNo.setText(express.getWaybillNo());
        sendAddress.setText(express.getSendAddress());
        receviceAddress.setText(express.getReceviceAddress());
        number.setText(express.getNumber());
        volumn.setText(express.getVolumn());
        weight.setText(express.getWeight());
        if(waybillstatis != null)
        {
            waybillstatis.setText(express.getWaybillStatus());
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
