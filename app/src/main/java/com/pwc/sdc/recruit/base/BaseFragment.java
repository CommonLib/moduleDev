package com.pwc.sdc.recruit.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.interf.ActivityPresenter;
import com.pwc.sdc.recruit.widget.LoadStateFrameLayout;
import com.thirdparty.proxy.log.TLog;
import com.thirdparty.proxy.utils.DensityUtil;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;


/**
 * 碎片基类
 *
 * @author
 * @created
 */
public abstract class BaseFragment<T extends ActivityPresenter> extends Fragment {

    protected LayoutInflater mInflater;
    public BaseActivity mActivity;
    protected android.view.View mContentView;
    private LoadStateFrameLayout mLoadStateFrameLayout;
    protected T mPresenter;
    protected PtrFrameLayout mPullToRefreshView;
    private Handler mHandler = new InflateHandler(this);
    private static final int INFLATE_COMPLETE = 0x1;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (BaseActivity) activity;
        mPresenter = (T) mActivity.mPresenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mInflater = inflater;
        TLog.d("before inflate time = " + SystemClock.currentThreadTimeMillis());
        if (enableAsyncInflateContentView()) {
            FrameLayout root = new FrameLayout(mActivity);
            root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            asyncInflate(root, getLayoutId());
            mContentView = root;
        } else {
            mContentView = inflater.inflate(getLayoutId(), container, false);
        }
        TLog.d("after inflate time = " + SystemClock.currentThreadTimeMillis());
        View pullToRefreshView = getPullToRefreshView(mContentView);
        if (pullToRefreshView != null) {
            mPullToRefreshView = mActivity.addPullToRefreshView(pullToRefreshView);
            initPullRefreshView(mPullToRefreshView);
            if (pullToRefreshView == mContentView) {
                mContentView = mPullToRefreshView;
            }
        }

        View loadStateView = getLoadStateView(mContentView);
        if (loadStateView != null) {
            mLoadStateFrameLayout = mActivity.addLoadingStateView(loadStateView);
            if (loadStateView == mContentView) {
                mContentView = mLoadStateFrameLayout;
            }
        }

        if (!enableAsyncInflateContentView()) {
            ButterKnife.bind(this, mContentView);
            initView();
        }

