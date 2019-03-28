package com.mantoo.yican;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mantoo.yican.adapter.NewsPicAdapter;
import com.mantoo.yican.adapter.TablePicAdapter;
import com.mantoo.yican.s8.R;
import com.mantoo.yican.model.News;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/12.
 */

public class NewsContentActivity extends BaseActivity implements View.OnClickListener {

    private News news;
    private List<Bitmap> picList = new ArrayList<Bitmap>();
    private ListView list_pic_news;
    private TextView contentnews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        findViewById(R.id.messages_content_return).setOnClickListener(this);

        contentnews = (TextView)findViewById(R.id.news_contents);
        list_pic_news = (ListView)findViewById(R.id.list_pic_news);

        Intent intent = getIntent();
        news = (News) intent.getSerializableExtra("news");
        contentnews.setText(news.getNewContent());
        final List<String> list = news.getPictureList();
        showLoadingDialog();
        //异步展示图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    Bitmap bitmap = null;
                    try
                    {
                        bitmap = getBitmap(list.get(i));
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    picList.add(bitmap);
                }

                Message message = new Message();
                message.obj = picList;
                handler.sendMessage(message);
            }
        }).start();
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Bitmap> lists = (List<Bitmap>)msg.obj;
            NewsPicAdapter adapter = new NewsPicAdapter(NewsContentActivity.this, R.layout.item_listview_news_pic, lists);
            list_pic_news.setAdapter(adapter);
            setListViewHeightBasedOnChildren(list_pic_news);
            list_pic_news.setVisibility(View.VISIBLE);
            hideLoadingDialog();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.messages_content_return:
                Intent intent = new Intent(NewsContentActivity.this, NewsActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(NewsContentActivity.this, NewsActivity.class);
            startActivity(intent);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

    public Bitmap getBitmap(String path) throws IOException {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
