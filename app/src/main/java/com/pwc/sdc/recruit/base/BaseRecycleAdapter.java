package com.pwc.sdc.recruit.base;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author:dongpo 创建时间: 6/21/2016
 * 描述:
 * 修改:
 */
public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    private SparseArray<List<? extends Object>> mDates_layouts;
    private SparseArray<View> headers_footers;
    private List<Integer> mHeaders;
    private List<Integer> mFooters;
    private int mHeaderCount;
    private int mFooterCount;
    private int mUserItemCount;
    private Random mRandom;
    private OnItemClickListener mListener;


    public BaseRecycleAdapter(List<T> data, int layoutId) {
        mDates_layouts = new SparseArray<>();
        mDates_layouts.put(layoutId, data);
        mUserItemCount = getUserItemCount();
    }

    public BaseRecycleAdapter() {
        mDates_layouts = new SparseArray<>();
    }

    public void addItemType(List<? extends Object> data, int layoutId) {
        mDates_layouts.put(layoutId, data);
        mUserItemCount = getUserItemCount();
        notifyDataSetChanged();
    }

    public void setData(List data, int layoutId) {
        if (!containsKey(layoutId, mDates_layouts)) {
            return;
        }
        mDates_layouts.put(layoutId, data);
        mUserItemCount = getUserItemCount();
        notifyDataSetChanged();
    }

    public void addData(List data, int layoutId) {
        List<? extends Object> datas = mDates_layouts.get(layoutId);
        datas.addAll(data);
        mUserItemCount = getUserItemCount();
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = getLayoutIdFromViewType(viewType);
        ViewHolder holder;
        if (headers_footers != null) {
            View itemView = headers_footers.get(layoutId);
            if (itemView == null) {
                holder = ViewHolder.get(parent.getContext(), null, parent, layoutId);
            } else {
                holder = ViewHolder.get(parent.getContext(), null, parent, itemView);
            }
        } else {
            holder = ViewHolder.get(parent.getContext(), null, parent, layoutId);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (mHeaders != null) {
            if (mHeaders.contains(viewType)) {
                return;
            }
        }

        if (mFooters != null) {
            if (mFooters.contains(viewType)) {
                return;
            }
        }

        final int userPosition = convertUserPosition(position);
        holder.updatePosition(userPosition);
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(holder, userPosition);
                }
            }
        });
        setItemData(holder, userPosition, viewType);
    }

    protected abstract void setItemData(ViewHolder holder, int position, int viewType);


    @Override
    public int getItemCount() {
        int totalCount = mHeaderCount + mFooterCount + mUserItemCount;
        return totalCount;
    }

    private List<? extends Object> getDataFromViewType(int viewType) {
        return mDates_layouts.get(viewType);
    }

    private int getLayoutIdFromViewType(int viewType) {
        return viewType;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderType(position)) {
            return mHeaders.get(position);
        } else if (isFooterType(position)) {
            position = convertFootPosition(position);
            return mFooters.get(position);
        } else {
            position = convertUserPosition(position);
            return getUserHolderType(position);
        }
    }

    private int convertUserPosition(int position) {
        return position - mHeaderCount;
    }

    private int convertFootPosition(int position) {
        return position - mHeaderCount - mUserItemCount;
    }

    protected int getUserHolderType(int position) {
        return mDates_layouts.keyAt(0);
    }

    public <K> List<K> getData(int layoutId) {
        return (List<K>) mDates_layouts.get(layoutId);
    }

    /**
     * @param position 根据recycleView的position判断是否是头布局
     * @return
     */
    public boolean isHeaderType(int position) {
        if (position < mHeaderCount) {
            return true;
        }
        return false;
    }

    /**
     * @param position 根据recycleView的position判断是否是脚布局
     * @return
     */
    public boolean isFooterType(int position) {

        int beforeFooterCount = mHeaderCount + mUserItemCount;
        int totalItemCount = getItemCount();
        if (position >= beforeFooterCount && position < totalItemCount) {
            return true;
        }
        return false;
    }

    /**
     * @param position 根据recycleView的position判断是否是用户的布局
     * @return
     */
    public boolean isUserItemType(int position) {
        if (position >= mHeaderCount && position < mHeaderCount + mUserItemCount) {
            return true;
        }
        return false;
    }

    /**
     * @return 返回头布局的数量
     */
    private int getHeaderCount() {
        if (mHeaders != null) {
            return mHeaders.size();
        }
        return 0;
    }

    /**
     * @return 返回脚布局的数量
     */
    private int getFooterCount() {
        if (mFooters != null) {
            return mFooters.size();
        }
        return 0;
    }

    /**
     * @return 返回用户的item数量
     */
    private final int getUserItemCount() {
        int userItemCount = 0;

        for (int i = 0; i < mDates_layouts.size(); i++) {
            int layoutId = mDates_layouts.keyAt(i);
            List<?> data = mDates_layouts.get(layoutId);
            userItemCount += data.size();
        }
        return userItemCount;
    }

    public int getUserItemCounts(){
        return mUserItemCount;
    }

    public void addHeader(int resId) {
        if (resId > 0) {
            if (mHeaders == null) {
                mHeaders = new ArrayList<>();
            }
            mHeaders.add(resId);
            mHeaderCount = getHeaderCount();
        }
    }

    public void addHeader(View header) {
        if (header != null && header.getParent() == null) {
            if (headers_footers == null) {
                headers_footers = new SparseArray<>();
            }

            int layoutId = generateLayoutId();
            if (mHeaders == null) {
                mHeaders = new ArrayList<>();
            }
            mHeaders.add(layoutId);
            headers_footers.put(layoutId, header);
            mHeaderCount = getHeaderCount();
        } else {
            throw new IllegalArgumentException("header view should not have a parent");
        }
    }

    public View getHeader() {
        if (mHeaders != null && mHeaders.size() > 0) {
            return headers_footers.get(mHeaders.get(mHeaders.size() - 1));
        }
        return null;
    }

    public View getHeader(int index) {
        if (mHeaders != null && mHeaders.size() > 0) {
            int size = mHeaders.size();
            if (index < size) {
                return headers_footers.get(mHeaders.get(index));
            } else {
                throw new IllegalArgumentException("position should not over size of collection");
            }

        }
        return null;
    }

    public void addFooter(int resId) {
        if (resId > 0) {
            if (mFooters == null) {
                mFooters = new ArrayList<>();
            }
            mFooters.add(resId);
            mFooterCount = getFooterCount();
        }
    }

    public void addFooter(View footer) {
        if (footer != null && footer.getParent() == null) {
            if (headers_footers == null) {
                headers_footers = new SparseArray<>();
            }

            int layoutId = generateLayoutId();
            if (mFooters == null) {
                mFooters = new ArrayList<>();
            }
            mFooters.add(layoutId);
            headers_footers.put(layoutId, footer);
            mFooterCount = getFooterCount();
        } else {
            throw new IllegalArgumentException("footer view should not have a parent");
        }
    }

    public View getFooter() {
        if (mFooters != null && mFooters.size() > 0) {
            return headers_footers.get(mFooters.get(mFooters.size() - 1));
        }
        return null;
    }

    public View getFooter(int index) {
        if (mFooters != null && mFooters.size() > 0) {
            int size = mFooters.size();
            if (index < size) {
                return headers_footers.get(mFooters.get(index));
            } else {
                throw new IllegalArgumentException("position should not over size of collection");
            }
        }
        return null;
    }

    /**
     * @return 生成一个不重复的随即数
     */
    public int generateLayoutId() {
        int layoutId = getRandomInt();
        while (containsKey(layoutId, headers_footers)) {
            layoutId = getRandomInt();
        }
        return layoutId;
    }

    private int getRandomInt() {
        if (mRandom == null) {
            mRandom = new Random();
        }
        return mRandom.nextInt(Integer.MAX_VALUE);
    }

    public boolean containsKey(int key, SparseArray sa) {
        int size = sa.size();
        for (int i = 0; i < size; i++) {
            int i1 = sa.keyAt(0);
            if (i1 == key) {
                return true;
            }
        }
        return false;
    }

    public void hideFooterView() {
        if (mFooterCount >= 1) {
            mFooterCount--;
            notifyDataSetChanged();
        }
    }

    public void showFooterView() {
        if (mFooterCount < getFooterCount()) {
            mFooterCount++;
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}
