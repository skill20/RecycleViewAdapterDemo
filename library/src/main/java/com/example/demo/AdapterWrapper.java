package com.example.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * #author pc-qing
 * #date 2016/6/16 15:07
 * #since 6.0.0
 * #copyright TCL-MIG
 */
@Deprecated
public abstract class AdapterWrapper<T> extends RecyclerView.Adapter<ViewHolder> {

    private Context context;
    private List<T> mDataList;
    private LayoutInflater inflater;
    private int layoutResId;

    private static final int LOADING_VIEW = 0x00000111;
    private static final int EMPTY_VIEW = 0x00000222;
    private static final int FAIL_VIEW = 0x00000333;
    private static final int LOAD_MORE_VIEW = 0x00000444;
    private static final int CONTENT_VIEW = 0x00000555;

    private boolean requestLoad = true;
    private boolean loadMore = false;
    private boolean failLoadMore = false;
    private int type = LOADING_VIEW;
    private int pageSize;

    private MultiItemTypeSupport<T> itemTypeSupport;

    private OnRecycleViewRequestFailListener onFailListener;
    private OnRecyclerViewItemClickListener<T> itemClickListener;
    private OnRecycleViewLoadMoreFailClickListener onLoadMoreFailListener;

    public AdapterWrapper(Context context, int layoutResId) {
        this.context = context;
        mDataList = new ArrayList<>();
        this.layoutResId = layoutResId;
        inflater = LayoutInflater.from(context);
    }

    public AdapterWrapper(Context context, MultiItemTypeSupport<T> itemTypeSupport) {
        this(context, 0);
        this.itemTypeSupport = itemTypeSupport;
    }

    public interface MultiItemTypeSupport<T> {

        int getLayoutId(int itemType);

        /**
         * 不同的type对应不同的布局，不能跟默认的冲突
         * LOADING_VIEW、EMPTY_VIEW、FAIL_VIEW、LOAD_MORE_VIEW、CONTENT_VIEW
         * 根据数据源的不同，设置不同的布局
         *
         * @param position
         * @param t
         * @return
         */
        int getItemViewType(int position, T t);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = holderLayoutResId(viewType);

        if (layout == 0) {
            layout = statusHolderLayout(viewType);
        }

        if (layout == 0)
            throw new RuntimeException("no recycle view holder layout for viewType = " + viewType);

        View view = inflater.inflate(layout, parent, false);

        return createHolder(view, viewType);
    }

    private int statusHolderLayout(int viewType) {
        int layout = 0;
        switch (viewType) {
            case LOADING_VIEW:
                layout = R.layout.loading_request;
                break;
            case EMPTY_VIEW:
                layout = R.layout.empty_result;
                break;
            case FAIL_VIEW:
                layout = R.layout.fail_result;
                break;
            case LOAD_MORE_VIEW:
                layout = R.layout.load_more;
                break;
        }
        return layout;
    }

    private ViewHolder createHolder(View view, int viewType) {
        final ViewHolder holder = new ViewHolder(context, view);
        switch (viewType) {
            case FAIL_VIEW:
                initRequestFailListener(holder);
            case LOADING_VIEW:
            case EMPTY_VIEW:
                break;
            case LOAD_MORE_VIEW:
                initFailLoadMoreClickListener(holder);
                break;
            default:
                initItemClickListener(holder);
                break;
        }
        return holder;
    }

    private void initFailLoadMoreClickListener(final ViewHolder holder) {
        if (onLoadMoreFailListener != null) {
            holder.setOnClickListener(R.id.fail_text, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.setVisible(R.id.fail_text, false);
                    holder.setVisible(R.id.progress, true);
                    onLoadMoreFailListener.onLoadMoreFailClick();
                }
            });
        }
    }

    private int holderLayoutResId(int viewType) {
        if (mDataList.isEmpty() || viewType == LOAD_MORE_VIEW) {
            return 0;
        }

        if (itemTypeSupport == null) {
            return this.layoutResId;
        }

        return itemTypeSupport.getLayoutId(viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case FAIL_VIEW:
            case LOADING_VIEW:
            case EMPTY_VIEW:
                break;
            case LOAD_MORE_VIEW:
                if (failLoadMore) {
                    failLoadMore(holder);
                }
                break;
            default:
                T t = mDataList.get(position);
                convert(holder, t);
                break;
        }
    }

    private void failLoadMore(final ViewHolder holder) {
        holder.setVisible(R.id.fail_text, true);
        holder.setVisible(R.id.progress, false);
    }

    private void initItemClickListener(final ViewHolder holder) {
        if (itemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    itemClickListener.onItemClick(v, position, mDataList.get(position));
                }
            });
        }
    }

    private void initRequestFailListener(ViewHolder holder) {
        if (onFailListener != null) {
            holder.itemView.findViewById(R.id.fail_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    type = LOADING_VIEW;
                    notifyItemChanged(0);
                    onFailListener.onFailClick();
                }
            });
        }
    }

    protected abstract void convert(ViewHolder holder, T item);

    @Override
    public int getItemViewType(int position) {
        if (mDataList.isEmpty() && requestLoad) {
            return getType();
        }

        if (position >= mDataList.size()) {
            return LOAD_MORE_VIEW;
        }

        if (itemTypeSupport == null) {
            return super.getItemViewType(position);
        }

        return itemTypeSupport.getItemViewType(position, mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mDataList.isEmpty() && requestLoad) {
            return 1;
        }
        int count = mDataList.size();
        if (isLoadMore()) {
            count += 1;
        }
        return count;
    }

    private boolean isLoadMore() {
        return loadMore;
    }


    private int getType() {
        return type;
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        setFullSpan(holder);
    }

    protected void setFullSpan(RecyclerView.ViewHolder holder) {
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams && mDataList.isEmpty()) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            params.setFullSpan(true);
        }
    }

    /**
     * 设置是否显示自带加载，默认显示
     *
     * @param load
     */
    public void setRequestLoad(boolean load) {
        this.requestLoad = load;
    }

    /**
     * 打开加载更多，默认关闭
     *
     * @param loadMore
     * @param size     分页显示的个数
     */
    public void openLoadMore(boolean loadMore, int size) {
        this.loadMore = loadMore;
        this.pageSize = size;
    }


    public void setMoreData(List<T> list) {
        if (!loadMore) {
            return;
        }
        if (list != null) {
            loadMore = list.size() >= pageSize;
            mDataList.addAll(list);
        } else {
            failLoadMore = true;
        }
        notifyDataSetChanged();
    }

    /**
     * 设置数据，根据数据源自动识别fail、empty、content
     *
     * @param list
     */
    public void setDataList(List<T> list) {
        if (list == null) {
            type = FAIL_VIEW;
        } else if (list.isEmpty()) {
            type = EMPTY_VIEW;
        } else {
            type = CONTENT_VIEW;
            mDataList.clear();
            mDataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public interface OnRecycleViewRequestFailListener {
        void onFailClick();
    }

    public interface OnRecycleViewLoadMoreFailClickListener {
        void onLoadMoreFailClick();
    }

    public interface OnRecyclerViewItemClickListener<T> {
        void onItemClick(View view, int position, T t);
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener<T> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public void setOnRecycleViewRequestFailListener(OnRecycleViewRequestFailListener onFailListener) {
        this.onFailListener = onFailListener;
    }

    public void setOnRecycleViewLoadMoreFailClickListener(OnRecycleViewLoadMoreFailClickListener onLoadMoreFailListener) {
        this.onLoadMoreFailListener = onLoadMoreFailListener;
    }

}
