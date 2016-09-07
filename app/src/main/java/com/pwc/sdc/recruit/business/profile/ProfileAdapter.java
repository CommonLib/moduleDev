package com.pwc.sdc.recruit.business.profile;

import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;
import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BaseRecycleAdapter;
import com.pwc.sdc.recruit.base.ViewHolder;
import com.pwc.sdc.recruit.data.model.Profile;
import com.thirdparty.proxy.utils.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author:dongpo 创建时间: 7/20/2016
 * 描述:
 * 修改:
 */
public class ProfileAdapter extends BaseRecycleAdapter<Profile> {
    private onChildClickListener mOnChildClickListener;

    public ProfileAdapter(List<Profile> data, int layoutId) {
        super(data, layoutId);
    }

    @Override
    protected void setItemData(ViewHolder holder, int position, int viewType) {
        List<Object> datas = getData(R.layout.item_profile_search);
        Object item = datas.get(position);
        if (isTimeStamp(position)) {
            String time = (String) item;
            if (time != null) {
                if (StringUtils.isToday(time)) {
                    holder.setText(R.id.profile_tv_date, PwcApplication.getInstance().getString(R.string.today));
                } else if (StringUtils.isYesterday(time)) {
                    holder.setText(R.id.profile_tv_date, PwcApplication.getInstance().getString(R.string.yesterday));
                } else {
                    holder.setText(R.id.profile_tv_date, time);
                }
            }

        } else {
            Profile profile = (Profile) item;
            Button assigned = holder.findViewById(R.id.search_btn_assigned);
            Button edit = holder.findViewById(R.id.search_btn_edit);
            holder.setText(R.id.search_tv_cn_name, profile.chineseName);
            holder.setText(R.id.search_tv_en_name, profile.englishName);
            if (profile.recruiters == null || profile.recruiters.size() == 0) {
                assigned.setVisibility(View.VISIBLE);
                edit.setVisibility(View.INVISIBLE);
            } else {
                assigned.setVisibility(View.INVISIBLE);
                edit.setVisibility(View.VISIBLE);
            }
            holder.setText(R.id.search_tv_phone, profile.phone);
            String date = StringUtils.getDateString(new Date(profile.submitTime));
            holder.setText(R.id.search_tv_interview_date, date);
            holder.setText(R.id.search_tv_candidate_position, profile.position);
            SimpleDraweeView ivHeader = holder.findViewById(R.id.search_sdv_header);
            if (profile.headUrl != null) {
                ivHeader.setImageURI(Uri.parse(profile.headUrl));
            }
        }
    }

    @Override
    public void addData(List data, int layoutId) {
        super.addData(data, layoutId);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        boolean userItemType = isUserItemType(position);
        if (userItemType) {
            if (!isTimeStamp(position)) {
                holder.findViewById(R.id.search_btn_assigned).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnChildClickListener != null) {
                            mOnChildClickListener.assign(holder, holder.mPosition);
                        }
                    }
                });
                holder.findViewById(R.id.search_btn_edit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnChildClickListener != null) {
                            mOnChildClickListener.editAssign(holder, holder.mPosition);
                        }
                    }
                });

                holder.findViewById(R.id.search_cv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnChildClickListener != null) {
                            mOnChildClickListener.onProfileClick(holder, holder.mPosition);
                        }
                    }
                });
            }
        }
    }

    public boolean isTimeStamp(int position) {
        List<Object> profileTimes = getData(R.layout.item_profile_search);
        Object item = profileTimes.get(position);
        if (item instanceof Profile) {
            return false;
        } else {
            return true;
        }
    }

    public interface onChildClickListener {
        void assign(ViewHolder holder, int position);

        void editAssign(ViewHolder holder, int position);

        void onProfileClick(ViewHolder holder, int position);
    }

    public void setOnChildClickListener(onChildClickListener onChildClickListener) {
        mOnChildClickListener = onChildClickListener;
    }

    public String formatRecruiter(List<String> recruiters) {
        if (recruiters == null || recruiters.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int size = recruiters.size();
        for (int i = 0; i < size; i++) {
            String recruiter = recruiters.get(i);
            if (i == size - 1) {
                sb.append(recruiter);
            } else {
                sb.append(recruiter).append(" ");
            }
        }
        return sb.toString();
    }

    @Override
    protected int getUserHolderType(int userPosition) {
        if (isTimeStamp(userPosition)) {
            return R.layout.item_search_profile_date;
        } else {
            return R.layout.item_profile_search;
        }
    }

}
