package com.pwc.sdc.recruit.business.info.resume;

import android.app.DatePickerDialog;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.EditText;

import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BasePresenter;
import com.pwc.sdc.recruit.business.info.regulation.RegulationActivity;
import com.pwc.sdc.recruit.constants.Constants;
import com.pwc.sdc.recruit.data.model.Candidate;
import com.pwc.sdc.recruit.manager.CandidateManager;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class InfoPresenter extends BasePresenter<InfoActivity, InfoMode> implements InfoConstract.Presenter {

    public IssueView mIssueView;

    public InfoPresenter(InfoActivity viewLayer, InfoMode modelLayer) {
        super(viewLayer, modelLayer);
    }


    @Override
    public void next() {
        if (mIssueView != null) {
            mViewLayer.setBlackColor(mIssueView);
        }

        IssueView issueView = mViewLayer.checkEmpty();
        if (issueView != null) {
            mViewLayer.setRedColor(issueView);
            mIssueView = issueView;
            mViewLayer.showAlertDialog();
        } else {
            if (mIssueView != null) {
                mViewLayer.setBlackColor(mIssueView);
            }

            Candidate candidate = mViewLayer.getInterViewer();
            //如果当前简历格式为中文格式，则检查中文字符是否为纯中文，用作后期拼音搜索使用
            if (TextUtils.equals(candidate.language, Constants.PROFILE_LANGUAGE_FORMAT_CHNIESE)) {
                boolean isChinese = isChinese(candidate.basic.chineseName);
                if (!isChinese) {
                    mViewLayer.showAlertDialog(mViewLayer.getString(R.string.chinese_name));
                    return;
                }
            }
            CandidateManager.getInstance().setCandidate(candidate);
            mViewLayer.startActivity(RegulationActivity.class);
        }
    }

    @Override
    public void pickDateTimer(EditText et) {
        pickTimer(et, System.currentTimeMillis(), -1);
    }

    @Override
    public void pickAvailableDateTimer(final EditText et) {
        pickTimer(et, -1, System.currentTimeMillis());
    }

    private void pickTimer(final EditText et, long maxDate, long minDate) {
        mViewLayer.showDateTimePicker(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = mModelLayer.formatDate(year, monthOfYear, dayOfMonth);
                mViewLayer.showSelectedDate(et, date);
                mViewLayer.hideDateTimerPicker();
            }
        }, maxDate, minDate);
    }

    @Override
    public void pickExpDateFromTimer(EditText view) {
        long expToDateLong = mModelLayer.getExpToDateLong(view);
        pickTimer(view, expToDateLong, -1);
    }

    @Override
    public void pickExpDateToTimer(EditText view) {
        long expFromDateLong = mModelLayer.getExpFromDateLong(view);
        pickTimer(view, System.currentTimeMillis(), expFromDateLong);
    }

    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        boolean bo = false;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                bo = true;
            } else {
                bo = false;
            }
        }
        return bo;
    }

    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

}