        return mContentView;
    }

    private void asyncInflate(final FrameLayout contentView, final int layoutId) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                View view = mInflater.inflate(layoutId, contentView, false);
                Message message = Message.obtain(mHandler);
                message.what = INFLATE_COMPLETE;
                View[] views = new View[2];
                views[0] = contentView;
                views[1] = view;
                message.obj = views;
                message.sendToTarget();
            }
        });
    }

    private void initPullRefreshView(PtrFrameLayout refreshView) {
        final MaterialHeader header = new MaterialHeader(getContext());
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, DensityUtil.dip2px(getContext(), 15), 0,
                DensityUtil.dip2px(getContext(), 10));
        header.setPtrFrameLayout(refreshView);
        refreshView.setHeaderView(header);
        refreshView.addPtrUIHandler(header);
        refreshView.setPinContent(true);
        refreshView.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPresenter.onRefresh(frame);
            }
        });
    }

    protected boolean enableAsyncInflateContentView() {
        return false;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            onShow();
        } else {
            onHidden();
        }
    }

    /**
     * 当一个Fragment已经添加，将要进行隐藏的时候，调用
     */
    protected void onHidden() {

    }

    /**
     * 当一个Fragment已经添加，将要进行显示的时候，调用
     */
    protected void onShow() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            onRestoreInitData(savedInstanceState);
        }

        Bundle bundle = mActivity.getTag();
        if (bundle != null) {
            String targetTag = bundle.getString(BaseActivity.FRAGMENT_MESSAGE_HEADER);
            String ownTag = getClass().getSimpleName();
            if (TextUtils.equals(targetTag, ownTag)) {
                handleBundle(bundle);
            }
        }
        initData();
    }

    protected void onNewBundle(Bundle bundle) {

    }

    protected void onRestoreInitData(Bundle savedInstanceState) {
    }

    protected void handleBundle(Bundle bundle) {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void startActivity(Class<? extends BaseActivity> clazz) {
        mActivity.startActivity(clazz);
    }

    public void startActivity(Class<? extends BaseActivity> clazz, Bundle bundle) {
        mActivity.startActivity(clazz, bundle);
    }

    public void startFragment(int container, BaseFragment targetFragment, Bundle bundle) {
        mActivity.startFragment(container, targetFragment, bundle);
    }

    public PwcApplication getApplication() {
        return mActivity.getPwcApplication();
    }

    protected android.view.View inflateView(int resId) {
        return mInflater.inflate(resId, null, false);
    }

    protected android.view.View inflateView(int resId, ViewGroup parent) {
        return mInflater.inflate(resId, parent, false);
    }

    public void LoadingProgressDialog() {
        mActivity.hideLoadingProgressDialog();
    }

    public Dialog showLoadingProgressDialog() {
        return mActivity.showLoadingProgressDialog();
    }

    public void hideWaitDialog() {
        mActivity.hideWaitDialog();
    }

    public ProgressDialog showWaitDialog() {
        return mActivity.showWaitDialog();
    }

    public ProgressDialog showWaitDialog(int resid) {
        return mActivity.showWaitDialog(resid);
    }

    public ProgressDialog showWaitDialog(String str) {
        return mActivity.showWaitDialog(str);
    }

    public void showToast(String message) {
        mActivity.showToast(message);
    }

    public PopupWindow showPopUpWindow(View contentView, int width, int height, View showAsDrop) {
        return showPopUpWindow(contentView, width, height, showAsDrop, 0, 0);
    }

    public PopupWindow showPopUpWindow(View contentView, int width, int height, View showAsDrop,
                                       int offsetX, int offsetY) {
        return mActivity.showPopUpWindow(contentView, width, height, showAsDrop, offsetX, offsetY);
    }

    public void hidePopUpWindow() {
        mActivity.hidePopUpWindow();
    }

    /**
     * 获取需要添加下拉刷新的View
     *
     * @return
     */
    public View getPullToRefreshView(View contentView) {
        return null;
    }

    /**
     * 获取需要添加4种加载状态的View
     *
     * @return
     */
    public View getLoadStateView(View contentView) {
        return null;
    }

    /**
     * 完成刷新
     */
    protected void completeRefresh() {
        if (mPullToRefreshView != null && mPullToRefreshView.isRefreshing()) {
            mPullToRefreshView.refreshComplete();
        }
    }

    protected AlertDialog showAlertDialog(String message, int resId, View.OnClickListener confirm) {
        return mActivity.showAlertDialog(message, resId, confirm);
    }

    protected AlertDialog showAlertDialog(String message, int resId, View.OnClickListener confirm,
                                          View.OnClickListener cancel) {
        return mActivity.showAlertDialog(message, resId, confirm, cancel);
    }

    protected void hideAlertDialog() {
        mActivity.hideAlertDialog();
    }

    /**
     * 获取默认的下拉刷新的布局参数
     *
     * @return
     */
    protected int getPullToRefreshLayout() {
        return 0;
    }


    public void updateViewState(int state) {
        if (mLoadStateFrameLayout != null) {
            mLoadStateFrameLayout.updateState(state);
        }
    }

    public final LoadStateFrameLayout getLoadStateView() {
        return mLoadStateFrameLayout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onFragmentSaveInstanceState(outState);
    }

    public int getColor(int resId) {
        return mActivity.getResources().getColor(resId);
    }

    public String[] getStringArray(int resId) {
        return mActivity.getResources().getStringArray(resId);
    }

    public float getDimenInPX(int resId) {
        return mActivity.getResources().getDimensionPixelSize(resId);
    }

    private static class InflateHandler extends Handler {

        private WeakReference<BaseFragment> mFragmentWeak;

        public InflateHandler(BaseFragment fragment) {
            super();
            mFragmentWeak = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == INFLATE_COMPLETE && mFragmentWeak.get() != null) {
                BaseFragment fragment = mFragmentWeak.get();
                View[] views = (View[]) msg.obj;
                ((FrameLayout) views[0]).addView(views[1]);
                ButterKnife.bind(fragment, fragment.mContentView);
                fragment.initView();
            }
        }
    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract int getLayoutId();

}
