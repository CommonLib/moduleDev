package com.pwc.sdc.recruit.business.info.resume;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @author:dongpo 创建时间: 7/19/2016
 * 描述:
 * 修改:
 */
public class IssueView {
    public String issueMessage;
    public ArrayList<View> issueViews;

    public IssueView(RadioGroup issueRG, TextView description, EditText issueET) {
        this(issueRG, description, issueET, null);
    }

    public IssueView(RadioGroup issueRG, TextView description, EditText issueET,
                     String issueMessage) {
        this(issueMessage, issueRG, description, issueET);
    }

    public IssueView(String issueMessage, View... views) {
        this.issueMessage = issueMessage;
        if (views != null) {
            issueViews = new ArrayList<>();
            for (View view : views) {
                issueViews.add(view);
            }
        }
    }
}
