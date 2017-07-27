package com.pwc.sdc.recruit.business.info.resume;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BaseFragment;
import com.pwc.sdc.recruit.base.ToolBarActivity;
import com.pwc.sdc.recruit.data.model.Candidate;

import java.util.Locale;

import butterknife.OnClick;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class InfoActivity extends ToolBarActivity<InfoPresenter> implements InfoConstract.View, View.OnClickListener {


    private InfoFragment mInfoFragment;
    private TextView mRightTvDes;

    protected BaseFragment getFirstFragment() {
        Locale currentLocale = getCurrentLocale();
        if ("zh".equalsIgnoreCase(currentLocale.getLanguage())) {
            mInfoFragment = obtainFragment(InfoCNFragment.class);
        } else {
            mInfoFragment = obtainFragment(InfoEnFragment.class);
        }
        return mInfoFragment;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        super.initView();
        setBackButtonEnable(false);
        setActionBarCenterTitle(R.string.login_create_profile);
        setActionBarCenterTextSize(20);
        setActionBarTitleEnable(false);
        setActionBarCenterTextColor(R.color.black_3f);
        mToolbar.setBackgroundColor(getResColor(R.color.background_holo_light));
        mRightTvDes = (TextView) mToolbar.findViewById(R.id.common_tv_right_des);
        mRightTvDes.setVisibility(View.VISIBLE);
        mRightTvDes.setText(getString(R.string.regulation_submit));
        mRightTvDes.setTextColor(getResColor(R.color.red_E3));
        BaseFragment firstFragment = getFirstFragment();
        replaceFragment(R.id.single_fl_container, firstFragment);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_single_fragment;
    }

    private Locale getCurrentLocale() {
        return getResources().getConfiguration().locale;
    }

    @Override
    public IssueView checkEmpty() {
        return mInfoFragment.checkEmpty();
    }

    @Override
    public void setBlackColor(IssueView issueView) {
        mInfoFragment.setBlackColor(issueView);
    }

    @Override
    public void setRedColor(IssueView issueView) {
        mInfoFragment.setRedColor(issueView);
    }

    @Override
    public void showAlertDialog() {
        showAlertDialog(null, R.layout.dialog_fill_blank, this);
    }

    @Override
    public void showAlertDialog(String message) {
        showAlertDialog(message, R.layout.dialog_fill_blank, this);
    }

    @Override
    public Candidate getInterViewer() {
        return mInfoFragment.getCandidate();
    }

    @Override
    public String getChineseName() {
        return mInfoFragment.getChineseName();
    }

    @Override
    public void showDateTimePicker(DatePickerDialog.OnDateSetListener dateListener, long maxDate, long minDate) {
        mInfoFragment.showDateTimePicker(dateListener, maxDate, minDate);
    }

    @Override
    public void showSelectedDate(EditText et, String date) {
        mInfoFragment.showSelectedDate(et, date);
    }

    @Override
    public void hideDateTimerPicker() {
        mInfoFragment.hideDateTimePicker();
    }

    @OnClick({R.id.common_tv_right_des})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_tv_right_des:
                mPresenter.next();
                break;
            case R.id.dialog_btn_confirm:
                hideAlertDialog();
                break;
        }
    }

}
