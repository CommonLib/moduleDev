package com.pwc.sdc.recruit.business.info.resume;

import android.app.DatePickerDialog;
import android.widget.EditText;

import com.pwc.sdc.recruit.base.interf.ActivityPresenter;
import com.pwc.sdc.recruit.base.interf.ViewLayer;
import com.pwc.sdc.recruit.data.model.Candidate;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public interface InfoConstract {

    interface View extends ViewLayer {
        IssueView checkEmpty();

        void setBlackColor(IssueView issueView);

        void setRedColor(IssueView issueView);

        void showAlertDialog();

        void showAlertDialog(String message);

        Candidate getInterViewer();

        String getChineseName();

        void showDateTimePicker(DatePickerDialog.OnDateSetListener dateListener, long maxDate, long minDate);

        void showSelectedDate(EditText et, String date);

        void hideDateTimerPicker();
    }

    interface Presenter extends ActivityPresenter {
        void next();

        void pickDateTimer(EditText et);

        void pickAvailableDateTimer(EditText view);

        void pickExpDateFromTimer(EditText view);

        void pickExpDateToTimer(EditText view);
    }
}
