package com.yvan.androidhttpoperation.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Yvan on 2015/5/27.
 */
public class NetOperationImpl implements NetOperation {
    private static final String hostUrl = "http://172.30.220.1:9090/SSH";

    @Override
    public String userLogin(String UID, String password) throws Exception {
        return doPost(UID, password);
    }

    private String doPost(String uid, String password) throws IOException, JSONException {
        String url = hostUrl + "/login.do";

        HttpClient httpClient = MyHttpClient.getHttpClient();
        HttpPost httpPost = new HttpPost(url);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("UID", uid);
        jsonObject.put("password", password);
        NameValuePair dataPair = new BasicNameValuePair("data", jsonObject.toString());
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(dataPair);
        httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        HttpResponse httpResponse = httpClient.execute(httpPost);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        String result = "";
        if (statusCode != HttpStatus.SC_OK) {
            result = "error";
        } else {
            result = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
        }
        Log.i("Result", result);
        JSONObject receiveObject = new JSONObject(result);
        result = receiveObject.getString("content");
        return result;
    }

    private String doGet(String UID, String password) throws IOException {
        String url = hostUrl + "/login.do?UID=" + UID + "&password=" + password;
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

    @Override
    public String userRegister(String UID, String password, String email, List<String> interests) throws Exception {
        String url = hostUrl + "/register.do";
        String result = "";

        HttpClient httpClient = MyHttpClient.getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        //封装数据成json格式
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

        NameValuePair data = new BasicNameValuePair("data", jsonObject.toString());
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
        JSONObject receiveObject = new JSONObject(result);
        result = receiveObject.getString("content");
//        int responseCode = receiveObject.getInt("code");
//        Log.i("JsonResult", receiveObject.toString());
//        Log.i("registerResponseCode", responseCode + "");

        return result;
    }

    @Override
    public Bitmap loadImg(String picId) throws Exception {
        String url = hostUrl + "/download?id="+picId;
        Log.i("URL", url);
        Bitmap bitmap = null;
        BufferedInputStream bis = null;
        HttpURLConnection connection = null;
        try {
            connection = MyHttpUrlConnection.getConnection(url, "GET");
            bis = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(bis);
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return bitmap;
    }

    @Override
    public String upload(InputStream inputStream, Map<String, String> data) throws Exception {
        String url = hostUrl + "/upload.do";

        HttpClient httpClient = MyHttpClient.getHttpClient();
        HttpPost httpPost = new HttpPost(url);

        /*
        ʹ使用httpmime实现文件上传
         */

        MultipartEntity multipartEntity = new MultipartEntity();
        //封装普通字符数据
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            multipartEntity.addPart(key, new StringBody(value, Charset.forName("UTF-8")));
        }
        String fileName = data.get("fileName");
        //封装二进制流数据
        multipartEntity.addPart("file", new InputStreamBody(inputStream, "multipart/form-data", fileName));

        httpPost.setEntity(multipartEntity);

        HttpResponse httpResponse = httpClient.execute(httpPost);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        String result = "";
        if (statusCode != HttpStatus.SC_OK) {
            throw new ServiceException();
        } else {
            result = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
        }
        return result;
    }

    @Override
    public BufferedInputStream download() throws Exception {
        String url = hostUrl + "/download.do";
        String result = "";
        BufferedInputStream bis = null;
        //HttpURLConnection connection = null;
        HttpClient httpClient = MyHttpClient.getHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        ;
//            URL httpUrl = new URL(url);
//            connection = (HttpURLConnection) httpUrl.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setConnectTimeout(10000);
//            connection.setReadTimeout(20000);
//            connection.setDoInput(true);
//            connection.connect();
        // int responseCode = connection.getResponseCode();
        int responseCode = httpResponse.getStatusLine().getStatusCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new ServiceException();
        } else {
            //bis = new BufferedInputStream(connection.getInputStream());

            bis = new BufferedInputStream(httpResponse.getEntity().getContent());
        }
        return bis;
    }
}
