package com.mantoo.yican;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.mantoo.yican.widget.SelectDialog;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/10/12.
 */

public class BaiduMapActivity extends BaseActivity implements View.OnClickListener{

    private MapView mMapView = null;
    private RoutePlanSearch mSearch;
    private BaiduMap bdMap;
    private Button select_map;

    // 经纬度坐标
    String startLng = "";
    String startLat = "";
    String endLng = "";
    String endLat = "";

    String activity = "";
    String missionNo = "";
    String type = "";

    private Configuration config;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_map);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        findViewById(R.id.baidumap_return).setOnClickListener(this);
        select_map = (Button) findViewById(R.id.select_map);
        findViewById(R.id.select_map).setOnClickListener(this);

        Intent intent = getIntent();
        missionNo = intent.getStringExtra("mission_no");
        type = intent.getStringExtra("type");
        activity = intent.getStringExtra("activity");
        startLng = intent.getStringExtra("startLng");
        startLat = intent.getStringExtra("startLat");
        endLng = intent.getStringExtra("endLng");
        endLat = intent.getStringExtra("endLat");
        if(startLng == null)
        {
            startLng = "116.205348";
        }
        if(startLat == null)
        {
            startLat = "39.995906";
        }
        if(endLng == null)
        {
            endLng = "116.70495";
        }
        if(endLat == null)
        {
            endLat = "39.859572";
        }

        mMapView = (MapView) findViewById(R.id.bmapView);
        mSearch = RoutePlanSearch.newInstance();
        bdMap = mMapView.getMap();

        OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {

            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
                //获取驾车线路规划结果
                bdMap.clear();
                if (drivingRouteResult == null
                        || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(BaiduMapActivity.this, "Sorry!", Toast.LENGTH_SHORT).show();
                }
                if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    return;
                }
                if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(bdMap);
                    drivingRouteOverlay.setData(drivingRouteResult.getRouteLines().get(0));

                    bdMap.setOnMarkerClickListener(drivingRouteOverlay);
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                }
            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        };

        mSearch.setOnGetRoutePlanResultListener(listener);
        // 120.401779,31.578329
        //119.97934,31.738291
        LatLng from = new LatLng(Double.parseDouble(startLat),
                Double.parseDouble(startLng));
        LatLng dest = new LatLng(Double.parseDouble(endLat),
                Double.parseDouble(endLng));

        PlanNode stNode = PlanNode.withLocation(from);
        PlanNode enNode = PlanNode.withLocation(dest);
        mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.baidumap_return:
                if(activity.equals("CaiWuInfoActivity"))
                {
                    Intent intent = new Intent(BaiduMapActivity.this, CaiWuInfoActivity.class);
                    intent.putExtra("mission_no", missionNo);
                    startActivity(intent);
                    finish();
                }
                if(activity.equals("CompletionInfoActivity"))
                {
                    Intent intent = new Intent(BaiduMapActivity.this, CompletionInfoActivity.class);
                    intent.putExtra("mission_no", missionNo);
                    startActivity(intent);
                    finish();
                }
                if(activity.equals("FaCheInfoActivity"))
                {
                    Intent intent = new Intent(BaiduMapActivity.this, FaCheInfoActivity.class);
                    intent.putExtra("mission_no", missionNo);
                    intent.putExtra("type", type);
                    startActivity(intent);
                    finish();
                }
                if(activity.equals("JieSuanInfoActivity"))
                {
                    Intent intent = new Intent(BaiduMapActivity.this, JieSuanInfoActivity.class);
                    intent.putExtra("mission_no", missionNo);
                    startActivity(intent);
                    finish();
                }
                if(activity.equals("TaskExpressInfoActivity"))
                {
                    Intent intent = new Intent(BaiduMapActivity.this, TaskExpressInfoActivity.class);
                    intent.putExtra("mission_no", missionNo);
                    startActivity(intent);
                    finish();
                }
                if(activity.equals("TaskInfoActivity"))
                {
                    Intent intent = new Intent(BaiduMapActivity.this, TaskInfoActivity.class);
                    intent.putExtra("mission_no", missionNo);
                    startActivity(intent);
                    finish();
                }
                if(activity.equals("TongJiInfoActivity"))
                {
                    Intent intent = new Intent(BaiduMapActivity.this, TongJiInfoActivity.class);
                    intent.putExtra("mission_no", missionNo);
                    startActivity(intent);
                    finish();
                }

                break;

            case R.id.select_map:
                List<String> names = new ArrayList<>();
                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                {
                    names.add("百度地图");
                    names.add("高德地图");
                    names.add("谷歌地图");
                }
                else
                {
                    names.add("Baidu map");
                    names.add("Auto Navi Map");
                    names.add("Google Map");
                }
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                if(!isAvilible(getApplicationContext(),"com.baidu.BaiduMap"))
                                {
                                    if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                    {
                                        showStringToastMsg("您尚未安装百度地图!");
                                    }
                                    else
                                    {
                                        showStringToastMsg("You have not installed Baidu map!");
                                    }
                                    return;
                                }
                                String url1 ="intent://map/direction?origin=latlng:"+startLat+","+startLng+
                                        "|name:&destination=latlng:"+endLat+","+endLng+
                                        "|name:&mode=driving&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
                                try
                                {
                                    Intent intent1 = Intent.getIntent(url1);
                                    startActivity(intent1);
                                } catch (URISyntaxException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                if(!isAvilible(getApplicationContext(),"com.autonavi.minimap"))
                                {
                                    if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                    {
                                        showStringToastMsg("您尚未高德百度地图!");
                                    }
                                    else
                                    {
                                        showStringToastMsg("You have not installed Auto Navi Map!");
                                    }
                                    return;
                                }
                                try
                                {
                                    String url2 = "androidamap://route?sourceApplication=S8&slat="+startLat+"&slon="+startLng+
                                            "&sname=&dlat="+endLat+"&dlon="+endLng+"&dname=&dev=0&m=0&t=1&showType=1";
                                    Intent intent2 = Intent.getIntent(url2);
                                    startActivity(intent2);
                                }
                                catch (Exception e)
                                {e.printStackTrace(); }
                                break;
                            default:
                                if(!isAvilible(getApplicationContext(),"com.google.android.apps.maps"))
                                {
                                    if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                    {
                                        showStringToastMsg("您尚未安装谷歌地图!");
                                    }
                                    else
                                    {
                                        showStringToastMsg("You have not installed Google Map!");
                                    }
                                    return;
                                }
                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://maps.google.com/maps?"
                                                + "saddr="+ startLat+ "," + startLng
                                                + "&daddr=" + endLat+ "," + endLng
                                                +"&avoid=highway"
                                                +"&language=zh-CN")
                                );

                                intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
                                startActivity(intent);
                                break;
                        }

                    }
                }, names);
                break;
        }
    }


    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearch.destroy();
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if(activity.equals("CaiWuInfoActivity"))
            {
                Intent intent = new Intent(BaiduMapActivity.this, CaiWuInfoActivity.class);
                intent.putExtra("mission_no", missionNo);
                startActivity(intent);
                finish();
            }
            if(activity.equals("CompletionInfoActivity"))
            {
                Intent intent = new Intent(BaiduMapActivity.this, CompletionInfoActivity.class);
                intent.putExtra("mission_no", missionNo);
                startActivity(intent);
                finish();
            }
            if(activity.equals("FaCheInfoActivity"))
            {
                Intent intent = new Intent(BaiduMapActivity.this, FaCheInfoActivity.class);
                intent.putExtra("mission_no", missionNo);
                intent.putExtra("type", type);
                startActivity(intent);
                finish();
            }
            if(activity.equals("JieSuanInfoActivity"))
            {
                Intent intent = new Intent(BaiduMapActivity.this, JieSuanInfoActivity.class);
                intent.putExtra("mission_no", missionNo);
                startActivity(intent);
                finish();
            }
            if(activity.equals("TaskExpressInfoActivity"))
            {
                Intent intent = new Intent(BaiduMapActivity.this, TaskExpressInfoActivity.class);
                intent.putExtra("mission_no", missionNo);
                startActivity(intent);
                finish();
            }
            if(activity.equals("TaskInfoActivity"))
            {
                Intent intent = new Intent(BaiduMapActivity.this, TaskInfoActivity.class);
                intent.putExtra("mission_no", missionNo);
                startActivity(intent);
                finish();
            }
            if(activity.equals("TongJiInfoActivity"))
            {
                Intent intent = new Intent(BaiduMapActivity.this, TongJiInfoActivity.class);
                intent.putExtra("mission_no", missionNo);
                startActivity(intent);
                finish();
            }
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }


    /**
     * 检查手机上是否安装了指定的软件
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAvilible(Context context, String packageName){
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        List<String> packageNames = new ArrayList<String>();
        if(packageInfos != null){
            for(int i = 0; i < packageInfos.size(); i++){
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        return packageNames.contains(packageName);
    }

}
