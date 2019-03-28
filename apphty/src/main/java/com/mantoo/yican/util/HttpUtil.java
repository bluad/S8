package com.mantoo.yican.util;

import android.content.Intent;

import com.mantoo.yican.FaCheActivity;
import com.mantoo.yican.QianShouAllActivity;
import com.mantoo.yican.cn.hty.R;
import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class HttpUtil {

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private final OkHttpClient client = new OkHttpClient();

    private PostFormBuilder mPost;
    private GetBuilder mGet;

    public HttpUtil() {
        OkHttpUtils.getInstance().getOkHttpClient().newBuilder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .writeTimeout(15 * 1000L, TimeUnit.MILLISECONDS)
                .build();

        mPost = OkHttpUtils.post();
        mGet = OkHttpUtils.get();
    }

    //封装请求
    public void postRequest(String url, Map<String, String> params, MyStringCallBack callback) {
        mPost.url(url)
                .params(params)
                .build()
                .execute(callback);
    }

    //上传文件
    public void postFileRequest(String url, String filePath, final String missionNo, final String waybillNos, MyStringCallBack callback) {



        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);


        //遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
        File file = new File(filePath);
        builder.addFormDataPart("file0", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));

        //构建请求体
        RequestBody requestBody = builder.build();

        //构建请求
        Request request = new Request.Builder()
                .url(url)//地址
                .post(requestBody)//添加请求体
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body= response.body().string();
                try
                {
                    JSONObject object = new JSONObject(body);
                    JSONObject data = object.optJSONObject(Constants.DATA);
                    JSONArray array = data.getJSONArray("picAddr");
                    String path = array.toString();

                    //请求开始
                    AjaxParams params=new AjaxParams();
                    params.put("type", "1");
                    params.put("missionNo", missionNo);
                    params.put("waybillNo", waybillNos);
                    params.put("driverid", AppCache.getString(AppCacheKey.driverid));
                    params.put("picAddr", path);
                    String url = AppCache.getString(AppCacheKey.http_url) + MainUrl.UPDATESIGNPIC;
                    PDAApplication.http.configTimeout(20000);
                    PDAApplication.http.post(url,params,new AjaxCallBack<Object>() {
                        public void onSuccess(Object t) {
                        };
                        public void onFailure(Throwable t, String strMsg) {
                        };
                    } );
                    //请求结束
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }
}
