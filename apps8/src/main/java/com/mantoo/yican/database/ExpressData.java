package com.mantoo.yican.database;

/**
 * Created by Administrator on 2017/10/12.
 */
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


public class ExpressData {


    /**
     * 数据库ID
     */
    @DatabaseField(generatedId = true)
    private int ID;
    /**
     * 快递员帐号
     */
    @DatabaseField(columnName = "username")
    private String username;
    /**
     * 是否上传
     */
    @DatabaseField(columnName = "isUpload")
    private int isUpload;
    public int getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(int  isUpload) {
        this.isUpload = isUpload;
    }
    /**
     * 是否上传成功状态
     */
    @DatabaseField(columnName = "state")
    private String state;
    public String getstate() {
        return state;
    }

    public void setstate(String  state) {
        this.state = state;
    }

    /**
     * 单号
     */
    @DatabaseField(columnName = "express")
    private String express;
    /**
     * 单号类型：0－收件,1-到件，2-发件，3-派件，4-签收
     */
    @DatabaseField(columnName = "expressType")
    private int expressType;
    /**
     * 网点
     */
    @DatabaseField(columnName = "pot")
    private String pot;
    /**
     * 创建时间
     */
    @DatabaseField(columnName = "create_time")
    private String create_time;

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    @DatabaseField(columnName = "create_date")
    private String create_date;

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public int getExpressType() {
        return expressType;
    }

    public void setExpressType(int expressType) {
        this.expressType = expressType;
    }

    public String getPot() {
        return pot;
    }

    public void setPot(String pot) {
        this.pot = pot;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
