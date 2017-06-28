package com.recycle.demo.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Create by pc-qing
 * On 2017/6/28 15:07
 * Copyright(c) 2017 XunLei
 * Description
 */
public class CommonViewPagerAdapter<T> extends PagerAdapter {

    private List<T> mDataList;
    private PagerHolderCreator mCreator;//ViewHolder生成器
    private View mCurrentView;
    private boolean mCycle;

    public CommonViewPagerAdapter(List<T> list, PagerHolderCreator mCreator) {
        this.mDataList = list;
        this.mCreator = mCreator;
    }

    public void setRecycle(boolean recycle) {
        mCycle = recycle;
    }

    @Override
    public int getCount() {
        return mCycle ? Short.MAX_VALUE : mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int pos = getCurrentPosition(position);
        View view = getView(pos, container);
        container.addView(view);
        return view;
    }

    private int getCurrentPosition(int position) {
        return mCycle ? position % mDataList.size() : position;
    }

    private View getView(int position, ViewGroup container) {
        ViewPagerHolder holder = mCreator.onCreateViewHolder();
        Context context = container.getContext();
        View view = holder.createView(context);
        holder.onBindViewHolder(context, view, mDataList.get(position), position);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentView = (View) object;
    }

    public View getPrimaryItem() {
        return mCurrentView;
    }
}
