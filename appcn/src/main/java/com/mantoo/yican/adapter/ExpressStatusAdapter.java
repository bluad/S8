package com.mantoo.yican.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mantoo.yican.cn.R;
import com.mantoo.yican.model.ExpressStatus;

import java.util.List;

/**
 * Created by Administrator on 2017/10/14.
 */

public class ExpressStatusAdapter extends ArrayAdapter<ExpressStatus> {

    private int resourceId;


    public ExpressStatusAdapter(Context context, int textViewResourceId,
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
        TextView distination = (TextView) view.findViewById(R.id.express_finish_distination);
        TextView pay = (TextView) view.findViewById(R.id.express_finish_pay);
        TextView status = (TextView) view.findViewById(R.id.express_finish_status);

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
        if(distination != null)
        {
            distination.setText(express.getDistination());
        }
        if(pay != null)
        {
            pay.setText(express.getPay());
        }
        if(status != null)
        {
            status.setText(express.getStatus());
        }

        return view;
    }

}
