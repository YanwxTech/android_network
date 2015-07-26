package com.yvan.androidhttpoperation.adapter;

import android.content.Context;
import android.widget.TextView;

import com.yvan.androidhttpoperation.R;
import com.yvan.androidhttpoperation.entity.SessionBean;
import com.yvan.androidhttpoperation.utils.CommonAdapter;
import com.yvan.androidhttpoperation.utils.ViewHolder;

import java.util.List;

/**
 * Created by Yvan on 2015/6/10.
 */
public class SessionAdapter extends CommonAdapter<SessionBean> {
    public SessionAdapter(Context context, List<SessionBean> mData, int dataLayout) {
        super(context, mData, dataLayout);
    }

    @Override
    public void convert(ViewHolder viewHolder, SessionBean sessionBean) {
        ((TextView) viewHolder.getView(R.id.id_text_uid)).setText(sessionBean.getUID());
        ((TextView) viewHolder.getView(R.id.id_text_title)).setText(sessionBean.getTitle());
        ((TextView) viewHolder.getView(R.id.id_text_simple_content)).setText(sessionBean.getContent());
        ((TextView) viewHolder.getView(R.id.id_text_commentcount)).setText(sessionBean.getCommentCount() + "");
        ((TextView) viewHolder.getView(R.id.id_text_praisecount)).setText(sessionBean.getPraiseCount() + "");

    }
}
