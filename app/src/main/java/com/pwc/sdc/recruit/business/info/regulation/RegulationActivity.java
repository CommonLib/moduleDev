package com.pwc.sdc.recruit.business.info.regulation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BaseFragment;
import com.pwc.sdc.recruit.base.SingleFragmentActivity;

import de.greenrobot.event.EventBus;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class RegulationActivity extends SingleFragmentActivity<RegulationPresenter> implements RegulationConstract.View, View.OnClickListener, DialogInterface.OnDismissListener {


    private RegulationFragment mRegulationFragment;
    private ProgressBar mUploadPbProgress;
    private TextView mUploadTvDescription;
    private TextView mUploadTvProgress;

    @Override
    protected BaseFragment getFirstFragment() {
        mRegulationFragment = obtainFragment(RegulationFragment.class);
        return mRegulationFragment;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void showAlertDialog() {
        showAlertDialog(getString(R.string.alert_dialog_message_regulation), R.layout.dialog_fill_blank, this);
    }

    @Override
    public void showProgressDialog() {
        AlertDialog ad = showAlertDialog(null, R.layout.dialog_upload_progress, null);
        mUploadPbProgress = (ProgressBar) ad.findViewById(R.id.upload_pb_progress);
        mUploadTvDescription = (TextView) ad.findViewById(R.id.upload_tv_description);
        mUploadTvProgress = (TextView) ad.findViewById(R.id.upload_tv_progress);
        mUploadPbProgress.setProgress(0);
        mUploadPbProgress.setMax(100);
        ad.setOnDismissListener(this);
        ad.setCancelable(false);
        ad.setCanceledOnTouchOutside(false);
    }

    @Override
    public void hideProgressDialog() {
        hideAlertDialog();
    }

    @Override
    public void setUploadDescription(String title) {
        mUploadTvDescription.setText(title);
    }

    @Override
    public void uploadProgressBar(int progress) {
        mUploadPbProgress.setProgress(progress);
        mUploadTvProgress.setText(progress + "%");
    }

    @Override
    public void onClick(View v) {
        hideAlertDialog();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mPresenter.onDialogDismiss();
    }
}
