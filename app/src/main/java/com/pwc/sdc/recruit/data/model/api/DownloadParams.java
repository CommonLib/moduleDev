package com.pwc.sdc.recruit.data.model.api;

import java.util.List;

/**
 * Created by byang059 on 8/15/17.
 */

public class DownloadParams {

    /**
     * type : excel
     * data : [{"candidateId":106}]
     */

    private String type;
    private List<DataBean> data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * candidateId : 106
         */

        private int candidateId;

        public int getCandidateId() {
            return candidateId;
        }

        public void setCandidateId(int candidateId) {
            this.candidateId = candidateId;
        }
    }
}
