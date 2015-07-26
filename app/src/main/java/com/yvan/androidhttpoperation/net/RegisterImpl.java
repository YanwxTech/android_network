package com.yvan.androidhttpoperation.net;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yvan on 2015/5/27.
 */
public class RegisterImpl implements Register {


    @Override
    public String userRegister(String UID, String password, String email, List<String> interests) throws Exception {
        String url = "http://10.71.25.184:9090/JSBKServer/register.do";
        String result = "";

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
        ////封装数据成json格式
        //-----------------------------------------------
        //json对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("UID", UID);
        jsonObject.put("password", password);
        jsonObject.put("email", email);
        //json数组
        JSONArray jsonArray = new JSONArray();
        if (interests != null && interests.size() > 0) {
            for (String interest : interests) {
                jsonArray.put(interest);
            }
        }
        jsonObject.put("interests", jsonArray);
        //-----------------------------------------------=

        NameValuePair data = new BasicNameValuePair("DATA", jsonObject.toString());
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(data);
        httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        HttpResponse httpResponse = httpClient.execute(httpPost);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            result = "error";
        } else {
            result = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
        }

        return result;
    }
}
