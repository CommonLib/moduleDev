package com.pwc.sdc.recruit.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.pwc.sdc.recruit.base.BaseRecycleAdapter;

/**
 * @author:dongpo 创建时间: 9/9/2016
 * 描述:
 * 修改:
 */
public class PtrLoadMoreRecycleView extends RecyclerView {
    private static final int STATE_COMPLETE = 1;
    private static final int STATE_LOADING = 2;
    private OnPullLoadMoreListener mOnPullLoadMoreListener;
    private LinearLayoutManager mLayoutManager;
    private PtrLoadMoreAdapter mAdapter;
    private int mBottomVisiblePosition;
    private int mLastBottomPosition;
    private int mLoadMoreState;

    public PtrLoadMoreRecycleView(Context context) {
        super(context);
    }

    public PtrLoadMoreRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if(layout instanceof StaggeredGridLayoutManager){
            throw new IllegalArgumentException("PtrLoadMoreRecycleView is not support StaggeredGridLayoutManager");
        }
        mLayoutManager = (LinearLayoutManager)layout;
        super.setLayoutManager(layout);
    }

    /**
     * @param adapter 对adapter的初始化必须在setAdapter之前
     */
    public void setAdapter(final PtrLoadMoreAdapter adapter) {
        super.setAdapter(adapter);
        View pullRefreshHeaderView = adapter.getPullRefreshView(this);
        if (pullRefreshHeaderView != null) {
            mAdapter.addHeader(pullRefreshHeaderView);
        }
        View loadMoreFooterView = adapter.getLoadMoreView(this);
        if (loadMoreFooterView != null) {
            adapter.addFooter(loadMoreFooterView);
        }

        if (mLayoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridManager = (GridLayoutManager) mLayoutManager;
            final int spanCount = gridManager.getSpanCount();
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (mAdapter.isHeaderType(position) || mAdapter.isFooterType(position)) ? spanCount : 1;
                }
            });
        }

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int bottomPosition = mLayoutManager.findLastVisibleItemPosition();
                if (bottomPosition == adapter.getItemCount() - 1 && mLastBottomPosition != bottomPosition) {
                    if (mLoadMoreState == STATE_COMPLETE && mOnPullLoadMoreListener != null) {
                        mLoadMoreState = STATE_LOADING;
                        mOnPullLoadMoreListener.onLoadMore(PtrLoadMoreRecycleView.this);
                    }
                }
                //记录用户上次滑动的状态
                if (mLastBottomPosition != bottomPosition) {
                    mLastBottomPosition = bottomPosition;
                }
            }
        });
    }

    public void setLoadMoreComplete(){
        mLoadMoreState = STATE_COMPLETE;
    }

    public interface OnPullLoadMoreListener {
        void onRefresh(PtrLoadMoreRecycleView ptrRecycleView);
        void onLoadMore(PtrLoadMoreRecycleView ptrRecycleView);
    }

    public abstract class PtrLoadMoreAdapter<T> extends BaseRecycleAdapter<T> {
        public abstract View getPullRefreshView(PtrLoadMoreRecycleView parent);
        public abstract View getLoadMoreView(PtrLoadMoreRecycleView parent);
    }

    public void setOnPullLoadMoreListener(OnPullLoadMoreListener onPullLoadMoreListener) {
        mOnPullLoadMoreListener = onPullLoadMoreListener;
    }
}
