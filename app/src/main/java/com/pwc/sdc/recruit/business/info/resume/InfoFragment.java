package com.pwc.sdc.recruit.business.info.resume;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BaseFragment;
import com.pwc.sdc.recruit.base.interf.ActivityPresenter;
import com.pwc.sdc.recruit.business.comment.InterViewerAdapter;
import com.pwc.sdc.recruit.business.comment.LevelAdapter;
import com.pwc.sdc.recruit.data.model.Candidate;
import com.pwc.sdc.recruit.data.model.Comment;
import com.pwc.sdc.recruit.data.model.Recruiter;
import com.pwc.sdc.recruit.manager.CandidateManager;
import com.pwc.sdc.recruit.widget.GRadioGroup;
import com.pwc.sdc.recruit.widget.WorkExperienceView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

/**
 * @author:dongpo 创建时间: 7/15/2016
 * 描述:
 * 修改:
 */
public abstract class InfoFragment<T extends ActivityPresenter> extends BaseFragment<T> {
    @BindView(R.id.info_iv_header)
    SimpleDraweeView mInfoIvHeader;
    @BindView(R.id.info_et_chinese_name)
    EditText mInfoEtChineseName;
    @BindView(R.id.info_rg_gender)
    RadioGroup mInfoRgGender;
    @BindView(R.id.info_et_english_name)
    EditText mInfoEtEnglishName;
    @BindView(R.id.info_et_date_of_birth)
    EditText mInfoEtDateOfBirth;
    @BindView(R.id.info_rg_marital_status)
    RadioGroup mInfoRgMaritalStatus;
    @BindView(R.id.info_et_passport_no)
    EditText mInfoEtPassportNo;
    @BindView(R.id.info_et_graduate_school)
    EditText mInfoEtGraduateSchool;
    @BindView(R.id.info_et_graduate_date)
    EditText mInfoEtGuaduateDate;
    @BindView(R.id.info_rg_english_language)
    RadioGroup mInfoRgEnglishLanguage;
    @BindView(R.id.info_rg_japanese_language)
    RadioGroup mInfoRgJapaneseLanguage;
    @BindView(R.id.info_et_language_others)
    EditText mInfoEtLanguageOthers;
    @BindView(R.id.info_et_major)
    EditText mInfoEtMajor;
    @BindView(R.id.info_et_email)
    EditText mInfoEtEmail;
    @BindView(R.id.info_et_mobile)
    EditText mInfoEtMobile;
    @BindView(R.id.info_et_telephone)
    EditText mInfoEtTelephone;
    @BindView(R.id.info_et_current_address)
    EditText mInfoEtCurrentAddress;
    @BindView(R.id.info_et_post_code)
    EditText mInfoEtPostCode;
    @BindView(R.id.info_et_emergency_contact)
    EditText mInfoEtEmergencyContact;
    @BindView(R.id.info_et_relationship)
    EditText mInfoEtRelationship;
    @BindView(R.id.info_et_telephone_no)
    EditText mInfoEtTelephoneNo;
    @BindView(R.id.info_rg_medical_history)
    RadioGroup mInfoRgMedicalHistory;
    @BindView(R.id.info_rg_employee_referral)
    RadioGroup mInfoRgEmployeeReferral;
    @BindView(R.id.info_et_yes_friend_name)
    EditText mInfoEtYesFriendName;
    @BindView(R.id.info_et_yes_referrer_name)
    EditText mInfoEtYesReferrerName;
    @BindView(R.id.info_rg_has_friend_pwc)
    RadioGroup mInfoRgHasFriendPwc;
    @BindView(R.id.info_et_hobbies)
    EditText mInfoEtHobbies;
    @BindView(R.id.info_et_position_applied_for)
    EditText mInfoEtPositionAppliedFor;
    @BindView(R.id.info_et_working_experience)
    EditText mInfoEtWorkingExperience;
    @BindView(R.id.info_et_channel_others)
    EditText mInfoEtChannelOthers;
    @BindView(R.id.info_et_date_available)
    EditText mInfoEtDateAvailable;

