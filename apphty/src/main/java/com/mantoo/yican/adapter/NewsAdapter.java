package com.mantoo.yican.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mantoo.yican.cn.hty.R;
import com.mantoo.yican.model.ExpressStatus;
import com.mantoo.yican.model.News;

import java.util.List;

/**
 * 消息列表
 * Created by Administrator on 2017/10/14.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    private int resourceId;

    public NewsAdapter(Context context, int textViewResourceId,
                       List<News> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        News news = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }

        TextView newsDate = (TextView) view.findViewById(R.id.news_date);
        TextView newsTime = (TextView) view.findViewById(R.id.news_time);
        TextView newstitle = (TextView) view.findViewById(R.id.messages_title);
        TextView newsContent = (TextView) view.findViewById(R.id.messages_content);

        newsDate.setText(news.getNewsDate());
        newsTime.setText(news.getNewsTime());
        newsContent.setText(news.getNewContent());
        newstitle.setText(news.getNewsTitle());

        return view;
    }


}
