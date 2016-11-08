package com.recycle.demo;

import android.content.Context;

import com.example.demo.DecorAdapter;
import com.example.demo.ViewHolder;

/**
 * Create by pc-qing
 * On 2016/11/7 19:42
 * Copyright(c) 2016 TCL-MIG
 * Description
 */
public class MoreAdapter extends DecorAdapter<String> {
    public MoreAdapter(Context context) {
        super(context, R.layout.list_text);
    }

    @Override
    protected void convert(ViewHolder holder, String item) {
        holder.setText(R.id.text, item);
    }
}