    @BindView(R.id.info_tv_chinese_name)
    TextView mInfoTvChineseName;
    @BindView(R.id.info_tv_english_name)
    TextView mInfoTvEnglishName;
    @BindView(R.id.info_tv_date_of_birth)
    TextView mInfoTvDateOfBirth;
    @BindView(R.id.info_tv_passport_no)
    TextView mInfoTvPassportNo;
    @BindView(R.id.info_tv_academic_degree)
    TextView mInfoTvAcademicDegree;
    @BindView(R.id.info_tv_language)
    TextView mInfoTvLanguage;
    @BindView(R.id.info_tv_major)
    TextView mInfoTvMajor;
    @BindView(R.id.info_tv_email)
    TextView mInfoTvEmail;
    @BindView(R.id.info_tv_mobile)
    TextView mInfoTvMobile;
    @BindView(R.id.info_tv_telephone)
    TextView mInfoTvTelephone;
    @BindView(R.id.info_tv_current_address)
    TextView mInfoTvCurrentAddress;
    @BindView(R.id.info_tv_post_code)
    TextView mInfoTvPostCode;
    @BindView(R.id.info_tv_emergency_contact)
    TextView mInfoTvEmergencyContact;
    @BindView(R.id.info_tv_relationship)
    TextView mInfoTvRelationship;
    @BindView(R.id.info_tv_telephone_no)
    TextView mInfoTvTelephoneNo;
    @BindView(R.id.info_tv_medical_history)
    TextView mInfoTvMedicalHistory;
    @BindView(R.id.info_tv_position_applied_for)
    TextView mInfoTvPositionAppliedFor;
    @BindView(R.id.info_tv_working_experience)
    TextView mInfoTvWorkingExperience;
    @BindView(R.id.info_tv_recruitment_channel)
    TextView mInfoTvRecruitmentChannel;
    @BindView(R.id.info_tv_date_available)
    TextView mInfoTvDateAvailable;
    @BindView(R.id.info_tv_employee_referral)
    TextView mInfoTvEmployeeReferral;
    @BindView(R.id.info_tv_has_friend_pwc)
    TextView mInfoTvHasFriendPwc;

