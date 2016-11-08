package com.recycle.demo;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Create by pc-qing
 * On 2016/11/3 10:15
 * Copyright(c) 2016 TCL-MIG
 * Description
 */
public class StickHeaderItemDecoration extends RecyclerView.ItemDecoration {


    private Rect mRect;
    private RecyclerView.Adapter mAdapter;
    private final SparseArray<StickTypeCreator> mStickTypeFactories = new SparseArray<>();

    public StickHeaderItemDecoration() {

    }

    @Override

    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        if (mAdapter == null) {
            mAdapter = parent.getAdapter();
        }

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager == null || layoutManager.getChildCount() <= 0) {
            return;
        }

        int firstVisiblePosition =
                ((RecyclerView.LayoutParams) layoutManager.getChildAt(0).getLayoutParams())
                        .getViewAdapterPosition();

        int stickTypePosition = findStickTypePosition(parent, firstVisiblePosition);

        if (stickTypePosition >= 0 && firstVisiblePosition >= stickTypePosition) {

            int viewType = mAdapter.getItemViewType(stickTypePosition);

            RecyclerView.ViewHolder viewHolder = mAdapter.createViewHolder(parent, viewType);
            View itemView = viewHolder.itemView;

            mAdapter.bindViewHolder(viewHolder, stickTypePosition);

            measureStickView(parent, itemView);

            itemView.layout(
                    mRect.left,
                    0,
                    itemView.getMeasuredWidth() - mRect.right,
                    itemView.getMeasuredHeight());

            int y = itemView.getTop() + itemView.getHeight();
            View behindView = parent.findChildViewUnder(c.getWidth() / 2, y + 1);
            int mStickTop;
            if (isStickView(parent, behindView)) {
                mStickTop = behindView.getTop() - itemView.getHeight();
            } else {
                mStickTop = 0;
            }

            c.clipRect(mRect, Region.Op.UNION);
            c.translate(mRect.left, mStickTop);
            itemView.draw(c);
        }

    }

    private void measureStickView(RecyclerView parent, View itemView) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(layoutParams);
        }
        mRect = new Rect(layoutParams.leftMargin, 0, layoutParams.rightMargin, layoutParams.bottomMargin);

        if (itemView.getWidth() > 0 && itemView.getHeight() > 0) {
            return;
        }
        // 测量高度
        int heightMode = View.MeasureSpec.getMode(layoutParams.height);
        int heightSize = View.MeasureSpec.getSize(layoutParams.height);

        if (heightMode == View.MeasureSpec.UNSPECIFIED) {
            heightMode = View.MeasureSpec.EXACTLY;
        }

        int maxHeight = parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom();
        if (heightSize > maxHeight) {
            heightSize = maxHeight;
        }

        int widthSize = View.MeasureSpec.getSize(layoutParams.width);
        int maxWidth = parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight();
        if (widthSize > maxWidth) {
            widthSize = maxWidth;
        }
        int ws = View.MeasureSpec.makeMeasureSpec(widthSize, View.MeasureSpec.EXACTLY);
        int hs = View.MeasureSpec.makeMeasureSpec(heightSize, heightMode);
        itemView.measure(ws, hs);
    }

    private int findStickTypePosition(RecyclerView parent, int fromPosition) {
        if (fromPosition > mAdapter.getItemCount() || fromPosition < 0) {
            return -1;
        }

        for (int position = fromPosition; position >= 0; position--) {
            final int viewType = mAdapter.getItemViewType(position);
            if (isStickViewType(parent, position, viewType)) {
                return position;
            }
        }

        return -1;
    }

    private boolean isStickViewType(RecyclerView parent, int adapterPosition, int viewType) {
        StickTypeCreator stickTypeCreator = mStickTypeFactories.get(viewType);
        return stickTypeCreator != null && stickTypeCreator.onCreate(parent, adapterPosition);
    }

    private boolean isStickView(RecyclerView parent, View v) {
        int position = parent.getChildAdapterPosition(v);
        return position != RecyclerView.NO_POSITION &&
                isStickViewType(parent, position, mAdapter.getItemViewType(position));

    }

    public void registerStickType(int itemType, StickTypeCreator stickTypeCreator) {
        mStickTypeFactories.put(itemType, stickTypeCreator);
    }

    interface StickTypeCreator {
        boolean onCreate(RecyclerView parent, int adapterPosition);
    }
}
