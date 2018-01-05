package com.jeepc.client;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    public static final String AUTHORITY = "com.jeepc.testcpservice.TestContentProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/test");
    public static Uri mCurrentURI = CONTENT_URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private int index=0;

    public void insert(View view) {
        ContentValues values = new ContentValues();
        values.put("name", "test"+index);
        index++;
        Uri uri = getContentResolver().insert(mCurrentURI,values);
        Log.e("jeepc1", uri.toString());
    }

    public void bulkInsert(View view) {
        ContentValues [] valuess = new ContentValues[5000];
        for (int i =0;i< valuess.length;i++) {
            ContentValues values = new ContentValues();
            values.put("name","testBulk"+index);
            valuess[i] = values;
            index++;
        }
        long startTime = System.currentTimeMillis();
        getContentResolver().bulkInsert(mCurrentURI,valuess);
        Log.e("jeepc1","consume time:"+(System.currentTimeMillis()-startTime));
    }

    public void query(View view) {
        Cursor cursor = this.getContentResolver().query(mCurrentURI,null,null,null,null );
        Log.e("jeepc1", "count=" + cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            Log.e("jeepc1", "name: " + name);
            cursor.moveToNext();
        }
        cursor.close();
    }

    public void update(View view) {
        ContentValues values = new ContentValues();
        values.put("name", "testUpdate");
        int count = this.getContentResolver().update(mCurrentURI, values, "_id = 2",null);
        Log.e("jeepc1 ", "count=" + count);
        query(null);
    }

    public void delete(View view) {
        int count = this.getContentResolver().delete(mCurrentURI, "_id = 3",null);
        Log.e("jeepc1 ", "count=" + count);
        query(null);
    }


}
