package com.example.recycleview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Create by pc-qing
 * On 2017/4/12 12:00
 * Copyright(c) 2017 XunLei
 * Description
 */
public class DefaultHeader extends LinearLayout implements BaseRefreshHeader {

    private static final String TAG = "DefaultHeader";

    private TextView msg;
    private int mState = STATE_NORMAL;
    private int mMeasuredHeight;
    private LinearLayout mContainer;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;
    private static final int ROTATE_ANIM_DURATION = 180;
    private View pullView;

    public DefaultHeader(Context context) {
        this(context, null);
    }

    public DefaultHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_header, this, false);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        setLayoutParams(lp);
        setPadding(0, 0, 0, 0);

        addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT, 0));
        setGravity(Gravity.BOTTOM);

        msg = (TextView) findViewById(R.id.msg);
        pullView = findViewById(R.id.pull_view);

        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredHeight();

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);


    }


    @Override
    public void onMove(float delta) {

        if (getVisibleHeight() > 0 || delta > 0) {
            setVisibleHeight((int) delta + getVisibleHeight());
            if (mState <= STATE_RELEASE_TO_REFRESH) { // 未处于刷新状态，更新箭头
                if (getVisibleHeight() > mMeasuredHeight) {
                    setState(STATE_RELEASE_TO_REFRESH);
                } else {
                    setState(STATE_NORMAL);
                }
            }
        }
    }

    @Override
    public boolean releaseAction() {
        boolean isOnRefresh = false;
        int height = getVisibleHeight();
        if (height == 0) // not visible.
            isOnRefresh = false;

        if (getVisibleHeight() > mMeasuredHeight && mState < STATE_REFRESHING) {
            setState(STATE_REFRESHING);
            isOnRefresh = true;
        }
        // refreshing and header isn't shown fully. do nothing.
        if (mState == STATE_REFRESHING && height <= mMeasuredHeight) {
            //return;
        }
        int destHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mState == STATE_REFRESHING) {
            destHeight = mMeasuredHeight;
        }
        smoothScrollTo(destHeight);

        return isOnRefresh;
    }

    private void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    @Override
    public void refreshComplete() {
        setState(STATE_DONE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                reset();
            }
        }, 200);
    }

    @Override
    public int getVisibleHeight() {
        return mContainer.getHeight();
    }

    @Override
    public int getState() {
        return mState;
    }

    private void setVisibleHeight(int height) {
        if (height < 0)
            height = 0;

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lp.height = height;
        mContainer.setLayoutParams(lp);

    }

    public void reset() {
        smoothScrollTo(0);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                setState(STATE_NORMAL);
            }
        }, 200);
    }

    public void setState(int state) {
        if (state == mState) {
            return;
        }

        switch (state) {
            case STATE_NORMAL:
                pullView.setVisibility(VISIBLE);
                if (mState == STATE_RELEASE_TO_REFRESH) {
                    pullView.startAnimation(mRotateDownAnim);
                }
                if (mState == STATE_REFRESHING) {
                    pullView.clearAnimation();
                }

                msg.setText(R.string.pull_to_refresh);
                break;
            case STATE_RELEASE_TO_REFRESH:
                pullView.clearAnimation();
                pullView.startAnimation(mRotateUpAnim);
                msg.setText(R.string.release_to_refresh);
                break;
            case STATE_REFRESHING:

                pullView.clearAnimation();
                pullView.setVisibility(View.GONE);
                msg.setText(R.string.refresh);
                break;
            case STATE_DONE:
                pullView.setVisibility(View.GONE);
                msg.setText(R.string.refresh_done);
                break;

            case STATE_AUTO:
                pullView.clearAnimation();
                pullView.setVisibility(View.GONE);
                msg.setText(R.string.refresh);
                smoothScrollTo(mMeasuredHeight);
                break;
            default:
        }
        mState = state;
    }
}
