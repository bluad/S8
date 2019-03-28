package com.mantoo.yican.util;
import com.mantoo.yican.config.MainUrl;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;


/**
 * 接口调用的工具类
 */
public interface APIService {

    //这里填写全部路径就会覆盖掉Build得BaseUrl

    @POST("translate?doctype=json&jsonversion=&type=&keyfrom=&model=&mid=&imei=&vendor=&screen=&ssid=&network=&abtest=")
    @FormUrlEncoded
    Call<Translation1> getCall(@Field("i") String targetSentence);


    //上传凭证图片
    @Multipart
    @POST(MainUrl.BASEURL + "UploadPictures")
    Call<MobBaseEntity<UploadVoucherPictures>> uploadFiles(
            @PartMap() Map<String, RequestBody> maps);

}
