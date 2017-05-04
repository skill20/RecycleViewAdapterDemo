package com.recycle.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.recycle.demo.statelayout.MultipleStateLayout;

/**
 * Create by pc-qing
 * On 2017/5/4 16:23
 * Copyright(c) 2017 XunLei
 * Description
 */
public class StateActivity extends AppCompatActivity implements View.OnClickListener {

    private MultipleStateLayout multipleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);

        multipleView = (MultipleStateLayout) findViewById(R.id.multiple);
        findViewById(R.id.loading).setOnClickListener(this);
        findViewById(R.id.error).setOnClickListener(this);
        findViewById(R.id.empty).setOnClickListener(this);
        findViewById(R.id.no_net).setOnClickListener(this);
        findViewById(R.id.content).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loading:
                multipleView.showLoading();
                break;

            case R.id.error:
                multipleView.showError();
                break;

            case R.id.empty:
                multipleView.showEmpty();
                break;

            case R.id.no_net:
                multipleView.showNoNetwork();
                break;

            case R.id.content:
                multipleView.showContent();
                break;
        }
    }
}
