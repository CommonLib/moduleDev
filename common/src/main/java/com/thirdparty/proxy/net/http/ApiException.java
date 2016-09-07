package com.thirdparty.proxy.net.http;

import com.thirdparty.proxy.R;
import com.thirdparty.proxy.base.BaseApplication;

/**
 * @author:dongpo 创建时间: 7/27/2016
 * 描述:
 * 修改:
 */
public class ApiException extends Exception {
    private final int mResultCode;
    private String[] apiErrors = null;

    public ApiException(int resultCode, String detailMessage) {
        super(detailMessage);
        mResultCode = resultCode;
    }

    public int getResultCode() {
        return mResultCode;
    }

    public String getApiDescription() {
        if (apiErrors == null) {
            apiErrors = BaseApplication.getInstance().getResources().getStringArray(R.array.api_error_description);
        }
        if (mResultCode < apiErrors.length && mResultCode >= 0) {
            return apiErrors[mResultCode];
        }
        return BaseApplication.getInstance().getResources().getString(R.string.no_found_result_code);

    }
}
