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
import com.mantoo.yican.model.ExpressStatus;

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

        ImageView status = (ImageView) view.findViewById(R.id.completion_express_status);
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
        TextView totalcom = (TextView) view.findViewById(R.id.totalcom);
        LinearLayout completion_Li = (LinearLayout)view.findViewById(R.id.completion_Li);

        if(totalcom != null)
        {
            if(express.getTotalAcount() != null && !express.getTotalAcount().equals("") && (Integer.parseInt(express.getTotalAcount()) > 0))
            {
                totalcom.setText(express.getTotalAcount());
                completion_Li.setVisibility(View.VISIBLE);
            }
        }

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



        if(status != null)
        {
            String flag = express.getStatus();
            if(flag.equals("部分完成"))
            {
                status.setImageResource(R.mipmap.bufenwancheng);
            }
            else if(flag.equals("待派送"))
            {
                status.setImageResource(R.mipmap.daipaisong);
            }
            else if(flag.equals("待配送"))
            {
                status.setImageResource(R.mipmap.daipeisong);
            }
            else if(flag.equals("待确认"))
            {
                status.setImageResource(R.mipmap.daiqueren);
            }
            else if(flag.equals("未处理"))
            {
                status.setImageResource(R.mipmap.weichuli);
            }
            else if(flag.equals("已处理"))
            {
                status.setImageResource(R.mipmap.yichuli);
            }
            else if(flag.equals("已签收"))
            {
                status.setImageResource(R.mipmap.yiqianshou);
            }
            else if(flag.equals("已完成"))
            {
                status.setImageResource(R.mipmap.yiwancheng);
            }
            else if(flag.equals("运输中"))
            {
                status.setImageResource(R.mipmap.yunshuzhong);
            }
            else if(flag.equals("直送"))
            {
                status.setImageResource(R.mipmap.zhisong);
            }
            else if(flag.equals("中转"))
            {
                status.setImageResource(R.mipmap.zhongzhuan);
            }
        }


        return view;
    }

}
