package com.pwc.sdc.recruit.business.info.regulation;

import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.base.BaseModel;
import com.pwc.sdc.recruit.config.AppConfig;
import com.pwc.sdc.recruit.data.model.CandidateId;
import com.thirdparty.proxy.net.http.retrofit.HttpResponse;
import com.thirdparty.proxy.net.http.retrofit.rx.HttpSubscriber;

import rx.Observable;
import rx.Subscription;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class RegulationMode extends BaseModel {

    public void uploadHead(Observable<HttpResponse<Object>> request, final HttpSubscriber<Object> callBack) {
        if (AppConfig.MODE_CONNECTION) {
            sendHttpRequest(request, callBack);
        } else {
            PwcApplication.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callBack.onSuccess(null);
                }
            }, AppConfig.DEBUG_WATING);
        }

    }

    public Subscription uploadResume(Observable<HttpResponse<CandidateId>> request, final HttpSubscriber<CandidateId> callBack) {
        if (AppConfig.MODE_CONNECTION) {
            return sendHttpRequest(request, callBack);
        } else {
            PwcApplication.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    CandidateId candidateId = new CandidateId();
                    candidateId.candidateId = "1312412421";
                    callBack.onSuccess(candidateId);
                }
            }, 3000);
        }
        return null;
    }


}
