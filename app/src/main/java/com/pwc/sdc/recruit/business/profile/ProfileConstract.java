package com.pwc.sdc.recruit.business.profile;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.pwc.sdc.recruit.base.ViewHolder;
import com.pwc.sdc.recruit.base.interf.ActivityPresenter;
import com.pwc.sdc.recruit.base.interf.ViewLayer;
import com.pwc.sdc.recruit.data.model.Recruiter;
import com.pwc.sdc.recruit.data.model.RecruiterCandidate;

import java.util.List;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public interface ProfileConstract {

    interface View extends ViewLayer {
        void showProfile(List<Object> list);

        void refreshProfile(List<Object> list);

        void showAssignDialog(List<Recruiter> recruiters, List<RecruiterCandidate> candidates);

        void showProgressDialog();

        void hideProgressDialog();

        void showAlertDialog();

        void updateChosenRecruiterList(List<Recruiter> recruiters, boolean isScrollToBottom);

        void showRecruiterCandidate(List<RecruiterCandidate> candidates);

        void refreshRecruiterCandidate(List<RecruiterCandidate> candidates);

        void updateProfileLoadMoreView(int loadMoreState);

        void updateRecruiterLoadMoreView(int loadMoreState);

        void hideProfileFooter();

        void showProfileFooter();

        void hideRecruiterFooter();

        void showRecruiterFooter();

        void hideAssignAlertDialog();

        void showProfileSearchEmpty(String keyword);

        void cancelShowProfileSearchEmpty();

        boolean isProfilePageInEmpty();

        void showRecruiterSearchEmpty(String keyword);

        void cancelShowRecruiterSearchEmpty();

        boolean isRecruiterPageInEmpty();

        String getCurrentProfileKeyword();
    }

    interface Presenter extends ActivityPresenter {
        void onProfileRecycleViewScrolled(GridLayoutManager manager, ProfileAdapter adapter, int dy);

        void onRecruiterRecycleViewScroll(LinearLayoutManager manager, AssignCandidateAdapter adapter, int dy);

        void assign(ViewHolder holder, int position);

        void editAssign(ViewHolder holder, int position);

        void onProfileClick(ViewHolder holder, int position);

        void assignConfirm();

        void onCandidateRecruiterClick(ViewHolder holder, int position);

        void onChosenRecruiterDelete(ViewHolder holder, int position);

        void cancelAssign();

        void search(String keyword, String field);

        void onProfileSearchEmptyRecover();

        void searchEmployee(String keyword);

        void onRecruiterSearchEmptyRecover();

        void onAssignAlertDialogDismiss();
    }
}