    @BindView(R.id.info_iv_add_experience)
    protected ImageView mInfoIvAddExperience;
    @BindView(R.id.info_ll_work_experience)
    LinearLayout mInfoLlWorkExperiences;
    @BindView(R.id.info_we_work_experience1)
    WorkExperienceView mInfoWeWorkExperience1;
    @BindView(R.id.comment_et_content)
    @Nullable
    EditText mCommentEtContent;
    @BindView(R.id.comment_rg_result)
    @Nullable
    RadioGroup mCommentRgResult;
    @BindView(R.id.comment_spinner_interviewers)
    Spinner mCommentSpinnerInterviewers;
    @BindView(R.id.comment_iv_interviewers_arrow)
    ImageView mCommentIvInterviewersArrow;
    @BindView(R.id.comment_spinner_levels)
    Spinner mCommentSpinnerLevels;
    @BindView(R.id.comment_spinner_arrow)
    ImageView mCommentSpinnerArrow;
    @BindView(R.id.comment_ll_section)
    LinearLayout mCommentLlSection;
    private InterViewerAdapter mInterViewerAdapter;
    private LevelAdapter mLevelAdapter;
    protected GRadioGroup mInfoAcademicDegree;
    protected GRadioGroup mInfoRecruitmentChannel;
    private DatePickerDialog mDatePickerDialog;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        if (mCommentLlSection == null) {
            mCommentLlSection = (LinearLayout) mContentView.findViewById(R.id.comment_ll_section);
        }
        mCommentLlSection.setVisibility(View.GONE);
        Bitmap header = CandidateManager.getInstance().getHeader();
        showHeader(header);
    }

    public void showDateTimePicker(DatePickerDialog.OnDateSetListener listener, long maxDate, long minDate) {// 获取当前的年、月、日、小时、分钟
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        //5.0以上设置dialog风格
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mDatePickerDialog = new DatePickerDialog(getContext(), R.style.date_picker_dialog, listener, year, month, day);
        } else {
            mDatePickerDialog = new DatePickerDialog(getContext(), listener, year, month, day);
        }
        DatePicker datePicker = mDatePickerDialog.getDatePicker();
        if (minDate > 0) {
            datePicker.setMinDate(minDate);
        }

        if (maxDate > 0) {
            datePicker.setMaxDate(maxDate);
        }


        mDatePickerDialog.show();
    }

    public void hideDateTimePicker() {
        if (mDatePickerDialog != null && mDatePickerDialog.isShowing()) {
            mDatePickerDialog.dismiss();
            mDatePickerDialog = null;
        }
    }

    public void setRedColor(IssueView issueView) {
        EditText et = issueView.issueET;
        TextView tv = issueView.descrption;
        RadioGroup rg = issueView.issueRG;
        if (et != null) {
            et.setBackgroundResource(R.drawable.shape_bg_info_edit_text_red);
        }

        if (tv != null) {
            tv.setTextColor(getColor(R.color.red));
        }

        if (rg != null) {
            int childCount = rg.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = rg.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton rb = (RadioButton) child;
                    rb.setTextColor(getColor(R.color.red));
                }
            }
        }
    }

    public void setBlackColor(IssueView issueView) {
        EditText et = issueView.issueET;
        TextView tv = issueView.descrption;
        RadioGroup rg = issueView.issueRG;
        if (et != null) {
            et.setBackgroundResource(R.drawable.shape_bg_info_edit_text_grey);
        }


        if (tv != null) {
            tv.setTextColor(getColor(R.color.gray_dark));
        }

        if (rg != null) {
            int childCount = rg.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = rg.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton rb = (RadioButton) child;
                    rb.setTextColor(getColor(R.color.black));
                }
            }
        }
    }

    protected String getIssueMessage(String profix, String body, String tail) {
        StringBuilder sb = new StringBuilder(profix);
        sb.append("\"").append(body).append("\"").append(tail);
        return sb.toString();
    }

    public IssueView checkEmpty() {
        EditText issueEditText = null;
        TextView issueTextView = null;
        RadioGroup issueRG = null;

        String mDateOfBirth = mInfoEtDateOfBirth.getText().toString().trim();
        if (TextUtils.isEmpty(mDateOfBirth)) {
            issueEditText = mInfoEtDateOfBirth;
            issueTextView = mInfoTvDateOfBirth;
            return new IssueView(issueRG, issueTextView, issueEditText);
        }

        String mMajor = mInfoEtMajor.getText().toString().trim();
        if (TextUtils.isEmpty(mMajor)) {
            issueEditText = mInfoEtMajor;
            issueTextView = mInfoTvMajor;
            return new IssueView(issueRG, issueTextView, issueEditText);
        }
        String mEmail = mInfoEtEmail.getText().toString().trim();
        if (TextUtils.isEmpty(mEmail)) {
            issueEditText = mInfoEtEmail;
            issueTextView = mInfoTvEmail;
            return new IssueView(issueRG, issueTextView, issueEditText);
        }
        String mMobile = mInfoEtMobile.getText().toString().trim();
        if (TextUtils.isEmpty(mMobile)) {
            issueEditText = mInfoEtMobile;
            issueTextView = mInfoTvMobile;
            return new IssueView(issueRG, issueTextView, issueEditText);
        }
        String mTelephone = mInfoEtTelephone.getText().toString().trim();
        if (TextUtils.isEmpty(mTelephone)) {
            issueEditText = mInfoEtTelephone;
            issueTextView = mInfoTvTelephone;
            return new IssueView(issueRG, issueTextView, issueEditText);
        }
        String mCurrentAddress = mInfoEtCurrentAddress.getText().toString().trim();
        if (TextUtils.isEmpty(mCurrentAddress)) {
            issueEditText = mInfoEtCurrentAddress;
            issueTextView = mInfoTvCurrentAddress;
            return new IssueView(issueRG, issueTextView, issueEditText);
        }
        String mPostCode = mInfoEtPostCode.getText().toString().trim();
        if (TextUtils.isEmpty(mPostCode)) {
            issueEditText = mInfoEtPostCode;
            issueTextView = mInfoTvPostCode;
            return new IssueView(issueRG, issueTextView, issueEditText);
        }
        String mEmergencyContact = mInfoEtEmergencyContact.getText().toString().trim();
        if (TextUtils.isEmpty(mEmergencyContact)) {
            issueEditText = mInfoEtEmergencyContact;
            issueTextView = mInfoTvEmergencyContact;
            return new IssueView(issueRG, issueTextView, issueEditText);
        }
        String mRelationship = mInfoEtRelationship.getText().toString().trim();
        if (TextUtils.isEmpty(mRelationship)) {
            issueEditText = mInfoEtRelationship;
            issueTextView = mInfoTvRelationship;
            return new IssueView(issueRG, issueTextView, issueEditText);
        }
        String mTelephoneNo = mInfoEtTelephoneNo.getText().toString().trim();
        if (TextUtils.isEmpty(mTelephoneNo)) {
            issueEditText = mInfoEtTelephoneNo;
            issueTextView = mInfoTvTelephoneNo;
            return new IssueView(issueRG, issueTextView, issueEditText);
        }

        String mPositionAppliedFor = mInfoEtPositionAppliedFor.getText().toString().trim();
        if (TextUtils.isEmpty(mPositionAppliedFor)) {
            issueEditText = mInfoEtPositionAppliedFor;
            issueTextView = mInfoTvPositionAppliedFor;
            return new IssueView(issueRG, issueTextView, issueEditText);
        }
        String mWorkingExperience = mInfoEtWorkingExperience.getText().toString().trim();
        if (TextUtils.isEmpty(mWorkingExperience)) {
            issueEditText = mInfoEtWorkingExperience;
            issueTextView = mInfoTvWorkingExperience;
            return new IssueView(issueRG, issueTextView, issueEditText);
        }

        for (int i = 0; i < mInfoLlWorkExperiences.getChildCount(); i++) {
            View child = mInfoLlWorkExperiences.getChildAt(i);
            if (child instanceof WorkExperienceView) {
                WorkExperienceView expView = (WorkExperienceView) child;
                if (expView.isFillUp()) {
                    if (expView.hasEmpty()) {
                        issueEditText = expView.getIssueET();
                        issueTextView = expView.getIssueTV();
                        return new IssueView(issueRG, issueTextView, issueEditText);
                    }
                }
            }
        }

        boolean hasGender = isRadioGroupSelected(mInfoRgGender);
        if (hasGender == false) {
            issueRG = mInfoRgGender;
            issueTextView = null;
            return new IssueView(issueRG, issueTextView, issueEditText);
        }

        boolean hasAcademicDegree = mInfoAcademicDegree.hasRadioButtonChecked();
        if (hasAcademicDegree == false) {
            issueRG = null;
            issueTextView = mInfoTvAcademicDegree;
            return new IssueView(issueRG, issueTextView, issueEditText);
        }

        boolean hasEnglishLanguage = isRadioGroupSelected(mInfoRgEnglishLanguage);
        if (hasEnglishLanguage == false) {
            issueRG = mInfoRgEnglishLanguage;
            issueTextView = mInfoTvLanguage;
            return new IssueView(issueRG, issueTextView, issueEditText);
        }

        boolean hasMedicalHistory = isRadioGroupSelected(mInfoRgMedicalHistory);
        if (hasMedicalHistory == false) {
            issueRG = mInfoRgMedicalHistory;
            issueTextView = mInfoTvMedicalHistory;
            return new IssueView(issueRG, issueTextView, issueEditText);
        }

        boolean hasEmployeeReferral = isRadioGroupSelected(mInfoRgEmployeeReferral);
        if (hasEmployeeReferral == false) {
            issueRG = mInfoRgEmployeeReferral;
            issueTextView = mInfoTvEmployeeReferral;
            return new IssueView(issueRG, issueTextView, issueEditText);
        } else {
            int checkId = mInfoRgEmployeeReferral.getCheckedRadioButtonId();
            if (checkId == R.id.info_rb_yes_referrer_name) {
                String yesReferrerName = mInfoEtYesReferrerName.getText().toString().trim();
                if (TextUtils.isEmpty(yesReferrerName)) {
                    issueEditText = mInfoEtYesReferrerName;
                    issueTextView = mInfoTvEmployeeReferral;
                    return new IssueView(issueRG, issueTextView, issueEditText);
                }
            }
        }

        boolean hasFriendPwc = isRadioGroupSelected(mInfoRgHasFriendPwc);
        if (hasFriendPwc == false) {
            issueRG = mInfoRgHasFriendPwc;
            issueTextView = mInfoTvHasFriendPwc;
            return new IssueView(issueRG, issueTextView, issueEditText);
        } else {
            int checkId = mInfoRgHasFriendPwc.getCheckedRadioButtonId();
            if (checkId == R.id.info_rb_yes_friend_name) {
                String yesFriendsName = mInfoEtYesFriendName.getText().toString().trim();
                if (TextUtils.isEmpty(yesFriendsName)) {
                    issueEditText = mInfoEtYesFriendName;
                    issueTextView = mInfoTvHasFriendPwc;
                    return new IssueView(issueRG, issueTextView, issueEditText);
                }
            }
        }

        boolean recruitmentChannel = mInfoRecruitmentChannel.hasRadioButtonChecked();
        if (recruitmentChannel == false) {
            issueRG = null;
            issueTextView = mInfoTvRecruitmentChannel;
            return new IssueView(issueRG, issueTextView, issueEditText);
        }

        return null;
    }

    protected boolean isRadioGroupSelected(RadioGroup rg) {
        return rg.getCheckedRadioButtonId() != -1;
    }

    protected String getRadioGroupValue(RadioGroup rg) {
        RadioButton selectBtn = getSelectRadioButtonView(rg);
        if (selectBtn != null) {
            return selectBtn.getText().toString();
        }
        return "";

    }

    protected int getRadioGroupSelectedIndex(RadioGroup rg) {
        RadioButton selectBtn = getSelectRadioButtonView(rg);
        return rg.indexOfChild(selectBtn);

    }

    protected String getEmplyeeReferralName() {
        int checkId = mInfoRgEmployeeReferral.getCheckedRadioButtonId();
        if (checkId == R.id.info_rb_yes_referrer_name) {
            return getEditText(mInfoEtYesReferrerName);
        }
        return getRadioGroupValue(mInfoRgEmployeeReferral);
    }

    protected String getHasFriendPwcName() {
        int checkId = mInfoRgHasFriendPwc.getCheckedRadioButtonId();
        if (checkId == R.id.info_rb_yes_friend_name) {
            return getEditText(mInfoEtYesFriendName);
        }
        return getRadioGroupValue(mInfoRgHasFriendPwc);
    }

    protected String getRecruitChannel() {
        int checkId = mInfoRecruitmentChannel.getCheckedRadioButtonId();
        if (checkId == R.id.info_rb_others) {
            return getEditText(mInfoEtChannelOthers);
        }
        return mInfoRecruitmentChannel.getCheckedRadioButtonDes();
    }

    protected RadioButton getSelectRadioButtonView(RadioGroup rg) {
        return (RadioButton) getView().findViewById(rg.getCheckedRadioButtonId());
    }

    public Candidate getCandidate() {
        Candidate candidate = new Candidate();
        candidate.basic = new Candidate.Basic();
        candidate.basic.chineseName = getEditText(mInfoEtChineseName);
        candidate.basic.englishName = getEditText(mInfoEtEnglishName);
        candidate.basic.gender = getRadioGroupValue(mInfoRgGender);
        candidate.basic.birth = getEditText(mInfoEtDateOfBirth);
        candidate.basic.married = getRadioGroupValue(mInfoRgMaritalStatus);
        candidate.basic.passportNo = getEditText(mInfoEtPassportNo);
        candidate.basic.graduateSchool = getEditText(mInfoEtGraduateSchool);
        candidate.basic.graduateDate = getEditText(mInfoEtGuaduateDate);
        candidate.basic.academicDegree = mInfoAcademicDegree.getCheckedRadioButtonDes();
        candidate.basic.languageLevel = new Candidate.LanguageLevel();
        candidate.basic.languageLevel.english = getRadioGroupValue(mInfoRgEnglishLanguage);
        candidate.basic.languageLevel.jspanese = getRadioGroupValue(mInfoRgJapaneseLanguage);
        candidate.basic.languageLevel.others = getEditText(mInfoEtLanguageOthers);

        candidate.basic.major = getEditText(mInfoEtMajor);
        candidate.basic.email = getEditText(mInfoEtEmail);
        candidate.basic.mobile = getEditText(mInfoEtMobile);
        candidate.basic.telephone = getEditText(mInfoEtTelephone);
        candidate.basic.currentAddress = getEditText(mInfoEtCurrentAddress);
        candidate.basic.postCode = getEditText(mInfoEtPostCode);

        candidate.basic.emergency = new Candidate.Contact(getEditText(mInfoEtEmergencyContact), getEditText(mInfoEtRelationship), getEditText(mInfoEtTelephoneNo));
        candidate.basic.employeeReferral = getEditText(mInfoEtYesReferrerName);
        candidate.basic.interests = getEditText(mInfoEtHobbies);
        candidate.basic.medicalHistory = getRadioGroupValue(mInfoRgMedicalHistory);
        candidate.basic.employeeReferral = getEmplyeeReferralName();
        candidate.basic.hasFriendsInPWC = getHasFriendPwcName();

        candidate.position = new Candidate.Position();
        candidate.position.applyFor = getEditText(mInfoEtPositionAppliedFor);
        candidate.position.experience = getEditText(mInfoEtWorkingExperience);
        candidate.position.recruitChannel = getRecruitChannel();
        candidate.workExperiences = new ArrayList<>();

        for (int i = 0; i < mInfoLlWorkExperiences.getChildCount(); i++) {
            View child = mInfoLlWorkExperiences.getChildAt(i);
            if (child instanceof WorkExperienceView) {
                WorkExperienceView expChild = (WorkExperienceView) child;
                if (expChild.isFillUp()) {
                    candidate.workExperiences.add(expChild.getExperience());
                }
            }
        }
        return candidate;
    }

    public String getEditText(EditText et) {
        return et.getText().toString().trim();
    }

    public void setCandidate(Candidate candidate) {
        mInfoEtChineseName.setText(candidate.basic.chineseName);
        mInfoEtEnglishName.setText(candidate.basic.englishName);
        mInfoEtDateOfBirth.setText(candidate.basic.birth);
        mInfoEtPassportNo.setText(candidate.basic.passportNo);
        mInfoEtGraduateSchool.setText(candidate.basic.graduateSchool);
        mInfoEtGuaduateDate.setText(candidate.basic.graduateDate);
        mInfoEtMajor.setText(candidate.basic.major);
        mInfoEtEmail.setText(candidate.basic.email);
        mInfoEtMobile.setText(candidate.basic.mobile);
        mInfoEtTelephone.setText(candidate.basic.telephone);
        mInfoEtCurrentAddress.setText(candidate.basic.currentAddress);
        mInfoEtPostCode.setText(candidate.basic.postCode);
        mInfoEtEmergencyContact.setText(candidate.basic.emergency.name);
        mInfoEtRelationship.setText(candidate.basic.emergency.relationship);
        mInfoEtTelephoneNo.setText(candidate.basic.emergency.phone);
        mInfoEtYesFriendName.setText(candidate.basic.hasFriendsInPWC);
        mInfoEtHobbies.setText(candidate.basic.interests);
        mInfoEtPositionAppliedFor.setText(candidate.position.applyFor);
        mInfoEtWorkingExperience.setText(candidate.position.experience);

        int expSize = candidate.workExperiences.size();
        for (int i = 0; i < expSize; i++) {
            Candidate.Experience exp = candidate.workExperiences.get(i);
            if (i == 0) {
                mInfoWeWorkExperience1.setExperience(exp);
            } else {
                WorkExperienceView expView = addExperienceLabelView();
                expView.setExperience(exp);
            }
        }

        String gender = candidate.basic.gender;
        if (TextUtils.equals(gender, "先生") || TextUtils.equals(gender, "Male")) {
            mInfoRgGender.check(R.id.info_rb_gender_male);
        } else {
            mInfoRgGender.check(R.id.info_rb_gender_female);
        }

        String married = candidate.basic.married;
        if (!TextUtils.isEmpty(married)) {
            if (TextUtils.equals(married, "已婚") || TextUtils.equals(married, "Married")) {
                mInfoRgMaritalStatus.check(R.id.info_rb_marital_status_married);
            } else {
                mInfoRgMaritalStatus.check(R.id.info_rb_marital_status_single);
            }
        }

        String academicDegree = candidate.basic.academicDegree;
        if (TextUtils.equals(academicDegree, "大专") || TextUtils.equals(academicDegree, "College")) {
            mInfoAcademicDegree.check(R.id.info_rb_academic_degree_college);
        } else if (TextUtils.equals(academicDegree, "本科") || TextUtils.equals(academicDegree, "Bachelor")) {
            mInfoAcademicDegree.check(R.id.info_rb_academic_degree_bachelor);
        } else if (TextUtils.equals(academicDegree, "硕士") || TextUtils.equals(academicDegree, "Master")) {
            mInfoAcademicDegree.check(R.id.info_rb_academic_degree_master);
        } else if (TextUtils.equals(academicDegree, "博士") || TextUtils.equals(academicDegree, "Doctorate")) {
            mInfoAcademicDegree.check(R.id.info_rb_academic_degree_doctorate);
        } else if (TextUtils.equals(academicDegree, "博士后")) {
            mInfoAcademicDegree.check(R.id.info_rb_academic_degree_doctorate_senior);
        }

        Candidate.LanguageLevel language = candidate.basic.languageLevel;
        String english = language.english;
        String others = language.others;
        if (TextUtils.equals(english, "Excellent") || TextUtils.equals(english, "TEM-8")) {
            mInfoRgEnglishLanguage.check(R.id.info_rb_language_excellent);
        } else if (TextUtils.equals(english, "Good") || TextUtils.equals(english, "TEM-4")) {
            mInfoRgEnglishLanguage.check(R.id.info_rb_language_good);
        } else if (TextUtils.equals(english, "Fair") || TextUtils.equals(english, "CET-6")) {
            mInfoRgEnglishLanguage.check(R.id.info_rb_language_fair);
        } else {
            mInfoRgEnglishLanguage.check(R.id.info_rb_language_poor);
        }

        if (!TextUtils.isEmpty(others)) {
            mInfoEtLanguageOthers.setText(others);
        }

        String medicalHistory = candidate.basic.medicalHistory;
        if (TextUtils.equals(medicalHistory, "有") || TextUtils.equals(medicalHistory, "Yes")) {
            mInfoRgMedicalHistory.check(R.id.info_rb_yes_medical_history);
        } else {
            mInfoRgMedicalHistory.check(R.id.info_rb_no_medical_history);
        }

        String emplyeeReferral = candidate.basic.employeeReferral;
        if (TextUtils.equals(emplyeeReferral, "No") || TextUtils.equals(emplyeeReferral, "无")) {
            mInfoRgEmployeeReferral.check(R.id.info_rb_no_referrer_name);
        } else {
            mInfoRgEmployeeReferral.check(R.id.info_rb_yes_referrer_name);
            mInfoEtYesReferrerName.setText(emplyeeReferral);
        }

        String hasFriendsInPWC = candidate.basic.hasFriendsInPWC;
        if (TextUtils.equals(hasFriendsInPWC, "No") || TextUtils.equals(hasFriendsInPWC, "无")) {
            mInfoRgHasFriendPwc.check(R.id.info_rb_no_friend_name);
        } else {
            mInfoRgHasFriendPwc.check(R.id.info_rb_yes_friend_name);
            mInfoEtYesFriendName.setText(hasFriendsInPWC);
        }

        String recruitChannel = candidate.position.recruitChannel;
        if (TextUtils.equals(recruitChannel, "猎头") || TextUtils.equals(recruitChannel, "Headhunting")) {
            mInfoRecruitmentChannel.check(R.id.info_rb_channel_headhunting);
        } else if (TextUtils.equals(recruitChannel, "51job")) {
            mInfoRecruitmentChannel.check(R.id.info_rb_51job);
        } else if (TextUtils.equals(recruitChannel, "智联招聘")) {
            mInfoRecruitmentChannel.check(R.id.info_rb_zhilian);
        } else if (TextUtils.equals(recruitChannel, "员工推荐") || TextUtils.equals(recruitChannel, "Employee Referral")) {
            mInfoRecruitmentChannel.check(R.id.info_rb_employee);
        } else if (TextUtils.equals(recruitChannel, "WeChat")) {
            mInfoRecruitmentChannel.check(R.id.info_rb_wechat);
        } else {
            mInfoRecruitmentChannel.check(R.id.info_rb_others);
            mInfoEtChannelOthers.setText(recruitChannel);
        }
    }

    public void setViewEnable(boolean enable) {
        if (mInfoEtChineseName != null) {
            mInfoEtChineseName.setEnabled(enable);
        }

        if (mInfoRgGender != null) {
            setRadioGroupEnable(mInfoRgGender, enable);
        }

        if (mInfoEtEnglishName != null) {
            mInfoEtEnglishName.setEnabled(enable);
        }

        if (mInfoEtDateOfBirth != null) {
            mInfoEtDateOfBirth.setEnabled(enable);
        }

        if (mInfoRgMaritalStatus != null) {
            setRadioGroupEnable(mInfoRgMaritalStatus, enable);
        }

        if (mInfoEtPassportNo != null) {
            mInfoEtPassportNo.setEnabled(enable);
        }

        if (mInfoAcademicDegree != null) {
            mInfoAcademicDegree.setEnabled(enable);
        }

        if (mInfoEtGraduateSchool != null) {
            mInfoEtGraduateSchool.setEnabled(enable);
        }

        if (mInfoEtGuaduateDate != null) {
            mInfoEtGuaduateDate.setEnabled(enable);
        }

        if (mInfoRgEnglishLanguage != null) {
            setRadioGroupEnable(mInfoRgEnglishLanguage, enable);
        }

        if (mInfoRgJapaneseLanguage != null) {
            setRadioGroupEnable(mInfoRgJapaneseLanguage, enable);
        }

        if (mInfoEtLanguageOthers != null) {
            mInfoEtLanguageOthers.setEnabled(enable);
        }

        if (mInfoEtMajor != null) {
            mInfoEtMajor.setEnabled(enable);
        }

        if (mInfoEtEmail != null) {
            mInfoEtEmail.setEnabled(enable);
        }

        if (mInfoEtMobile != null) {
            mInfoEtMobile.setEnabled(enable);
        }

        if (mInfoEtTelephone != null) {
            mInfoEtTelephone.setEnabled(enable);
        }

        if (mInfoEtCurrentAddress != null) {
            mInfoEtCurrentAddress.setEnabled(enable);
        }

        if (mInfoEtPostCode != null) {
            mInfoEtPostCode.setEnabled(enable);
        }

        if (mInfoEtEmergencyContact != null) {
            mInfoEtEmergencyContact.setEnabled(enable);
        }

        if (mInfoEtRelationship != null) {
            mInfoEtRelationship.setEnabled(enable);
        }

        if (mInfoEtTelephoneNo != null) {
            mInfoEtTelephoneNo.setEnabled(enable);
        }

        if (mInfoRgMedicalHistory != null) {
            setRadioGroupEnable(mInfoRgMedicalHistory, enable);
        }

        if (mInfoRgEmployeeReferral != null) {
            setRadioGroupEnable(mInfoRgEmployeeReferral, enable);
        }

        if (mInfoEtYesFriendName != null) {
            mInfoEtYesFriendName.setEnabled(enable);
        }

        if (mInfoEtYesReferrerName != null) {
            mInfoEtYesReferrerName.setEnabled(enable);
        }

        if (mInfoRgHasFriendPwc != null) {
            setRadioGroupEnable(mInfoRgHasFriendPwc, enable);
        }

        if (mInfoEtHobbies != null) {
            mInfoEtHobbies.setEnabled(enable);
        }

        if (mInfoEtPositionAppliedFor != null) {
            mInfoEtPositionAppliedFor.setEnabled(enable);
        }

        if (mInfoEtWorkingExperience != null) {
            mInfoEtWorkingExperience.setEnabled(enable);
        }

        if (mInfoEtChannelOthers != null) {
            mInfoEtChannelOthers.setEnabled(enable);
        }

        if (mInfoRecruitmentChannel != null) {
            mInfoRecruitmentChannel.setEnabled(enable);
        }

        for (int i = 0; i < mInfoLlWorkExperiences.getChildCount(); i++) {
            View child = mInfoLlWorkExperiences.getChildAt(i);
            if (child instanceof WorkExperienceView) {
                WorkExperienceView expView = (WorkExperienceView) child;
                expView.setExpEnable(enable);
            }
        }
    }

    public void setRadioGroupEnable(RadioGroup rg, boolean enable) {
        rg.setEnabled(enable);
        int childCount = rg.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = rg.getChildAt(i);
            if (child instanceof RadioButton) {
                child.setEnabled(enable);
            }
        }
    }

    public Comment getComment() {
        Comment comment = new Comment();
        comment.comment = getEditText(mCommentEtContent);
        comment.result = getRadioGroupSelectedIndex(mCommentRgResult);
        comment.commentTime = System.currentTimeMillis();
        Recruiter recruiter = (Recruiter) mCommentSpinnerInterviewers.getSelectedItem();
        comment.recruiterName = recruiter.name;
        comment.guid = recruiter.guid;
        comment.level = mCommentSpinnerLevels.getSelectedItem().toString();
        return comment;
    }

    public boolean checkCommentEmpty() {
        String comment = getEditText(mCommentEtContent);
        if (TextUtils.isEmpty(comment)) {
            return true;
        }

        boolean result = isRadioGroupSelected(mCommentRgResult);
        if (!result) {
            return true;
        }

        String select = mCommentSpinnerInterviewers.getSelectedItem().toString();
        if (TextUtils.isEmpty(select)) {
            return true;
        }

        String level = mCommentSpinnerLevels.getSelectedItem().toString();
        if (TextUtils.isEmpty(level)) {
            return true;
        }

        return false;
    }

    public void initSpinner(List<Recruiter> recruiters) {
        mInterViewerAdapter = new InterViewerAdapter(recruiters);
        mCommentSpinnerInterviewers.setAdapter(mInterViewerAdapter);

        String[] levels = getStringArray(R.array.levels);
        mLevelAdapter = new LevelAdapter(levels);
        mCommentSpinnerLevels.setAdapter(mLevelAdapter);
        mCommentSpinnerLevels.setSelection(levels.length - 1, true);
    }

    public void showHeader(Bitmap pic) {
        mInfoIvHeader.setImageBitmap(pic);
    }

    public void loadHeadIcon(String headUrl) {
        if (headUrl != null) {
            mInfoIvHeader.setImageURI(Uri.parse(headUrl));
        }

    }

    public String getChineseName() {
        return getEditText(mInfoEtChineseName);
    }

    public WorkExperienceView addExperienceLabelView() {
        View divideLine = inflateView(R.layout.view_divide_line_add_experience, mInfoLlWorkExperiences);
        mInfoLlWorkExperiences.addView(divideLine);
        WorkExperienceView expView = new WorkExperienceView(getContext());
        mInfoLlWorkExperiences.addView(expView, mInfoWeWorkExperience1.getLayoutParams());
        return expView;
    }

    public void showSelectedDate(EditText et, String date) {
        et.setText(date);
    }
}
