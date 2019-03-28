package com.mantoo.yican;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.hit.library.capture.CaptureActivity;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.R;

public class SequenceActivity extends BaseActivity {

	 String str,str1;
	private com.mantoo.yican.util.MD5Utils md;
	private EditText et;
	ImageView  scan;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//Toast.makeText(getApplicationContext(), "code:"+msg.obj.toString(), Toast.LENGTH_SHORT).show();
			if(msg.obj.toString().equals("2")){
				
				//查看本地是否有信息存储
				if(sp.getString("ports", "") != ""){
//					Toast.makeText(getApplicationContext(), "输入正确即将为您永久关闭此验证", Toast.LENGTH_SHORT).show();
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
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_sequence);
		et = (EditText) findViewById(R.id.ed_text);
		scan = (ImageView) findViewById(R.id.scan);
		
		findViewById(R.id.rl_btn_submit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				str = et.getText().toString();
				str1 = md.encode(str);
				post();
//				new Thread(postThread).start();
			}
		});
		
		scan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent topiecesIntent = new Intent(SequenceActivity.this, CaptureActivity.class);
				startActivityForResult(topiecesIntent, Constants.REQUESTCODE_ADDRESSEE_NEW);
			}
		});
		initScaner();
	}
	private void initScaner() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.zkc.scancode");
		this.registerReceiver(scanBroadcastReceiver, intentFilter);

	}

	// PDA扫描广播
	BroadcastReceiver scanBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (isExist) {
				String text = intent.getExtras().getString("code");
				et.setText(text);
				findViewById(R.id.rl_btn_submit).performClick();
			}
		}
	};
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.REQUESTCODE_ADDRESSEE_NEW && resultCode == RESULT_OK) {
			String code = data.getStringExtra("data");
			et.setText(code);
		}
	}
	
	
	private void post() {
		// TODO Auto-generated method stub
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
					Log.i("MainActivity", "result_code:"+reader.getString("data"));
					Editor editor = sp.edit();
					//editor.putString("ports", "http://192.168.1.103:8080");//接口
					editor.putString("ports", reObject.getString("ports"));//接口
//					editor.putString("province", reObject.getString("province"));//省
//					editor.putString("city", reObject.getString("city"));//市
//					editor.putString("town", reObject.getString("town"));//区
					editor.putString("registerId", reObject.getString("registerId"));
					editor.commit();
					Log.i("SequenceActivity", sp.getString("province", "0")+sp.getString("city", "0")+sp.getString("town", "0"));
					Log.i("MainActivity", "ports:"+reObject.getString("ports"));
					Message msg = new Message();
					msg.obj = reader.getString("result_code");
					handler.sendMessage(msg);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
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

	/*
	 * 访问数据库
	 */
	private Thread postThread = new Thread() {

		public void run() {
			String target = MainUrl.ADDRESS; //要提交的目标地址
			URL url;
			try {
				url = new URL(target);
				HttpURLConnection urlConn = (HttpURLConnection) url
						.openConnection(); // 创建一个HTTP连接
				urlConn.setRequestMethod("POST"); // 指定使用POST请求方式
				urlConn.setDoInput(true); // 向连接中写入数据
				urlConn.setDoOutput(true); // 从连接中读取数据
				urlConn.setUseCaches(false); // 禁止缓存
				urlConn.setInstanceFollowRedirects(true); //自动执行HTTP重定向
				urlConn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded"); // 设置内容类型
				DataOutputStream out = new DataOutputStream(
						urlConn.getOutputStream()); // 获取输出流
				String param = "screatStr="
						+ URLEncoder.encode(str1,
								"UTF-8") + "&serialnumber="
						+ URLEncoder.encode(str, "UTF-8"); //连接要提交的数
				out.writeBytes(param);//将要传递的数据写入数据输出流
				out.flush();  //输出缓存
				out.close(); //关闭数据输出流
				
				if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) { // 判断是否响应成功
					InputStreamReader in = new InputStreamReader(
							urlConn.getInputStream(),"utf-8"); // 获得读取的内容，utf-8获取内容的编码
					BufferedReader buffer = new BufferedReader(in); // 获取输入流对象
					String inputLine = null;
					while ((inputLine = buffer.readLine()) != null) {
						
						Log.d("MainActivity", inputLine + "\n");
						try {
							JSONObject reader = new JSONObject(inputLine);//使用JSONObject解析
							JSONObject reObject = reader.getJSONObject("data");
							Log.i("MainActivity", "result_code:"+reader.getString("data"));
							Editor editor = sp.edit();
							//editor.putString("ports", "http://192.168.1.103:8080");//接口
							editor.putString("ports", reObject.getString("ports"));//接口
							editor.putString("province", reObject.getString("province"));//省
							editor.putString("city", reObject.getString("city"));//市
							editor.putString("town", reObject.getString("town"));//区
							editor.putString("registerId", reObject.getString("registerId"));
							editor.commit();
							Log.i("SequenceActivity", sp.getString("province", "0")+sp.getString("city", "0")+sp.getString("town", "0"));
							Log.i("MainActivity", "ports:"+reObject.getString("ports"));
							Message msg = new Message();
							msg.obj = reader.getString("result_code");
							handler.sendMessage(msg);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Log.i("MainAcitvity", e.getMessage());
						}
					}
					in.close(); //关闭字符输入流
				}
				urlConn.disconnect();  //断开连接
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
	};
}
