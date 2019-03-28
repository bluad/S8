package com.mantoo.yican.util;


import android.util.Log;

import com.google.gson.Gson;
import com.lzy.imagepicker.bean.ImageItem;
import com.mantoo.yican.s8.R;
import com.mantoo.yican.application.PDAApplication;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by maning on 2017/5/9.
 * Mob SDK 相关的API
 */

public class MobApi {

    public final static String GET_DATA_FAIL = PDAApplication.getInstance().getString(R.string.gank_get_data_fail);
    public final static String NET_FAIL = PDAApplication.getInstance().getString(R.string.gank_net_fail);

    //上传图片
    public static Call<MobBaseEntity<UploadVoucherPictures>> uploadFiles(ArrayList<ImageItem> imageList, final int what, final MyCallBack myCallBack) {

        Map<String, RequestBody> bodyMap = new HashMap<>();
        if (imageList.size() > 0) {
            for (int i = 0; i < imageList.size(); i++) {
                File file = new File(imageList.get(i).path);
                bodyMap.put("file" + i + "\"; filename=\"" + file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
            }
        }
        Call<MobBaseEntity<UploadVoucherPictures>> call = BuildApi.getAPIService().uploadFiles(bodyMap);
        call.enqueue(new Callback<MobBaseEntity<UploadVoucherPictures>>() {
            @Override
            public void onResponse(Call<MobBaseEntity<UploadVoucherPictures>> call, Response<MobBaseEntity<UploadVoucherPictures>> response) {
                if (response.isSuccessful()) {
                    MobBaseEntity<UploadVoucherPictures> body = response.body();
                    Log.e(" body.toString()", "" + new Gson().toJson(body).toString());
                    if (body != null) {
                        if (body.getResult_code() == 0) {
                            myCallBack.onSuccess(what, body.getData());
                        } else {
                            myCallBack.onFail(what, body.getMessage());
                        }
                    } else {
                        myCallBack.onFail(what, GET_DATA_FAIL);
                    }
                } else {
                    myCallBack.onFail(what, GET_DATA_FAIL);
                }
            }

            @Override
            public void onFailure(Call<MobBaseEntity<UploadVoucherPictures>> call, Throwable t) {
                //数据错误
                myCallBack.onFail(what, NET_FAIL);
            }
        });
        return call;
    }

}
