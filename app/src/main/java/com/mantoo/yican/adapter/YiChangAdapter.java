package com.mantoo.yican.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mantoo.yican.R;
import com.mantoo.yican.model.TaskInfo;
import com.mantoo.yican.widget.RotateTextView;

import java.util.List;

/**
 * 异常
 * Created by Administrator on 2017/10/14.
 */

public class YiChangAdapter extends ArrayAdapter<TaskInfo> {

    private int resourceId;

    public YiChangAdapter(Context context, int textViewResourceId,
                            List<TaskInfo> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
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

        TextView waybillNo = (TextView) view.findViewById(R.id.yichang_express_info_waybillNo);
        TextView sendAddress = (TextView) view.findViewById(R.id.yichang_express_info_sendAddress);
        TextView receviceAddress = (TextView) view.findViewById(R.id.yichang_express_info_receviceAddress);
        TextView number = (TextView) view.findViewById(R.id.yichang_express_info_number);
        TextView volumn = (TextView) view.findViewById(R.id.yichang_express_info_volumn);
        TextView weight = (TextView) view.findViewById(R.id.yichang_express_info_weight);
        TextView ishandle = (RotateTextView) view.findViewById(R.id.yichang_ishandle);

        waybillNo.setText(express.getWaybillNo());
        sendAddress.setText(express.getSendAddress());
        receviceAddress.setText(express.getReceviceAddress());
        number.setText(express.getNumber());
        volumn.setText(express.getVolumn());
        weight.setText(express.getWeight());
        if(ishandle != null)
        {
            ishandle.setText(express.getIsHandle());
        }

        return view;
    }


}
