package com.recycle.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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
public class StickRecycleViewActivity extends AppCompatActivity {


    public static final int FIRST_STICKY_VIEW = 10;
    public static final int HAS_STICKY_VIEW = 20;
    public static final int NONE_STICKY_VIEW = 30;

    private RecyclerView recycleView;
    private AdapterWrapper<MulBean> wrapper;
    private View include;
    private List<MulBean> dataList;
    private TextView textView;

    private String mDesc;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick);

        include = findViewById(R.id.include);
        recycleView = (RecyclerView) findViewById(R.id.recycle_view);
        textView = (TextView) include.findViewById(R.id.text);
        recycleView.setLayoutManager(new LinearLayoutManager(this));

        wrapper = new AdapterWrapper<MulBean>(this, new StickRecycleViewActivity.MutSupport()) {
            @Override
            protected void convert(ViewHolder holder, MulBean item) {
                if (item.type == 2) {
                    holder.setText(R.id.text, item.str);
                } else if (item.type == 3) {
                    holder.setText(R.id.text_mul, item.str);
                }

//                int position = holder.getAdapterPosition();
//                if (position == 0) {
//                    holder.itemView.setTag(FIRST_STICKY_VIEW);
//                } else {
//                    if (position % 5 == 0) {
//                        holder.itemView.setTag(HAS_STICKY_VIEW);
//                    } else {
//                        holder.itemView.setTag(NONE_STICKY_VIEW);
//                    }
//                }
//                holder.itemView.setContentDescription(item.strType);


            }

        };

        recycleView.setAdapter(wrapper);

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                dataList = getData();
                wrapper.setDataList(dataList);
                include.setVisibility(View.VISIBLE);
                textView.setText(getData().get(0).str);
            }
        }, 3000);


//        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                // Get the sticky information from the topmost view of the screen.
//                View stickyInfoView = recyclerView.findChildViewUnder(
//                        include.getMeasuredWidth() / 2, 5);
//
//                if (stickyInfoView != null && stickyInfoView.getContentDescription() != null) {
//                    textView.setText(String.valueOf(stickyInfoView.getContentDescription()));
//                }
//
//                // Get the sticky view's translationY by the first view below the sticky's height.
//                View transInfoView = recyclerView.findChildViewUnder(
//                        include.getMeasuredWidth() / 2, include.getMeasuredHeight() + 1);
//
//                if (transInfoView != null && transInfoView.getTag() != null) {
//                    int transViewStatus = (int) transInfoView.getTag();
//                    int dealtY = transInfoView.getTop() - include.getMeasuredHeight();
//                    if (transViewStatus == HAS_STICKY_VIEW) {
//                        // If the first view below the sticky's height scroll off the screen,
//                        // then recovery the sticky view's translationY.
//                        if (transInfoView.getTop() > 0) {
//                            include.setTranslationY(dealtY);
//                        } else {
//                            include.setTranslationY(0);
//                        }
//                    } else if (transViewStatus == NONE_STICKY_VIEW) {
//                        include.setTranslationY(0);
//                    }
//                }
//            }
//        });
        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                if (manager instanceof LinearLayoutManager) {

                    int position =
                            ((RecyclerView.LayoutParams) manager.getChildAt(0).getLayoutParams())
                                    .getViewAdapterPosition();

                    int typePosition = findStickTypePosition(recyclerView, position);
                    if (typePosition < 0) {
                        return;
                    }

                    int itemViewType = recyclerView.getAdapter().getItemViewType(position + 1);

                    if (itemViewType == 2) {
                        View view = manager.findViewByPosition(position);
                        int top = view.getTop();
                        include.setY(top);

                    } else {
                        include.setY(0);
                    }

                    textView.setText(wrapper.getmDataList().get(typePosition).str);

                }

            }

            private int findStickTypePosition(RecyclerView parent, int fromPosition) {
                RecyclerView.Adapter adapter = parent.getAdapter();
                if (fromPosition > adapter.getItemCount() || fromPosition < 0) {
                    return -1;
                }

                for (int position = fromPosition; position >= 0; position--) {
                    int viewType = adapter.getItemViewType(position);
                    if (viewType == 2) {
                        return position;
                    }
                }

                return -1;
            }
        });
    }

    private List<MulBean> getData() {
        ArrayList<MulBean> list = new ArrayList<>();

        MulBean mulBean;
        String type = "";
        for (int i = 0; i < 20; i++) {
            mulBean = new MulBean();
            if (i % 5 == 0) {
                mulBean.str = "MulBean--" + i;
                mulBean.type = 2;
                type = "MulBean--" + i;
            } else {
                mulBean.str = "MulBean--" + i;
                mulBean.type = 3;
            }
            mulBean.strType = type;

            list.add(mulBean);
        }
        return list;
    }

    private static class MutSupport implements AdapterWrapper.MultiItemTypeSupport<MulBean> {

        @Override
        public int getLayoutId(int itemType) {
            if (itemType == 2) {
                return R.layout.recycle_list_def;
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
