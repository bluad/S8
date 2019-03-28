package com.mantoo.yican.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mantoo.yican.R;
import com.mantoo.yican.model.ExpressStatus;

import java.util.List;
import java.util.Locale;

/**
 * 发车列表
 * Created by Administrator on 2017/10/14.
 */

public class FaCheAdapter extends ArrayAdapter<ExpressStatus> {

    private int resourceId;
    private Configuration mconfig;

    public FaCheAdapter(Context context, int textViewResourceId,
                        List<ExpressStatus> objects,Configuration config) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        this.mconfig = config;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        ExpressStatus express = getItem(position);
        if(express.getMissionStatus().equals("3"))
        {
            return 3;
        }
        else
        {
            return 1;
        }
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

        TextView missionNo = (TextView) view.findViewById(R.id.fache_express_missionNo);
        TextView expressDate = (TextView) view.findViewById(R.id.fache_express_date);
        TextView expressTime = (TextView) view.findViewById(R.id.fache_express_time);
        TextView line = (TextView) view.findViewById(R.id.fache_express_line);
        TextView way = (TextView) view.findViewById(R.id.fache_express_way);
        TextView number = (TextView) view.findViewById(R.id.fache_express_number);
        TextView jianShu = (TextView) view.findViewById(R.id.fache_express_jianshu);
        TextView tiJi = (TextView) view.findViewById(R.id.fache_express_tiji);
        TextView weight = (TextView) view.findViewById(R.id.fache_express_weight);
        TextView pay = (TextView) view.findViewById(R.id.fache_express_pay);
        //设置图片
        ImageView img_status = (ImageView) view.findViewById(R.id.img_status);

        if(mconfig.locale.equals(Locale.SIMPLIFIED_CHINESE))
        {
            if(express.getMissionStatus().equals("3"))
            {
                img_status.setImageResource(R.mipmap.wei_dao_da);
            }
            else
            {
                img_status.setImageResource(R.mipmap.wei_qian_shou);
            }

        }
        else
        {
            if(express.getMissionStatus().equals("3"))
            {
                img_status.setImageResource(R.mipmap.wei_dao_da_e);
            }
            else
            {
                img_status.setImageResource(R.mipmap.wei_qian_shou_e);
            }
        }

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
        return view;
    }


}
