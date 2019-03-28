package com.mantoo.yican;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.mantoo.yican.s8.R;
import com.google.gson.Gson;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.mantoo.yican.adapter.ImagePickerAdapter;
import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.mantoo.yican.util.HttpUtil;
import com.mantoo.yican.util.MobApi;
import com.mantoo.yican.util.MyCallBack;
import com.mantoo.yican.util.MyStringCallBack;
import com.mantoo.yican.util.UploadVoucherPictures;
import com.mantoo.yican.widget.SelectDialog;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;


/**
 * Created by Administrator on 2017/10/12.
 */

public class YiChangSubmitActivity extends BaseActivity implements View.OnClickListener,ImagePickerAdapter.OnRecyclerViewItemClickListener {

    private Configuration config;
    private Resources resources;

    private EditText problem, waybillNo;
    private String intentWayBillno;

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 3;               //允许选择图片最大数

    private HttpUtil httpUtil;

    private String resultNo; // 扫描返回值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yichang_submit);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        httpUtil = new HttpUtil();
        initView();
        //最好放到 Application oncreate执行
        initImagePicker();
        initWidget();

    }

    private void initView() {
        findViewById(R.id.yichang_submit_return).setOnClickListener(this);
        findViewById(R.id.yichang_submit_commit).setOnClickListener(this);
        findViewById(R.id.yichang_saosao).setOnClickListener(this);

        problem = (EditText) findViewById(R.id.yichang_submit_problem);
        waybillNo = (EditText) findViewById(R.id.yichang_submit_waybillNo);

        Intent intent = getIntent();
        intentWayBillno = intent.getStringExtra("waybillNo");
        waybillNo.setText(intentWayBillno);
        resultNo = intent.getStringExtra("result");
        if(resultNo != null)
        {
            waybillNo.setText(resultNo.toString());
        }
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    private void initWidget() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.yichang_submit_picAddr);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
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
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                List<String> names = new ArrayList<>();
                if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                {
                    names.add("拍照");
                    names.add("相册");
                }
                else
                {
                    names.add("Photo");
                    names.add("Gallery");
                }
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0: // 直接调起相机
                                /**
                                 * 0.4.7 目前直接调起相机不支持裁剪，如果开启裁剪后不会返回图片，请注意，后续版本会解决
                                 *
                                 * 但是当前直接依赖的版本已经解决，考虑到版本改动很少，所以这次没有上传到远程仓库
                                 *
                                 * 如果实在有所需要，请直接下载源码引用。
                                 */
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(YiChangSubmitActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(YiChangSubmitActivity.this, ImageGridActivity.class);
                                /* 如果需要进入选择的时候显示已经选中的图片，
                                 * 详情请查看ImagePickerActivity
                                 * */
//                                intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }

                    }
                }, names);


                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    ArrayList<ImageItem> images = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yichang_submit_commit:  //提报异常
                if(images == null)
                {
                    //请求开始
                    AjaxParams params=new AjaxParams();
                    params.put("driverid", AppCache.getString(AppCacheKey.driverid));
                    params.put("waybillNo", waybillNo.getText().toString());
                    params.put("problem", problem.getText().toString());
                    params.put("picAddr", "");
                    String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.ABNORMALREPOTING;
                    PDAApplication.http.configTimeout(8000);
                    PDAApplication.http.post(url,params,new AjaxCallBack<Object>() {
                        public void onSuccess(Object t) {
                            hideLoadingDialog();
                            JSONObject object;
                            try {
                                object = new JSONObject(t.toString());
                                if (object.optInt(Constants.RESULT_CODE) == 0) {
                                    if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                    {
                                        showStringToastMsg(object.optString(Constants.MESSAGE));
                                    }
                                    else
                                    {
                                        showStringToastMsg("Submit Successful！");
                                    }
                                    Intent intent = new Intent(YiChangSubmitActivity.this, YiChangActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                    {
                                        showStringToastMsg(object.optString(Constants.MESSAGE));
                                    }
                                    else
                                    {
                                        showStringToastMsg("Submit Failed！");
                                    }
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        };
                        public void onFailure(Throwable t, String strMsg) {
                            hideLoadingDialog();
                            showToastMsg(R.string.error_network);
                            return;
                        };
                    } );
                    //请求结束
                    return;
                }
                showLoadingDialog();
                // 上传图片
                MobApi.uploadFiles(images, 0x030, new MyCallBack() {
                    @Override
                    public void onSuccess(int what, Object result) {
                        UploadVoucherPictures picAdd = (UploadVoucherPictures) result;
                        String picList = new Gson().toJson(picAdd.getPicAddr()).toString();
                        //请求开始
                        AjaxParams params=new AjaxParams();
                        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
                        params.put("waybillNo", waybillNo.getText().toString());
                        params.put("problem", problem.getText().toString());
                        params.put("picAddr", picList);
                        String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.ABNORMALREPOTING;
                        PDAApplication.http.configTimeout(8000);
                        PDAApplication.http.post(url,params,new AjaxCallBack<Object>() {
                            public void onSuccess(Object t) {
                                hideLoadingDialog();
                                JSONObject object;
                                try {
                                    object = new JSONObject(t.toString());
                                    if (object.optInt(Constants.RESULT_CODE) == 0) {
                                        if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                        {
                                            showStringToastMsg(object.optString(Constants.MESSAGE));
                                        }
                                        else
                                        {
                                            showStringToastMsg("Submit Successful！");
                                        }
                                        Intent intent = new Intent(YiChangSubmitActivity.this, YiChangActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                        {
                                            showStringToastMsg(object.optString(Constants.MESSAGE));
                                        }
                                        else
                                        {
                                            showStringToastMsg("Submit Failed！");
                                        }
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

                    @Override
                    public void onSuccessList(int what, List results) {
                    }

                    @Override
                    public void onFail(int what, String result) {
                        showStringToastMsg(result);
                    }
                });

                break;
            case R.id.yichang_submit_picAddr:
                // 扫描
                break;
            case R.id.yichang_saosao:
                Intent intent = new Intent(YiChangSubmitActivity.this, LongnerScanActivity.class);
                intent.putExtra("activityType", "YiChangSubmitActivity");
                startActivity(intent);
                break;
            case R.id.yichang_submit_return:
                Intent intent3 = new Intent(YiChangSubmitActivity.this, YiChangActivity.class);
                startActivity(intent3);
                finish();
                break;

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(YiChangSubmitActivity.this, YiChangActivity.class);
            startActivity(intent);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }
}
