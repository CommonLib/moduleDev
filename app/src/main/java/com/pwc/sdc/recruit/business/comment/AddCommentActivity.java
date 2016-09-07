package com.pwc.sdc.recruit.business.comment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.ToolBarActivity;
import com.pwc.sdc.recruit.business.info.resume.InfoFragment;
import com.pwc.sdc.recruit.constants.Constants;
import com.pwc.sdc.recruit.data.model.Candidate;
import com.pwc.sdc.recruit.data.model.Comment;
import com.pwc.sdc.recruit.data.model.Recruiter;

import java.util.ArrayList;

import butterknife.OnClick;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class AddCommentActivity extends ToolBarActivity<AddCommentPresenter> implements AddCommentContract.View, View.OnClickListener {

    private InfoFragment mInfoFragment;
    private Bundle mBundle;
    private TextView mToolRightDes;
    private AlertDialog mExitDialog;
    private AlertDialog mFillCommentDialog;

    @Override
    protected void initView() {
        super.initView();
        mToolRightDes = (TextView) mToolbar.findViewById(R.id.common_tv_right_des);
        setBackButtonEnable(false);
        setActionBarCenterTitle(R.string.profile);
        setActionBarCenterTextSize(20);
        setActionBarTitleEnable(false);
        setActionBarCenterTextColor(R.color.black_3f);
        mToolbar.setBackgroundColor(getResColor(R.color.background_holo_light));
        mToolRightDes.setVisibility(View.VISIBLE);
        mToolRightDes.setTextColor(getResColor(R.color.red_E3));
        mToolRightDes.setText(getString(R.string.leave_comment));
    }

    @Override
    protected void initData() {
        mPresenter.requestCandidate();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_single_fragment;
    }

    @Override
    protected AddCommentPresenter instancePresenter() {
        return new AddCommentPresenter(this, new AddCommentMode());
    }

    @Override
    public void showCandidate(Candidate candidate) {
        mInfoFragment = null;
        if (TextUtils.equals(candidate.language, Constants.PROFILE_LANGUAGE_FORMAT_CHNIESE)) {
            mInfoFragment = obtainFragment(AddCommentCNFragment.class);
        } else {
            mInfoFragment = obtainFragment(AddCommentENFragment.class);
        }
        mBundle.putParcelable("candidate", candidate);
        sendParcelToFragment(mInfoFragment, mBundle);
        replaceFragment(R.id.single_fl_container, mInfoFragment);
    }

    @Override
    public void onBackPressed() {
        mExitDialog = showAlertDialog(null, R.layout.dialog_exit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PwcApplication.getInstance().exit();
            }
        }, this);
    }

    @Override
    public Comment getComment() {
        return mInfoFragment.getComment();
    }


    @Override
    public void showInterViewer(ArrayList<Recruiter> recruiters) {
        mBundle = obtainBundle();
        mBundle.putParcelableArrayList("recruiters", recruiters);
    }

    @Override
    public boolean checkCommentEmpty() {
        return mInfoFragment.checkCommentEmpty();
    }

    @Override
    public void showAlertDialog() {
        mFillCommentDialog = showAlertDialog(getString(R.string.alert_dialog_message_comment), R.layout.dialog_fill_blank, this);
    }

    @Override
    protected void handleIntent(Intent intent) {
        mPresenter.handleIntent(intent);
    }

    @OnClick({R.id.common_tv_right_des})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_btn_confirm:
                mFillCommentDialog.dismiss();
                break;
            case R.id.common_tv_right_des:
                mPresenter.next();
                break;
            case R.id.dialog_btn_cancel:
                mExitDialog.dismiss();
                break;
        }
    }
}
