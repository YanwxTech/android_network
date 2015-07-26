package com.yvan.androidhttpoperation.db;

import com.yvan.androidhttpoperation.entity.ThreadInfo;

import java.util.List;

/**
 * Created by Yvan on 2015/6/15.
 */
public interface ThreadDao {
    /**
     * 插入线程信息
     * @param threadInfo
     */
    void insertThread(ThreadInfo threadInfo);

    /**
     * 删除线程信息
     * @param url
     */
    void deleteThread(String url);

    /**
     * 更新线程信息
     * @param url
     * @param thread_id
     * @param finished
     */
    void updateThread(String url,int thread_id,int finished);

    /**
     * 根据Url查询获取其所有线程信息
     * @param url
     * @return
     */
    List<ThreadInfo> getAllThread(String url);

    /**
     * 判断线程是否已存在
     * @param url
     * @param thread_id
     * @return
     */
    boolean isExists(String url,int thread_id);


}
