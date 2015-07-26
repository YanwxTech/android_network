package com.yvan.androidhttpoperation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.yvan.androidhttpoperation.R;
import com.yvan.androidhttpoperation.net.NetOperation;
import com.yvan.androidhttpoperation.net.NetOperationImpl;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yvan on 2015/5/26.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {
    private EditText et_register_uid;
    private EditText et_register_password;
    private EditText et_register_email;
    private Button btn_register;
    private Button btn_register_reset;
    private CheckBox chk_sport;
    private CheckBox chk_read;
    private CheckBox chk_music;
    private static final String LogInfo = "loginInfo";
    private NetOperation register = new NetOperationImpl();
    private List<String> interests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        initView();
        initEvent();
    }

    private void initView() {
        et_register_uid = (EditText) findViewById(R.id.id_et_register_UID);
        et_register_password = (EditText) findViewById(R.id.id_et_register_password);
        et_register_email = (EditText) findViewById(R.id.id_et_register_email);
        btn_register = (Button) findViewById(R.id.id_btn_register);
        btn_register_reset = (Button) findViewById(R.id.id_btn_register_reset);
        chk_music = (CheckBox) findViewById(R.id.id_chk_register_music);
        chk_read = (CheckBox) findViewById(R.id.id_chk_register_read);
        chk_sport = (CheckBox) findViewById(R.id.id_chk_register_sport);
    }

    private void initEvent() {
        btn_register.setOnClickListener(this);
        btn_register_reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_register:
                String UID = et_register_uid.getText().toString();
                String password = et_register_password.getText().toString();
                String email = et_register_email.getText().toString();
                interests = new ArrayList<String>();
                if (chk_sport.isChecked()) {
                    interests.add(chk_sport.getText().toString());
                }
                if (chk_read.isChecked()) {
                    interests.add(chk_read.getText().toString());
                }
                if (chk_music.isChecked()) {
                    interests.add(chk_music.getText().toString());
                }

                //输入验证
                Log.i(LogInfo, UID + "," + password);

                new HttpAsyncTask().execute(UID, password, email);
                break;
            case R.id.id_btn_register_reset:
                et_register_uid.setText("");
                et_register_password.setText("");
                et_register_email.setText("");
                break;
            default:
                break;
        }
    }

    class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String UID = strings[0];
            String password = strings[1];
            String email = strings[2];
            String result = "";
            try {
                result = register.userRegister(UID, password, email, interests);
            } catch (ConnectTimeoutException e) {
                result = "请求超时";
            } catch (SocketTimeoutException e) {
                result = "响应超时";
            } catch (Exception e) {
                result = "注册异常";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //根据传回的结果进行操作
            updateUI(result);
        }


    }

    private void updateUI(String result) {
        if (result.equals("success")) {
            Toast.makeText(this, "成功注册,请登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (result.equals("failure")) {
            Toast.makeText(this, "当前用户名已被注册，请选择新的用户名！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }
    }
}
