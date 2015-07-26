package com.yvan.androidhttpoperation.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Yvan on 2015/5/27.
 */
public class MyHttpUrlConnection {
    private static HttpURLConnection connection = null;

    public static synchronized HttpURLConnection getConnection(String url, String requestMethod) {
        try {
            URL httpUrl = new URL(url);
            connection = (HttpURLConnection) httpUrl.openConnection();

            connection.setConnectTimeout(3000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod(requestMethod);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
