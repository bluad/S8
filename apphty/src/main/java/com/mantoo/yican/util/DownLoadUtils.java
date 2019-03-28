package com.mantoo.yican.util;

import java.io.File;

/*
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
*/

/*
 * 下载工具类
 * jonathan 2016年8月19日 星期五
 */
public class DownLoadUtils {
	public void downapk(String url,String targerFile, final MyCallBack myCallBack){
		//创建HttpUtils对象
		/*HttpUtils httpUtils = new HttpUtils();
		//调用HttpUtils 下载的方法下载指定文件
		httpUtils.download(url, targerFile,new RequestCallBack<File>(){

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				myCallBack.onFailure(arg0, arg1);
			}

			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				// TODO Auto-generated method stub
				myCallBack.onSuccess(arg0);
			}
			
			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				// TODO Auto-generated method stub
				super.onLoading(total, current, isUploading);
				myCallBack.onLoadding(total, current, isUploading);
			}
			
		});*/
	}
	
	public interface MyCallBack{
		/* 下载成功时调用*/
		/*void onSuccess(ResponseInfo<File> arg0);
		*//*下载失败时调用*//*
		void onFailure(HttpException arg0, String arg1);
		*//*下载中调用*//*
		void onLoadding(long total, long current, boolean isUploading);*/
	}
}
