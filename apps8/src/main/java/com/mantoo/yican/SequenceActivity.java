package com.mantoo.yican;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import com.mantoo.yican.s8.R;
import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.util.APIService;

public class SequenceActivity extends BaseActivity {

	private String str,str1;
	private com.mantoo.yican.util.MD5Utils md;
	private EditText et;
	private String resultNo; // 扫描返回码
	ImageView  scan;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.obj.toString().equals("2")){
				
				//查看本地是否有信息存储
				if(sp.getString("ports", "") != ""){
					Intent intent = new Intent(SequenceActivity.this,LoginActivity.class);//LoginActivity
					startActivity(intent);
					finish();
				}
			}else if(!(msg.obj.toString().equals("2"))){
				
				Toast.makeText(getApplicationContext(), "输入的序列号不正确!", Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_sequence);
		et = (EditText) findViewById(R.id.ed_text);
		scan = (ImageView) findViewById(R.id.scan);

		Intent intent = getIntent();
		resultNo = intent.getStringExtra("result");
		if (resultNo != null) {
			et.setText(resultNo);
		}
		
		findViewById(R.id.rl_btn_submit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				str = et.getText().toString();
				str1 = md.encode(str);
				post();
			}
		});
		
		scan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//扫二维码
				Intent intent3 = new Intent(SequenceActivity.this, LongnerScanActivity.class);
				intent3.putExtra("activityType", "SequenceActivity");
				startActivity(intent3);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.REQUESTCODE_ADDRESSEE_NEW && resultCode == RESULT_OK) {
			String code = data.getStringExtra("data");
			et.setText(code);
		}
	}
	
	
	private void post() {
		showLoadingDialog();
		AjaxParams params=new AjaxParams();
		params.put("screatStr", str1);
		params.put("serialnumber", str);
		System.out.println(params.toString());
		String target = MainUrl.ADDRESS; //要提交的目标地址
		PDAApplication.http.post(target, params,new AjaxCallBack<Object>() {
			@Override
			public void onSuccess(Object t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				hideLoadingDialog();
				System.out.println(t.toString());
					try {
						JSONObject	reader = new JSONObject(t.toString());
					
						JSONObject reObject = reader.getJSONObject("data");
						Editor editor = sp.edit();
						editor.putString("ports", reObject.getString("ports"));//接口
						editor.putString("registerId", reObject.getString("registerId"));
						editor.commit();

						AppCache.putString(AppCacheKey.http_url, reObject.getString("ports"));
						AppCache.putString(AppCacheKey.registerId, reObject.getString("registerId"));

						Message msg = new Message();
						msg.obj = reader.getString("result_code");
						handler.sendMessage(msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}//使用JSONObject解析
			}
			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
				hideLoadingDialog();
				showToastMsg(R.string.error_network);
			}
		});

	}

}
