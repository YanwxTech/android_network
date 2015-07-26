package com.yvan.androidhttpoperation.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Yvan on 2015/6/15.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "download.db";
    private static final int DB_VERSION = 1;
    private static final String CREATE_TABLE = "create table thread_info" +
            "(_id  integer primary key autoincrement,thread_id integer," +
            "url text,start integer,end integer,finished integer)";
    private static final String DROP_TABLE = "drop table if exits thread_info";

    private static DBHelper sDBHelper;
    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    public static DBHelper getInstance(Context context){
        if (sDBHelper==null){
            sDBHelper=new DBHelper(context);
        }
        return sDBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        db.execSQL(CREATE_TABLE);
    }
}
