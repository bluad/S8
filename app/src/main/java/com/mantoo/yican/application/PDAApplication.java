package com.mantoo.yican.application;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.baidu.mapapi.SDKInitializer;
import com.gprinter.aidl.GpService;
import com.gprinter.io.GpDevice;
import com.gprinter.service.GpPrintService;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.database.ExpressDao;
import com.mantoo.yican.util.ACache;
import com.mantoo.yican.util.FinalHttpLog;
import com.mantoo.yican.util.NetUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.smartdevice.aidl.IZKCService;
import com.wby.EEApplication;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import com.mantoo.yican.R;

/**
 * Created by Administrator on 2017/10/12.
 */

public class PDAApplication extends EEApplication {

    private  static Context context;
    private static ACache aCache;
    private static Handler mHandler;
    private static PDAApplication instance;
    //private LocationClient mLocationClient = null; // 定位类
    //public BMapManager mBMapManager = null;// 百度地图管理类

    private PrinterServiceConnection conn = null;
    public static GpService mGpService = null;

    private Configuration config;
    private DisplayMetrics dm;
    private Resources resources;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //initEngineManager(this);
        initImageLoader(getApplicationContext());
        new ExpressDao(this).deleteData();

        JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this); // 初始化 JPush
        http = new FinalHttpLog();
        http.configUserAgent("baseAndroid/1.0");
        http.configTimeout(10000);
        context=getApplicationContext();

        //初始化XUtils3
        x.Ext.init(this);
        //设置debug模式
        x.Ext.setDebug(true);
        //初始化ACache类
        aCache = ACache.get(this);

        mHandler = new Handler();

        PDAApplication.getACache().put("city", "");
        SharedPreferences sp = getSharedPreferences("cpnfig", MODE_PRIVATE);
        sp.edit().putBoolean("push", true).commit();
        sp.edit().putBoolean("sound", true).commit();

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());

        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        dm = resources.getDisplayMetrics();

        if(AppCache.getString(AppCacheKey.language).equals("cn"))
        {
            config.locale = Locale.SIMPLIFIED_CHINESE;
            resources.updateConfiguration(config, dm);
        }
        else
        {
            config.locale = Locale.US;
            resources.updateConfiguration(config, dm);
        }

        /*mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.setCoorType("bd09ll"); // 设置返回的坐标类型
        mLocationClient.setTimeSpan(1000); // 设置时间
        mLocationClient.setAddrType("city"); // 返回地址类型
        mLocationClient.setServiceMode(LocServiceMode.Background); // 定位方式为：即时定位
        mLocationClient.addRecerveListener(new MyReceiveListenner());
        mLocationClient.start(); // 打开定位
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mLocationClient.getLocation();
            }
        }) {

        }.start();*/

        connection();
        bindService();
    }
    public static ACache getACache() {
        return aCache;
    }
    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        return mHandler;
    }

    public static OkHttpClient defaultOkHttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.writeTimeout(30 * 1000, TimeUnit.MILLISECONDS);
        client.readTimeout(20 * 1000, TimeUnit.MILLISECONDS);
        client.connectTimeout(15 * 1000, TimeUnit.MILLISECONDS);
        //设置缓存路径
        File httpCacheDirectory = new File(instance.getCacheDir(), "okhttpCache");
        //设置缓存 10M
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
        client.cache(cache);
        //设置拦截器
        client.addInterceptor(LoggingInterceptor);
//        client.addInterceptor(new ChuckInterceptor(application));
        client.addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
        client.addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
        return client.build();
    }

    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            boolean netWorkConection = NetUtils.hasNetWorkConection(PDAApplication.getInstance());
            Request request = chain.request();
            if (!netWorkConection) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            Response response = chain.proceed(request);
            if (netWorkConection) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                response.newBuilder()
                        .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .header("Cache-Control", cacheControl)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 7;
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    };

    private static final Interceptor LoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            Response response = chain.proceed(chain.request());
            long t2 = System.nanoTime();
            okhttp3.MediaType mediaType = response.body().contentType();
            String content = response.body().string();
//            KLog.i(TAG, "-----LoggingInterceptor----- :\nrequest url:" + request.url() + "\ntime:" + (t2 - t1) / 1e6d + "\nbody:" + content + "\n");
            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, content))
                    .build();
        }
    };

    public void bindService() {
        Intent intent = new Intent("com.zkc.aidl.all");
        intent.setPackage("com.smartdevice.aidl");
        bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);
    }
    public boolean bindSuccessFlag = false;
    public static IZKCService mIzkcService;
    public static String MODULE_FLAG = "module_flag";
    public static int module_flag = 0;
    public static int DEVICE_MODEL = 0;
    private ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
