package com.pwc.sdc.recruit.business.profile;

import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BaseRecycleAdapter;
import com.pwc.sdc.recruit.base.ViewHolder;
import com.pwc.sdc.recruit.data.model.RecruiterCandidate;

import java.util.List;

/**
 * @author:dongpo 创建时间: 7/22/2016
 * 描述:
 * 修改:
 */
public class AssignCandidateAdapter extends BaseRecycleAdapter<RecruiterCandidate> {
    public AssignCandidateAdapter(List<RecruiterCandidate> data, int layoutId) {
        super(data, layoutId);
    }

    @Override
    protected void setItemData(ViewHolder holder, int position, int viewType) {
        List<RecruiterCandidate> candidates = getData(viewType);
        RecruiterCandidate candidateName = candidates.get(position);
        holder.setText(R.id.candidate_tv_name, candidateName.name);
        holder.setText(R.id.candidate_tv_guid, candidateName.guid);
        holder.setText(R.id.candidate_tv_department, candidateName.department);

    }
}
