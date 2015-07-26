package com.yvan.androidhttpoperation.net;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

/**
 * Created by Yvan on 2015/5/27.
 */
public class MyHttpClient {
    private static HttpClient client = null;

    public static synchronized HttpClient getHttpClient() {
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
        if (client == null) {
            client = new DefaultHttpClient(coman, param);
        }
        return client;
    }
}
