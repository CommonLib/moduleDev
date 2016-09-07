package com.pwc.sdc.recruit.business.profile;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BaseFragment;
import com.pwc.sdc.recruit.base.BaseRecycleAdapter;
import com.pwc.sdc.recruit.base.ViewHolder;
import com.pwc.sdc.recruit.config.AppConfig;
import com.pwc.sdc.recruit.constants.Constants;
import com.pwc.sdc.recruit.data.model.Profile;
import com.pwc.sdc.recruit.data.model.Recruiter;
import com.pwc.sdc.recruit.data.model.RecruiterCandidate;
import com.pwc.sdc.recruit.util.ClickFilter;
import com.pwc.sdc.recruit.widget.FooterAutoRecycleView;
import com.pwc.sdc.recruit.widget.LoadStateFrameLayout;
import com.thirdparty.proxy.utils.DensityUtil;
import com.thirdparty.proxy.utils.WindowUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class ProfileFragment extends BaseFragment<ProfilePresenter> implements ProfileAdapter.onChildClickListener, View.OnClickListener, TextView.OnEditorActionListener, DialogInterface.OnDismissListener {

    @BindView(R.id.search_rv_content)
    RecyclerView mSearchRvContent;
    FooterAutoRecycleView mAssignRvChosenRecruiter;
    RecyclerView mAssignRvCandidateRecruiter;
    private ProfileAdapter mProfileAdapter;
    private LoadStateFrameLayout mProfileLoadMoreFooter;
    private GridLayoutManager mManager;
    private AssignCandidateAdapter mCandidateAdapter;
    private AssignChosenAdapter mChosenAdapter;
    private LinearLayoutManager mCandidateManager;
    private EditText mEmployeeSearch;
    private LoadStateFrameLayout mRecruiterLoadMoreFooter;
    private Dialog mAssignDialog;
    private LoadStateFrameLayout mRecruiterCandidateLoadStateView;
    private LinearLayoutManager mChosenManager;
    private LinearLayout mChosenSearchFooter;


    @Override
    protected void initView() {
        mPullToRefreshView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mPullToRefreshView.autoRefresh(true);
                mPullToRefreshView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        mPullToRefreshView.setKeepHeaderWhenRefresh(true);
    }

    @Override
    protected void initData() {
        mSearchRvContent.setVisibility(View.VISIBLE);
        mManager = new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);
        mProfileAdapter = new ProfileAdapter(new ArrayList<Profile>(), R.layout.item_profile_search);
        View footer = LayoutInflater.from(getContext()).inflate(R.layout.footer_load_more, null, false);
        mProfileLoadMoreFooter = mActivity.addLoadingStateView(footer);
        mProfileLoadMoreFooter.setEmptyText(getString(R.string.search_load_more_no_more), 14, getColor(R.color.black_3f));
        mProfileLoadMoreFooter.setErrorText(getString(R.string.search_load_failed), 14, getColor(R.color.black_3f));
        mProfileAdapter.addFooter(mProfileLoadMoreFooter);
        hideProfileFooter();
        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (mProfileAdapter.isHeaderType(position) || mProfileAdapter.isFooterType(position)) || mProfileAdapter.isTimeStamp(position) ? mManager.getSpanCount() : 1;
            }
        });

        mSearchRvContent.setLayoutManager(mManager);
        mSearchRvContent.setAdapter(mProfileAdapter);
        mSearchRvContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isProfileFooterShow()) {
                    mPresenter.onProfileRecycleViewScrolled(mManager, mProfileAdapter, dy);
                }
            }
        });

        mProfileAdapter.setOnChildClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_profile;
    }

    @Override
    public View getPullToRefreshView(View contentView) {
        return contentView.findViewById(R.id.search_rv_content);
    }

    @Override
    public View getLoadStateView(View contentView) {
        return contentView.findViewById(R.id.search_rv_content);
    }

    public void showProfile(List<Object> list) {
        mProfileAdapter.setData(list, R.layout.item_profile_search);
    }

    public void refreshProfile(List<Object> list) {
        if (list != null) {
            mProfileAdapter.setData(list, R.layout.item_profile_search);
            mSearchRvContent.setAdapter(mProfileAdapter);
        }
    }

    @Override
    public void assign(ViewHolder holder, int position) {
        mPresenter.assign(holder, position);
    }

    @Override
    public void editAssign(ViewHolder holder, int position) {
        mPresenter.editAssign(holder, position);
    }

    @Override
    public void onProfileClick(ViewHolder holder, int position) {
        mPresenter.onProfileClick(holder, position);
    }

    public void showAssignDialog(List<Recruiter> recruiters, List<RecruiterCandidate> candidate) {
        mAssignDialog = new Dialog(mActivity, R.style.popupDialog);
        initDialog(mAssignDialog);
        mAssignRvCandidateRecruiter = (RecyclerView) mAssignDialog.findViewById(R.id.assign_rv_candidate_recruiter);
        mAssignRvChosenRecruiter = (FooterAutoRecycleView) mAssignDialog.findViewById(R.id.assign_rv_chosen_recruiter);
        initChosenRV(recruiters);
        initCandidateRV(candidate);
        mAssignRvChosenRecruiter.scrollToPosition(mChosenAdapter.getItemCount() - 1);
    }

    private void initDialog(Dialog dialog) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_assign_recuiter);
        //dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.findViewById(R.id.dialog_btn_confirm).setOnClickListener(this);
        dialog.findViewById(R.id.dialog_btn_cancel).setOnClickListener(this);
        dialog.findViewById(R.id.assign_iv_search_icon).setOnClickListener(this);
        WindowManager.LayoutParams lay = dialog.getWindow().getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Rect rect = new Rect();
        View view = mActivity.getWindow().getDecorView();//decorView是window中的最顶层view，可以从window中获取到decorView
        view.getWindowVisibleDisplayFrame(rect);
        lay.height = dm.heightPixels - rect.top;
        lay.width = dm.widthPixels;
        dialog.setOnDismissListener(this);
        dialog.show();
    }

    /**
     * 初始化面试官已选列表的recycleView
     */
    private void initChosenRV(List<Recruiter> recruiters) {
        mChosenManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mAssignRvChosenRecruiter.setLayoutManager(mChosenManager);
        mChosenAdapter = new AssignChosenAdapter(recruiters, R.layout.item_assgin_chosen);
        mChosenSearchFooter = (LinearLayout) inflateView(R.layout.item_assgin_chosen_search, mAssignRvChosenRecruiter);
        mEmployeeSearch = (EditText) mChosenSearchFooter.findViewById(R.id.chosen_et_search);
        mEmployeeSearch.setOnEditorActionListener(this);
        mEmployeeSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String recruiterKeyword = mEmployeeSearch.getText().toString().trim();
                if (TextUtils.isEmpty(recruiterKeyword)) {
                    mPresenter.onRecruiterSearchEmptyRecover();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mChosenAdapter.addFooter(mChosenSearchFooter);
        mAssignRvChosenRecruiter.setAdapter(mChosenAdapter);
        mAssignRvChosenRecruiter.startGlobalLayoutListener();
        mChosenAdapter.setOnChildClickListener(new AssignChosenAdapter.OnChildClickListener() {
            @Override
            public void onDelete(ViewHolder holder, int position) {
                mPresenter.onChosenRecruiterDelete(holder, position);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mAssignRvCandidateRecruiter != null){
            mAssignRvChosenRecruiter.removeGlobalOnLayoutListener();
        }
    }

    /**
     * 初始化面试官候选列的recycleView
     */
    private void initCandidateRV(List<RecruiterCandidate> candidate) {
        mCandidateManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mCandidateAdapter = new AssignCandidateAdapter(candidate, R.layout.item_assgin_recruiter_candidate);
        mAssignRvCandidateRecruiter.setLayoutManager(mCandidateManager);
        View candidateRecruiterFooter = inflateView(R.layout.view_footer_recruiter_candidate);
        mRecruiterLoadMoreFooter = mActivity.addLoadingStateView(candidateRecruiterFooter);
        mRecruiterLoadMoreFooter.setEmptyText(getString(R.string.search_load_more_no_more), 18, getColor(R.color.white));
        mRecruiterLoadMoreFooter.setErrorText(getString(R.string.search_load_failed), 18, getColor(R.color.white));
        mCandidateAdapter.addFooter(mRecruiterLoadMoreFooter);
        mAssignRvCandidateRecruiter.setAdapter(mCandidateAdapter);
        ItemDecorateLine dividerLine = new ItemDecorateLine();
        dividerLine.setSize(DensityUtil.dip2px(getContext(), 1));
        dividerLine.setColor(getColor(R.color.gray_light));
        mAssignRvCandidateRecruiter.addItemDecoration(dividerLine);
        mCandidateAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewHolder holder, int position) {
                mPresenter.onCandidateRecruiterClick(holder, position);
            }
        });
        mAssignRvCandidateRecruiter.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isRecruiterFooterShow()) {
                    mPresenter.onRecruiterRecycleViewScroll(mCandidateManager, mCandidateAdapter, dy);
                }
            }
        });

        mRecruiterCandidateLoadStateView = mActivity.addLoadingStateView(mAssignRvCandidateRecruiter);

    }

    public void updateChosenRecruiterList(List<Recruiter> recruiters, boolean isScrollToBottom) {
        mChosenAdapter.setData(recruiters, R.layout.item_assgin_chosen);
        if (isScrollToBottom) {
            mAssignRvChosenRecruiter.scrollToPosition(mChosenAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_btn_confirm:
                //4秒内多次点击无效
                if (ClickFilter.filter(v, AppConfig.BUTTON_REQUEST_CLICK_USELESS_TIME)) {
                    return;
                }
                mPresenter.assignConfirm();
                break;
            case R.id.dialog_btn_cancel:
                mPresenter.cancelAssign();
                break;
            case R.id.assign_iv_search_icon:
                if (ClickFilter.filter(v)) {
                    return;
                }
                String keyword = mEmployeeSearch.getText().toString().trim();
                mEmployeeSearch.clearFocus();
                WindowUtils.hideSoftKeyboard(mEmployeeSearch);
                mPresenter.searchEmployee(keyword);
                break;
        }
    }

    /**
     * 刷新面试官列表
     *
     * @param candidates
     */
    public void showRecruiterCandidate(List<RecruiterCandidate> candidates) {
        mCandidateAdapter.setData(candidates, R.layout.item_assgin_recruiter_candidate);
    }

    /**
     * 重新设置面试官列表
     *
     * @param candidates
     */
    public void refreshRecruiterCandidate(List<RecruiterCandidate> candidates) {
        mCandidateAdapter.setData(candidates, R.layout.item_assgin_recruiter_candidate);
        mAssignRvCandidateRecruiter.setAdapter(mCandidateAdapter);
    }

    /**
     * 搜索时，点击软键盘上的搜索按钮，直接进行搜索
     *
     * @param v
     * @param actionId
     * @param event
     * @return
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            int action = event.getAction();
            if (action == KeyEvent.ACTION_UP) {
                String keyword = mEmployeeSearch.getText().toString().trim();
                mPresenter.searchEmployee(keyword);
            } else if (action == KeyEvent.ACTION_DOWN) {
                WindowUtils.hideSoftKeyboard(mEmployeeSearch);
            }
            return true;
        }
        return false;
    }

    /**
     * 更新简历列表页，上拉加载的view
     *
     * @param loadMoreState
     */
    public void updateProfileLoadMoreView(int loadMoreState) {
        mProfileLoadMoreFooter.updateState(loadMoreState);
    }


    /**
     * 更新面试官列表页，上拉加载的view
     *
     * @param loadMoreState
     */
    public void updateRecruiterLoadMoreView(int loadMoreState) {
        mRecruiterLoadMoreFooter.updateState(loadMoreState);
    }

    /**
     * 隐藏简历列表页的上拉view
     */
    public void hideProfileFooter() {
        if (mProfileLoadMoreFooter.getVisibility() == View.VISIBLE) {
            mProfileLoadMoreFooter.setVisibility(View.GONE);
        }
    }

    /**
     * 显示简历列表页的上拉view
     */
    public void showProfileFooter() {
        if (mProfileLoadMoreFooter.getVisibility() == View.GONE) {
            mProfileLoadMoreFooter.setVisibility(View.VISIBLE);
        }
    }

    public boolean isProfileFooterShow() {
        return mProfileLoadMoreFooter.getVisibility() == View.VISIBLE;
    }

    public boolean isRecruiterFooterShow() {
        return mRecruiterLoadMoreFooter.getVisibility() == View.VISIBLE;
    }

    public void hideRecruiterFooter() {
        if (mRecruiterLoadMoreFooter.getVisibility() == View.VISIBLE) {
            mRecruiterLoadMoreFooter.setVisibility(View.GONE);
            mCandidateAdapter.hideFooterView();
        }
    }

    public void showRecruiterFooter() {
        if (mRecruiterLoadMoreFooter.getVisibility() == View.GONE) {
            mRecruiterLoadMoreFooter.setVisibility(View.VISIBLE);
            mCandidateAdapter.showFooterView();
        }
    }

    public void hideAssignAlertDialog() {
        if (mAssignDialog != null && mAssignDialog.isShowing()) {
            mAssignDialog.dismiss();
        }
    }

    public void showProfileEmpty(String des) {
        getLoadStateView().setEmptyText(des, 18, getColor(R.color.black_3f));
        getLoadStateView().setEmptyBackground(getColor(R.color.white_f2));
        //显示为空的界面
        updateViewState(Constants.STATE_EMPTY);
    }

    public void cancelShowProfileEmpty() {
        updateViewState(Constants.STATE_USER);
    }

    public boolean isProfilePageInEmpty() {
        return getLoadStateView().getState() == Constants.STATE_EMPTY;
    }

    public void showRecruiterSearchEmpty(String des) {
        mRecruiterCandidateLoadStateView.setEmptyText(des, 18, getColor(R.color.white));
        //显示为空的界面
        mRecruiterCandidateLoadStateView.updateState(Constants.STATE_EMPTY);
    }

    public void cancelShowRecruiterSearchEmpty() {
        mRecruiterCandidateLoadStateView.updateState(Constants.STATE_USER);
    }

    public boolean isRecruiterPageInEmpty() {
        return mRecruiterCandidateLoadStateView.getState() == Constants.STATE_EMPTY;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mPresenter.onAssignAlertDialogDismiss();
    }

}


