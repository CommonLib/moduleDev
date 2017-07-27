package com.pwc.sdc.recruit.business.comment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.pwc.sdc.recruit.base.BasePresenter;
import com.pwc.sdc.recruit.business.login.LoginActivity;
import com.pwc.sdc.recruit.constants.Constants;
import com.pwc.sdc.recruit.data.model.Candidate;
import com.pwc.sdc.recruit.data.model.Comment;
import com.pwc.sdc.recruit.data.model.Profile;
import com.pwc.sdc.recruit.data.model.Recruiter;
import com.thirdparty.proxy.net.http.retrofit.HttpResponse;
import com.thirdparty.proxy.net.http.retrofit.rx.HttpSubscriber;

import java.util.ArrayList;

import rx.Observable;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class AddCommentPresenter extends BasePresenter<AddCommentActivity, AddCommentMode> implements AddCommentContract.Presenter {

    private Profile mProfile;
    private boolean isSubmiting = false;

    @Override
    public void requestCandidate() {
        //请求网络, 拿到数据
        Observable<HttpResponse<Candidate>> candidateReq = getBackPointService().getCandidate(mProfile.candidateId);
        mModelLayer.getCandidate(candidateReq, new HttpSubscriber<Candidate>() {
            @Override
            public void onSuccess(Candidate candidate) {
                candidate.headUrl = mProfile.headUrl;
                mViewLayer.showCandidate(candidate);
            }

            @Override
            public void onFailure(int errorCode, String message) {
            }
        });
    }

    @Override
    public void handleIntent(Intent intent) {
        mProfile = intent.getParcelableExtra("profile");
        ArrayList<Recruiter> recruiters = mModelLayer.filterRecruiter(mProfile.recruiters);
        mViewLayer.showInterViewer(recruiters);
    }

    @Override
    public void next() {
        boolean isEmpty = mViewLayer.checkCommentEmpty();
        if (isEmpty) {
            mViewLayer.showAlertDialog();
            return;
        }

        Comment comment = mViewLayer.getComment();
        comment.candidateId = mProfile.candidateId;
        Observable<HttpResponse<Object>> uploadCommentReq = getBackPointService().uploadComment(comment);
        isSubmiting = true;
        mModelLayer.uploadComment(uploadCommentReq, new HttpSubscriber<Object>() {
            @Override
            public void onSuccess(Object o) {
                Bundle bundle = mViewLayer.obtainBundle();
                bundle.putBoolean(Constants.ACTION_SHOW_DIALOG, true);
                mViewLayer.startActivity(LoginActivity.class, bundle);
                isSubmiting = false;
            }

            @Override
            public void onFailure(int errorCode, String message) {
                isSubmiting = false;
            }
        });

    }

    /**
     * 此方法在这里没有作用
     * @param
     */
    @Override
    public void pickDateTimer(EditText et) {

    }

    @Override
    public void pickAvailableDateTimer(EditText view) {

    }

    @Override
    public void pickExpDateFromTimer(EditText view) {

    }

    @Override
    public void pickExpDateToTimer(EditText view) {

    }

}
