package com.pwc.sdc.recruit.business.info.resume;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.constants.Constants;
import com.pwc.sdc.recruit.data.model.Candidate;
import com.pwc.sdc.recruit.widget.GRadioGroup;
import com.pwc.sdc.recruit.widget.WorkExperienceView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author:dongpo 创建时间: 7/15/2016
 * 描述:
 * 修改:
 */
public class InfoEnFragment extends InfoFragment<InfoConstract.Presenter> implements View.OnClickListener {

    @BindView(R.id.info_et_country)
    EditText mInfoEtCountry;

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initView() {
        super.initView();
        mInfoAcademicDegree = new GRadioGroup(mContentView, R.id.info_rb_academic_degree_bachelor
                , R.id.info_rb_academic_degree_college, R.id.info_rb_academic_degree_doctorate
                , R.id.info_rb_academic_degree_master);
        mInfoRecruitmentChannel = new GRadioGroup(mContentView, R.id.info_rb_channel_headhunting
                , R.id.info_rb_51job, R.id.info_rb_employee, R.id.info_rb_wechat
                , R.id.info_rb_others);
        mInfoWeWorkExperience1.setOnWorkExpDateFromClickListener(this);
        mInfoWeWorkExperience1.setOnWorkExpDateToClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_info_en;
    }

