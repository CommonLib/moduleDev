package com.pwc.sdc.recruit.business.finder;

import com.pwc.sdc.recruit.base.BasePresenter;

/**
 * Created by lhuang126 on 7/12/2016.
 */
public class JobFinderPresenter extends BasePresenter<JobFinderActivity,JobFinderDataSource> implements JobFinderContract.Presenter {
    public JobFinderDataSource dataSource;
    public JobFinderActivity view;

    public JobFinderPresenter(JobFinderActivity view, JobFinderDataSource dataSource) {
        this.dataSource = dataSource;
        this.view = view;
    }
}
