package com.pwc.sdc.recruit.business.comment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.data.model.Recruiter;

import java.util.List;

/**
 * @author:dongpo 创建时间: 7/25/2016
 * 描述:
 * 修改:
 */
public class InterViewerAdapter extends BaseAdapter {
    private static final int TYPE_EMPTY = 1;
    private static final int TYPE_VIEW = 2;
    private final List<Recruiter> mRecruiters;

    public InterViewerAdapter(List<Recruiter> recruiters) {
        mRecruiters = recruiters;
    }

    @Override
    public int getCount() {
        return mRecruiters.size();
    }

    @Override
    public Object getItem(int position) {
        return mRecruiters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_text_view, parent, false);
            holder.tvName = (TextView) convertView.findViewById(R.id.comment_tv_name);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        Recruiter recruiter = mRecruiters.get(position);
        holder.tvName.setText(recruiter.name);
        holder.tvName.getPaint().setFakeBoldText(false);
        return convertView;
    }

    public static class ViewHolder {
        TextView tvName;
    }
}
