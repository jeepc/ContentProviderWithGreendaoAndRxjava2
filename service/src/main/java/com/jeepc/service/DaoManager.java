package com.jeepc.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jeepc.service.greendao.DaoMaster;
import com.jeepc.service.greendao.DaoSession;


public class DaoManager {

    private DaoSession mDaoSession;
    private DaoMaster.DevOpenHelper mDevOpenHelper;
    private SQLiteDatabase mReadableSQLiteDatabase;
    private SQLiteDatabase mWritableSQLiteDatabase;

    private DaoManager() {
    }


    private static class SingleInstanceHolder {
        private static final DaoManager INSTANCE = new DaoManager();
    }


    public static DaoManager getInstance() {
        return SingleInstanceHolder.INSTANCE;
    }


    public void init(Context context) {
        mDevOpenHelper = new DaoMaster.DevOpenHelper(context.getApplicationContext(), "fda-db");
        DaoMaster daoMaster = new DaoMaster(mDevOpenHelper.getWritableDatabase());
        mDaoSession = daoMaster.newSession();
    }

    public SQLiteDatabase getReadableDatabase() {
        if (mReadableSQLiteDatabase == null)
            mReadableSQLiteDatabase = mDevOpenHelper.getReadableDatabase();
        return mReadableSQLiteDatabase;
    }

    public SQLiteDatabase getWritableDatabase() {
        if (mWritableSQLiteDatabase == null)
            mWritableSQLiteDatabase = mDevOpenHelper.getWritableDatabase();
        return mWritableSQLiteDatabase;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}
