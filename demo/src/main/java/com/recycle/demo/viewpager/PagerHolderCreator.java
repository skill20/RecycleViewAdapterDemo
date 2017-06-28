package com.recycle.demo.viewpager;

/**
 * Create by pc-qing
 * On 2017/6/28 15:08
 * Copyright(c) 2017 XunLei
 * Description
 */
public interface PagerHolderCreator<VH extends ViewPagerHolder> {
    VH onCreateViewHolder();
}
