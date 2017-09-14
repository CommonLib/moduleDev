package com.pwc.sdc.recruit.data.model.api;

/**
 * Created by byang059 on 8/15/17.
 */

public class SearchParams {


    /**
     * chineseName : 李卫
     * englishName : wei
     * appliedPosition : 工程师
     * interviewDate : 20170805
     */

    private String chineseName;
    private String englishName;
    private String appliedPosition;
    private int interviewDate;

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getAppliedPosition() {
        return appliedPosition;
    }

    public void setAppliedPosition(String appliedPosition) {
        this.appliedPosition = appliedPosition;
    }

    public int getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(int interviewDate) {
        this.interviewDate = interviewDate;
    }
}
