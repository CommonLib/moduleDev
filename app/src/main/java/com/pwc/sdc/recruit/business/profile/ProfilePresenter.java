package com.pwc.sdc.recruit.business.profile;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BasePresenter;
import com.pwc.sdc.recruit.base.ViewHolder;
import com.pwc.sdc.recruit.business.comment.AddCommentActivity;
import com.pwc.sdc.recruit.config.AppConfig;
import com.pwc.sdc.recruit.constants.Constants;
import com.pwc.sdc.recruit.data.model.Profile;
import com.pwc.sdc.recruit.data.model.Recruiter;
import com.pwc.sdc.recruit.data.model.RecruiterCandidate;
import com.pwc.sdc.recruit.data.model.api.Search;
import com.pwc.sdc.recruit.data.model.api.UploadRecruiter;
import com.thirdparty.proxy.net.http.retrofit.HttpResponse;
import com.thirdparty.proxy.net.http.retrofit.rx.HttpSubscriber;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;
import rx.Observable;
import rx.Subscription;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class ProfilePresenter extends BasePresenter<ProfileActivity, ProfileMode> implements ProfileConstract.Presenter {

    /**
     * 上拉加载的状态
     */
    private int mProfileLoadingState = Constants.STATE_USER;
    private int mRecruiterLoadingState = Constants.STATE_USER;

    private List<Object> mProfileTimes;
    private List<RecruiterCandidate> mRecruiterCandidate;
    private Profile mProfile;
    private ArrayList<Recruiter> mBufferedRecruiters;
    private int profileCurrentPage = 1;
    private int recruiterCurrentPage = 1;
    private List<Profile> mSearchList;
    private List<Profile> mProfiles;
    private List<Object> mSearchTimes;
    private List<RecruiterCandidate> mRecruiterSearch;

    /**
     * 简历页面是否在搜索界面的标志位
     */
    private boolean mIsProfileSearchPage = false;
    private boolean mIsRecruiterSearchPage = false;
    private Observable<HttpResponse<List<Profile>>> mSearchApi;
    private String mProfileSearchKeyword;
    private String mRecruiterSearchKeyword;
    private int mProfileLastBottomPosition = 0;
    private int mRecruiterLastBottomPosition = 0;
    private Subscription mUploadRecruiterRequest;

    public ProfilePresenter(ProfileActivity viewLayer, ProfileMode modelLayer) {
        super(viewLayer, modelLayer);
    }

    /**
     * 下拉刷新时调用
     *
     * @param frame
     */
    @Override
    public void onRefresh(final PtrFrameLayout frame) {
        //如果在搜索界面下拉刷新，就重新请求搜索结果
        if (mIsProfileSearchPage) {
            search(mSearchApi, frame);
        } else {
            Observable<HttpResponse<List<Profile>>> profiles = getBackPointService().profiles(1, AppConfig.LOAD_MORE_COUNT);
            mModelLayer.requestProfiles(profiles, new HttpSubscriber<List<Profile>>() {

                @Override
                public void onSuccess(List<Profile> profiles) {
                    profileCurrentPage = 1;
                    mProfiles = profiles;
                    mProfileTimes = mModelLayer.getTimeZone(mProfiles);
                    mViewLayer.refreshProfile(mProfileTimes);
                    frame.refreshComplete();
                    int firstPageSize = profiles.size();
                    if (firstPageSize < AppConfig.LOAD_MORE_COUNT) {
                        //加载更多的数量比请求的少，即认为没有更多数据了
                        mViewLayer.hideProfileFooter();
                        mProfileLoadingState = Constants.STATE_EMPTY;
                    } else {
                        mViewLayer.showProfileFooter();
                        mViewLayer.updateProfileLoadMoreView(Constants.STATE_USER);
                        mProfileLoadingState = Constants.STATE_USER;
                    }
                }

                @Override
                public void onFailure(int errorCode, String message) {
                    frame.refreshComplete();
                    if (errorCode == Constants.API_CODE_PROFILE_SEARCH_EMPTY) {
                        mViewLayer.showProfileSearchEmpty(mModelLayer.getEmptyDes());
                    }
                }
            });
        }

    }

    /**
     * 上拉加载更多
     */
    public void onProfileLoadMore() {
        mViewLayer.updateProfileLoadMoreView(Constants.STATE_USER);
        profileCurrentPage++;
        mProfileLoadingState = Constants.STATE_LOADING;
        Observable<HttpResponse<List<Profile>>> profiles = getBackPointService().profiles(profileCurrentPage, AppConfig.LOAD_MORE_COUNT);
        mModelLayer.requestLoadMore(profiles, new HttpSubscriber<List<Profile>>() {
            @Override
            public void onSuccess(List<Profile> profiles) {
                mProfiles.addAll(profiles);
                mProfileTimes = mModelLayer.getTimeZone(mProfiles);
                mViewLayer.showProfile(mProfileTimes);
                int loadingMoreSize = profiles.size();
                if (loadingMoreSize < AppConfig.LOAD_MORE_COUNT) {
                    //加载更多的数量比请求的少，即认为没有更多数据了
                    mProfileLoadingState = Constants.STATE_EMPTY;
                    mViewLayer.updateProfileLoadMoreView(mProfileLoadingState);
                } else {
                    mProfileLoadingState = Constants.STATE_USER;
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                profileCurrentPage--;
                if (errorCode == Constants.API_CODE_PROFILE_SEARCH_EMPTY) {
                    mProfileLoadingState = Constants.STATE_EMPTY;
                    mViewLayer.updateProfileLoadMoreView(Constants.STATE_EMPTY);
                } else {
                    mProfileLoadingState = Constants.STATE_ERROR;
                    mViewLayer.updateProfileLoadMoreView(Constants.STATE_ERROR);
                }

            }
        });
    }

    private void onRecruiterLoadMore() {
        recruiterCurrentPage++;
        mRecruiterLoadingState = Constants.STATE_LOADING;
        Observable<HttpResponse<List<RecruiterCandidate>>> employeeApi = getBackPointService().employee(recruiterCurrentPage, AppConfig.LOAD_MORE_COUNT);
        mModelLayer.employee(employeeApi, new HttpSubscriber<List<RecruiterCandidate>>() {
            @Override
            public void onSuccess(List<RecruiterCandidate> recruiters) {
                mRecruiterCandidate.addAll(recruiters);
                mViewLayer.showRecruiterCandidate(mRecruiterCandidate);
                int recruiterLoadMoreSize = recruiters.size();
                if (recruiterLoadMoreSize < AppConfig.LOAD_MORE_COUNT) {
                    //加载更多的数量比请求的少，即认为没有更多数据了
                    mRecruiterLoadingState = Constants.STATE_EMPTY;
                    mViewLayer.updateRecruiterLoadMoreView(mRecruiterLoadingState);
                } else {
                    mRecruiterLoadingState = Constants.STATE_USER;
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                recruiterCurrentPage--;
                if (errorCode == Constants.API_CODE_PROFILE_SEARCH_EMPTY) {
                    mRecruiterLoadingState = Constants.STATE_EMPTY;
                    mViewLayer.updateProfileLoadMoreView(Constants.STATE_EMPTY);
                } else {
                    mRecruiterLoadingState = Constants.STATE_ERROR;
                    mViewLayer.updateProfileLoadMoreView(Constants.STATE_ERROR);
                }

            }
        });
    }

    /**
     * 鉴定recycleview onScroll事件，实现滑到最后一行时进行加载
     *
     * @param manager
     * @param adapter
     * @param dy
     */
    @Override
    public void onProfileRecycleViewScrolled(GridLayoutManager manager, ProfileAdapter adapter, int dy) {
        int bottomPosition = manager.findLastVisibleItemPosition();
        if (bottomPosition == adapter.getItemCount() - 1 && mProfileLastBottomPosition != bottomPosition && !mIsProfileSearchPage) {
            if (mProfileLoadingState == Constants.STATE_USER || mProfileLoadingState == Constants.STATE_ERROR) {
                onProfileLoadMore();
            }
        }
        //记录用户上次滑动的状态
        if (mProfileLastBottomPosition != bottomPosition) {
            mProfileLastBottomPosition = bottomPosition;
        }
    }

    /**
     * 候选面试官recycleview上拉滑动调用
     *
     * @param manager
     * @param adapter
     * @param dy
     */
    @Override
    public void onRecruiterRecycleViewScroll(LinearLayoutManager manager, AssignCandidateAdapter adapter, int dy) {
        int bottomPosition = manager.findLastVisibleItemPosition();
        if (bottomPosition == adapter.getItemCount() - 1 && bottomPosition != mRecruiterLastBottomPosition) {
            if (mRecruiterLoadingState == Constants.STATE_USER || mRecruiterLoadingState == Constants.STATE_ERROR) {
                onRecruiterLoadMore();
            }
        }
        //记录用户上次滑动的状态
        if (mRecruiterLastBottomPosition != bottomPosition) {
            mRecruiterLastBottomPosition = bottomPosition;
        }
    }

    /**
     * 添加面试官
     *
     * @param holder
     * @param position
     */
    @Override
    public void assign(ViewHolder holder, int position) {
        recruiterCurrentPage = 1;
        mRecruiterLoadingState = Constants.STATE_USER;
        if (mIsProfileSearchPage) {
            mProfile = (Profile) mSearchTimes.get(position);
        } else {
            mProfile = (Profile) mProfileTimes.get(position);
        }

        mRecruiterCandidate = new ArrayList<>();
        mBufferedRecruiters = new ArrayList<>();
        if (mProfile.recruiters != null) {
            mBufferedRecruiters.addAll(mProfile.recruiters);
        }
        mViewLayer.showAssignDialog(mBufferedRecruiters, mRecruiterCandidate);
        mIsRecruiterSearchPage = false;
        Observable<HttpResponse<List<RecruiterCandidate>>> employeeAPI = getBackPointService().employee(1, AppConfig.LOAD_MORE_COUNT);
        mModelLayer.employee(employeeAPI, new HttpSubscriber<List<RecruiterCandidate>>() {
            @Override
            public void onSuccess(List<RecruiterCandidate> candidates) {
                mRecruiterCandidate.addAll(candidates);
                mViewLayer.showRecruiterCandidate(mRecruiterCandidate);
                int firstPageSize = candidates.size();
                if (firstPageSize < AppConfig.LOAD_MORE_COUNT) {
                    //加载更多的数量比请求的少，即认为没有更多数据了
                    mViewLayer.hideRecruiterFooter();
                    mRecruiterLoadingState = Constants.STATE_EMPTY;
                } else {
                    mViewLayer.showRecruiterFooter();
                    mViewLayer.updateRecruiterLoadMoreView(Constants.STATE_USER);
                    mRecruiterLoadingState = Constants.STATE_USER;
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                mViewLayer.updateRecruiterLoadMoreView(Constants.STATE_ERROR);

            }
        });
    }

    /**
     * 编辑面试官
     *
     * @param holder
     * @param position
     */
    @Override
    public void editAssign(ViewHolder holder, int position) {
        assign(holder, position);
    }

    /**
     * 点击候选人简历
     *
     * @param holder
     * @param position
     */
    @Override
    public void onProfileClick(ViewHolder holder, int position) {
        Bundle bundle = mViewLayer.obtainBundle();
        Profile profile = null;
        if (mIsProfileSearchPage) {
            profile = (Profile) mSearchTimes.get(position);
        } else {
            profile = (Profile) mProfileTimes.get(position);
        }
        bundle.putParcelable("profile", profile);
        ArrayList<Recruiter> recruitersAvailable = mModelLayer.filterRecruiter(profile.recruiters);
        if (recruitersAvailable.size() == 0) {
            mViewLayer.showAlertDialog();
        } else {
            mViewLayer.startActivity(AddCommentActivity.class, bundle);
        }
    }

    /**
     * 确认候选面试官
     */
    @Override
    public void assignConfirm() {
        UploadRecruiter uploadEntity = new UploadRecruiter(mProfile.candidateId, mBufferedRecruiters);
        Observable<HttpResponse<Void>> uploadRecruiter = getBackPointService().uploadRecruiter(uploadEntity);
        //如果当前界面为搜索界面，则刷新搜索界面，否则刷新简历界面
        mUploadRecruiterRequest = mModelLayer.uploadRecruiter(uploadRecruiter, new HttpSubscriber<Void>() {
            @Override
            public void onSuccess(Void o) {
                mProfile.recruiters = mBufferedRecruiters;
                mBufferedRecruiters = null;
                //如果当前界面为搜索界面，则刷新搜索界面，否则刷新简历界面
                if (mIsProfileSearchPage) {
                    mSearchTimes = mModelLayer.getTimeZone(mSearchList);
                    mViewLayer.showProfile(mSearchTimes);
                } else {
                    mProfileTimes = mModelLayer.getTimeZone(mProfiles);
                    mViewLayer.showProfile(mProfileTimes);
                }
                mViewLayer.hideAssignAlertDialog();
            }

            @Override
            public void onFailure(int errorCode, String message) {
            }
        });
    }

    @Override
    public void onCandidateRecruiterClick(ViewHolder holder, int position) {
        RecruiterCandidate chosenName = null;
        if (mIsRecruiterSearchPage) {
            chosenName = mRecruiterSearch.get(position);
        } else {
            chosenName = mRecruiterCandidate.get(position);
        }
        Recruiter newRecru = new Recruiter();
        newRecru.name = chosenName.name;
        newRecru.department = chosenName.department;
        newRecru.guid = chosenName.guid;

        if (mBufferedRecruiters.contains(newRecru)) {
            mViewLayer.showToast(mViewLayer.getString(R.string.recruiter_exit));
        } else {
            mBufferedRecruiters.add(newRecru);
            mViewLayer.updateChosenRecruiterList(mBufferedRecruiters, true);
        }
    }

    @Override
    public void onChosenRecruiterDelete(ViewHolder holder, int position) {
        mBufferedRecruiters.remove(position);
        mViewLayer.updateChosenRecruiterList(mBufferedRecruiters, false);
    }

    @Override
    public void cancelAssign() {
        mBufferedRecruiters = null;
        mProfile = null;
        mViewLayer.hideAssignAlertDialog();
    }

    @Override
    public void search(String keyword, String field) {
        mProfileSearchKeyword = keyword;
        Search search = new Search();
        search.keyword = keyword;
        search.field = field;
        mSearchApi = getBackPointService().searchCandidate(search);
        search(mSearchApi, null);
    }

    private void search(Observable<HttpResponse<List<Profile>>> searchApi, final PtrFrameLayout ptr) {
        mModelLayer.search(searchApi, new HttpSubscriber<List<Profile>>() {

            @Override
            public void onSuccess(List<Profile> profiles) {
                //如果在空的界面，取消空界面
                if (mViewLayer.isProfilePageInEmpty()) {
                    mViewLayer.cancelShowProfileSearchEmpty();
                }

                mIsProfileSearchPage = true;
                mSearchList = profiles;
                mSearchTimes = mModelLayer.getTimeZone(mSearchList);
                mViewLayer.refreshProfile(mSearchTimes);
                mViewLayer.hideProfileFooter();
                if (ptr != null && ptr.isRefreshing()) {
                    ptr.refreshComplete();
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                if (ptr != null && ptr.isRefreshing()) {
                    ptr.refreshComplete();
                }
                if (errorCode == Constants.API_CODE_PROFILE_SEARCH_EMPTY) {
                    mViewLayer.showProfileSearchEmpty(mModelLayer.getSearchEmptyDes(mProfileSearchKeyword));
                }
            }
        });
    }

    @Override
    public void onProfileSearchEmptyRecover() {
        if (mViewLayer.isProfilePageInEmpty()) {
            mViewLayer.cancelShowProfileSearchEmpty();
        }
        mViewLayer.refreshProfile(mProfileTimes);
        mIsProfileSearchPage = false;
        if (mProfileTimes != null) {
            //当前数量比请求的数量大
            if (mProfileTimes.size() >= AppConfig.LOAD_MORE_COUNT) {
                mViewLayer.showProfileFooter();
            } else {
                mViewLayer.hideProfileFooter();
            }
        }
    }

    @Override
    public void onRecruiterSearchEmptyRecover() {
        if (mViewLayer.isRecruiterPageInEmpty()) {
            mViewLayer.cancelShowRecruiterSearchEmpty();
        }
        mViewLayer.showRecruiterCandidate(mRecruiterCandidate);
        mIsRecruiterSearchPage = false;
        //当前数量比请求的数量大
        if (mRecruiterCandidate.size() >= AppConfig.LOAD_MORE_COUNT) {
            mViewLayer.showRecruiterFooter();
        } else {
            mViewLayer.hideRecruiterFooter();
        }
    }

    /**
     * assign dialog消失的时候，终止网络请求
     */
    @Override
    public void onAssignAlertDialogDismiss() {
        if (mUploadRecruiterRequest != null && !mUploadRecruiterRequest.isUnsubscribed()) {
            mUploadRecruiterRequest.unsubscribe();
            mUploadRecruiterRequest = null;
        }
    }

    @Override
    public void searchEmployee(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            return;
        }
        mRecruiterSearchKeyword = keyword;
        Observable<HttpResponse<List<RecruiterCandidate>>> searchEmployeeApi = getBackPointService().searchEmployee(keyword);
        mModelLayer.searchEmployee(searchEmployeeApi, new HttpSubscriber<List<RecruiterCandidate>>() {
            @Override
            public void onSuccess(List<RecruiterCandidate> candidates) {
                mRecruiterSearch = candidates;
                mIsRecruiterSearchPage = true;
                mViewLayer.hideRecruiterFooter();
                mViewLayer.refreshRecruiterCandidate(mRecruiterSearch);
            }

            @Override
            public void onFailure(int errorCode, String message) {
                //没有找到结果要更新为空页面
                if (errorCode == Constants.API_CODE_EMPLOYEE_SEARCH_EMPTY) {
                    mViewLayer.showRecruiterSearchEmpty(mModelLayer.getSearchEmptyDes(mRecruiterSearchKeyword));
                }
            }
        });
    }


}
