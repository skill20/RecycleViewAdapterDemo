package com.recycle.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.demo.AdapterWrapper;
import com.example.demo.ViewHolder;
import com.recycle.demo.bean.MulBean;

import java.util.ArrayList;
import java.util.List;

/**
 * #author pc-qing
 * #date 2016/6/18 14:55
 * #since 6.0.0
 * #copyright TCL-MIG
 */
public class MulRecycleViewActivity extends AppCompatActivity {

    private RecyclerView recycleView;
    private AdapterWrapper<MulBean> wrapper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_def);

        recycleView = (RecyclerView) findViewById(R.id.recycle_view);
//        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        wrapper = new AdapterWrapper<MulBean>(this, new MutSupport()) {

            @Override
            protected void convert(ViewHolder holder, MulBean item) {
                if (item.type == 2) {
                    holder.setText(R.id.text, item.str);
                } else if (item.type == 3) {
                    holder.setText(R.id.text_mul, item.str);
                }
            }
        };

        recycleView.setAdapter(wrapper);

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                wrapper.setDataList(getData());
            }
        }, 3000);
    }

    public List<MulBean> getData() {
        ArrayList<MulBean> list = new ArrayList<>();

        MulBean mulBean;
        for (int i = 0; i < 20; i++) {
            mulBean = new MulBean();
            if (i % 2 == 0) {
                mulBean.str = "MulBean--" + i;
                mulBean.type = 2;
            } else {
                mulBean.str = "MulBean--" + i;
                mulBean.type = 3;
            }
            list.add(mulBean);
        }
        return list;
    }

    static class MutSupport implements AdapterWrapper.MultiItemTypeSupport<MulBean> {

        @Override
        public int getLayoutId(int itemType) {
            if (itemType == 2) {
                return R.layout.recyele_list_def;
            } else if (itemType == 3) {
                return R.layout.recyele_list_mul;
            }
            return 0;
        }

        @Override
        public int getItemViewType(int position, MulBean mulBean) {
            return mulBean.type;
        }
    }
}
