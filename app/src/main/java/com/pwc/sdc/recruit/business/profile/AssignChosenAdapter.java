package com.pwc.sdc.recruit.business.profile;

import android.view.View;
import android.widget.LinearLayout;

import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BaseRecycleAdapter;
import com.pwc.sdc.recruit.base.ViewHolder;
import com.pwc.sdc.recruit.data.model.Recruiter;

import java.util.List;

/**
 * @author:dongpo 创建时间: 7/22/2016
 * 描述:
 * 修改:
 */
public class AssignChosenAdapter extends BaseRecycleAdapter<Recruiter> {
    private OnChildClickListener mListener;

    public AssignChosenAdapter(List<Recruiter> data, int layoutId) {
        super(data, layoutId);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        boolean userItemType = isUserItemType(position);
        if (userItemType) {
            holder.findViewById(R.id.chosen_tv_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onDelete(holder, holder.mPosition);
                    }
                }
            });
        }
    }

    @Override
    protected void setItemData(ViewHolder holder, int position, int viewType) {
        List<Recruiter> chosen = getData(viewType);
        Recruiter recruiter = chosen.get(position);
        LinearLayout llBg = holder.findViewById(R.id.chosen_ll_bg);
        View delete = holder.findViewById(R.id.chosen_tv_delete);
        holder.setText(R.id.chosen_tv_name, recruiter.name);
        if (recruiter.isInterview) {
            //是否已经面过
            llBg.setBackgroundColor(PwcApplication.getInstance().getResColor(android.R.color.transparent));
            delete.setVisibility(View.GONE);
        } else {
            llBg.setBackgroundResource(R.drawable.shape_bg_assign_chose);
            delete.setVisibility(View.VISIBLE);
        }
    }


    public interface OnChildClickListener {
        void onDelete(ViewHolder holder, int position);
    }

    public void setOnChildClickListener(OnChildClickListener listener) {
        mListener = listener;
    }
}
