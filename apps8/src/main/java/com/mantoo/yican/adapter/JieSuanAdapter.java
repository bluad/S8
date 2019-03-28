package com.mantoo.yican.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mantoo.yican.s8.R;
import com.mantoo.yican.model.ExpressStatus;

import java.util.List;

/**
 * 结算列表
 * Created by Administrator on 2017/10/14.
 */

public class JieSuanAdapter extends ArrayAdapter<ExpressStatus>{

    private int resourceId;

    public JieSuanAdapter(Context context, int textViewResourceId,
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

        TextView missionNo = (TextView) view.findViewById(R.id.jiesuan_express_missionNo);
        TextView expressDate = (TextView) view.findViewById(R.id.jiesuan_express_date);
        TextView expressTime = (TextView) view.findViewById(R.id.jiesuan_express_time);
        TextView line = (TextView) view.findViewById(R.id.jiesuan_express_line);
        ImageView way = (ImageView) view.findViewById(R.id.jiesuan_express_way);
        TextView number = (TextView) view.findViewById(R.id.jiesuan_express_number);
        TextView jianShu = (TextView) view.findViewById(R.id.jiesuan_express_jianshu);
        TextView tiJi = (TextView) view.findViewById(R.id.jiesuan_express_tiji);
        TextView weight = (TextView) view.findViewById(R.id.jiesuan_express_weight);
        TextView pay = (TextView) view.findViewById(R.id.jiesuan_express_pay);

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
            String status = express.getWay();
            if(status.equals("部分完成"))
            {
                way.setImageResource(R.mipmap.bufenwancheng);
            }
            else if(status.equals("待派送"))
            {
                way.setImageResource(R.mipmap.daipaisong);
            }
            else if(status.equals("待配送"))
            {
                way.setImageResource(R.mipmap.daipeisong);
            }
            else if(status.equals("待确认"))
            {
                way.setImageResource(R.mipmap.daiqueren);
            }
            else if(status.equals("未处理"))
            {
                way.setImageResource(R.mipmap.weichuli);
            }
            else if(status.equals("已处理"))
            {
                way.setImageResource(R.mipmap.yichuli);
            }
            else if(status.equals("已签收"))
            {
                way.setImageResource(R.mipmap.yiqianshou);
            }
            else if(status.equals("已完成"))
            {
                way.setImageResource(R.mipmap.yiwancheng);
            }
            else if(status.equals("运输中"))
            {
                way.setImageResource(R.mipmap.yunshuzhong);
            }
            else if(status.equals("直送"))
            {
                way.setImageResource(R.mipmap.zhisong);
            }
            else if(status.equals("中转"))
            {
                way.setImageResource(R.mipmap.zhongzhuan);
            }
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

        return view;
    }

}
