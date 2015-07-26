package com.yvan.androidhttpoperation.services;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yvan.androidhttpoperation.entity.FileInfo;

import org.apache.http.HttpStatus;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yvan on 2015/6/15.
 */
public class DownloadService extends Service {
    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static final String ACTION_UPDATE="ACTION_UPDATE";
    public static final String ACTION_FINISHED = "ACTION_FINISHED";

    public static final String DOWNLOAD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/download/";

    private static final int MSG_INIT = 0x001;

    private Map<Integer,DownloadTask> mTasks=new LinkedHashMap<Integer,DownloadTask>();//下载任务集合

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_START.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
            Log.i("fileInfo", "start:" + fileInfo.toString());
            //启动初始化线程
            DownloadTask.sExecutorService.execute(new InitThread(fileInfo));
        } else if (ACTION_PAUSE.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
            Log.i("fileInfo", "pause:" + fileInfo.toString());
            //从任务集合获得任务
            DownloadTask task=mTasks.get(fileInfo.getId());
            //暂停下载
            if(task!=null){
                task.isPause=true;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INIT:
                    //获得初始化的结果
                    FileInfo fileInfo = (FileInfo) msg.obj;
                    Log.i("fileTest", fileInfo.toString());
                    //启动下载任务
                    DownloadTask task=new DownloadTask(DownloadService.this,fileInfo,3);
                    task.download();

                    //把下载任务加入 到下载任务集合
                    mTasks.put(fileInfo.getId(),task);
                    break;
            }
        }

    };

    class InitThread extends Thread {
        FileInfo fileInfo = null;

        public InitThread(FileInfo fileInfo) {
            this.fileInfo = fileInfo;
        }

        @Override
        public void run() {
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            try {
                URL url = new URL(fileInfo.getFileUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000);
                conn.setRequestMethod("GET");
                int length = -1;
                if (conn.getResponseCode() == HttpStatus.SC_OK) {
                    length = conn.getContentLength();
                }
                if (length < 0) {
                    return;
                }

                File dir = new File(DOWNLOAD_DIR);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File file = new File(dir, fileInfo.getFilename());
                raf = new RandomAccessFile(file, "rwd");
                raf.setLength(length);
                fileInfo.setLength(length);
                mHandler.obtainMessage(MSG_INIT, fileInfo).sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null) {
                        conn.disconnect();
                    }
                    if (raf != null) {
                        raf.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
