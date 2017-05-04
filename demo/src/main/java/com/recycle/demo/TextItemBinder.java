package com.recycle.demo;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demo.ViewHolder;

import multitype.ItemViewBinder;

/**
 * Create by pc-qing
 * On 2017/4/24 10:33
 * Copyright(c) 2017 XunLei
 * Description
 */
public class TextItemBinder extends ItemViewBinder<String,ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.list_text, parent, false);
        return new ViewHolder(parent.getContext(),root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull String item) {
        holder.setText(R.id.text, item);
    }
}
