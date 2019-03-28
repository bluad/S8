package com.mantoo.yican.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.mantoo.yican.cn.hty.R;
import com.mantoo.yican.model.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/11.
 */

public class VehicleNoAdapter extends BaseAdapter {
    private Context mContext;
    private List<Vehicle> mDatas = new ArrayList();

    public VehicleNoAdapter(Context context) {
        mContext = context;
    }

    public void setDatas(List datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Vehicle getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_vehicleno, parent, false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mTvTitle = (TextView) convertView.findViewById(R.id.item_tv);
        mViewHolder.mCbCheckbox = (CheckBox) convertView.findViewById(R.id.item_cb);


        if (mDatas.get(position).isChecked()) {
            mViewHolder.mCbCheckbox.setChecked(true);
        } else {
            mViewHolder.mCbCheckbox.setChecked(false);
        }

        mViewHolder.mTvTitle.setText(mDatas.get(position).getTitle());
        return convertView;
    }


    static class ViewHolder {
        TextView mTvTitle;
        CheckBox mCbCheckbox;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}