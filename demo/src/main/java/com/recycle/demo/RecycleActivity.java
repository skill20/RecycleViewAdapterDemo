package com.recycle.demo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demo.ViewHolder;
import com.example.recycleview.XRecycleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by pc-qing
 * On 2017/4/12 17:16
 * Copyright(c) 2017 XunLei
 * Description
 */
public class RecycleActivity extends AppCompatActivity {

    private XRecycleView mRecycleView;
    private Adapter adapterWrapper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);

        mRecycleView = (XRecycleView) findViewById(R.id.recycle_view);

        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        adapterWrapper = new Adapter(this);

        mRecycleView.setAdapter(adapterWrapper);

//        View header = LayoutInflater.from(this).inflate(R.layout.recyclerview_header, mRecycleView,false);
//        mRecycleView.addHeaderView(header);

        mRecycleView.setLoadingListener(new XRecycleView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        count = 10;
                        adapterWrapper.setData(getData(20));
                        mRecycleView.setLoadingMoreEnabled(true);
                        mRecycleView.refreshComplete();
                        adapterWrapper.notifyDataSetChanged();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        boolean b = isNetworkConnected(getApplication());

                        if (!b) {
                            mRecycleView.loadMoreFail();
                            return;
                        }

                        adapterWrapper.setMoreData(getData(count--));
                        mRecycleView.setLoadingMoreEnabled(count >= 7);
//                        mRecycleView.loadMoreComplete();
                        adapterWrapper.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });

        mRecycleView.setLoadMoreFailClickEnable();
        adapterWrapper.setData(getData(20));
    }

    int count = 10;

    public List<String> getData(int count) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {

            list.add("string---" + i);
        }
        return list;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空
            if (networkInfo != null)
                return networkInfo.isAvailable();
        }
        return false;
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private Context context;
        private List<String> mData;


        public Adapter(Context context) {
            this.context = context;
            mData = new ArrayList<>();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(viewType, parent, false);
            return new ViewHolder(context, v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.setText(R.id.text, mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.list_text;
        }

        public void setData(List<String> data) {
            mData.clear();
            mData.addAll(data);
        }

        public void setMoreData(List<String> data) {
            mData.addAll(data);
        }
    }
}
