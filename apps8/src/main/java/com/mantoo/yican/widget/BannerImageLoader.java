package com.mantoo.yican.widget;

import android.content.Context;
import android.widget.ImageView;
import com.youth.banner.loader.ImageLoader;
/**
 * Created by Administrator on 2017/11/7.
 */

public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object obj, ImageView imageView) {

        com.nostra13.universalimageloader.core.ImageLoader imageLoaderInstance = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoaderInstance.displayImage((String) obj, imageView);

    }
}