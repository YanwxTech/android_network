package com.yvan.androidhttpoperation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.yvan.androidhttpoperation.activity.DownloadActivity;
import com.yvan.androidhttpoperation.activity.ListViewActivity;
import com.yvan.androidhttpoperation.activity.LoadImgActivity;
import com.yvan.androidhttpoperation.activity.LoginActivity;
import com.yvan.androidhttpoperation.activity.MultiDownLoadActivity;
import com.yvan.androidhttpoperation.activity.RegisterActivity;
import com.yvan.androidhttpoperation.activity.UploadActivity;


public class MainActivity extends ActionBarActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_login_test:
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.id_btn_register_test:
                intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.id_btn_upload_test:
                intent = new Intent(MainActivity.this, UploadActivity.class);
                startActivity(intent);
                break;
            case R.id.id_btn_loadimg_test:
                intent = new Intent(MainActivity.this, LoadImgActivity.class);
                startActivity(intent);
                break;
            case R.id.id_btn_download_test:
                intent = new Intent(MainActivity.this, DownloadActivity.class);
                startActivity(intent);
                break;
            case R.id.id_btn_listview_test:
                intent = new Intent(MainActivity.this, ListViewActivity.class);
                startActivity(intent);
                break;
            case R.id.id_btn_multi_thread_download_test:
                intent=new Intent(MainActivity.this,MultiDownLoadActivity.class);
                startActivity(intent);
                break;
            default:
                break;


        }
    }

}
