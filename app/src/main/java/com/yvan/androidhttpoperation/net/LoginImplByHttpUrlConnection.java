package com.yvan.androidhttpoperation.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Yvan on 2015/5/26.
 */
public class LoginImplByHttpUrlConnection implements Login {
    @Override
    public String userLogin(String UID, String password) throws Exception {
        String url = "http://10.71.28.73:9090/JSBKServer/login.do?UID=" + UID + "&password=" + password;
        URL httpUrl = new URL(url);
        HttpURLConnection connection = null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            connection = (HttpURLConnection) httpUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.connect();
            is = connection.getInputStream();
            isr = new InputStreamReader(is, "utf-8");
            br = new BufferedReader(isr);
            sb = new StringBuffer();
            String str="";
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
        } finally {
            br.close();
            isr.close();
            is.close();
            connection.disconnect();

        }
        return sb.toString();
    }
}
