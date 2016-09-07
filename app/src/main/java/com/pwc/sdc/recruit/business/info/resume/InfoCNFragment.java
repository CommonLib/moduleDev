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
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class InfoCNFragment extends InfoFragment<InfoConstract.Presenter> implements View.OnClickListener {
    @BindView(R.id.info_et_identity_card)
    EditText mInfoEtIdentityCard;
    @BindView(R.id.info_et_take_service)
    EditText mInfoTakeService;

    @BindView(R.id.info_tv_identity_card)
    TextView mInfoTvIdentityCard;

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_info_cn;
    }

    @Override
    protected void initView() {
        super.initView();
        mInfoAcademicDegree = new GRadioGroup(mContentView, R.id.info_rb_academic_degree_bachelor
                , R.id.info_rb_academic_degree_college, R.id.info_rb_academic_degree_doctorate, R.id.info_rb_academic_degree_doctorate_senior
                , R.id.info_rb_academic_degree_master);
        mInfoRecruitmentChannel = new GRadioGroup(mContentView, R.id.info_rb_channel_headhunting
                , R.id.info_rb_51job, R.id.info_rb_employee, R.id.info_rb_wechat, R.id.info_rb_zhilian
                , R.id.info_rb_others);
        mInfoWeWorkExperience1.setOnWorkExpDateFromClickListener(this);
        mInfoWeWorkExperience1.setOnWorkExpDateToClickListener(this);

    }

    /**
     * 添加测试数据
     */
    protected void fillTestData() {
        mInfoEtChineseName.setText("张东方");
        mInfoEtEnglishName.setText("mInfoEtEnglishName");
        mInfoEtDateOfBirth.setText("1992/4/25");
        mInfoEtPassportNo.setText("EU66666666");
        mInfoEtGraduateSchool.setText("天津工业大学");
        mInfoEtMajor.setText("纺织工程");
        mInfoEtEmail.setText("asdong@163.com");
        mInfoEtMobile.setText("18516585792");
        mInfoEtTelephone.setText("0530-5460432");
        mInfoEtCurrentAddress.setText("上海市闵行区七宝镇佳宝四村28幢602");
        mInfoEtPostCode.setText("387400");
        mInfoEtEmergencyContact.setText("吴燕");
        mInfoEtRelationship.setText("朋友");
        mInfoEtTelephoneNo.setText("15922060713");
        mInfoEtYesFriendName.setText("张安茂");
        mInfoEtHobbies.setText("打羽毛球，玩游戏，写代码");
        mInfoEtPositionAppliedFor.setText("开发工程师");
        mInfoEtWorkingExperience.setText("2");
        mInfoEtChannelOthers.setText("51job");
        mInfoEtDateAvailable.setText("2016/5/6");
        mInfoEtIdentityCard.setText("372928199206097219");
        mInfoTakeService.setText("没有");

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
        mInfoRgGender.check(R.id.info_rb_gender_male);
        mInfoRgMaritalStatus.check(R.id.info_rb_marital_status_married);
        mInfoRgEnglishLanguage.check(R.id.info_rb_language_excellent);
        mInfoRgJapaneseLanguage.check(R.id.info_rb_language_japanese_level1);
        mInfoRgMedicalHistory.check(R.id.info_rb_yes_medical_history);
        mInfoRgEmployeeReferral.check(R.id.info_rb_no_referrer_name);
        mInfoRgHasFriendPwc.check(R.id.info_rb_yes_friend_name);
        mInfoRecruitmentChannel.check(R.id.info_rb_zhilian);
    }


    @OnClick({R.id.info_iv_header, R.id.info_iv_add_experience, R.id.info_et_date_of_birth, R.id.info_et_date_available, R.id.info_et_graduate_date})
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

        String mChineseName = mInfoEtChineseName.getText().toString().trim();
        if (TextUtils.isEmpty(mChineseName)) {
            issueEditText = mInfoEtChineseName;
            issueTextView = mInfoTvChineseName;
            return new IssueView(null, issueTextView, issueEditText);
        }

        String identityCard = mInfoEtIdentityCard.getText().toString().trim();
        if (TextUtils.isEmpty(identityCard)) {
            issueEditText = mInfoEtIdentityCard;
            issueTextView = mInfoTvIdentityCard;
            return new IssueView(null, issueTextView, issueEditText);
        }
        String available = mInfoEtDateAvailable.getText().toString().trim();
        if (TextUtils.isEmpty(available)) {
            issueEditText = mInfoEtDateAvailable;
            issueTextView = mInfoTvDateAvailable;
            return new IssueView(null, issueTextView, issueEditText);
        }
        return null;
    }

    @Override
    public Candidate getCandidate() {
        Candidate candidate = super.getCandidate();
        candidate.basic.identityNo = getEditText(mInfoEtIdentityCard);
        candidate.basic.country = getString(R.string.country);
        candidate.basic.takeService = getEditText(mInfoTakeService);
        candidate.position.availableDate = getEditText(mInfoEtDateAvailable);
        candidate.language = Constants.PROFILE_LANGUAGE_FORMAT_CHNIESE;
        return candidate;
    }

    @Override
    public void setCandidate(Candidate candidate) {
        super.setCandidate(candidate);
        mInfoEtIdentityCard.setText(candidate.basic.identityNo);
        mInfoTakeService.setText(candidate.basic.takeService);
        mInfoEtDateAvailable.setText(candidate.position.availableDate);
        String japanese = candidate.basic.languageLevel.jspanese;
        if (!TextUtils.isEmpty(japanese)) {
            if (TextUtils.equals(japanese, "日语1级")) {
                mInfoRgJapaneseLanguage.check(R.id.info_rb_language_japanese_level1);
            } else if (TextUtils.equals(japanese, "日语2级")) {
                mInfoRgJapaneseLanguage.check(R.id.info_rb_language_japanese_level2);
            } else {
                mInfoRgJapaneseLanguage.check(R.id.info_rb_language_japanese_level3);
            }
        }
    }

    @Override
    public void setViewEnable(boolean enable) {
        super.setViewEnable(enable);
        if (mInfoEtIdentityCard != null) {
            mInfoEtIdentityCard.setEnabled(enable);
        }
        if (mInfoEtDateAvailable != null) {
            mInfoEtDateAvailable.setEnabled(enable);
        }
        if (mInfoTakeService != null) {
            mInfoTakeService.setEnabled(enable);
        }
        if (mInfoEtIdentityCard != null) {
            mInfoEtIdentityCard.setEnabled(enable);
        }
    }

}
