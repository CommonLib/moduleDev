package com.pwc.sdc.recruit.business.info.regulation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.widget.CheckBox;

import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BasePresenter;
import com.pwc.sdc.recruit.business.login.LoginActivity;
import com.pwc.sdc.recruit.config.AppConfig;
import com.pwc.sdc.recruit.constants.Constants;
import com.pwc.sdc.recruit.data.model.Candidate;
import com.pwc.sdc.recruit.data.model.CandidateId;
import com.pwc.sdc.recruit.data.model.UploadInfo;
import com.pwc.sdc.recruit.manager.CandidateManager;
import com.thirdparty.proxy.log.TLog;
import com.thirdparty.proxy.net.http.retrofit.HttpResponse;
import com.thirdparty.proxy.net.http.retrofit.rx.HttpSubscriber;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import rx.Observable;
import rx.Subscription;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class RegulationPresenter extends BasePresenter<RegulationActivity, RegulationMode> implements RegulationConstract.Presenter {

    private UploadBroadCastReceiver mUploadReceiver;
    private Subscription mUploadResumeConnection;

    public boolean mIsHideProgressDialog = false;


    @Override
    public void submit(CheckBox regulation1, CheckBox regulation2) {
        if (!regulation1.isChecked() || !regulation2.isChecked()) {
            mViewLayer.showAlertDialog();
            return;
        }
        mViewLayer.showProgressDialog();
        mViewLayer.setUploadDescription(mViewLayer.getString(R.string.upload_resume));
        Candidate candidate = CandidateManager.getInstance().getCandidate();
        candidate.submitTime = System.currentTimeMillis();

        Observable<HttpResponse<CandidateId>> uploadResApi = getBackPointService().uploadResume(candidate);
        mUploadResumeConnection = mModelLayer.uploadResume(uploadResApi, new HttpSubscriber<CandidateId>() {
            @Override
            public void onSuccess(CandidateId id) {
                CandidateManager.getInstance().getCandidate().candidateId = id.candidateId;
                //更新进度条
                mViewLayer.uploadProgressBar(100);
                mViewLayer.setUploadDescription(mViewLayer.getString(R.string.upload_header));
                //开启服务上传头像
                mViewLayer.uploadProgressBar(0);

                //无网络测试使用
                if (AppConfig.MODE_CONNECTION) {
                    Intent service = new Intent(mViewLayer, UploadService.class);
                    mViewLayer.startService(service);
                } else {
                    PwcApplication.getInstance().sendBroadCast(Constants.ACTION_UPLOAD_SUCCESS, Constants.CATEGORY_DEFAULT);
                }

            }

            @Override
            public void onFailure(int errorCode, String message) {
                mViewLayer.hideProgressDialog();
            }
        });
    }

    @Override
    public void onActivityCreate() {
        EventBus.getDefault().register(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_UPLOAD_FAILURE);
        filter.addAction(Constants.ACTION_UPLOAD_SUCCESS);
        filter.addAction(Constants.ACTION_UPLOAD_UPDATE_PROGRESS);
        filter.addCategory(Constants.CATEGORY_DEFAULT);
        mUploadReceiver = new UploadBroadCastReceiver();
        mViewLayer.registerReceiver(mUploadReceiver, filter);
    }

    @Override
    public void unSubscribe() {
        super.unSubscribe();
        mViewLayer.unregisterReceiver(mUploadReceiver);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onReceive(UploadInfo info) {
        if (TextUtils.equals(info.action, Constants.ACTION_UPLOAD_SUCCESS)) {
            Bundle bundle = mViewLayer.obtainBundle();
            bundle.putBoolean(Constants.ACTION_SHOW_DIALOG, true);
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (mViewLayer.isVisible()) {
                        mViewLayer.hideProgressDialog();
                    } else {
                        mIsHideProgressDialog = true;
                    }
                }
            });
            mViewLayer.startActivity(LoginActivity.class, bundle);
            mViewLayer.showToast(mViewLayer.getString(R.string.upload_success));
        } else if (TextUtils.equals(info.action, Constants.ACTION_UPLOAD_FAILURE)) {
            if (mViewLayer.isVisible()) {
                mViewLayer.hideProgressDialog();
            } else {
                mIsHideProgressDialog = true;
            }
            String errorMessage = info.errorMessage;
            int errorCode = info.errorCode;
            TLog.d("上传失败" + errorCode + errorMessage);
            mViewLayer.showToast(mViewLayer.getString(R.string.upload_failure));
        } else {
            long totalSize = info.totalSize;
            long currentSize = info.currentSize;
            long speed = info.speed;
            String netSpeed = Formatter.formatFileSize(mViewLayer, speed);
            int percent = (int) (Math.round(currentSize * 100) * 1.0f / totalSize);
            if (mViewLayer.isVisible()) {
                mViewLayer.uploadProgressBar(percent);
            }
        }
    }

    public class UploadBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.equals(action, Constants.ACTION_UPLOAD_SUCCESS)) {
                Bundle bundle = mViewLayer.obtainBundle();
                bundle.putBoolean(Constants.ACTION_SHOW_DIALOG, true);
                if (mViewLayer.isVisible()) {
                    mViewLayer.hideProgressDialog();
                } else {
                    mIsHideProgressDialog = true;
                }
                mViewLayer.startActivity(LoginActivity.class, bundle);
                mViewLayer.showToast(mViewLayer.getString(R.string.upload_success));
            } else if (TextUtils.equals(action, Constants.ACTION_UPLOAD_FAILURE)) {
                if (mViewLayer.isVisible()) {
                    mViewLayer.hideProgressDialog();
                } else {
                    mIsHideProgressDialog = true;
                }
                String errorMessage = intent.getStringExtra(Constants.UPLOAD_ERROR_MESSAGE);
                int errorCode = intent.getIntExtra(Constants.UPLOAD_ERROR_CODE, -1);
                TLog.d("上传失败" + errorCode + errorMessage);
                mViewLayer.showToast(mViewLayer.getString(R.string.upload_failure));
            } else {
                long totalSize = intent.getLongExtra(Constants.UPLOAD_TOTAL_SIZE, -1);
                long currentSize = intent.getLongExtra(Constants.UPLOAD_CURRENT_SIZE, -1);
                long speed = intent.getLongExtra(Constants.NETWORK_SPEED, -1);
                String netSpeed = Formatter.formatFileSize(mViewLayer, speed);
                int percent = (int) (Math.round(currentSize * 100) * 1.0f / totalSize);
                if (mViewLayer.isVisible()) {
                    mViewLayer.uploadProgressBar(percent);
                }
            }
        }
    }

    @Override
    public void onDialogDismiss() {
        //dialog消失的时候要取消网络请求
        if (mUploadResumeConnection != null && !mUploadResumeConnection.isUnsubscribed()) {
            mUploadResumeConnection.unsubscribe();
            mUploadResumeConnection = null;
        }
    }

}
