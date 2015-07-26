package com.yvan.androidhttpoperation.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yvan.androidhttpoperation.R;
import com.yvan.androidhttpoperation.net.NetOperation;
import com.yvan.androidhttpoperation.net.NetOperationImpl;
import com.yvan.androidhttpoperation.net.ServiceException;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yvan on 2015/5/26.
 */
public class UploadActivity extends Activity {
    private Button btn_upload;
    private static final int REQUEST_CODE = 1000;
    private String filePath;
    private NetOperation upload = new NetOperationImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_layout);
        initView();
        initEvent();
    }


    private void initView() {
        btn_upload = (Button) findViewById(R.id.id_btn_upload);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (data == null) {
                Toast.makeText(this, "你没有选择任何图片", Toast.LENGTH_LONG).show();
            } else {
                Uri uri = data.getData();
                if (uri == null) {
                    Toast.makeText(this, "你没有选择任何图片", Toast.LENGTH_LONG).show();
                } else {
                    String path = null;
                    String[] pojo = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, pojo, null, null, null);
                    if (cursor != null) {
                        int columnIndex = cursor.getColumnIndex(pojo[0]);
                        cursor.moveToFirst();
                        path = cursor.getString(columnIndex);
                        cursor.close();
                    }
                    if (path == null) {
                        Toast.makeText(this, "未能获得图片的物理路径", Toast.LENGTH_LONG).show();
                    } else {
                        filePath = path;
                        Toast.makeText(this, "图片的物理路径:" + path, Toast.LENGTH_LONG).show();
                        new AlertDialog.Builder(this).setMessage("上传文件确认").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                new HttpAsyncTask().execute();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create().show();

                    }

                }
            }

        }
    }

    private void initEvent() {
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }


    class HttpAsyncTask extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {
            InputStream inputStream = null;
            String result = null;
            try {
                File file = new File(filePath);
                String fileName = file.getName();
                inputStream = new FileInputStream(file);
                Map<String, String> data = new HashMap<>();
                data.put("UID", "Yvan");
                data.put("password", "02997135");
                data.put("fileName", fileName);
                result = upload.upload(inputStream, data);
            } catch (ServiceException e) {
                result = e.getException();
            } catch (FileNotFoundException e) {
                result = "未发现该文件";
            } catch (ConnectTimeoutException e) {
                result = "请求超时";
            } catch (SocketTimeoutException e) {
                result = "响应超时";
            } catch (Exception e) {
                result = "上传出现异常";
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            updateUI(s);

        }
    }

    private void updateUI(String result) {
        if (result.equals("success")) {
            Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
        } else if (result.equals("failure")) {
            Toast.makeText(this, "上传失败！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }
    }
}
