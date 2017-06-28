package com.recycle.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.recycle.demo.viewpager.CommonViewPagerAdapter;
import com.recycle.demo.viewpager.PagerHolderCreator;
import com.recycle.demo.viewpager.ViewPagerHolder;

import java.util.ArrayList;

/**
 * Create by pc-qing
 * On 2017/6/28 15:00
 * Copyright(c) 2017 XunLei
 * Description
 */
public class ViewPagerActivity extends AppCompatActivity {


    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        initViewPager();
    }

    private void initViewPager() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(R.drawable.ic_flash_img1);
        list.add(R.drawable.ic_flash_img2);
        list.add(R.drawable.ic_flash_img3);
        CommonViewPagerAdapter<Integer> adapter = new CommonViewPagerAdapter<>(list, new PagerHolderCreator() {
            @Override
            public ViewPagerHolder onCreateViewHolder() {
                return new ViewImageHolder();
            }
        });
        adapter.setRecycle(true);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(Short.MAX_VALUE / 2 - Short.MAX_VALUE / 2 % list.size());
    }


}
