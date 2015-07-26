package com.yvan.androidhttpoperation.net;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yvan on 2015/5/26.
 */
public class LoginImplByHttpClient implements Login {
    @Override
    public String userLogin(String UID, String password) throws Exception {
        return doPost(UID, password);
    }

    //使用post方式请求
    private String doPost(String uid, String password) throws IOException {
        String url = "http://10.71.29.171:9090/JSBKServer/login.do";

        HttpParams param = new BasicHttpParams();
        //设置请求的字符集
        HttpProtocolParams.setContentCharset(param, HTTP.UTF_8);
        //设置连接超时时间
        HttpConnectionParams.setConnectionTimeout(param, 3000);
        //设置服务器响应的超时时间
        HttpConnectionParams.setSoTimeout(param, 3000);
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", PlainSocketFactory.getSocketFactory(), 433));
        ClientConnectionManager coman = new ThreadSafeClientConnManager(param, registry);
        HttpClient httpClient = new DefaultHttpClient(coman, param);
        HttpPost httpPost = new HttpPost(url);
        NameValuePair UIDPair = new BasicNameValuePair("UID", uid);
        NameValuePair passwordPair = new BasicNameValuePair("password", password);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(UIDPair);
        params.add(passwordPair);
        httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        HttpResponse httpResponse = httpClient.execute(httpPost);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        String result = "";
        if (statusCode != HttpStatus.SC_OK) {
            result = "error";
        } else {
            result = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
        }
        return result;
    }

    //使用get方式请求
    private String doGet(String UID, String password) throws IOException {
        String url = "http://10.71.28.73:9090/JSBKServer/login.do?UID=" + UID + "&password=" + password;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        String result = "";
        if (statusCode != HttpStatus.SC_OK) {
            result = "error";
        } else {
            result = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
        }
        return result;
    }
}
