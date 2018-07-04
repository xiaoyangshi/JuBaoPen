package com.jinjiuyun.miban.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 配置数据库
 * Created by lty on 2018/2/5.
 */

public class GreenDaoSetUp {
    private static DaoSession daoSession;
    /**
     * 配置数据库
     */
    public static void setupDatabase(Context context) {
        //创建数据库 "roborder.db"
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "roborder.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }
    
    public static DaoSession getDaoInstant() {
        return daoSession;
    }
}

