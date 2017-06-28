package com.recycle.demo;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.recycle.demo.viewpager.ViewPagerHolder;

/**
 * Create by pc-qing
 * On 2017/6/28 15:20
 * Copyright(c) 2017 XunLei
 * Description
 */
public class ViewImageHolder implements ViewPagerHolder<Integer> {

    private ImageView mImageView;

    @Override
    public View createView(Context context) {
        View v = View.inflate(context, R.layout.holder_view_pager, null);
        mImageView = (ImageView) v.findViewById(R.id.image_view);
        return v;
    }

    @Override
    public void onBindViewHolder(Context context, View v, Integer data, int position) {
        mImageView.setImageResource(data);
    }
}
