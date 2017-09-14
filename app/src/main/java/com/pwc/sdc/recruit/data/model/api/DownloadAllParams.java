package com.pwc.sdc.recruit.data.model.api;

/**
 * Created by byang059 on 8/15/17.
 */

public class DownloadAllParams {


    /**
     * downloadType : excel
     * newComplexSearch : {"chineseName":"张"}
     */

    private String downloadType;
    private NewComplexSearchBean newComplexSearch;

    public String getDownloadType() {
        return downloadType;
    }

    public void setDownloadType(String downloadType) {
        this.downloadType = downloadType;
    }

    public NewComplexSearchBean getNewComplexSearch() {
        return newComplexSearch;
    }

    public void setNewComplexSearch(NewComplexSearchBean newComplexSearch) {
        this.newComplexSearch = newComplexSearch;
    }

    public static class NewComplexSearchBean {
        /**
         * chineseName : 张
         */

        private String chineseName;

        public String getChineseName() {
            return chineseName;
        }

        public void setChineseName(String chineseName) {
            this.chineseName = chineseName;
        }
    }
}
