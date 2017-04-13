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
import android.util.Log;

import com.example.demo.OnRecycleScrollListener;
import com.example.demo.SuperAdapter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

/**
 * #author pc-qing
 * #date 2016/6/18 14:55
 * #since 6.0.0
 * #copyright TCL-MIG
 */
public class LoadMoreRecycleViewActivity extends AppCompatActivity {
    private RecyclerView recycleView;
    private MoreAdapter moreAdapter;
    private SpringView mSpringView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_def);

        recycleView = (RecyclerView) findViewById(R.id.recycle_view);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.addOnScrollListener(mOnScrollListener);

        mSpringView = (SpringView) findViewById(R.id.spring_view);
        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setHeader(new DefaultHeader(this));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSpringView.onFinishFreshAndLoad();
                    }
                }, 1000);
            }

            @Override
            public void onLoadmore() {

            }
        });


        moreAdapter = new MoreAdapter(this);
        recycleView.setAdapter(moreAdapter);

        moreAdapter.setDataList(getData(20), true);
        moreAdapter.setOnLoadMoreFailListener(new SuperAdapter.OnLoadMoreFailClickListener() {
            @Override
            public void onLoadMoreFailClick() {
                loadMore();
            }
        });
    }

    private void getDelayData() {
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                moreAdapter.setDataList(getData(20));
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

    boolean loadMore = true;
    private OnRecycleScrollListener mOnScrollListener = new OnRecycleScrollListener() {
        @Override
        public void onLoadMore() {
            loadMore();
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            moreAdapter.showLoadMoreProgress(true);
        }
    };

    int count = 10;

    private void loadMore() {

        if (!loadMore) {
            Log.i("load more", "more false");
            return;
        }

        loadMore = false;

        if (!isNetworkConnected(getApplication())) {
            Log.i("load more", "no net work");
            getWindow().getDecorView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    moreAdapter.setMoreData(null);
                    loadMore = true;
                }
            }, 3000);
            return;
        }


        Log.i("load more", "more true");
        loadMore();
        loadMore = true;

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadMore = count > 8;
                moreAdapter.setMoreData(getData(--count), loadMore);
            }
        }, 3000);
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

}
