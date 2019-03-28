package com.mantoo.yican.util;

import java.io.Serializable;

/**
 * Created by maning on 2017/4/11.
 *
 * 基础类
 */

public class MobBaseEntity<T> implements Serializable {
    private static final long serialVersionUID = -4553802208756427393L;

    private String message;

    private int result_code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private T data;


    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    @Override
    public String toString() {
        return "MobBaseEntity{" +
                "message='" + message + '\'' +
                ", result_code='" + result_code + '\'' +
                ", data=" + (data == null ? "" : data.toString())+
                '}';
    }

}
