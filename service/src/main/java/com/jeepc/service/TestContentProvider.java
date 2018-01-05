package com.jeepc.service;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jeepc.service.greendao.TestBeanDao;

import java.util.ArrayList;

/**
 * Created by jeepc on 2018/1/3.
 */

public class TestContentProvider extends ContentProvider {

    public static class ProviderInfo {

        public static final String AUTHORITY = "com.jeepc.testcpservice.TestContentProvider";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/test");


        public static final String TABLE_NAME = TestBeanDao.TABLENAME;

        public static final String ID = TestBeanDao.Properties.Id.columnName;
        public static final String NAME = TestBeanDao.Properties.Name.columnName;

    }

    private static final UriMatcher sUriMatcher;
    private static final int MATCH_FIRST = 1;
    private static final int MATCH_SECOND = 2;


    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(ProviderInfo.AUTHORITY, "first", MATCH_FIRST);
        sUriMatcher.addURI(ProviderInfo.AUTHORITY, "second", MATCH_SECOND);
    }
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ProviderInfo.TABLE_NAME);
        SQLiteDatabase db = DaoManager.getInstance().getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs,null,null,sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    private boolean mIsInTransaction = false;
    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(
            ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        SQLiteDatabase db = DaoManager.getInstance().getWritableDatabase();
        db.beginTransaction();//开始事务
        mIsInTransaction = true;
        try{
            ContentProviderResult[] results = super.applyBatch(operations);
            db.setTransactionSuccessful();//设置事务标记为successful
            return results;
        } finally {
            mIsInTransaction = false;
            db.endTransaction();//结束事务
            notifyChange();
        }
    }
    protected void notifyChange() {
        if(!mIsInTransaction) {
            App.get().getContentResolver().notifyChange(ProviderInfo.CONTENT_URI,null,
                    false);
        }
    }

   /* @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = DaoManager.getInstance().getWritableDatabase();
        db.beginTransaction();//开始事务
        mIsInTransaction = true;
        try{
            int numValues = super.bulkInsert(uri,values);
            db.setTransactionSuccessful();//设置事务标记为successful
            return numValues;
        } finally {
            mIsInTransaction = false;
            db.endTransaction();//结束事务
            notifyChange();
        }
    }
*/
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowId = DaoManager.getInstance().getWritableDatabase().insert(ProviderInfo.TABLE_NAME, "", values);
        if (rowId > 0) {
            Uri rowUri = ContentUris.withAppendedId(ProviderInfo.CONTENT_URI, rowId);
            if(!mIsInTransaction) {
                App.get().getContentResolver().notifyChange(rowUri, null);
            }
            return rowUri;
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = DaoManager.getInstance().getWritableDatabase();
        int count = db.delete(ProviderInfo.TABLE_NAME, selection, selectionArgs);
        App.get().getContentResolver().notifyChange(uri,null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = DaoManager.getInstance().getWritableDatabase();
        int count = db.update(ProviderInfo.TABLE_NAME, values, selection, selectionArgs);
        App.get().getContentResolver().notifyChange(uri,null);
        return count;
    }
}
