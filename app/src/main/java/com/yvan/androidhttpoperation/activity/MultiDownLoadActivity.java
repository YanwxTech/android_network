package com.yvan.androidhttpoperation.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.yvan.androidhttpoperation.R;
import com.yvan.androidhttpoperation.adapter.DownloadAdapter;
import com.yvan.androidhttpoperation.entity.FileInfo;
import com.yvan.androidhttpoperation.services.DownloadService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yvan on 2015/6/15.
 */
public class MultiDownLoadActivity extends Activity {
    private ListView download_list;
    private DownloadAdapter adapter;
    private List<FileInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_thread_download_layout);
        initView();
        initEvent();
    }

    private void initView() {
        download_list = (ListView) findViewById(R.id.id_lv_multi_thread_download);
    }

    private void initEvent() {
        list = new ArrayList<FileInfo>();
        FileInfo fileInfo = new FileInfo(0, "http://wap3.ucweb.com/files/UCBrowser/zh-cn/999/" +
                "UCBrowser_V10.4.2.585_android_pf145_(Build15060211).apk", "UCBrowser.apk", 0, 0);
        list.add(fileInfo);
        fileInfo = new FileInfo(1, "http://shouji.360tpcdn.com/150122/34f21668a99379da9e298af50f97929c/com.youku.phone_68.apk", "youku.apk", 0, 0);
        list.add(fileInfo);
        fileInfo = new FileInfo(2, "http://www.iwifihome.com/download/wifihome.apk", "wifihome.apk", 0, 0);
        list.add(fileInfo);
        fileInfo = new FileInfo(3, "http://wifi.pingan.com/android/prd/PINGAN_WIFI_ANDROID.apk", "pinganwifi.apk", 0, 0);
        list.add(fileInfo);
        fileInfo = new FileInfo(4, "http://s1.music.126.net/download/android/CloudMusic_official_2.7.1.apk", "cloudmusic.apk", 0, 0);
        list.add(fileInfo);

        adapter = new DownloadAdapter(this, list);

        download_list.setAdapter(adapter);

        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_UPDATE);
        filter.addAction(DownloadService.ACTION_FINISHED);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    /**
     * 更新UI广播接收器
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadService.ACTION_UPDATE.equals(intent.getAction())) {
                int position = intent.getIntExtra("id", 0);
                int finished = intent.getIntExtra("finished", 0);
                //更新进度条
                adapter.updateUI(position, finished);
            }
            if (DownloadService.ACTION_FINISHED.equals(intent.getAction())) {
                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
                adapter.updateUI(fileInfo.getId(), 0);
                Toast.makeText(MultiDownLoadActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
