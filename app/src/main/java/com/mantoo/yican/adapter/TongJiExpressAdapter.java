package com.mantoo.yican.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mantoo.yican.R;
import com.mantoo.yican.model.ExpressStatus;

import java.util.List;

/**
 * Created by Administrator on 2017/10/14.
 */

public class TongJiExpressAdapter extends ArrayAdapter<ExpressStatus> {

    private int resourceId;


    public TongJiExpressAdapter(Context context, int textViewResourceId,
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
        TextView expressDate = (TextView) view.findViewById(R.id.express_finish_date);
        TextView expressTime = (TextView) view.findViewById(R.id.express_finish_time);
        TextView line = (TextView) view.findViewById(R.id.express_finish_line);
        TextView way = (TextView) view.findViewById(R.id.express_finish_way);
        TextView number = (TextView) view.findViewById(R.id.express_finish_number);
        TextView jianShu = (TextView) view.findViewById(R.id.express_finish_jianshu);
        TextView tiJi = (TextView) view.findViewById(R.id.express_finish_tiji);
        TextView weight = (TextView) view.findViewById(R.id.express_finish_weight);
        TextView waybillno = (TextView) view.findViewById(R.id.express_finish_wanbillno);
        TextView pay = (TextView) view.findViewById(R.id.express_finish_pay);
        TextView status = (TextView) view.findViewById(R.id.express_finish_status);

        expressDate.setText(express.getExpressDate());
        expressTime.setText(express.getExpressTime());
        line.setText(express.getLine());
        way.setText(express.getWay());
        number.setText(express.getNumber());
        jianShu.setText(express.getJianShu());
        weight.setText(express.getWeight());
        tiJi.setText(express.getTiJi());
        weight.setText(express.getWeight());
        waybillno.setText(express.getMissionNo());
        pay.setText(express.getPay());
        if(status != null)
        {
            status.setText(express.getStatus());
        }

        return view;
    }

}