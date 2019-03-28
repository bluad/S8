package com.mantoo.yican.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mantoo.yican.model.Msg;

import java.util.List;
import com.mantoo.yican.R;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/22.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<Msg> mDatas;

    public ChatAdapter(Context context, List<Msg> datas) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDatas = datas;
    }

    //添加消息显示在RecyclerView中
    public void addItem(Msg msg) {
        mDatas.add(msg);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Msg.TYPE_BLE) {
            View view = mLayoutInflater.inflate(R.layout.item_chat_left, parent, false);
            return new ChatLeftViewHolder(view);
        } else {
            View view = mLayoutInflater.inflate(R.layout.item_chat_right, parent, false);
            return new ChatRightViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Msg msg = mDatas.get(position);
        String time = msg.getTime();
        String content = msg.getContent();
        String name = msg.getName();
        if(holder instanceof ChatLeftViewHolder){
            ((ChatLeftViewHolder) holder).mTvLeftTime.setText(time);
            ((ChatLeftViewHolder) holder).mTvMsgLeft.setText(content);
            ((ChatLeftViewHolder) holder).mTvLeftName.setText(name);
        }else if(holder instanceof ChatRightViewHolder){
            ((ChatRightViewHolder) holder).mTvRightTime.setText(time);
            ((ChatRightViewHolder) holder).mTvMsgRight.setText(content);
            ((ChatRightViewHolder) holder).mTvRightName.setText(name);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class ChatLeftViewHolder extends RecyclerView.ViewHolder {
        TextView mTvLeftTime;
        TextView mTvMsgLeft;
        TextView mTvLeftName;

        ChatLeftViewHolder(View view) {
            super(view);
            mTvLeftTime = view.findViewById(R.id.tv_left_time);
            mTvMsgLeft = view.findViewById(R.id.tv_msg_left);
            mTvLeftName = view.findViewById(R.id.tv_name_left);
            ButterKnife.bind(this, view);
        }
    }

    static class ChatRightViewHolder extends RecyclerView.ViewHolder{
        TextView mTvRightTime;
        TextView mTvMsgRight;
        TextView mTvRightName;

        ChatRightViewHolder(View view) {
            super(view);
            mTvRightTime = view.findViewById(R.id.tv_right_time);
            mTvMsgRight = view.findViewById(R.id.tv_msg_right);
            mTvRightName = view.findViewById(R.id.tv_right_name);
            ButterKnife.bind(this, view);
        }
    }
}