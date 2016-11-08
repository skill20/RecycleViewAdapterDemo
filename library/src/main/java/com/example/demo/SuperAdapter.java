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
public abstract class SuperAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private static final int LOAD_MORE_VIEW = 99;
    private int layoutResId;
    private Context context;
    private List<T> mDataList;
    private LayoutInflater inflater;

    private boolean canLoadMore = false;
    private boolean showLoadMoreProgress = true;

    private MultiItemTypeSupport<T> itemTypeSupport;

    private OnItemClickListener<T> onItemClickListener;
    private OnLoadMoreFailClickListener onLoadMoreFailListener;

    public SuperAdapter(Context context, int layoutResId) {
        this.context = context;
        mDataList = new ArrayList<>();
        this.layoutResId = layoutResId;
        inflater = LayoutInflater.from(context);
    }

    public SuperAdapter(Context context, MultiItemTypeSupport<T> itemTypeSupport) {
        this(context, 0);
        this.itemTypeSupport = itemTypeSupport;
    }

    public interface MultiItemTypeSupport<T> {

        int getLayoutId(int itemType);

        int getItemViewType(int position, T t);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = holderLayoutResId(viewType);

        if (viewType == LOAD_MORE_VIEW) {
            layout = R.layout.load_more_adv;
        }

        if (layout == 0)
            throw new RuntimeException("no recycle view holder layout for viewType = " + viewType);

        View view = inflater.inflate(layout, parent, false);

        return createHolder(view, viewType);
    }


    private ViewHolder createHolder(View view, int viewType) {
        final ViewHolder holder = new ViewHolder(context, view);
        switch (viewType) {
            case LOAD_MORE_VIEW:
                onLoadMoreFailClickListener(holder);
                break;
            default:
                onItemClickListener(holder);
                break;
        }
        return holder;
    }

    private void onLoadMoreFailClickListener(final ViewHolder holder) {
        if (onLoadMoreFailListener != null) {
            holder.setOnClickListener(R.id.fail_text, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoadMoreProgress = true;
                    updateLoadMoreUi(holder, true);
                    onLoadMoreFailListener.onLoadMoreFailClick();
                }
            });
        }
    }

    private void updateLoadMoreUi(ViewHolder holder, boolean showProgress) {
        holder.setVisible(R.id.fail_text, !showProgress);
        holder.setVisible(R.id.progress, showProgress);
    }

    private int holderLayoutResId(int viewType) {
        if (mDataList.isEmpty() || viewType == LOAD_MORE_VIEW) {
            return 0;
        }

        return itemTypeSupport == null ? layoutResId : itemTypeSupport.getLayoutId(viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case LOAD_MORE_VIEW:
                updateLoadMoreUi(holder, showLoadMoreProgress);
                break;
            default:
                T t = mDataList.get(position);
                convert(holder, t);
                break;
        }
    }

    private void onItemClickListener(final ViewHolder holder) {
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    onItemClickListener.onItemClick(v, position, mDataList.get(position));
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position >= mDataList.size()) {
            return LOAD_MORE_VIEW;
        }

        return itemTypeSupport == null ? super.getItemViewType(position) :
                itemTypeSupport.getItemViewType(position, mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        int count = mDataList.size();
        return canLoadMore ? count + 1 : count;
    }

    protected abstract void convert(ViewHolder holder, T item);
    public List<T> getItemData() {
        return mDataList;
    }


    public interface OnLoadMoreFailClickListener {
        void onLoadMoreFailClick();
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, int position, T t);
    }

    public void setOnItemClickListener(OnItemClickListener<T> itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    public void setOnLoadMoreFailListener(OnLoadMoreFailClickListener onLoadMoreFailListener) {
        this.onLoadMoreFailListener = onLoadMoreFailListener;
    }

    public void showLoadMoreProgress(boolean show) {
        if (showLoadMoreProgress != show) {
            showLoadMoreProgress = show;
        }
    }

    public void setMoreData(List<T> list) {
        setMoreData(list, canLoadMore);
    }

    public void setMoreData(List<T> list, boolean hasMore) {

        if (list == null || list.isEmpty()) {
            showLoadMoreProgress = false;
        } else {
            mDataList.addAll(list);
            showLoadMoreProgress = true;
        }

        canLoadMore = hasMore;

        notifyDataSetChanged();
    }

    public void setDataList(List<T> list) {
        setDataList(list, false);
    }

    public void setDataList(List<T> list, boolean hasMore) {
        if (list == null || list.isEmpty()) {
            return;
        }
        mDataList.clear();
        mDataList.addAll(list);

        canLoadMore = hasMore;

        notifyDataSetChanged();
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        setFullSpan(holder);
    }

    private void setFullSpan(RecyclerView.ViewHolder holder) {
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams && mDataList.isEmpty()) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            params.setFullSpan(true);
        }
    }

}
