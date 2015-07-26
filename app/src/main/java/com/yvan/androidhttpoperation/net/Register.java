package com.yvan.androidhttpoperation.net;

import java.util.List;

/**
 * Created by Yvan on 2015/5/27.
 */
public interface Register {
    String userRegister(String UID, String password, String email, List<String> interests) throws Exception;
}
