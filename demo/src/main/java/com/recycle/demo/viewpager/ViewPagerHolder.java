package com.recycle.demo.viewpager;

import android.content.Context;
import android.view.View;

/**
 * Create by pc-qing
 * On 2017/6/28 15:04
 * Copyright(c) 2017 XunLei
 * Description
 */
public interface ViewPagerHolder<T> {

    View createView(Context context);

    void onBindViewHolder(Context context, View v, T data, int position);
}
