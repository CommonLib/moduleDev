package com.pwc.sdc.recruit.business;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.config.AppConfig;
import com.pwc.sdc.recruit.constants.Constants;
import com.pwc.sdc.recruit.data.remote.RetrofitHelper;
import com.thirdparty.proxy.cache.SharedPreHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author:dongpo 创建时间: 8/15/2016
 * 描述:
 * 修改:
 */
public class DebugActivity extends Activity {
    @BindView(R.id.debug_tv_current_url)
    TextView mDebugTvCurrentUrl;
    @BindView(R.id.debug_cv_current_url)
    CheckBox mDebugCvCurrentUrl;
    @BindView(R.id.debug_ll_current)
    LinearLayout mDebugLlCurrent;
    @BindView(R.id.debug_tv_internal_url)
    TextView mDebugTvInternalUrl;
    @BindView(R.id.debug_cv_internal_url)
    CheckBox mDebugCvInternalUrl;
    @BindView(R.id.debug_ll_testing)
    LinearLayout mDebugLlTesting;
    @BindView(R.id.debug_tv_production_url)
    TextView mDebugTvProductionUrl;
    @BindView(R.id.debug_cv_production_url)
    CheckBox mDebugCvProductionUrl;
    @BindView(R.id.debug_ll_production)
    LinearLayout mDebugLlProduction;
    @BindView(R.id.debug_ll_no_connection)
    LinearLayout mDebugLlNoConnection;
    @BindView(R.id.debug_tv_other_url)
    EditText mDebugTvOtherUrl;
    @BindView(R.id.debug_cv_other_url)
    CheckBox mDebugCvOtherUrl;
    @BindView(R.id.debug_cv_no_connection)
    CheckBox mDebugCvNoConnection;
    @BindView(R.id.debug_ll_other)
    LinearLayout mDebugLlOther;
    @BindView(R.id.dialog_btn_confirm)
    Button mDialogBtnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void initView() {
        mDebugCvCurrentUrl.setClickable(false);
        mDebugCvInternalUrl.setClickable(false);
        mDebugCvProductionUrl.setClickable(false);
        mDebugCvOtherUrl.setClickable(false);
        mDebugCvNoConnection.setClickable(false);
        if(AppConfig.MODE_CONNECTION == false){
            mDebugCvNoConnection.setChecked(true);
        }else{
            String baseUrl = RetrofitHelper.getInstance().getBaseUrl();
            mDebugTvCurrentUrl.setText(baseUrl);
            mDebugCvCurrentUrl.setChecked(true);
            mDebugTvProductionUrl.setText("http://sinw069030:8080/RecruitmentSystem/");

        }

    }

    protected void initData() {

    }


    @OnClick({R.id.debug_ll_current, R.id.debug_ll_testing, R.id.debug_ll_production, R.id.debug_ll_other, R.id.dialog_btn_confirm, R.id.debug_ll_no_connection})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.debug_ll_current:
                onCheckBoxClick(mDebugCvCurrentUrl);
                break;
            case R.id.debug_ll_testing:
                onCheckBoxClick(mDebugCvInternalUrl);
                break;
            case R.id.debug_ll_production:
                onCheckBoxClick(mDebugCvProductionUrl);
                break;
            case R.id.debug_ll_other:
                onCheckBoxClick(mDebugCvOtherUrl);
                break;
            case R.id.debug_ll_no_connection:
                onCheckBoxClick(mDebugCvNoConnection);
                break;
            case R.id.dialog_btn_confirm:
                String baseUrl = null;
                if (mDebugCvCurrentUrl.isChecked()) {
                    baseUrl = mDebugTvCurrentUrl.getText().toString().trim();
                }

                if (mDebugCvInternalUrl.isChecked()) {
                    baseUrl = mDebugTvInternalUrl.getText().toString().trim();
                }

                if (mDebugCvProductionUrl.isChecked()) {
                    baseUrl = mDebugTvProductionUrl.getText().toString().trim();
                }

                if (mDebugCvOtherUrl.isChecked()) {
                    baseUrl = mDebugTvOtherUrl.getText().toString().trim();
                }

                if(mDebugCvNoConnection.isChecked()){
                    AppConfig.MODE_CONNECTION = false;
                    SharedPreHelper.put(PwcApplication.getInstance(), Constants.MODE_CONNECTION, AppConfig.MODE_CONNECTION);
                    baseUrl = null;
                    PwcApplication.showToast("当前为无网络状态，数据为模拟数据");
                }
                if (!TextUtils.isEmpty(baseUrl)) {
                    AppConfig.MODE_CONNECTION = true;
                    SharedPreHelper.put(PwcApplication.getInstance(), Constants.BASE_URL, baseUrl);
                    SharedPreHelper.put(PwcApplication.getInstance(), Constants.MODE_CONNECTION, AppConfig.MODE_CONNECTION);
                    RetrofitHelper.getInstance().setBaseUrl(baseUrl);
                    PwcApplication.showToast("成功切换到BASE_URL: " + baseUrl);
                }
                finish();
                break;
        }
    }

    public void onCheckBoxClick(CheckBox targetCheckBox) {
        if (targetCheckBox.isChecked()) {
            targetCheckBox.setChecked(false);
        } else {
            if (mDebugCvCurrentUrl.isChecked()) {
                mDebugCvCurrentUrl.setChecked(false);
            }

            if (mDebugCvInternalUrl.isChecked()) {
                mDebugCvInternalUrl.setChecked(false);
            }

            if (mDebugCvProductionUrl.isChecked()) {
                mDebugCvProductionUrl.setChecked(false);
            }

            if (mDebugCvOtherUrl.isChecked()) {
                mDebugCvOtherUrl.setChecked(false);
            }

            if(mDebugCvNoConnection.isChecked()){
                mDebugCvNoConnection.setChecked(false);
            }

            targetCheckBox.setChecked(true);
        }

    }
}
