package com.yvan.androidhttpoperation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yvan.androidhttpoperation.R;
import com.yvan.androidhttpoperation.net.NetOperation;
import com.yvan.androidhttpoperation.net.NetOperationImpl;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.SocketTimeoutException;

/**
 * Created by Yvan on 2015/5/26.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText et_uid;
    private EditText et_password;
    private Button btn_login;
    private Button btn_login_reset;
    private static final String LogInfo = "loginInfo";
    private NetOperation login = new NetOperationImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initView();
        initEvent();
    }

    private void initView() {
        et_uid = (EditText) findViewById(R.id.id_et_UID);
        et_password = (EditText) findViewById(R.id.id_et_password);
        btn_login = (Button) findViewById(R.id.id_btn_login);
        btn_login_reset = (Button) findViewById(R.id.id_btn_login_rest);
    }

    private void initEvent() {
        btn_login.setOnClickListener(this);
        btn_login_reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_login:
                String UID = et_uid.getText().toString();
                String password = et_password.getText().toString();
                //输入验证
                Log.i(LogInfo, UID + "," + password);

                new HttpAsyncTask().execute(UID, password);
                break;
            case R.id.id_btn_login_rest:
                et_uid.setText("");
                et_password.setText("");
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
            String result = "";
            try {
                result = login.userLogin(UID, password);
            } catch (ConnectTimeoutException e) {
                result = "请求超时";
            } catch (SocketTimeoutException e) {
                result = "响应超时";
            } catch (Exception e) {
                result = "登录异常";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //根据传回的结果进行操作
            updateUI(result);
        }

        @Override
        protected void onPreExecute() {
            //TODO
            super.onPreExecute();
        }
    }

    private void updateUI(String result) {
        if (result.equals("success")) {
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginSuccessActivity.class);
            startActivity(intent);
        } else if (result.equals("failure")) {
            Toast.makeText(this, "用户名或密码不正确！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }
    }
}
