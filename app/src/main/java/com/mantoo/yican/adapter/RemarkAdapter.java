package com.mantoo.yican.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mantoo.yican.R;
import com.mantoo.yican.model.Remark;

import java.util.List;

/**
 * 发车列表
 * Created by Administrator on 2017/10/14.
 */

public class RemarkAdapter extends ArrayAdapter<Remark> {

    private int resourceId;

    public RemarkAdapter(Context context, int textViewResourceId,
                         List<Remark> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Remark remark = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }

        TextView remark_time = (TextView) view.findViewById(R.id.remark_time);
        TextView remark_name = (TextView) view.findViewById(R.id.remark_name);
        TextView remark_content = (TextView) view.findViewById(R.id.remark_content);

        remark_time.setText(remark.getCreatedAt());
        remark_name.setText(remark.getName());
        remark_content.setText(remark.getNote());

        return view;
    }


}
