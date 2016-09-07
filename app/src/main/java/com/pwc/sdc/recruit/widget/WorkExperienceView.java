package com.pwc.sdc.recruit.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.data.model.Candidate;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author:dongpo 创建时间: 7/15/2016
 * 描述:
 * 修改:
 */
public class WorkExperienceView extends FrameLayout {
    @BindView(R.id.info_tv_company_name)
    TextView mInfoTvCompanyName;
    @BindView(R.id.info_et_company_name)
    EditText mInfoEtCompanyName;
    @BindView(R.id.info_tv_dates_from_to)
    TextView mInfoTvDatesFromTo;
    @BindView(R.id.info_et_dates_from)
    EditText mInfoEtDatesFrom;
    @BindView(R.id.info_et_dates_to)
    EditText mInfoEtDatesTo;
    @BindView(R.id.info_tv_contact_no)
    TextView mInfoTvContactNo;
    @BindView(R.id.info_et_contact_no)
    EditText mInfoEtContactNo;
    @BindView(R.id.info_tv_position)
    TextView mInfoTvPosition;
    @BindView(R.id.info_et_position)
    EditText mInfoEtPosition;
    @BindView(R.id.info_tv_leaving_reasons)
    TextView mInfoTvLeavingReasons;
    @BindView(R.id.info_et_leaving_reasons)
    EditText mInfoEtLeavingReasons;
    @BindView(R.id.info_tv_referee)
    TextView mInfoTvReferee;
    @BindView(R.id.info_et_referee)
    EditText mInfoEtReferee;
    @BindView(R.id.info_tv_relationship)
    TextView mInfoTvRelationship;
    @BindView(R.id.info_et_relationship)
    EditText mInfoEtRelationship;
    @BindView(R.id.info_tv_telephone_no)
    TextView mInfoTvTelephoneNo;
    @BindView(R.id.info_et_telephone_no)
    EditText mInfoEtTelephoneNo;
    private String mCompanyName;
    private String mContactNo;
    private String mDatesFrom;
    private String mDatesTo;
    private String mLeavingReasons;
    private String mPosition;
    private String mReferee;
    private String mRelationship;
    private String mTelephoneNo;

    private EditText mIssueET;
    private TextView mIssueTV;


    public WorkExperienceView(Context context) {
        this(context, null);
    }

