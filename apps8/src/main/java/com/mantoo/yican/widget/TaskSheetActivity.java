package com.mantoo.yican.widget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.mantoo.yican.s8.R;

public class TaskSheetActivity extends AppCompatActivity {
   private Toolbar taskSheetToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_sheet);
        //初始化
        initView();
        //监听
        initClick();
    }

    private void initClick() {
        //设置小箭头返回监听
        taskSheetToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void initView() {
        taskSheetToolbar = (Toolbar) findViewById(R.id.tasksheet_tb);
        //设置返回箭头
        taskSheetToolbar.setNavigationIcon(R.mipmap.btn_back);


    }

    //重写menu菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_guide,menu);
        return true;
    }
}
