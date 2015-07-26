package com.yvan.androidhttpoperation.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yvan.androidhttpoperation.R;
import com.yvan.androidhttpoperation.net.MyHttpClient;
import com.yvan.androidhttpoperation.net.ServiceException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLDecoder;

/**
 * Created by Yvan on 2015/5/26.
 */
public class DownloadActivity extends Activity {
    private EditText et_download;
    private Button btn_download;
    private ProgressDialog progressDialog = null;
    private String filename = "noNameAndExtensionsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_layout);
        et_download = (EditText) findViewById(R.id.et_download_name);
        btn_download = (Button) findViewById(R.id.id_btn_download);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(DownloadActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMax(100);
                String downName = et_download.getText().toString();
                new HttpAsyncTask().execute("http://172.30.220.1:9090/SSH/download?id=" + downName);
            }
        });

    }


    class HttpAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String result = "";
            double contentLength = 0;
            InputStream is = null;
            BufferedOutputStream bos = null;
            HttpClient httpClient = MyHttpClient.getHttpClient();
            HttpGet httpGet = new HttpGet(url);


            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);

                int responseCode = httpResponse.getStatusLine().getStatusCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new ServiceException();
                } else {
                    HttpEntity entity = httpResponse.getEntity();
                    Header[] headers = httpResponse.getHeaders("Content-Disposition");
                    for (int i = 0; i < headers.length; i++) {
                        String content = headers[i].getValue();
                        Log.i("headers", content);
                        if (content.contains("filename=")) {
                            filename = content.split("=")[1];
                            filename = URLDecoder.decode(filename, "utf-8");
                            break;
                        }

                    }
                    long maxLength = entity.getContentLength();
                    is = entity.getContent();
                    Log.i("maxLength", maxLength + "");
                    bos = new BufferedOutputStream(new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/" + filename)));
                    byte[] b = new byte[204800];
                    int len = 0;
                    while ((len = is.read(b)) != -1) {
                        contentLength += len;
                        bos.write(b, 0, len);
                        publishProgress((int) (contentLength / maxLength * 100));
                    }
                    result = "下载完毕";

                }
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                result = "未找到该路径";
            } catch (ServiceException e) {
                result = e.getException();
            } catch (IOException e1) {
                e1.printStackTrace();
                result = "网络异常";
            } catch (Exception e1) {
                result = "下载异常";
                e1.printStackTrace();
            } finally {

                try {
                    if (bos != null) {
                        bos.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();

                }

            }
            return result;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            updateUI(s);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("文件下载中...");
            if ("陈奕迅 - 不要说话.mp3".equals(filename)) {
                Toast.makeText(DownloadActivity.this, "服务器上没有找到相应文件，将下载默认文件：\"陈奕迅 - 不要说话.mp3\"", Toast.LENGTH_LONG).show();
            }
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

    }


    private void updateUI(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        if ("下载完毕".equals(s)) {
            Toast.makeText(this, filename + ",已存于根目录下", Toast.LENGTH_LONG).show();
        }
    }
}

