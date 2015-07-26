package com.yvan.androidhttpoperation.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yvan.androidhttpoperation.R;
import com.yvan.androidhttpoperation.net.NetOperation;
import com.yvan.androidhttpoperation.net.NetOperationImpl;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.SocketTimeoutException;

/**
 * Created by Yvan on 2015/5/26.
 */
public class LoadImgActivity extends Activity {
    private ImageView imageView;
    private EditText et_img;
    private Button btn_load;
    private String result = "";
    private NetOperation loadImg = new NetOperationImpl();
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadimg_layout);
        initView();
        initEvent();
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.id_img_loadimg);
        et_img = (EditText) findViewById(R.id.et_img_id);
        btn_load = (Button) findViewById(R.id.btn_load_img);
    }

    private void initEvent() {
        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = et_img.getText().toString().trim();
                Log.i("picId", id);
                new HttpAsyncTask().execute(id);
            }
        });
    }


    class HttpAsyncTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            result="";
            try {
                bitmap = loadImg.loadImg(id);
            } catch (ConnectTimeoutException e) {
                result = "请求超时";
            } catch (SocketTimeoutException e) {
                result = "响应超时";
            } catch (Exception e) {
                result = "加载图片出错";
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
            if (!result.equals("")) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
