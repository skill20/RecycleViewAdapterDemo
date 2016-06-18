package com.recycle.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.demo.AdapterWrapper;
import com.example.demo.OnRecycleScrollListener;
import com.example.demo.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * #author pc-qing
 * #date 2016/6/18 14:55
 * #since 6.0.0
 * #copyright TCL-MIG
 */
public class DefRecycleViewActivity extends AppCompatActivity {

    private RecyclerView recycleView;
    private AdapterWrapper<String> adapterWrapper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_def);

        recycleView = (RecyclerView) findViewById(R.id.recycle_view);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.addOnScrollListener(mOnScrollListener);

        adapterWrapper = new AdapterWrapper<String>(this, R.layout.recyele_list_def) {

            @Override
            protected void convert(ViewHolder holder, String item) {
                holder.setText(R.id.text, item);
            }
        };

        adapterWrapper.setOnRecyclerViewItemClickListener(new AdapterWrapper.OnRecyclerViewItemClickListener<String>() {
            @Override
            public void onItemClick(View view, int position, String s) {
                Log.i("onItemClick", "position---" + position + "--data---" + s);
            }
        });

        adapterWrapper.setOnRecycleViewRequestFailListener(new AdapterWrapper.OnRecycleViewRequestFailListener() {
            @Override
            public void onFailClick() {
                getDelayData();
            }
        });
        adapterWrapper.openLoadMore(true, 9);
        recycleView.setAdapter(adapterWrapper);

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapterWrapper.setDataList(null);
            }
        }, 3000);
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

    boolean loadMore = true;
    private OnRecycleScrollListener mOnScrollListener = new OnRecycleScrollListener() {
        @Override
        public void onLoadMore() {

            if (!loadMore) {
                return;
            }

            Log.i("load more", "more");
            loadMore();
        }
    };

    int count = 10;

    private void loadMore() {
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapterWrapper.setMoreData(getData(count--));
            }
        }, 3000);
    }
}
