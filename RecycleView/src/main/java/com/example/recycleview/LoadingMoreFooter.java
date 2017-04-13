package com.example.recycleview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Create by pc-qing
 * On 2017/4/12 14:02
 * Copyright(c) 2017 XunLei
 * Description
 */
public class LoadingMoreFooter extends LinearLayout {

    public final static int STATE_LOADING = 0;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NOMORE = 2;

    private TextView mText;
    private View mIvProgress;

    public LoadingMoreFooter(Context context) {
        this(context, null);
    }

    public LoadingMoreFooter(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingMoreFooter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    public void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_footer, this);
        mText = (TextView) findViewById(R.id.msg);
        mIvProgress = findViewById(R.id.iv_progress);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                mIvProgress.setVisibility(View.VISIBLE);
                mText.setText(R.string.loading);
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                mText.setText(R.string.loading);
                this.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                mText.setText(R.string.no_more_data);
                mIvProgress.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
        }
    }
}
