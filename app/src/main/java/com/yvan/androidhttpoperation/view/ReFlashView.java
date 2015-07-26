package com.yvan.androidhttpoperation.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yvan.androidhttpoperation.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Yvan on 2015/6/9.
 */
public class ReFlashView extends ListView implements AbsListView.OnScrollListener {
    private View header;// 顶部布局文件；
    private int headerHeight;// 顶部布局文件的高度；
    private int firstVisibleItem;// 当前第一个可见的item的位置；
    private int scrollState;// listView 当前滚动状态；
    private boolean isRemark;// 标记，当前是在listView最顶端摁下的；
    private int startY;// 摁下时的Y值；

    private int state;// 当前的状态；
    private static final int NONE = 0;// 正常状态；
    private static final int PULL = 1;// 提示下拉状态；
    private static final int RELEASE = 2;// 提示释放状态；
    private static final int REFLASHING = 3;// 刷新状态；

    public interface IReFlashListener {
        void onReFlash();
    }

    private IReFlashListener reFlashListener;// 刷新数据的接口

    public void setReFlashListener(IReFlashListener reFlashListener) {
        this.reFlashListener = reFlashListener;
    }

    public ReFlashView(Context context) {
        this(context, null);
    }

    public ReFlashView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReFlashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        header = LayoutInflater.from(context).inflate(R.layout.header_layout, null);
        measureView(header);
        headerHeight = header.getMeasuredHeight();
        Log.i("tag", "headerHeight = " + headerHeight);
        topPadding(-headerHeight);
        this.addHeaderView(header);
        this.setOnScrollListener(this);
    }


    /**
     * 通知父布局，占用的宽，高；
     *
     * @param view
     */
    private void measureView(View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int width = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
            int height = 0;
            int tempHeight = lp.height;
            if (tempHeight > 0) {
                height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
            } else {
                height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            }
            view.measure(width, height);
        }
    }

    /**
     * 设置header 布局 上边距；
     *
     * @param topPadding
     */
    private void topPadding(int topPadding) {
        header.setPadding(header.getPaddingLeft(), topPadding, header.getPaddingRight(), header.getPaddingBottom());
        header.invalidate();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
        Log.i("scrollState", scrollState + "");
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
        Log.i("firstVisibleItem", firstVisibleItem + "");
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstVisibleItem == 0) {
                    isRemark = true;
                    startY = (int) event.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                onMove(event);
                break;
            case MotionEvent.ACTION_UP:
                if (state == RELEASE) {
                    state = REFLASHING;
                    // 加载最新数据；
                    reFlashViewByState();
                    reFlashListener.onReFlash();
                } else if (state == PULL) {
                    state = NONE;
                    isRemark = false;
                    reFlashViewByState();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 判断移动过程操作；
     *
     * @param event
     */

    private void onMove(MotionEvent event) {
        if (!isRemark) {
            return;
        }
        int tempY = (int) event.getY();
        int space = tempY - startY;
        int topPadding = space - headerHeight;
        switch (state) {
            case NONE:
                Log.d("topPadding", topPadding + "");
                if (space > 0) {
                    state = PULL;
                    reFlashViewByState();
                }
                break;
            case PULL:
                Log.d("topPadding", topPadding + "");
                topPadding(topPadding);
                if (space >= (headerHeight + 30)
                        && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    state = RELEASE;
                    reFlashViewByState();
                }
                break;
            case RELEASE:
                Log.d("topPadding", topPadding + "");
                topPadding(topPadding);
                if (space < (headerHeight + 30)) {
                    state = PULL;
                    reFlashViewByState();
                } else if (space <= 0) {
                    state = NONE;
                    isRemark = false;
                    reFlashViewByState();
                }
                break;
        }
    }

    private void reFlashViewByState() {
        TextView tip = (TextView) header.findViewById(R.id.tip);
        ImageView arrow = (ImageView) header.findViewById(R.id.arrow);
        ProgressBar progress = (ProgressBar) header.findViewById(R.id.progress);
        RotateAnimation anim = new RotateAnimation(0, 180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(500);
        anim.setFillAfter(true);
        RotateAnimation anim1 = new RotateAnimation(180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim1.setDuration(500);
        anim1.setFillAfter(true);
        switch (state) {
            case NONE:
                arrow.clearAnimation();
                topPadding(-headerHeight);
                break;

            case PULL:
                arrow.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                tip.setText("下拉可以刷新！");
                arrow.clearAnimation();
                arrow.setAnimation(anim1);
                break;
            case RELEASE:
                arrow.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                tip.setText("松开可以刷新！");
                arrow.clearAnimation();
                arrow.setAnimation(anim);
                break;
            case REFLASHING:
                topPadding(50);
                arrow.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                tip.setText("正在刷新...");
                arrow.clearAnimation();
                break;
        }
    }

    private void setHeaderEnable(boolean enable, View view) {
        if (!enable) this.removeHeaderView(view);
        else this.addHeaderView(view);
    }

    public void reFlashComplete() {
        state = NONE;
        isRemark = false;
        reFlashViewByState();
        TextView lastUpdateTime = (TextView) header
                .findViewById(R.id.lastupdate_time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        Date date = new Date();
        String time = format.format(date);
        lastUpdateTime.setText(time);
    }
}
