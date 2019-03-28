package com.mantoo.yican.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mantoo.yican.cn.hty.R;
import com.mantoo.yican.model.ExpressStatus;

import java.util.List;

/**
 * 派单
 * Created by Administrator on 2017/10/14.
 */

public class ExpressPaiDanAdapter extends ArrayAdapter<ExpressStatus> {

    private int resourceId;

    public ExpressPaiDanAdapter(Context context, int textViewResourceId,
                                List<ExpressStatus> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ExpressStatus express = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }


        TextView missionNo = (TextView) view.findViewById(R.id.paidan_task_missionNo);
        TextView expressDate = (TextView) view.findViewById(R.id.paidan_task_date);
        TextView expressTime = (TextView) view.findViewById(R.id.paidan_task_time);
        TextView line = (TextView) view.findViewById(R.id.paidan_task_line);
        TextView way = (TextView) view.findViewById(R.id.paidan_task_way);
        TextView number = (TextView) view.findViewById(R.id.paidan_task_number);
        TextView jianShu = (TextView) view.findViewById(R.id.paidan_task_jianshu);
        TextView tiJi = (TextView) view.findViewById(R.id.paidan_task_tiji);
        TextView weight = (TextView) view.findViewById(R.id.paidan_task_weight);
        TextView pay = (TextView) view.findViewById(R.id.paidan_task_pay);
        TextView totalpaidan = (TextView) view.findViewById(R.id.totalpaidan);
        LinearLayout paidanLi = (LinearLayout)view.findViewById(R.id.paidanLi);


        if(missionNo != null)
        {
            missionNo.setText(express.getMissionNo());
        }
        if(expressDate != null)
        {
            expressDate.setText(express.getExpressDate());
        }
        if(expressTime != null)
        {
            expressTime.setText(express.getExpressTime());
        }
        if(line != null)
        {
            line.setText(express.getLine());
        }
        if(way != null)
        {
            way.setText(express.getWay());
        }
        if(number != null)
        {
            number.setText(express.getNumber());
        }
        if(jianShu != null)
        {
            jianShu.setText(express.getJianShu());
        }
        if(weight != null)
        {
            weight.setText(express.getWeight());
        }
        if(tiJi != null)
        {
            tiJi.setText(express.getTiJi());
        }
        if(weight != null)
        {
            weight.setText(express.getWeight());
        }
        if(pay != null)
        {
            pay.setText(express.getPay());
        }

        if(express.getTotalAcount() != null && !express.getTotalAcount().equals("") && (Integer.parseInt(express.getTotalAcount()) > 0))
        {
            totalpaidan.setText(express.getTotalAcount());
            paidanLi.setVisibility(View.VISIBLE);
        }

        return view;
    }


}
