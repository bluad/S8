package com.mantoo.yican;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mantoo.yican.cn.hty.R;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/10/16.
 */

public class QianShouActivity extends BaseActivity implements View.OnClickListener, ImagePickerAdapter.OnRecyclerViewItemClickListener {

    private String missionNo, waybillNos;

    private TextView sender,sendPhone,sendAddress,receiver,receivePhone,receiveAddress,fee,billNo,daishouhuokuanfee;
    private LinearLayout daishouhuokuanfeeL,checkboxT,dingdanffL;

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private CheckBox dingdanfeebox;

    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 3;               //允许选择图片最大数

    private HttpUtil httpUtil;

    private Configuration config;
    private Resources resources;

    private ImageView qianshouUsername;

    private String picPath;

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qianshou);
        resources = getResources();// 获得res资源对象
        config = resources.getConfiguration();// 获得设置对象
        httpUtil = new HttpUtil();
        findViewById(R.id.one_qianshou_button).setOnClickListener(this);
        findViewById(R.id.qianshou_submit_picAddr).setOnClickListener(this);
        findViewById(R.id.qianshou_info).setOnClickListener(this);
        findViewById(R.id.qianshou_return).setOnClickListener(this);
        dingdanffL = (LinearLayout)findViewById(R.id.dingdanffL);
        daishouhuokuanfeeL = (LinearLayout)findViewById(R.id.daishouhuokuanfeeL1);
        checkboxT = (LinearLayout)findViewById(R.id.checkboxT);
        daishouhuokuanfee = (TextView)findViewById(R.id.daishouhuokuanfee1);
        dingdanfeebox = (CheckBox)findViewById(R.id.dingdanfeebox);

        qianshouUsername = (ImageView) findViewById(R.id.qianshou_username);
        qianshouUsername.setVisibility(View.GONE);


        billNo = (TextView) findViewById(R.id.qianshou_billwayno);
        sender = (TextView) findViewById(R.id.qianshou_sender);
        sendPhone = (TextView) findViewById(R.id.qianshou_sender_phone);
        sendAddress = (TextView) findViewById(R.id.qianshou_sender_address);
        receiver = (TextView) findViewById(R.id.qianshou_receiver);
        receivePhone = (TextView) findViewById(R.id.qianshou_receiver_phone);
        receiveAddress = (TextView) findViewById(R.id.qianshou_receiver_address);
        fee = (TextView) findViewById(R.id.qianshou_fee);

        initImagePicker();
        initWidget();
        initView();

        dingdanfeebox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    dingdanfeebox.setSelected(true);
                }else{
                    dingdanfeebox.setSelected(false);
                }
            }
        });
    }

    private void initView() {
        Intent intent = getIntent();
        missionNo = intent.getStringExtra("mission_no");
        waybillNos = intent.getStringExtra("waybillNo");
        type = intent.getStringExtra("type");

        //请求开始
        AjaxParams params=new AjaxParams();
        params.put("waybillNo", waybillNos);
        params.put("missionNo", missionNo);
        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
        String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.WAYBILLDETAILS;
        System.out.println(params+url);
        showLoadingDialog();
        PDAApplication.http.configTimeout(20000);
        PDAApplication.http.post(url,params,new AjaxCallBack<Object>() {
            public void onSuccess(Object t) {
                hideLoadingDialog();
                JSONObject object;
                try {
                    object = new JSONObject(t.toString());
                    if (object.optInt(Constants.RESULT_CODE) == 0) {
                        JSONObject data = object.optJSONObject(Constants.DATA);
                        billNo.setText(waybillNos);
                        sender.setText(data.optString("sender"));
                        sendPhone.setText(data.optString("sendPhone"));
                        sendAddress.setText(data.optString("sendAddress"));
                        receiver.setText(data.optString("receiver"));
                        receivePhone.setText(data.optString("receivePhone"));
                        receiveAddress.setText(data.optString("receiveAddress"));
                        if(data.optString("paytype").equals("3"))
                        {
                            fee.setText(data.optString("account") + "/元");
                            dingdanffL.setVisibility(View.VISIBLE);
                        }
                        if(data.optString("daishouAmount") != null && !data.optString("daishouAmount").equals(""))
                        {
                            daishouhuokuanfee.setText(data.optString("daishouAmount") + "/元");
                            daishouhuokuanfeeL.setVisibility(View.VISIBLE);
                            dingdanfeebox.setSelected(false);
                            checkboxT.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            dingdanfeebox.setSelected(true);
                        }
                    }
                    else
                    {
                        if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                        {
                            showStringToastMsg("无数据！");
                        }
                        else
                        {
                            showStringToastMsg("No Data！");
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

        picPath = intent.getStringExtra("pic_path");
        if(picPath != null)
        {
            Bitmap bitmap = getLoacalBitmap(picPath); //从本地取图片
            qianshouUsername.setImageBitmap(bitmap);	//设置Bitmap
            qianshouUsername.setVisibility(View.VISIBLE);

            uploadImage(AppCache.getString(AppCacheKey.http_url) + MainUrl.UPLOADPICTURES, picPath,missionNo, "[\"" + waybillNos + "\"]");
            return;
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
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.qianshou_submit_picAddr);
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
                                Intent intent = new Intent(QianShouActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(QianShouActivity.this, ImageGridActivity.class);
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

    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.one_qianshou_button:
                if(!dingdanfeebox.isSelected())
                {
                    showStringToastMsg("请勾选确认已收款！");
                    return;
                }
                if(images == null)
                {
                    if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                    {
                        showStringToastMsg("请添加照片！");
                    }
                    else
                    {
                        showStringToastMsg("Please add photos！");
                    }
                    return;
                }
                showLoadingDialog();
                MobApi.uploadFiles(images, 0x030, new MyCallBack() {
                    @Override
                    public void onSuccess(int what, Object result) {
                        UploadVoucherPictures picAdd = (UploadVoucherPictures) result;
                        String picList = new Gson().toJson(picAdd.getPicAddr()).toString();
                        AjaxParams params=new AjaxParams();
                        params.put("type", "1");
                        params.put("missionNo", missionNo);
                        params.put("waybillNo", "[\"" + waybillNos + "\"]");
                        params.put("driverid", AppCache.getString(AppCacheKey.driverid));
                        params.put("picAddr", picList);
                        String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.UPDATESIGN;
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
                                            showStringToastMsg("签收成功！");
                                        }
                                        else
                                        {
                                            showStringToastMsg("Sign Success！");
                                        }
                                        Intent intent = new Intent(QianShouActivity.this, FaCheActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        if(config.locale.equals(Locale.SIMPLIFIED_CHINESE))
                                        {
                                            showStringToastMsg("签收失败！");
                                        }
                                        else
                                        {
                                            showStringToastMsg("Sign Failed！");
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
                    }

                    @Override
                    public void onSuccessList(int what, List results) {
                    }

                    @Override
                    public void onFail(int what, String result) {
                        showStringToastMsg(result);
                        hideLoadingDialog();
                    }
                });
                break;
            case R.id.qianshou_info:
                Intent intent = new Intent(QianShouActivity.this, PenActivity.class);
                intent.putExtra("acitvity", "QianShouActivity");
                intent.putExtra("mission_no", missionNo);
                intent.putExtra("waybillNo", waybillNos);

                startActivity(intent);
                finish();
                break;
            case R.id.qianshou_return:
                Intent intent1 = new Intent(QianShouActivity.this, FaCheInfoActivity.class);
                intent1.putExtra("mission_no", missionNo);
                intent1.putExtra("type", type);
                startActivity(intent1);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent1 = new Intent(QianShouActivity.this, FaCheInfoActivity.class);
            intent1.putExtra("mission_no", missionNo);
            intent1.putExtra("type", type);
            startActivity(intent1);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

    private void uploadImage(String url, String path, final String missionNo, final String waybillNos) {
        httpUtil.postFileRequest(url, path, missionNo, waybillNos,new MyStringCallBack() {

            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {
                super.onResponse(response, id);
            }
        });
    }



}

