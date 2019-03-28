package com.mantoo.yican;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.service.BlueToothService;
import com.mantoo.yican.service.LocationService;
import com.mantoo.yican.util.Intents;
import com.mantoo.yican.widget.BannerImageLoader;
import com.mantoo.yican.widget.NumImageView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoaderInterface;


public class MainActivity extends BaseActivity implements OnClickListener {
	private View headerView = null;
	private Banner banner;
	private List li = new ArrayList<String>();
	private List<String> title = new ArrayList<String>();

	private Configuration config;
	private Resources resources;

	// 单号
	private EditText expressEdittext;
	// 查询
	private ImageView queryView, main_tongzhi_message;
	private ImageView scan;
	private boolean isExist = true;
	private String isActivity = "true";
	/*
	 * 定位
	 */
	private TextView locationCity;
	private LocationManager locationManager;
	private Location location;

	private long exitTime;
	private TextView notice;
	private TextView tips;

	private EditText main_express_no;

	private String resultNo; // 扫描返回码


	private NumImageView jiedanNumber; //接单
	private NumImageView tihuoNumber; //提货
	private NumImageView daohuoNumber; //到货
	private NumImageView wanchenNumber; //完成

	private NumImageView wentijiannumber; //问题件
	//private NumImageView jiesuannumber; //结算
	private NumImageView tongjinumber; //统计
	private NumImageView caiwunumber; //财务

