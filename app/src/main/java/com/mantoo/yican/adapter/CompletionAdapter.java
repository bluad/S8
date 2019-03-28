package com.mantoo.yican.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mantoo.yican.R;
import com.mantoo.yican.model.ExpressStatus;
import com.mantoo.yican.widget.RotateTextView;

import java.util.List;

/**
 * 发车列表
 * Created by Administrator on 2017/10/14.
 */

public class CompletionAdapter extends ArrayAdapter<ExpressStatus>{

    private int resourceId;

    public CompletionAdapter(Context context, int textViewResourceId,
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

        RotateTextView status = (RotateTextView) view.findViewById(R.id.completion_express_status);
        TextView missionNo = (TextView) view.findViewById(R.id.completion_express_missionNo);
        TextView expressDate = (TextView) view.findViewById(R.id.completion_express_date);
        TextView expressTime = (TextView) view.findViewById(R.id.completion_express_time);
        TextView line = (TextView) view.findViewById(R.id.completion_express_line);
        TextView way = (TextView) view.findViewById(R.id.completion_express_way);
        TextView number = (TextView) view.findViewById(R.id.completion_express_number);
        TextView jianShu = (TextView) view.findViewById(R.id.completion_express_jianshu);
        TextView tiJi = (TextView) view.findViewById(R.id.completion_express_tiji);
        TextView weight = (TextView) view.findViewById(R.id.completion_express_weight);
        TextView pay = (TextView) view.findViewById(R.id.completion_express_pay);

        if(missionNo != null)
        {
            missionNo.setText(express.getReceiveAddr());
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
            pay.setText(express.getComplayName());
        }
        if(status != null)
        {
            status.setText(express.getStatus());
        }


        return view;
    }

}
