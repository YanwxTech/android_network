package com.yvan.androidhttpoperation.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.yvan.androidhttpoperation.R;
import com.yvan.androidhttpoperation.adapter.SessionAdapter;
import com.yvan.androidhttpoperation.entity.SessionBean;
import com.yvan.androidhttpoperation.view.PullView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yvan on 2015/6/9.
 */
public class ListViewActivity extends Activity implements PullView.IPullListener {
    private PullView pullView;
    private List<SessionBean> beanList;
    private SessionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout);
        beanList=new ArrayList<>();
        pullView = (PullView) findViewById(R.id.list_view);
        pullView.setPullListener(this);
        getBeanList();
        adapter=new SessionAdapter(this,beanList,R.layout.session_item);
        pullView.setAdapter(adapter);

    }

    private void getBeanList() {
        for (int i = 0; i < 5; i++) {
            SessionBean bean = new SessionBean();
            bean.setUID("yvan001"+i);
            bean.setTitle("安卓学习");
            bean.setContent("内容概要。。。。内容概要。。。。内容概要。。。。内容概要。。。。");
            bean.setCommentCount(5+i);
            bean.setPraiseCount(3+i);
            beanList.add(bean);
            bean = new SessionBean();
            bean.setUID("yvan002"+i);
            bean.setTitle("安卓学习1");
            bean.setContent("内容。。。。内容。。。。内容。。。。内容。。。。");
            bean.setCommentCount(6+i);
            bean.setPraiseCount(9+i);
            beanList.add(bean);
            bean = new SessionBean();
            bean.setUID("yvan003"+i);
            bean.setTitle("安卓学习2");
            bean.setContent("内容123。。。。内容123。。。。内容123。。。。内容123。。。。");
            bean.setCommentCount(8+i);
            bean.setPraiseCount(3+i);
            beanList.add(bean);
        }
    }


    @Override
    public void onReFlash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SessionBean bean = new SessionBean();
                bean.setUID("newHeadItem");
                bean.setTitle("安卓学习");
                bean.setContent("内容概要。。。。内容概要。。。。内容概要。。。。内容概要。。。。");
                bean.setCommentCount(5);
                bean.setPraiseCount(3);
                beanList.add(0,bean);
                adapter.notifyDataSetChanged();
                pullView.reFlashComplete();
            }
        }, 2000);

    }

    @Override
    public void onReLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < 5; i++) {
                    SessionBean bean = new SessionBean();
                    bean.setUID("newFootItem" + i);
                    bean.setTitle("安卓学习");
                    bean.setContent("内容概要。。。。内容概要。。。。内容概要。。。。内容概要。。。。");
                    bean.setCommentCount(5);
                    bean.setPraiseCount(3);
                    beanList.add(bean);
                }
                adapter.notifyDataSetChanged();
                pullView.reLoadComplete();
            }
        }, 2000);
    }
}
