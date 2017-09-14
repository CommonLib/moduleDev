package com.pwc.sdc.recruit.business.info.resume;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BaseModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class InfoMode extends BaseModel {

    public String formatDate(int year, int monthOfYear, int dayOfMonth) {
        return new StringBuilder().append(year).append("/").append(monthOfYear + 1).append("/").append(dayOfMonth).toString();
    }

    public long parseDateString(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.parse(date).getTime();
    }

    public long getExpFromDateLong(EditText toDate) {
        LinearLayout parent = (LinearLayout) toDate.getParent();
        EditText et_from = (EditText) parent.findViewById(R.id.info_et_dates_from);
        String fromDate = et_from.getText().toString().trim();
        if (!TextUtils.isEmpty(fromDate)) {
            try {
                return parseDateString(fromDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public long getExpToDateLong(EditText fromDate) {
        LinearLayout parent = (LinearLayout) fromDate.getParent();
        EditText et_to = (EditText) parent.findViewById(R.id.info_et_dates_to);
        String toDate = et_to.getText().toString().trim();
        if (!TextUtils.isEmpty(toDate)) {
            try {
                return parseDateString(toDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return System.currentTimeMillis();
    }


}
