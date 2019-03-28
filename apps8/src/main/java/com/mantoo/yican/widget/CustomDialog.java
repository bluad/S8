package com.mantoo.yican.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

import com.mantoo.yican.adapter.VehicleNoAdapter;
import com.mantoo.yican.s8.R;
import com.mantoo.yican.model.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/9.
 */

public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;
        private ListView lvv = null;
        private VehicleNoAdapter myAdapter = null;
        public Builder(Context context) {
            this.context = context;
            myAdapter = new VehicleNoAdapter(context);
        }
        private String selectV;
        private List<Vehicle> datas = new ArrayList();
        private static List<Vehicle> temps = new ArrayList();
        private SearchView mSearchView;

        public Builder setMessage(List<Vehicle> datas) {
            this.datas = datas;
            for(Vehicle v : datas)
            {
                temps.add(v);
            }
            return this;
        }

        public String getSelectV()
        {
            return selectV;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomDialog dialog = new CustomDialog(context,R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_normal_layout, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            // set the confirm button
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
            }
            // set the content message
            if (datas != null) {
                mSearchView = (SearchView) layout.findViewById(R.id.searchView);
                lvv = ((ListView) layout.findViewById(R.id.message));
                myAdapter.setDatas(datas);
                lvv.setAdapter(myAdapter);
                lvv.setTextFilterEnabled(true);
                datas.get(0).setChecked(true);
                selectV = datas.get(0).getTitle();

                mSearchView.setIconifiedByDefault(false);

                // 设置搜索文本监听
                mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    // 当点击搜索按钮时触发该方法
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    // 当搜索内容改变时触发该方法
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        searchItem(newText);
                        myAdapter = new VehicleNoAdapter(context);
                        myAdapter.setDatas(datas);
                        lvv.setAdapter(myAdapter);
                        myAdapter.notifyDataSetChanged();
                        return false;
                    }
                });


                lvv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    int currentNum = -1;
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for(Vehicle person : datas){
                            person.setChecked(false);
                        }

                        if(currentNum == -1){ //选中
                            datas.get(position).setChecked(true);
                            currentNum = position;
                        }else if(currentNum == position){ //同一个item选中变未选中
                            for(Vehicle person : datas){
                                person.setChecked(false);
                            }
                            currentNum = -1;
                        }else if(currentNum != position){ //不是同一个item选中当前的，去除上一个选中的
                            for(Vehicle person : datas){
                                person.setChecked(false);
                            }
                            datas.get(position).setChecked(true);
                            currentNum = position;
                        }
                        selectV = datas.get(position).getTitle();
                        myAdapter.notifyDataSetChanged();
                    }
                });
            } else if (contentView != null) {
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
            }

            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(false);

            return dialog;
        }

        public void searchItem(String name) {
            datas.clear();
            for(Vehicle v : temps)
            {
                datas.add(v);
            }
            List<Vehicle> mSearchList = new ArrayList<Vehicle>();
            for (int i = 0; i < datas.size(); i++) {
                int index = datas.get(i).getTitle().indexOf(name);
                // 存在匹配的数据
                if (index != -1) {
                    mSearchList.add(datas.get(i));
                }
            }
            datas.clear();
            datas = mSearchList;
        }
    }
}