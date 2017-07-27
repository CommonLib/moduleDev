package com.pwc.sdc.recruit.business.comment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pwc.sdc.recruit.R;

/**
 * @author:dongpo 创建时间: 7/25/2016
 * 描述:
 * 修改:
 */
public class LevelAdapter extends BaseAdapter {
    private final String[] mLevels;

    public LevelAdapter(String[] levels) {
        mLevels = levels;
    }

    @Override
    public int getCount() {
        return mLevels.length;
    }

    @Override
    public Object getItem(int position) {
        return mLevels[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_text_view, parent, false);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.comment_tv_name);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        String level = mLevels[position];
        holder.tvName.setText(level);
        holder.tvName.getPaint().setFakeBoldText(false);
        return convertView;
    }

    public static class ViewHolder {
        TextView tvName;
    }
}
