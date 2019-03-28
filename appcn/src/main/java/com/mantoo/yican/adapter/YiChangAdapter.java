package com.mantoo.yican.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mantoo.yican.cn.R;
import com.mantoo.yican.model.TaskInfo;

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
        ImageView ishandle = (ImageView) view.findViewById(R.id.yichang_ishandle);

        // 到货时间和代收款
        LinearLayout daishouL = (LinearLayout)view.findViewById(R.id.yichangdaishou);
        LinearLayout daoTimeL = (LinearLayout)view.findViewById(R.id.yichangshijian);
        TextView daishouT = (TextView) view.findViewById(R.id.yichangdaishouT);
        TextView daoTimeT = (TextView) view.findViewById(R.id.yichangshijianT);

        if(express.getDaishouAmount() != null && !express.getDaishouAmount().equals("") && !express.getDaishouAmount().equals("元"))
        {
            daishouT.setText(express.getDaishouAmount());
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
        if(ishandle != null)
        {
            String status = express.getIsHandle();

            if(status.equals("待确认"))
            {
                ishandle.setImageResource(R.mipmap.daiqueren);
            }
            else if(status.equals("未处理"))
            {
                ishandle.setImageResource(R.mipmap.weichuli);
            }
            else if(status.equals("已处理"))
            {
                ishandle.setImageResource(R.mipmap.yichuli);
            }
            else if(status.equals("已完成"))
            {
                ishandle.setImageResource(R.mipmap.yiwancheng);
            }
        }

        return view;
    }


}
