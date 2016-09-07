package com.pwc.sdc.recruit.business.info.resume;

import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * @author:dongpo 创建时间: 7/19/2016
 * 描述:
 * 修改:
 */
public class IssueView {
    public EditText issueET;
    public TextView descrption;
    public RadioGroup issueRG;

    public IssueView(RadioGroup issueRG, TextView descrption, EditText issueET) {
        this.issueRG = issueRG;
        this.descrption = descrption;
        this.issueET = issueET;
    }
}
