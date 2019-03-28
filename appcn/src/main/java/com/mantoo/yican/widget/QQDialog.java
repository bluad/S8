package com.mantoo.yican.widget;

import com.mantoo.yican.BaseActivity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import com.mantoo.yican.cn.R;


/**
 * Created by Administrator on 2017/10/12.
 */

public class QQDialog extends Dialog implements android.view.View.OnClickListener {

    private Button1Listener listener1;
    private Button2Listener listener2;

    public QQDialog(Context context, String button1, String button2, Button1Listener listener1,
                    Button2Listener listener2) {
        super(context, R.style.transparentFrameWindowStyle);
        this.listener1 = listener1;
        this.listener2 = listener2;
        initView(context, button1, button2);
    }

    private void initView(Context context, String button1, String button2) {
        View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        Button pic = (Button) view.findViewById(R.id.pic);
        pic.setOnClickListener(this);
        pic.setText(button1);
        Button takepic = (Button) view.findViewById(R.id.takepic);
        takepic.setOnClickListener(this);
        takepic.setText(button2);
        view.findViewById(R.id.cancel).setOnClickListener(this);
        this.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        Window window = getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = ((BaseActivity) context).getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        this.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        this.setCanceledOnTouchOutside(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pic:
                if (listener1 != null) {
                    listener1.onButton1();
                    dismiss();
                }
                break;
            case R.id.takepic:
                if (listener2 != null) {
                    listener2.onButton2();
                    dismiss();
                }
                break;
            case R.id.cancel:
                dismiss();
                break;
        }

    }

    public interface Button1Listener {
        public void onButton1();
    }

    public interface Button2Listener {
        public void onButton2();
    }

}