//			Log.e("client", "onServiceDisconnected");
            mIzkcService = null;
            bindSuccessFlag = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//			Log.e("client", "onServiceConnected");
            mIzkcService = IZKCService.Stub.asInterface(service);
            if(mIzkcService!=null){
                try {
                    DEVICE_MODEL = mIzkcService.getDeviceModel();
                    mIzkcService.setModuleFlag(module_flag);
                    if(module_flag==3){
                        mIzkcService.openBackLight(1);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                bindSuccessFlag = true;
            }
        }
    };
    private void connection() {
        conn = new PrinterServiceConnection();
        Intent intent = new Intent(this, GpPrintService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
    }

    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mGpService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
        }
    }

    public boolean[] getConnectState() {
        boolean[] state = new boolean[GpPrintService.MAX_PRINTER_CNT];
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            state[i] = false;
        }
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            try {
                if (mGpService.getPrinterConnectStatus(i) == GpDevice.STATE_CONNECTED) {
                    state[i] = true;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return state;
    }

    /*public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(new MyGeneralListener())) {
            Toast.makeText(PDAApplication.getInstance().getApplicationContext(), "BMapManager  初始化错误!",
                    Toast.LENGTH_LONG).show();
        }

    }*/

  /*  // 常用事件监听，用来处理通常的网络错误，授权验证错误等
    public static class MyGeneralListener implements MKGeneralListener {

        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(PDAApplication.getInstance().getApplicationContext(), R.string.error_network, Toast.LENGTH_LONG)
                        .show();
            } else if (iError == MKEvent.ERROR_NETWORK_DATA) {
            }
            // ...
        }

        @Override
        public void onGetPermissionState(int arg0) {
            // TODO Auto-generated method stub

        }
    }*/

    public static PDAApplication getInstance() {
        return instance;
    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800)
                // max width, max height，即保存的每个缓存文件的最大长宽
                // Can slow ImageLoader, use it carefully (Better don't use
                // it)/设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)
                // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                // You can pass your own memory cache
                // implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024).discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO).discCacheFileCount(100)
                // 缓存的文件数量
                // 自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
                .writeDebugLogs() // Remove for release app
                .build();// 开始构建
        // Initialize ImageLoader with configuration.

        ImageLoader.getInstance().init(config);// 全局初始化此配置
    }

    public DisplayImageOptions getDisplayImageOptions() {
        DisplayImageOptions mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showStubImage(android.R.drawable.ic_menu_rotate).showImageForEmptyUri(R.mipmap.default_error)
                .showImageOnFail(R.mipmap.default_error)
                // .cacheInMemory(true)
                .cacheOnDisc(true).displayer(new FadeInBitmapDisplayer(400))
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565).build();

        return mDisplayImageOptions;
    }

    /**
     * 贵州省
     *
     * @return
     */
    public static int getProvice() {
        return 22;
    }

    /**
     * 贵阳市
     *
     * @return
     */
    public static int getCity() {
        return 6;
    }

    /**
     * 区
     *
     * @return
     */
    public static int getDistrict() {
        return 0;
    }

    // private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.myjpushexample.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    // public void registerMessageReceiver() {
    // mMessageReceiver = new MessageReceiver();
    // IntentFilter filter = new IntentFilter();
    // filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
    // filter.addAction(MESSAGE_RECEIVED_ACTION);
    // registerReceiver(mMessageReceiver, filter);
    // }
    // public class MessageReceiver extends BroadcastReceiver {
    // @Override
    // public void onReceive(Context context, Intent intent) {
    // mLocationClient = new LocationClient(context);
    // mLocationClient.setCoorType("bd09ll"); // 设置返回的坐标类型
    // mLocationClient.setTimeSpan(1000); // 设置时间
    // mLocationClient.setAddrType("city"); // 返回地址类型
    // mLocationClient.setServiceMode(LocServiceMode.Background); // 定位方式为：即时定位
    // mLocationClient.addRecerveListener(new MyReceiveListenner());
    // mLocationClient.start(); // 打开定位
    // new Thread(new Runnable() {
    //
    // @Override
    // public void run() {
    // try {
    // Thread.sleep(1000 * 3);
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }
    // mLocationClient.getLocation();
    // }
    // }) {
    //
    // }.start();
    // }
    // }
    /**
     * 定位SDK监听函数
     */
   /* private class MyReceiveListenner implements ReceiveListener {
        @Override
        public void onReceive(String strData) {
            System.out.println(strData);
            if (strData == null) {
                return;
            }
            String mData = strData.trim();
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(mData);
                JSONObject jsonjingweidu = jsonObject.getJSONObject("content").getJSONObject("point");
                String longitude = jsonjingweidu.getString("y");
                String latitude = jsonjingweidu.getString("x");
                AppCache.putString("Location_latitude", latitude);
                AppCache.putString("Location_longitude", longitude);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }*/

    private List<Activity> activityList = new LinkedList<Activity>();

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 遍历所有Activity并finish
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }

}
