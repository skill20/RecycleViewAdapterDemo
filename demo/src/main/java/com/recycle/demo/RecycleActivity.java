package com.recycle.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.example.recycleview.SuperRecycleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by pc-qing
 * On 2017/4/12 17:16
 * Copyright(c) 2017 XunLei
 * Description
 */
public class RecycleActivity extends AppCompatActivity {

    private SuperRecycleView mRecycleView;
    private MoreAdapter adapterWrapper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);

        mRecycleView = (SuperRecycleView) findViewById(R.id.recycle_view);

        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        adapterWrapper = new MoreAdapter(this);

        adapterWrapper.setDataList(getData(30));
        mRecycleView.setAdapter(adapterWrapper);

//        View header = LayoutInflater.from(this).inflate(R.layout.recyclerview_header, mRecycleView,false);
//        mRecycleView.addHeaderView(header);

        mRecycleView.setLoadingListener(new SuperRecycleView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecycleView.setLoadingMoreEnabled(true);
                        mRecycleView.refreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapterWrapper.setMoreData(getData(10));
                        mRecycleView.setLoadingMoreEnabled(false);
                    }
                }, 1000);
            }
        });

    }

    private void getDelayData() {
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapterWrapper.setDataList(getData(20));
            }
        }, 3000);
    }

    public List<String> getData(int count) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {

            list.add("string---" + i);
        }
        return list;
    }
}
