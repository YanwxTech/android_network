package com.yvan.androidhttpoperation.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yvan.androidhttpoperation.db.ThreadDao;
import com.yvan.androidhttpoperation.db.ThreadDaoImpl;
import com.yvan.androidhttpoperation.entity.FileInfo;
import com.yvan.androidhttpoperation.entity.ThreadInfo;

import org.apache.http.HttpStatus;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Yvan on 2015/6/15.
 */
public class DownloadTask {
    private Context mContext;
    private FileInfo mFileInfo;

    private ThreadDao threadDao;

    public boolean isPause = false;

    private int mFinished = 0;

    private int mThreadCount = 1;//线程数

    private List<DownloadThread> threadList;//线程集合

    public static ExecutorService sExecutorService=
            Executors.newCachedThreadPool();//线程池

    public DownloadTask(Context context, FileInfo mFileInfo, int threadCount) {
        this.mContext = context;
        this.mFileInfo = mFileInfo;
        this.mThreadCount = threadCount;
        threadDao = new ThreadDaoImpl(context);
    }

    public void download() {
        Log.i("start", "download start...");
        List<ThreadInfo> threads = threadDao.getAllThread(mFileInfo.getFileUrl());
        ThreadInfo threadInfo = null;
        if (threads.size() == 0) {
            int length = mFileInfo.getLength() / mThreadCount;
            for (int i = 0; i < mThreadCount; i++) {
                //每个线程信息
                threadInfo = new ThreadInfo(i, mFileInfo.getFileUrl(), length * i, (i + 1) * length - 1, 0);
                if (i == mThreadCount - 1) {
                    threadInfo.setEnd(mFileInfo.getLength());
                }
                threads.add(threadInfo);
                //向数据库插入线程信息
                threadDao.insertThread(threadInfo);
            }
            Log.i("threadInfo", "new:" + threadInfo.toString());
        }
        threadList = new ArrayList<DownloadThread>();
        //启动多线程下载
        for (ThreadInfo info : threads) {
            DownloadThread downloadThread = new DownloadThread(info);
            sExecutorService.execute(downloadThread);
            threadList.add(downloadThread);
        }
    }

    /**
     * 判断是否所有线程执行完毕
     */
    private synchronized void checkAllThreadFinished() {
        boolean allFinished = true;
        for (DownloadThread thread : threadList) {
            if (!thread.isFinished) {
                allFinished = false;
                break;
            }
        }
        if (allFinished) {
            //把完成情况发送广播给Activity
            Intent intent = new Intent(DownloadService.ACTION_FINISHED);
            intent.putExtra("fileInfo", mFileInfo);
            mContext.sendBroadcast(intent);
            threadDao.deleteThread(mFileInfo.getFileUrl());
        }
    }

    class DownloadThread extends Thread {
        private ThreadInfo mThreadInfo;

        public boolean isFinished = false;//标记线程是否完成

        public DownloadThread(ThreadInfo mThreadInfo) {
            this.mThreadInfo = mThreadInfo;
        }

        @Override
        public void run() {
            //设置下载位置
            HttpURLConnection conn = null;
            BufferedInputStream bis = null;
            RandomAccessFile raf = null;
            try {
                URL url = new URL(mThreadInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000);
                conn.setRequestMethod("GET");
                int start = mThreadInfo.getStart() + mThreadInfo.getFinished();
                conn.setRequestProperty("Range", "bytes=" + start + "-" + mThreadInfo.getEnd());
                //设置文件写入路径
                File file = new File(DownloadService.DOWNLOAD_DIR, mFileInfo.getFilename());
                raf = new RandomAccessFile(file, "rwd");
                //设置文件写入位置
                raf.seek(start);
                mFinished += mThreadInfo.getFinished();
                //开始下载
                if (conn.getResponseCode() == HttpStatus.SC_PARTIAL_CONTENT) {
                    bis = new BufferedInputStream(conn.getInputStream());
                    byte[] buffer = new byte[1024 * 4];
                    int len = -1;
                    long time = System.currentTimeMillis();
                    while ((len = bis.read(buffer)) != -1) {
                        raf.write(buffer, 0, len);
                        //累加整个文件进度
                        mFinished += len;
                        //累加每个线程的完成进度
                        mThreadInfo.setFinished(mThreadInfo.getFinished() + len);
                        if (System.currentTimeMillis() - time > 1000) {
                            time = System.currentTimeMillis();
                            //把下载进度改送广播给Activity
                            Intent intent = new Intent(DownloadService.ACTION_UPDATE);
                            intent.putExtra("finished", mFinished * 100 / mFileInfo.getLength());
                            intent.putExtra("id", mFileInfo.getId());
                            mContext.sendBroadcast(intent);
                            //下载暂停时，保存下载进度到数据库
                            if (isPause) {
                                threadDao.updateThread(mThreadInfo.getUrl(), mThreadInfo.getId(), mThreadInfo.getFinished());
                                return;
                            }
                        }
                    }

                }
                //标识线程执行完毕
                isFinished = true;
                //检查下载任务是否已完成
                checkAllThreadFinished();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null) {
                        conn.disconnect();
                    }
                    if (bis != null) {
                        bis.close();
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