	private NumImageView unreadMessage; //未读消息


	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals("com.mantoo.yican.service.CHANGE_STATUS")) {
				queryNumber();
			}
		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);

		resources = getResources();// 获得res资源对象
		config = resources.getConfiguration();// 获得设置对象

		jiedanNumber = (NumImageView) findViewById(R.id.jiedan_number);
		tihuoNumber = (NumImageView) findViewById(R.id.tihuo_number);
		daohuoNumber = (NumImageView) findViewById(R.id.daohuo_number);
		wanchenNumber = (NumImageView) findViewById(R.id.wanchen_number);

		wentijiannumber = (NumImageView) findViewById(R.id.wentidan_number);
		//jiesuannumber = (NumImageView) findViewById(R.id.jiesuan_number);
		tongjinumber = (NumImageView) findViewById(R.id.tongji_number);
		caiwunumber = (NumImageView) findViewById(R.id.caiwu_number);
		unreadMessage = (NumImageView) findViewById(R.id.unread_message);


		jiedanNumber.setVisibility(View.GONE);
		tihuoNumber.setVisibility(View.GONE);
		daohuoNumber.setVisibility(View.GONE);
		wanchenNumber.setVisibility(View.GONE);

		wentijiannumber.setVisibility(View.GONE);
		unreadMessage.setVisibility(View.GONE);
		tongjinumber.setVisibility(View.GONE);
		caiwunumber.setVisibility(View.GONE);

		initView(this);
		getDD();
		queryNumber();

		IntentFilter filter = new IntentFilter();
		filter.addAction("com.mantoo.yican.service.CHANGE_STATUS");
		registerReceiver(mReceiver, filter);

		Intent intent = getIntent();
		resultNo = intent.getStringExtra("result");
		if (resultNo != null) {
			Intent intent11 = new Intent(MainActivity.this, TongJiActivity.class);
			intent11.putExtra("result", resultNo.toString());
			startActivity(intent11);
			finish();
			return;
		}

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		// 获取location对象
		location = getBestLocation(locationManager);

		updateView(location);

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				3000, 8, new LocationListener() {

					@Override
					public void onStatusChanged(String provider, int status,
												Bundle extras) {
					}

					@Override
					public void onProviderEnabled(String provider) {
						if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
							return;
						}
						updateView(locationManager
								.getLastKnownLocation(provider));
					}

					@Override
					public void onProviderDisabled(String provider) {
						updateView(null);
					}

					@Override
					public void onLocationChanged(Location location) {
						location = getBestLocation(locationManager);// 每次都去获取GPS_PROVIDER优先的location对象
						updateView(location);
					}
				});

		main_express_no.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					String searchContext = main_express_no.getText().toString().trim();
					Intent intent = new Intent(MainActivity.this, TongJiActivity.class);
					intent.putExtra("result", searchContext);
					startActivity(intent);
					finish();
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * 获取location对象，优先以GPS_PROVIDER获取location对象，当以GPS_PROVIDER获取到的locaiton为null时
	 * ，则以NETWORK_PROVIDER获取location对象，这样可保证在室内开启网络连接的状态下获取到的location对象不为空
	 *
	 * @param locationManager
	 * @return
	 */
	private Location getBestLocation(LocationManager locationManager) {
		Location result = null;
		if (locationManager != null) {
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			}
			result = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (result != null) {
				return result;
			} else {
				result = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				return result;
			}
		}
		return result;
	}

	private void updateView(Location location) {
		if (location != null) {
			//"经度" location.getLongitude() " 纬度"location.getLatitude()
			String mcityName = null;
			Geocoder geocoder = new Geocoder(getApplicationContext());
			List<Address> addList = null;// 解析经纬度
			try {
				addList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (addList != null && addList.size() > 0) {
				for (int i = 0; i < addList.size(); i++) {
					Address add = addList.get(i);
					mcityName = add.getLocality();
					locationCity.setText(mcityName);
				}}
		} else {
			if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
			{
				locationCity.setText("定位失败");
			}
			else
			{
				locationCity.setText("Positioning failed");
			}
		}
	}


	protected void initView(Context context) {

		main_express_no = (EditText) findViewById(R.id.main_express_no);
		scan = (ImageView) findViewById(R.id.scan_strip);
		expressEdittext = (EditText) findViewById(R.id.main_express_no);
		scan.setOnClickListener(this);

		((TextView) findViewById(R.id.index_text))
				.setText(AppCache.getString(AppCacheKey.name));

		notice = (TextView) findViewById(R.id.tv_notice_edit);// 用于显示最新消息的text
		tips = (TextView) findViewById(R.id.tips);
		if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
		{
			notice.setText(sp.getString("content", "当前沒有最新消息"));
		}
		else
		{
			notice.setText(sp.getString("content", "There is no current news"));
		}

		locationCity = (TextView) findViewById(R.id.tv_location);// 用于显示当前地址

		findViewById(R.id.main_tongzhi_message).setOnClickListener(this);
		findViewById(R.id.ll_notice).setOnClickListener(this);
		findViewById(R.id.task).setOnClickListener(this);
		findViewById(R.id.paidan).setOnClickListener(this);
		findViewById(R.id.fache).setOnClickListener(this);
		findViewById(R.id.completion).setOnClickListener(this);
		findViewById(R.id.problem).setOnClickListener(this);
		findViewById(R.id.jiesuan).setOnClickListener(this);
		findViewById(R.id.tongji).setOnClickListener(this);
		findViewById(R.id.paidan).setOnClickListener(this);
		findViewById(R.id.scan_strip).setOnClickListener(this);
		findViewById(R.id.query).setOnClickListener(this);
		findViewById(R.id.img).setOnClickListener(this);
		findViewById(R.id.caiwu).setOnClickListener(this);
		expressEdittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					query(expressEdittext.getText().toString());
					return true;
				}
				return false;
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 个人中心
		case R.id.img:
			Intent intent = new Intent(MainActivity.this, PersionalInfoActivity.class);
			startActivity(intent);
			break;
		// 接单
		case R.id.paidan:
			Intents.getIntents().Intent(MainActivity.this, PaiDanActivity.class);
			finish();
			break;
		// 提货
		case R.id.task:
			Intents.getIntents().Intent(MainActivity.this, TaskActivity.class);
			finish();
			break;
		// 到货
		case R.id.fache:
			Intents.getIntents().Intent(MainActivity.this, FaCheActivity.class);
			finish();
			break;
		// 完成
		case R.id.completion:
			Intents.getIntents().Intent(MainActivity.this, CompletionActivity.class);
			finish();
			break;
		// 异常
		case R.id.problem:
			Intents.getIntents().Intent(MainActivity.this, YiChangActivity.class);
			break;
		// 消息
		case R.id.jiesuan:
			Intents.getIntents().Intent(MainActivity.this, MessagingActivity.class);
			finish();
			break;
		// 统计
		case R.id.tongji:
			Intents.getIntents().Intent(MainActivity.this, TongJiActivity.class);
			break;
		// 财务
		case R.id.caiwu:
			Intents.getIntents().Intent(MainActivity.this, CaiWuActivity.class);
			break;
		// 消息
		case R.id.ll_notice:
			Intent intent2 = new Intent(MainActivity.this, NewsActivity.class);
			startActivity(intent2);
			break;
		case R.id.scan_strip:
			Intent intent3 = new Intent(MainActivity.this, LongnerScanActivity.class);
			intent3.putExtra("activityType", "MainActivity");
			startActivity(intent3);
			break;
		// 查询
		case R.id.query:
			query(expressEdittext.getText().toString().trim());
			break;
		// 消息中心
			case R.id.main_tongzhi_message:
				Intents.getIntents().Intent(MainActivity.this, NewsActivity.class);
				break;
		}

	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.REQUESTCODE_ADDRESSEE_NEW && resultCode == RESULT_OK) {
			String code = data.getStringExtra("data");
			expressEdittext.setText(code);
			query(code);
		}
	}


	public void showStringToastMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			exitApp();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

    protected void exitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
            {
                showStringToastMsg("再按一次退出应用！");
            }
            else
            {
                showStringToastMsg("Press again to exit the app！");
            }
            exitTime = System.currentTimeMillis();
        } else {
            PDAApplication.getInstance().exit();
        }
    }

	@Override
	protected void onDestroy() {
		isExist = false;
		Intent intent = new Intent(this, BlueToothService.class);
		stopService(intent);

		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		isExist = true;
		getNumber();
		super.onStart();
	}

	@Override
	protected void onResume() {
		isExist = true;
		super.onResume();
	}

	@Override
	protected void onPause() {
		isExist = false;
		super.onPause();
	}

	@Override
	public void showNext() {
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void showPre() {

	}

	// 获取待收件，已收件等件数
	private void getNumber() {
		//请求开始
		AjaxParams params=new AjaxParams();
		params.put("driverid", AppCache.getString(AppCacheKey.driverid));
		String url = MainUrl.URL + MainUrl.GETSTATISTICS;
		System.out.println(params+url);
		showLoadingDialog();
		PDAApplication.http.configTimeout(8000);
		PDAApplication.http.post(url,params,new AjaxCallBack<Object>() {
			public void onSuccess(Object t) {
				hideLoadingDialog();
				JSONObject object;
				try {
					object = new JSONObject(t.toString());
					if (object.optInt(Constants.RESULT_CODE) == 0) {
						JSONObject data = object.optJSONObject(Constants.DATA);

						if(!data.optString("dispatchNumber").equals("0"))
						{
							jiedanNumber.setNum(Integer.parseInt(data.optString("dispatchNumber")));
							jiedanNumber.bringToFront();
							jiedanNumber.setVisibility(View.VISIBLE);
						}
						if(!data.optString("deliverNumber").equals("0"))
						{
							tihuoNumber.setNum(Integer.parseInt(data.optString("deliverNumber")));
							tihuoNumber.bringToFront();
							tihuoNumber.setVisibility(View.VISIBLE);
						}
						if(!data.optString("transportNumber").equals("0"))
						{
							daohuoNumber.setNum(Integer.parseInt(data.optString("transportNumber")));
							daohuoNumber.bringToFront();
							daohuoNumber.setVisibility(View.VISIBLE);
						}
						if(!data.optString("finishNumber").equals("0"))
						{
							wanchenNumber.setNum(Integer.parseInt(data.optString("finishNumber")));
							wanchenNumber.bringToFront();
							wanchenNumber.setVisibility(View.VISIBLE);
						}
					}
					else
					{
						/*if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
						{
							showStringToastMsg("无数据！");
						}
						else
						{
							showStringToastMsg("No Data！");
						}*/
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			};
			public void onFailure(Throwable t, String strMsg) {
				hideLoadingDialog();
				showToastMsg(R.string.error_network);
			};
		} );
		//请求结束
	}


	// 设置Banner
	private void getss() {
		banner = (Banner) findViewById(R.id.banner);
		banner.setImageLoader((ImageLoaderInterface) new BannerImageLoader());
		banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
		banner.setImages(li);
		banner.setBannerAnimation(Transformer.ZoomOut);
		banner.isAutoPlay(true);
		banner.setDelayTime(3000);
		banner.setIndicatorGravity(BannerConfig.CENTER);
		banner.start();
	}

	// 获取网络图片
	private void getDD() {
		AjaxParams params = new AjaxParams();
		params.put("driverid", AppCache.getString(AppCacheKey.driverid));
		String url = MainUrl.URL + MainUrl.getPictures;
		PDAApplication.http.post(url, params, new AjaxCallBack<Object>() {
			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				JSONObject object;
				try {
					object = new JSONObject(t.toString());
					JSONObject data = object.getJSONObject("data");
					JSONArray ob = data.getJSONArray("pictureList");
					for (int i = 0; i < ob.length(); i++) {
						JSONObject o = ob.getJSONObject(i);
						li.add(o.getString("pictureLink"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				getss();

			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
			}
		});
	}

	// 查询数量
	public void queryNumber() {
		AjaxParams params = new AjaxParams();
		params.put("driverId", AppCache.getString(AppCacheKey.driverid));
		String url = MainUrl.URL + MainUrl.GETUNREANUMBER;
		PDAApplication.http.post(url, params, new AjaxCallBack<Object>() {
			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				JSONObject object;
				try {
					object = new JSONObject(t.toString());
					String data = object.getString("data");
					if(data != null && !data.equals(""))
					{
						int number = 0;
						try
						{
							number = Integer.parseInt(data);
						}
						catch(Exception e)
						{
							number = 0;
						}
						if(number > 0)
						{
							unreadMessage.setNum(number);
							unreadMessage.bringToFront();
							unreadMessage.setVisibility(View.VISIBLE);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
			}
		});
	}

}