    public WorkExperienceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_working_experience, this);
        ButterKnife.bind(this);
    }

    /**
     * @return 工作经验这一栏有没有填过，false为没有填过，true填过
     */
    public boolean isFillUp() {
        getText();
        if (!TextUtils.isEmpty(mCompanyName)) {
            return true;
        }
        if (!TextUtils.isEmpty(mDatesFrom)) {
            return true;
        }
        if (!TextUtils.isEmpty(mDatesTo)) {
            return true;
        }
        if (!TextUtils.isEmpty(mContactNo)) {
            return true;
        }
        if (!TextUtils.isEmpty(mPosition)) {
            return true;
        }
        if (!TextUtils.isEmpty(mLeavingReasons)) {
            return true;
        }
        if (!TextUtils.isEmpty(mReferee)) {
            return true;
        }
        if (!TextUtils.isEmpty(mRelationship)) {
            return true;
        }
        if (!TextUtils.isEmpty(mTelephoneNo)) {
            return true;
        }
        return false;
    }

    /**
     * 获取控件上的所有值
     */
    private void getText() {
        mCompanyName = mInfoEtCompanyName.getText().toString().trim();
        mContactNo = mInfoEtContactNo.getText().toString().trim();
        mDatesFrom = mInfoEtDatesFrom.getText().toString().trim();
        mDatesTo = mInfoEtDatesTo.getText().toString().trim();
        mLeavingReasons = mInfoEtLeavingReasons.getText().toString().trim();
        mPosition = mInfoEtPosition.getText().toString().trim();
        mReferee = mInfoEtReferee.getText().toString().trim();
        mRelationship = mInfoEtRelationship.getText().toString().trim();
        mTelephoneNo = mInfoEtTelephoneNo.getText().toString().trim();
    }

    /**
     * @return 检查有没有必填项，实际没有填写，true代表有，false没有
     */
    public boolean hasEmpty() {
        if (TextUtils.isEmpty(mCompanyName)) {
            mIssueET = mInfoEtCompanyName;
            mIssueTV = mInfoTvCompanyName;
            return true;
        }
        if (TextUtils.isEmpty(mContactNo)) {
            mIssueET = mInfoEtContactNo;
            mIssueTV = mInfoTvContactNo;
            return true;
        }
        if (TextUtils.isEmpty(mDatesFrom)) {
            mIssueET = mInfoEtDatesFrom;
            mIssueTV = mInfoTvDatesFromTo;
            return true;
        }

        if (TextUtils.isEmpty(mDatesTo)) {
            mIssueET = mInfoEtDatesTo;
            mIssueTV = mInfoTvDatesFromTo;
            return true;
        }
        if (TextUtils.isEmpty(mLeavingReasons)) {
            mIssueET = mInfoEtLeavingReasons;
            mIssueTV = mInfoTvLeavingReasons;
            return true;
        }
        if (TextUtils.isEmpty(mPosition)) {
            mIssueET = mInfoEtPosition;
            mIssueTV = mInfoTvPosition;
            return true;
        }
        if (TextUtils.isEmpty(mReferee)) {
            mIssueET = mInfoEtReferee;
            mIssueTV = mInfoTvReferee;
            return true;
        }
        if (TextUtils.isEmpty(mRelationship)) {
            mIssueET = mInfoEtRelationship;
            mIssueTV = mInfoTvRelationship;
            return true;
        }
        if (TextUtils.isEmpty(mTelephoneNo)) {
            mIssueET = mInfoEtTelephoneNo;
            mIssueTV = mInfoTvTelephoneNo;
            return true;
        }
        mIssueET = null;
        mIssueTV = null;
        return false;
    }

    /**
     * @param exp 设置经验
     */
    public void setExperience(Candidate.Experience exp) {
        if (exp != null) {
            mInfoEtCompanyName.setText(exp.companyName);
            mInfoEtContactNo.setText(exp.companyPhone);
            mInfoEtDatesFrom.setText(exp.from);
            mInfoEtDatesTo.setText(exp.to);
            mInfoEtLeavingReasons.setText(exp.leaveReason);
            mInfoEtPosition.setText(exp.position);
            mInfoEtReferee.setText(exp.referee.name);
            mInfoEtRelationship.setText(exp.referee.relationship);
            mInfoEtTelephoneNo.setText(exp.referee.phone);
        }
    }

    /**
     * @return 获取当前经验
     */
    public Candidate.Experience getExperience() {
        Candidate.Experience exp = new Candidate.Experience();
        Candidate.Contact contact = new Candidate.Contact(mReferee, mRelationship, mTelephoneNo);
        exp.companyName = mCompanyName;
        exp.companyPhone = mContactNo;
        exp.leaveReason = mLeavingReasons;
        exp.position = mPosition;
        exp.from = mDatesFrom;
        exp.to = mDatesTo;
        exp.referee = contact;
        return exp;
    }

    public void setExpEnable(boolean enable) {
        if (mInfoEtCompanyName != null) {
            mInfoEtCompanyName.setEnabled(enable);
        }
        if (mInfoEtContactNo != null) {
            mInfoEtContactNo.setEnabled(enable);
        }
        if (mInfoEtDatesFrom != null) {
            mInfoEtDatesFrom.setEnabled(enable);
        }
        if (mInfoEtDatesTo != null) {
            mInfoEtDatesTo.setEnabled(enable);
        }
        if (mInfoEtLeavingReasons != null) {
            mInfoEtLeavingReasons.setEnabled(enable);
        }
        if (mInfoEtPosition != null) {
            mInfoEtPosition.setEnabled(enable);
        }
        if (mInfoEtReferee != null) {
            mInfoEtReferee.setEnabled(enable);
        }
        if (mInfoEtRelationship != null) {
            mInfoEtRelationship.setEnabled(enable);
        }
        if (mInfoEtTelephoneNo != null) {
            mInfoEtTelephoneNo.setEnabled(enable);
        }
    }

    public EditText getIssueET() {
        return mIssueET;
    }

    public TextView getIssueTV() {
        return mIssueTV;
    }

    public EditText getCompanyNameET() {
        return mInfoEtCompanyName;
    }

    public TextView getCompanyNameTv() {
        return mInfoTvCompanyName;
    }

    public void setOnWorkExpDateFromClickListener(View.OnClickListener listener){
        mInfoEtDatesFrom.setOnClickListener(listener);
    }

    public void setOnWorkExpDateToClickListener(View.OnClickListener listener){
        mInfoEtDatesTo.setOnClickListener(listener);
    }
}
