package com.mantoo.yican.application;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by Administrator on 2017/10/12.
 */

public class AppCache {


    private static SharedPreferences sharedPreferences;

    private static synchronized SharedPreferences getInstance() {
        if (sharedPreferences == null) {
            Context context = PDAApplication.getInstance().getApplicationContext();
            sharedPreferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    /**
     * 打印所有
     */
    public static void print() {
        System.out.println(getInstance().getAll());
    }

    /**
     * 清空保存在默认SharePreference下的所有数据
     */
    public static void clear() {
        getInstance().edit().clear().commit();
    }

    /**
     * 保存字符串
     *
     * @return
     */
    public static void putString(String key, String value) {
        getInstance().edit().putString(key, value).commit();
    }

    /**
     * 读取字符串
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        return getInstance().getString(key, "");
    }

    /**
     * 读取字符串
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(String key, String defaultValue) {
        return getInstance().getString(key, defaultValue);
    }

    /**
     * 保存整型值
     *
     * @return
     */
    public static void putInt(String key, int value) {
        getInstance().edit().putInt(key, value).commit();
    }

    /**
     * 读取整型值
     *
     * @param key
     * @return 默认值为0
     */
    public static int getInt(String key) {
        return getInstance().getInt(key, 0);
    }

    /**
     * 读取整型值
     *
     * @param key
     * @return defaultValue 默认值
     */
    public static int getInt(String key, int defaultValue) {
        return getInstance().getInt(key, defaultValue);
    }

    /**
     * 保存布尔值
     *
     * @return
     */
    public static void putBoolean(String key, Boolean value) {
        getInstance().edit().putBoolean(key, value).commit();
    }

    public static void putLong(String key, long value) {
        getInstance().edit().putLong(key, value).commit();
    }

    /**
     * @param key
     * @return 默认值为0
     */
    public static long getLong(String key) {
        return getInstance().getLong(key, 0);
    }

    /**
     * t 读取布尔值
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return getInstance().getBoolean(key, defValue);
    }

    /**
     * 移除字段
     *
     * @return
     */
    public static void removeString(String key) {
        getInstance().edit().remove(key).commit();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 缓存名称
     */
    public static final String SharedPreferencesName = "wuxi";


}
