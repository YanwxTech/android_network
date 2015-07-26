package com.yvan.androidhttpoperation.net;

import android.graphics.Bitmap;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by Yvan on 2015/5/27.
 */
public interface NetOperation {
    String userLogin(String UID, String password) throws Exception;

    String userRegister(String UID, String password, String email, List<String> interests) throws Exception;

    Bitmap loadImg(String picId) throws Exception;

    String upload(InputStream inputStream, Map<String, String> data) throws Exception;

    BufferedInputStream download() throws Exception;
}
