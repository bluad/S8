package com.mantoo.yican.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.google.gson.Gson;
/**
 * Created by Administrator on 2017/10/12.
 */

public class JsonParser {


    private static Gson gson = new Gson();

    /**
     * 将json格式字符串转换成class对象格式
     *
     * @param json
     * @param clz
     * @return
     */
    public static <T> T deserializeFromJson(String json, Class<T> clz) {
        return gson.fromJson(json, clz);
    }

    /**
     * 将json格式字符串转换成class对象格式
     *
     * @param json
     * @param type
     *            对象类型
     * @return
     */
    public static <T> T deserializeFromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }

    /**
     * 将对象转换成JSON格式字符串
     *
     * @param object
     * @return
     */
    public static String serializeToJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * 将JSONArray转换成List<JavaBean>
     *
     * @param array
     * @param clz
     * @return
     */
    public static <T> List<T> deserializeFromJsonArray(JSONArray array, Class<T> clz) {
        List<T> listResult = new ArrayList<T>();
        for (int i = 0; i < array.length(); i++) {
            T t = deserializeFromJson(array.optJSONObject(i).toString(), clz);
            if (t != null) {
                listResult.add(t);
            }
        }
        return listResult;
    }

}
