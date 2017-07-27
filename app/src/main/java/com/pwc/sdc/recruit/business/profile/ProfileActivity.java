package com.pwc.sdc.recruit.business.profile;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.ToolBarActivity;
import com.pwc.sdc.recruit.data.model.Recruiter;
import com.pwc.sdc.recruit.data.model.RecruiterCandidate;
import com.pwc.sdc.recruit.manager.AccountManager;
import com.thirdparty.proxy.utils.DensityUtil;
import com.thirdparty.proxy.utils.WindowUtils;

import java.util.List;

import butterknife.OnClick;

/**
 * @author:dongpo 创建时间: 7/12/2016
 * 描述:
 * 修改:
 */
public class ProfileActivity extends ToolBarActivity<ProfilePresenter> implements ProfileConstract.View, View.OnClickListener, ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener, TextWatcher, View.OnFocusChangeListener {

    private ProfileFragment mProfileFragment;
    private LinearLayout mSearchLlParent;
    private ImageView mSearchIvIcon;
    private EditText mSearchEtContent;
    private TextView mSearchTvDelete;
    private boolean mIsAnimationProcess = false;
    private ValueAnimator mCloseSearchAnimation;
    private ValueAnimator mOpenSearchAnimation;
    private boolean isPopupShow = false;
    private PopupWindow mPopupWindow;

    TextView mSearchTvChineseName;
    TextView mSearchTvChinesePinyin;
    TextView mSearchTvEnglishName;
    TextView mSearchTvPhone;
    TextView mSearchTvEmail;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        super.initView();
        mProfileFragment = obtainFragment(ProfileFragment.class);
        replaceFragment(R.id.single_fl_container, mProfileFragment);
        mSearchIvIcon = (ImageView) mToolbar.findViewById(R.id.common_tv_right_icon);
        mSearchLlParent = (LinearLayout) mToolbar.findViewById(R.id.toolbar_ll_search);
        mSearchEtContent = (EditText) mToolbar.findViewById(R.id.search_et_search);
        mSearchTvDelete = (TextView) mToolbar.findViewById(R.id.search_tv_delete);

        mSearchEtContent.addTextChangedListener(this);
        mSearchEtContent.setOnFocusChangeListener(this);

        mSearchLlParent.setVisibility(View.GONE);
        mSearchIvIcon.setVisibility(View.VISIBLE);