    @OnClick({R.id.info_iv_header, R.id.info_iv_add_experience,R.id.info_et_date_of_birth, R.id.info_et_date_available, R.id.info_et_graduate_date})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.info_iv_header:
                fillTestData();
                break;
            case R.id.info_iv_add_experience:
                addExperienceLabelView();
                break;
            case R.id.info_et_date_of_birth:
            case R.id.info_et_graduate_date:
                mPresenter.pickDateTimer((EditText) view);
                break;
            case R.id.info_et_dates_from:
                mPresenter.pickExpDateFromTimer((EditText) view);
                break;
            case R.id.info_et_dates_to:
                mPresenter.pickExpDateToTimer((EditText) view);
                break;
            case R.id.info_et_date_available:
                mPresenter.pickAvailableDateTimer((EditText) view);
                break;
        }
    }

    @Override
    public WorkExperienceView addExperienceLabelView() {
        WorkExperienceView workExp = super.addExperienceLabelView();
        workExp.setOnWorkExpDateFromClickListener(this);
        workExp.setOnWorkExpDateToClickListener(this);
        return workExp;
    }

    @Override
    public IssueView checkEmpty() {
        IssueView issueView = super.checkEmpty();
        if (issueView != null) {
            return issueView;
        } else {
            return checkSpecialEmpty();
        }
    }

    private IssueView checkSpecialEmpty() {
        EditText issueEditText = null;
        TextView issueTextView = null;
        String englishName = mInfoEtEnglishName.getText().toString().trim();
        if(TextUtils.isEmpty(englishName)){
            issueEditText = mInfoEtEnglishName;
            issueTextView = mInfoTvEnglishName;
            return new IssueView(null, issueTextView, issueEditText);
        }

        String mPassportNo = mInfoEtPassportNo.getText().toString().trim();
        if (TextUtils.isEmpty(mPassportNo)) {
            issueEditText = mInfoEtPassportNo;
            issueTextView = mInfoTvPassportNo;
            return new IssueView(null, issueTextView, issueEditText);
        }

        String dateAvaiable = mInfoEtDateAvailable.getText().toString().trim();
        if (TextUtils.isEmpty(dateAvaiable)) {
            issueEditText = mInfoEtDateAvailable;
            issueTextView = mInfoTvDateAvailable;
            return new IssueView(null, issueTextView, issueEditText);
        }

        return null;
    }

    /**
     * 添加测试数据
     */
    protected void fillTestData() {
        mInfoEtChineseName.setText("张东方");
        mInfoEtEnglishName.setText("Adelaide");
        mInfoEtDateOfBirth.setText("1992/4/25");
        mInfoEtPassportNo.setText("EU66666666");
        mInfoEtGraduateSchool.setText("Massachusetts Institute of Technology");
        mInfoEtMajor.setText("Mechanical manufacture and Automation Major");
        mInfoEtEmail.setText("asdong@163.com");
        mInfoEtMobile.setText("18516585792");
        mInfoEtTelephone.setText("0530-5460432");
        mInfoEtCurrentAddress.setText("Room 401, Lane NO 23-30, Guangxindonglu Road 205, Haicheng Street, Yumen village, Longwan District, Wenzhou, Zhejiang Province");
        mInfoEtPostCode.setText("387400");
        mInfoEtEmergencyContact.setText("Adelaide");
        mInfoEtRelationship.setText("friend");
        mInfoEtTelephoneNo.setText("15922060713");
        mInfoEtYesFriendName.setText("Burt");
        mInfoEtHobbies.setText("Play Games, codeing, driveing, listen music");
        mInfoEtPositionAppliedFor.setText("software engenieer");
        mInfoEtWorkingExperience.setText("2");
        mInfoEtChannelOthers.setText("51job");
        mInfoEtCountry.setText("American");
        mInfoEtDateAvailable.setText("2016/5/6");
        Candidate.Experience exp = new Candidate.Experience();
        exp.companyName = "上海网络科技有限公司";
        exp.from = "2015/9/14";
        exp.to = "2015/8/14";
        exp.companyPhone = "020-5460311";
        exp.position = "软件工程师";
        exp.leaveReason = "想换一家氛围";
        exp.referee = new Candidate.Contact("张三", "领导", "15922060713");

        mInfoWeWorkExperience1.setExperience(exp);

        mInfoAcademicDegree.check(R.id.info_rb_academic_degree_college);
        //mInfoRgGender.check(R.id.info_rb_gender_male);
        mInfoRgMaritalStatus.check(R.id.info_rb_marital_status_married);
        mInfoRgEnglishLanguage.check(R.id.info_rb_language_excellent);
        mInfoRgJapaneseLanguage.check(R.id.info_rb_language_jap_excellent);
        mInfoRgMedicalHistory.check(R.id.info_rb_yes_medical_history);
        mInfoRgEmployeeReferral.check(R.id.info_rb_no_referrer_name);
        mInfoRgHasFriendPwc.check(R.id.info_rb_yes_friend_name);
        mInfoRecruitmentChannel.check(R.id.info_rb_51job);
    }

    @Override
    public Candidate getCandidate() {
        Candidate candidate = super.getCandidate();
        candidate.basic.country = getEditText(mInfoEtCountry);
        candidate.position.availableDate = getEditText(mInfoEtDateAvailable);
        candidate.language = Constants.PROFILE_LANGUAGE_FORMAT_ENGLISH;
        return candidate;
    }

    @Override
    public void setCandidate(Candidate candidate) {
        super.setCandidate(candidate);

        mInfoEtCountry.setText(candidate.basic.country);
        mInfoEtDateAvailable.setText(candidate.position.availableDate);

        String japanese = candidate.basic.languageLevel.jspanese;
        if (!TextUtils.isEmpty(japanese)) {
            if (TextUtils.equals(japanese, "Excellent")) {
                mInfoRgJapaneseLanguage.check(R.id.info_rb_language_jap_excellent);
            } else if (TextUtils.equals(japanese, "Good")) {
                mInfoRgJapaneseLanguage.check(R.id.info_rb_language_jap_good);
            } else if (TextUtils.equals(japanese, "Fair")) {
                mInfoRgJapaneseLanguage.check(R.id.info_rb_language_jap_fair);
            } else {
                mInfoRgJapaneseLanguage.check(R.id.info_rb_language_jap_poor);
            }
        }
    }


    @Override
    public void setViewEnable(boolean enable) {
        super.setViewEnable(enable);
        if (mInfoEtCountry != null) {
            mInfoEtCountry.setEnabled(enable);
        }
        if (mInfoEtDateAvailable != null) {
            mInfoEtDateAvailable.setEnabled(enable);
        }
    }
}
