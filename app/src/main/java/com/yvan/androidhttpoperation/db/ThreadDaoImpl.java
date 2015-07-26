package com.yvan.androidhttpoperation.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yvan.androidhttpoperation.entity.ThreadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yvan on 2015/6/15.
 */
public class ThreadDaoImpl implements ThreadDao {
    private Context mContext;

    public ThreadDaoImpl(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public synchronized void insertThread(ThreadInfo threadInfo) {
        Log.i("insertThread",threadInfo.toString());
        DBHelper dbHelper = DBHelper.getInstance(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("insert into thread_info(thread_id,url,start,end,finished) values(?,?,?,?,?)",
                new String[]{threadInfo.getId() + "", threadInfo.getUrl(), threadInfo.getStart() + "", threadInfo.getEnd() + "", threadInfo.getFinished() + ""});
        db.close();
    }

    @Override
    public synchronized void deleteThread(String url) {
        DBHelper dbHelper = DBHelper.getInstance(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from thread_info where url=?", new String[]{url});
        db.close();
    }

    @Override
    public synchronized void updateThread(String url, int thread_id, int finished) {
        DBHelper dbHelper = DBHelper.getInstance(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update thread_info set finished=? where url=? and thread_id=?",
                new String[]{finished + "", url, thread_id + ""});
        db.close();
    }

    @Override
    public List<ThreadInfo> getAllThread(String url) {
        List<ThreadInfo> list = new ArrayList<ThreadInfo>();
        DBHelper dbHelper = DBHelper.getInstance(mContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from thread_info where url=?", new String[]{url});
        while (cursor.moveToNext()) {
            ThreadInfo threadInfo = new ThreadInfo();
            threadInfo.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            threadInfo.setStart(cursor.getInt(cursor.getColumnIndex("start")));
            threadInfo.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
            threadInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            list.add(threadInfo);
        }
        cursor.close();
        db.close();

        return list;
    }

    @Override
    public boolean isExists(String url, int thread_id) {
        DBHelper dbHelper = DBHelper.getInstance(mContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from thread_info where url=? and thread_id=?", new String[]{url, thread_id + ""});
        boolean isExists = cursor.moveToNext();
        cursor.close();
        db.close();
        return isExists;
    }
}