        setBackButtonEnable(false);
        setActionBarTitleEnable(false);
        setActionBarCenterTextColor(R.color.black_3f);
        setActionBarCenterTextSize(20);
        setActionBarCenterTitle(R.string.search_submitted_profile);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_single_fragment;
    }

    @Override
    public void showProfile(List<Object> list) {
        mProfileFragment.showProfile(list);
    }

    @Override
    public void refreshProfile(List<Object> list) {
        mProfileFragment.refreshProfile(list);
    }

    @Override
    public void showAssignDialog(List<Recruiter> recruiters, List<RecruiterCandidate> candidate) {
        mProfileFragment.showAssignDialog(recruiters, candidate);
    }

    @Override
    public void showProgressDialog() {
        ProgressDialog pd = showWaitDialog(R.string.loading);
        pd.setCanceledOnTouchOutside(false);
    }

    @Override
    public void hideProgressDialog() {
        hideWaitDialog();
    }

    @Override
    public void showAlertDialog() {
        AlertDialog alertDialog = showAlertDialog(getString(R.string.no_available_recruiter), R.layout.dialog_fill_blank, this);
        TextView tvSubtitle = (TextView) alertDialog.findViewById(R.id.dialog_tv_subtitle);
        tvSubtitle.setVisibility(View.GONE);
    }

    @Override
    public void updateChosenRecruiterList(List<Recruiter> recruiters, boolean isScrollToBottom) {
        mProfileFragment.updateChosenRecruiterList(recruiters, isScrollToBottom);
    }

    @Override
    public void showRecruiterCandidate(List<RecruiterCandidate> candidates) {
        mProfileFragment.showRecruiterCandidate(candidates);
    }

    @Override
    public void refreshRecruiterCandidate(List<RecruiterCandidate> candidates) {
        mProfileFragment.refreshRecruiterCandidate(candidates);
    }

    @Override
    public void updateProfileLoadMoreView(int loadMoreState) {
        mProfileFragment.updateProfileLoadMoreView(loadMoreState);
    }

    @Override
    public void updateRecruiterLoadMoreView(int loadMoreState) {
        mProfileFragment.updateRecruiterLoadMoreView(loadMoreState);
    }

    @Override
    public void hideProfileFooter() {
        mProfileFragment.hideProfileFooter();
    }

    @Override
    public void showProfileFooter() {
        mProfileFragment.showProfileFooter();
    }

    @Override
    public void hideRecruiterFooter() {
        mProfileFragment.hideRecruiterFooter();
    }

    @Override
    public void showRecruiterFooter() {
        mProfileFragment.showRecruiterFooter();
    }

    @Override
    public void hideAssignAlertDialog() {
        mProfileFragment.hideAssignAlertDialog();
    }

    @Override
    public void showProfileSearchEmpty(String keyword) {
        mProfileFragment.showProfileEmpty(keyword);
    }

    @Override
    public void cancelShowProfileSearchEmpty() {
        mProfileFragment.cancelShowProfileEmpty();
    }

    @Override
    public boolean isProfilePageInEmpty() {
        return mProfileFragment.isProfilePageInEmpty();
    }

    @Override
    public void showRecruiterSearchEmpty(String keyword) {
        mProfileFragment.showRecruiterSearchEmpty(keyword);
    }

    @Override
    public void cancelShowRecruiterSearchEmpty() {
        mProfileFragment.cancelShowRecruiterSearchEmpty();
    }

    @Override
    public boolean isRecruiterPageInEmpty() {
        return mProfileFragment.isRecruiterPageInEmpty();
    }

    @Override
    public String getCurrentProfileKeyword() {
        return mSearchEtContent.getText().toString().trim();
    }

    @OnClick({R.id.common_tv_right_icon, R.id.search_tv_delete})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_btn_confirm:
                hideAlertDialog();
                break;
            case R.id.common_tv_right_icon:
                if (!mIsAnimationProcess) {
                    startOpenSearchAnimation();
                }
                break;
            case R.id.search_tv_delete:
                if (!mIsAnimationProcess) {
                    startCloseSearchAnimation();
                }
                break;
            case R.id.search_ll_chinese_name:
                onSearch("chineseName");
                break;
            case R.id.search_ll_chinese_pinyin:
                onSearch("pinYin");
                break;
            case R.id.search_ll_english_name:
                onSearch("englishName");
                break;
            case R.id.search_ll_phone:
                onSearch("mobile");
                break;
            case R.id.search_ll_email:
                onSearch("email");
                break;
        }
    }

    /**
     * 执行关闭动画
     */
    private void startCloseSearchAnimation() {
        mCloseSearchAnimation = ValueAnimator.ofFloat(DensityUtil.dip2px(this, 200) * (-1), 0);
        mCloseSearchAnimation.setDuration(200);
        mCloseSearchAnimation.addUpdateListener(this);
        mCloseSearchAnimation.addListener(this);
        mCloseSearchAnimation.start();
    }

    /**
     * 执行打开搜索动画
     */
    private void startOpenSearchAnimation() {
        mOpenSearchAnimation = ValueAnimator.ofFloat(0, DensityUtil.dip2px(this, 200) * (-1));
        mOpenSearchAnimation.setDuration(200);
        mOpenSearchAnimation.addUpdateListener(this);
        mOpenSearchAnimation.addListener(this);
        mOpenSearchAnimation.start();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        Float animatedValue = (Float) animation.getAnimatedValue();
        mSearchIvIcon.setTranslationX(animatedValue);
    }

    @Override
    public void onAnimationStart(Animator animation) {
        mSearchLlParent.setVisibility(View.GONE);
        mIsAnimationProcess = true;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        mSearchEtContent.setTranslationX(0);
        mIsAnimationProcess = false;
        if (animation == mOpenSearchAnimation) {
            mSearchLlParent.setVisibility(View.VISIBLE);
            mSearchEtContent.requestFocus();
            WindowUtils.showSoftKeyboard(mSearchEtContent);
        } else {
            //清除搜索框所有内容
            mSearchEtContent.setText("");
            WindowUtils.hideSoftKeyboard(mSearchEtContent);
            //mPresenter.onProfileSearchEmptyRecover(); 因为setText()方法会回调onTextChange方法，这里不再显示调用
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String keyword = getCurrentProfileKeyword();
        if (!TextUtils.isEmpty(keyword)) {
            if (isPopupShow == false) {
                showPopUpWindow();
            }
            setPopupWindowData(mPopupWindow.getContentView(), keyword);
        } else {
            hidePopUpWindow();
            mPresenter.onProfileSearchEmptyRecover();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void onSearch(String field) {
        String keyword = mSearchEtContent.getText().toString().trim();
        mSearchEtContent.clearFocus();
        WindowUtils.hideSoftKeyboard(mSearchEtContent);
        hidePopUpWindow();
        mPresenter.search(keyword, field);
    }

    /**
     * 搜索框获取焦点时，弹出popupwindow，搜索框没有关键字则不弹出，失去焦点时，不显示popupwindow
     *
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (mPopupWindow == null) {
                View contentView = inflate(R.layout.view_popup_window_search);
                mPopupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            }

            View contentView = mPopupWindow.getContentView();
            String keyword = mSearchEtContent.getText().toString().trim();
            if (!TextUtils.isEmpty(keyword)) {
                setPopupWindowData(contentView, keyword);
                showPopUpWindow();
            }
        } else {
            hidePopUpWindow();
        }
    }

    /**
     * 后退注销登录
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AccountManager.getInstance().logout();
    }

    /**
     * @param event
     * @return 如果activity相应的滑动，让就popupwindow消失
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mSearchEtContent.isFocused()) {
            mSearchEtContent.clearFocus();
            WindowUtils.hideSoftKeyboard(mSearchEtContent);
            if (isPopupShow) {
                hidePopUpWindow();
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 在搜索的EditText下显示PopupWindow
     */
    public void showPopUpWindow() {
        int offset = DensityUtil.dip2px(this, 10) * (-1);
        mPopupWindow.showAsDropDown(mSearchEtContent, offset, offset);
        isPopupShow = true;
    }

    /**
     * 搜索框消失
     */
    public void hidePopUpWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            isPopupShow = false;
            mPopupWindow.dismiss();
        }
    }

    /**
     * @param contentView 给view设置数据
     * @param keyword     关键字
     */
    public void setPopupWindowData(View contentView, String keyword) {

        if (mSearchTvChineseName == null) {
            mSearchTvChineseName = (TextView) contentView.findViewById(R.id.search_tv_chinese_name);
            contentView.findViewById(R.id.search_ll_chinese_name).setOnClickListener(this);

        }
        if (mSearchTvChinesePinyin == null) {
            mSearchTvChinesePinyin = (TextView) contentView.findViewById(R.id.search_tv_chinese_pinyin);
            contentView.findViewById(R.id.search_ll_chinese_pinyin).setOnClickListener(this);
        }
        if (mSearchTvEnglishName == null) {
            mSearchTvEnglishName = (TextView) contentView.findViewById(R.id.search_tv_english_name);
            contentView.findViewById(R.id.search_ll_english_name).setOnClickListener(this);
        }
        if (mSearchTvPhone == null) {
            mSearchTvPhone = (TextView) contentView.findViewById(R.id.search_tv_phone);
            contentView.findViewById(R.id.search_ll_phone).setOnClickListener(this);
        }
        if (mSearchTvEmail == null) {
            mSearchTvEmail = (TextView) contentView.findViewById(R.id.search_tv_email);
            contentView.findViewById(R.id.search_ll_email).setOnClickListener(this);
        }
        mSearchTvChineseName.setText(formatKeyword(keyword));
        mSearchTvChinesePinyin.setText(formatKeyword(keyword));
        mSearchTvEnglishName.setText(formatKeyword(keyword));
        mSearchTvPhone.setText(formatKeyword(keyword));
        mSearchTvEmail.setText(formatKeyword(keyword));
    }

    /**
     * 格式化关键字
     *
     * @param keyword
     * @return “keyword”
     */
    public String formatKeyword(String keyword) {
        StringBuilder builder = new StringBuilder();
        return builder.append("\"").append(keyword).append("\"").toString();
    }
}
