package com.pwc.sdc.recruit.business.info.regulation;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;

import com.facebook.common.internal.Preconditions;
import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.constants.Constants;
import com.pwc.sdc.recruit.data.remote.RetrofitHelper;
import com.pwc.sdc.recruit.data.remote.RxHelper;
import com.pwc.sdc.recruit.manager.CandidateManager;
import com.thirdparty.proxy.net.http.retrofit.HttpResponse;
import com.thirdparty.proxy.net.http.retrofit.rx.UpLoadSubscriber;
import com.thirdparty.proxy.utils.BitmapUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * @author:dongpo 创建时间: 8/2/2016
 * 描述:
 * 修改:
 */
public class UploadService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UploadService(String name) {
        super(name);
    }

    public UploadService() {
        this("uploadThread");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        File header = CandidateManager.getInstance().getHeaderFile();
        String fileName = CandidateManager.getInstance().getCandidate().candidateId + ".png";
        Preconditions.checkNotNull(header);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), header);
        Map<String, RequestBody> map = new HashMap<>();

        map.put("image\"; filename=\"" + fileName + "", fileBody);
        Observable<HttpResponse<Void>> uploadApi =
                RetrofitHelper.getInstance().getService().uploadHeader(map);
        UpLoadSubscriber<Void> callBack = new UpLoadSubscriber<Void>() {
            @Override
            public void onSuccess(Void o) {
                uploadSignature();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                Intent intent = PwcApplication.getInstance().newIntent(Constants.ACTION_UPLOAD_FAILURE, Constants.CATEGORY_DEFAULT);
                intent.putExtra(Constants.UPLOAD_ERROR_CODE, errorCode);
                intent.putExtra(Constants.UPLOAD_ERROR_MESSAGE, message);
                sendBroadcast(intent);
            }

            @Override
            public void onUpLoadProgress(String tag, long totalSize, long currentSize, long speed, boolean done) {
                Intent intent = PwcApplication.getInstance().newIntent(Constants.ACTION_UPLOAD_UPDATE_PROGRESS, Constants.CATEGORY_DEFAULT);
                intent.putExtra(Constants.UPLOAD_TOTAL_SIZE, totalSize);
                intent.putExtra(Constants.UPLOAD_CURRENT_SIZE, currentSize);
                intent.putExtra(Constants.UPLOAD_TAG, tag);
                intent.putExtra(Constants.NETWORK_SPEED, speed);
                sendBroadcast(intent);
            }
        };
        RxHelper.uploadSync(uploadApi, callBack);
    }

    private void uploadSignature() {
        String fileName = CandidateManager.getInstance().getCandidate().candidateId + ".png";
        Bitmap signature = CandidateManager.getInstance().getSignature();
        RequestBody signBody = RequestBody.create(MediaType.parse("image/png"), BitmapUtils
                .bitmap2Bytes(signature));
        Map<String, RequestBody> signatureMap = new HashMap<>();

        signatureMap.put("image\"; filename=\"" + fileName + "", signBody);
        Observable<HttpResponse<Void>> uploadSignatureApi =
                RetrofitHelper.getInstance().getService().uploadSignedImage(signatureMap);
        UpLoadSubscriber<Void> signatureCallBack = new UpLoadSubscriber<Void>() {
            @Override
            public void onSuccess(Void o) {
                PwcApplication.getInstance().sendBroadCast(Constants.ACTION_UPLOAD_SUCCESS, Constants.CATEGORY_DEFAULT);
            }

            @Override
            public void onFailure(int errorCode, String message) {
                Intent intent = PwcApplication.getInstance().newIntent(Constants.ACTION_UPLOAD_FAILURE, Constants.CATEGORY_DEFAULT);
                intent.putExtra(Constants.UPLOAD_ERROR_CODE, errorCode);
                intent.putExtra(Constants.UPLOAD_ERROR_MESSAGE, message);
                sendBroadcast(intent);
            }

            @Override
            public void onUpLoadProgress(String tag, long totalSize, long currentSize, long speed, boolean done) {
            }
        };
        RxHelper.uploadSync(uploadSignatureApi, signatureCallBack);
    }
}
