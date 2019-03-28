package com.mantoo.yican;

import com.mantoo.yican.model.AddresseeData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mantoo.yican.s8.R;
/**
 * Created by Administrator on 2017/10/12.
 */

public class ExpressinfoDetailsActivity extends BaseActivity implements OnClickListener {


    TextView sender, sendphone, address, huopin;
    TextView resender, rephone, readdress, note;
    TextView myexpress;
    LinearLayout confirm, wuliu;

    String express;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expressinfodetais);
        initView();
        initData();

    }

    private void initData() {
        // TODO Auto-generated method stub
        Intent intent = getIntent();
        AddresseeData mDate = (AddresseeData) getIntent().getSerializableExtra("addData");
        express = intent.getStringExtra("express");
        myexpress.setText(express);
        if (intent.getStringExtra("flag").equals("0")) {
            wuliu.setVisibility(View.VISIBLE);
        }
        sender.setText(mDate.getSender());
        sendphone.setText(mDate.getSendphone());
        address.setText(mDate.getSendaddress());
        readdress.setText(mDate.getReceiveaddress());
        resender.setText(mDate.getReceiver());
        rephone.setText(mDate.getReceivephone());
        huopin.setText(mDate.getRemark());
        note.setText(mDate.getNote());

//		getExpressinfo(express);
    }

    private void initView() {
        // 点击这个Activity的边缘不会消失
        this.setFinishOnTouchOutside(false);
        myexpress = (TextView) findViewById(R.id.main_express_no);
        sender = (TextView) findViewById(R.id.sender);
        resender = (TextView) findViewById(R.id.resender);
        sendphone = (TextView) findViewById(R.id.sendphone);
        rephone = (TextView) findViewById(R.id.rephone);
        sender = (TextView) findViewById(R.id.sender);
        address = (TextView) findViewById(R.id.address);
        readdress = (TextView) findViewById(R.id.readdress);
        huopin = (TextView) findViewById(R.id.huopin);
        note = (TextView) findViewById(R.id.note);
        confirm = (LinearLayout) findViewById(R.id.confirm);
        wuliu = (LinearLayout) findViewById(R.id.wuliu);
        confirm.setOnClickListener(this);
        wuliu.setOnClickListener(this);
        sendphone.setOnClickListener(this);
        rephone.setOnClickListener(this);
    }

   /* @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.confirm:
                finish();
                break;
            case R.id.sendphone:
                String str=Html.fromHtml("<u>" + sendphone.getText().toString() + "</u>").toString();
                Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + str));
                startActivity(intent1);
                break;
            case R.id.rephone:
                String str1=Html.fromHtml("<u>" + rephone.getText().toString() + "</u>").toString();
                Intent intent2 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + str1));
                startActivity(intent2);
                break;
            case R.id.wuliu:
                Intent intent = new Intent(ExpressinfoDetailsActivity.this, CaiWuActivity.class);
                intent.putExtra("express", express);
                startActivity(intent);
                finish();

                break;

            default:
                break;
        }

    }*/



    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

    }

}
