package com.mantoo.yican.database;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.mantoo.yican.util.DateUtil;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
/**
 * Created by Administrator on 2017/10/12.
 */

public class ExpressDao {


    private Dao<ExpressData, Integer> expressDao;
    private DatabaseHelper helper;

    @SuppressWarnings("unchecked")
    public ExpressDao(Context context) {
        try {
            helper = DatabaseHelper.getHelper(context);
            expressDao = helper.getDao(ExpressData.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ExpressData> listByExpressType(String username, int type, int isUpload) {
        try {
            return expressDao.queryBuilder().where().eq("username", username).and().eq("expressType", type).and()
                    .eq("isUpload", isUpload).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ExpressData> listByExpressType(String username, int type) {
        try {
            return expressDao.queryBuilder().where().eq("username", username).and().eq("expressType", type).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void add(ExpressData expressData) {
        try {
            int result = expressDao.create(expressData);
            System.out.println(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 查询数据库是否有单号
    public boolean queryData(String express, int type) {
        String date = DateUtil.getDate(new Date());
        boolean isexit = false;
        try {
            QueryBuilder<ExpressData, Integer> queryBuilder = expressDao.queryBuilder();
            queryBuilder.where().eq("expressType", type).and().eq("express", express);
            List<ExpressData> result = queryBuilder.query();
            if (result.size() > 0) {
                isexit = true;
            } else {
                isexit = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isexit;
    }

    public void deleteData() {
        String date = DateUtil.getDate(new Date());
        try {
            DeleteBuilder<ExpressData, Integer> deleteBuilder = expressDao.deleteBuilder();
            deleteBuilder.where().ne("create_date", date);
            int result = deleteBuilder.delete();
            System.out.println(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 跟新数据
    public boolean updateData(String express, int type, int isUpload,String state ) {
        String date = DateUtil.getDate(new Date());
        try {
            UpdateBuilder<ExpressData, Integer> deleteBuilder = expressDao.updateBuilder();
            deleteBuilder.updateColumnValue("isUpload", isUpload).where().eq("express", express).and().eq("expressType",
                    type);
            deleteBuilder.updateColumnValue("state", state).where().eq("express", express).and().eq("expressType",
                    type);
            int result = deleteBuilder.update();
            System.out.println(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据对象删除某条数据
     *
     */
    public void delete(int type, int isUpload) {
        DeleteBuilder<ExpressData, Integer> deleteBuilder = expressDao.deleteBuilder();
        try {
            deleteBuilder.where().eq("expressType", type).and().eq("isUpload", isUpload);
            deleteBuilder.delete();
            int result = deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据对象删除某条数据
     *
     */
    public void delete(int type) {
        DeleteBuilder<ExpressData, Integer> deleteBuilder = expressDao.deleteBuilder();
        try {
            deleteBuilder.where().eq("expressType", type);
            deleteBuilder.delete();
            int result = deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
