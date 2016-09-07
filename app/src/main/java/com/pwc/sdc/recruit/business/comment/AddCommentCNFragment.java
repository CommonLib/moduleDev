package com.pwc.sdc.recruit.business.comment;

import android.os.Bundle;
import android.view.View;

import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.business.info.resume.InfoCNFragment;
import com.pwc.sdc.recruit.data.model.Candidate;
import com.pwc.sdc.recruit.data.model.Recruiter;
import com.pwc.sdc.recruit.widget.GRadioGroup;

import java.util.ArrayList;

import butterknife.OnClick;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class AddCommentCNFragment extends InfoCNFragment {

    @Override
    protected void initView() {
        mInfoAcademicDegree = new GRadioGroup(mContentView, R.id.info_rb_academic_degree_bachelor
                , R.id.info_rb_academic_degree_college, R.id.info_rb_academic_degree_doctorate, R.id.info_rb_academic_degree_doctorate_senior
                , R.id.info_rb_academic_degree_master);
        mInfoRecruitmentChannel = new GRadioGroup(mContentView, R.id.info_rb_channel_headhunting
                , R.id.info_rb_51job, R.id.info_rb_employee, R.id.info_rb_wechat, R.id.info_rb_zhilian
                , R.id.info_rb_others);
        setViewEnable(false);
        mInfoIvAddExperience.setVisibility(View.GONE);
    }

    @Override
    protected void handleBundle(Bundle bundle) {
        ArrayList<Recruiter> recruiters = bundle.getParcelableArrayList("recruiters");
        Candidate candidate = bundle.getParcelable("candidate");
        setCandidate(candidate);
        initSpinner(recruiters);
        loadHeadIcon(candidate.headUrl);
    }

    @OnClick({R.id.comment_btn_submit})
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.comment_btn_submit:
                mPresenter.next();
        }
    }
}
