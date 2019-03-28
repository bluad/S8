package com.mantoo.yican.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mantoo.yican.cn.hty.R;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.model.GoodInfo;
import com.mantoo.yican.model.TaskInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 输入框adapter
 * Created by Administrator on 2017/10/14.
 */

public class GoodInfoAdapter extends ArrayAdapter<GoodInfo>{

    private int resourceId;
    private Context context;

    public GoodInfoAdapter(Context context, int textViewResourceId,
                           List<GoodInfo> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final GoodInfo goodInfo = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }

        TextView goodOrderNo = (TextView) view.findViewById(R.id.good_info_order_no);
        TextView goodCustomerName = (TextView) view.findViewById(R.id.good_info_customer_name);
        EditText goodInputNumbers = (EditText) view.findViewById(R.id.good_info_input_numbers);

        goodInputNumbers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                MainUrl.goodMap.put(goodInfo.getOrderNumber(),str);

            }
        });

        goodOrderNo.setText(goodInfo.getOrderNumber());
        goodCustomerName.setText(goodInfo.getCustomerName());

        return view;
    }

}
