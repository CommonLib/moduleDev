package com.pwc.sdc.recruit.business.finder;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pwc.sdc.recruit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhuang126 on 7/13/2016.
 */
public class JobFinderAdapter extends RecyclerView.Adapter<JobFinderAdapter.JobFinderViewHolder> implements JobFinderContract.AdapterPresenter {
    private Context mContext;
    JobFinderDataSource mDataSource;
    Cursor mCursor;
    List<OnCardViewClickListener> mListenerList;
    boolean isCardViewClickAble = true;

    JobFinderAdapter(Context context) {
        mContext = context;
        mListenerList = new ArrayList<>();
    }

    private void init() {
        if (mDataSource == null) {
            throw new RuntimeException("please use setDataSource(JobFinderDataSource dataSource) set dataSource ");
        }
        mDataSource.getJobFinderCursor(null, new JobFinderModel.LoadJobNameCallback() {
            @Override
            public void onJobNameLoaded(Cursor cursor) {
                mCursor = cursor;
                notifyDataSetChanged();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    public void setDataSource(JobFinderDataSource dataSource) {
        mDataSource = dataSource;
        init();
    }

    @Override
    public JobFinderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new JobFinderViewHolder(LayoutInflater.from(mContext).inflate(R.layout.job_finder_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final JobFinderViewHolder holder, final int position) {
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.moveToPosition(position);
            String finderName = mCursor.getString(mCursor.getColumnIndex(JobFinderModel.JOB_FINDER_NAME));
            holder.jobFinderName.setText(finderName + position);
            //如果holder中有多个view需要处理点击事情，都不要忘记执行第二步，
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // 第一步是处理点击事件，
                    onCardViewClick(isCardViewClickAble);
                    //第二步向外部传送点击事件 目的是如果当前是状态应该把软键盘隐藏起来
                    for (OnCardViewClickListener listener : mListenerList) {
                        listener.onClick(holder.itemView, position, isCardViewClickAble);
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        if (mCursor != null && !mCursor.isClosed()) {
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public void updateCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public void updateQueryString(String columnName) {
        mDataSource.getJobFinderCursor(columnName, new JobFinderModel.LoadJobNameCallback() {
            @Override
            public void onJobNameLoaded(Cursor cursor) {
                updateCursor(cursor);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void updateCardViewClickAble(boolean able) {
        isCardViewClickAble = able;
    }

    @Override
    public void onCardViewClick(boolean isCardViewClickAble) {
        if (isCardViewClickAble) {
         //TODO 点击事件
        } else {
            // 这个时候虽然CardView被点击了，但是并不想执行被点击情况

        }
    }

    @Override
    public void addOnCardViewClickListener(OnCardViewClickListener listener) {
        mListenerList.add(listener);
    }

    @Override
    public void removeOnCardViewClickListener(OnCardViewClickListener listener) {
        mListenerList.remove(listener);
    }

    class JobFinderViewHolder extends RecyclerView.ViewHolder {
        TextView jobFinderName;
        ImageView jobFinderImg;

        public JobFinderViewHolder(View itemView) {
            super(itemView);
            jobFinderName = (TextView) itemView.findViewById(R.id.text);
            jobFinderImg = (ImageView) itemView.findViewById(R.id.image_view);
        }
    }
}
