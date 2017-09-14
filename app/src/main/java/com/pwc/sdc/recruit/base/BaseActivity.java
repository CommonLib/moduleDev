package com.pwc.sdc.recruit.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.interf.ActivityPresenter;
import com.pwc.sdc.recruit.constants.Constants;
import com.pwc.sdc.recruit.widget.LoadStateFrameLayout;
import com.thirdparty.proxy.cache.SharedPreHelper;
import com.thirdparty.proxy.log.TLog;
import com.thirdparty.proxy.utils.DialogHelp;
import com.thirdparty.proxy.utils.WindowUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * @author:dongpo 创建时间: 5/24/2016
 * 描述:
 * 修改:
 */
public abstract class BaseActivity<T extends ActivityPresenter> extends AppCompatActivity {

    private boolean mIsVisible;
    private ProgressDialog _waitDialog;
    private AlertDialog mAlertDialog;
    private PopupWindow _popWindow;
    private LoadStateFrameLayout mLoadStateFrameLayout;
    protected T mPresenter;
    protected LayoutInflater mInflater;
    /**
     * startFragment中携带的bundle的头信息
     */
    public static final String FRAGMENT_MESSAGE_HEADER = "start_fragment_message_header";
    private FragmentManager mFragmentManager;
    private Bundle mObj;
    private View mDialogCustomView;
    private Dialog mLoadingProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = getLayoutInflater();
        mFragmentManager = getSupportFragmentManager();
        setLanguage();
        setContentView(getLayoutId());

        ButterKnife.bind(this);
        initView();

        mPresenter = instancePresenter();

        Intent intent = getIntent();
        if (intent != null) {
            handleIntent(intent);
        }
        if (savedInstanceState != null) {
            onRestoreInitData(savedInstanceState);
        }
        initData();
        mPresenter.onActivityCreate();
        EventBus.getDefault().register(this);
    }

    private void setLanguage() {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        String language = (String) SharedPreHelper
                .get(this, Constants.KEY_LANGUAGE, Constants.LANGUAGE_CHINESE);
        if (TextUtils.equals(language, Constants.LANGUAGE_CHINESE)) {
            configuration.locale = Locale.CHINESE;
        } else {
            configuration.locale = Locale.ENGLISH;
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    protected void handleIntent(@NonNull Intent intent) {
    }

    protected void onRestoreInitData(@NonNull Bundle savedInstanceState) {
    }

    /**
     * @param targetView 用户要添加加载状态的view
     * @return 添加加载状态
     */
    public LoadStateFrameLayout addLoadingStateView(View targetView) {
        LoadStateFrameLayout loadStateView = new LoadStateFrameLayout(this);
        ViewGroup parent = (ViewGroup) targetView.getParent();
        ViewGroup.LayoutParams targetParams = targetView.getLayoutParams();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        targetView.setLayoutParams(params);
        if (parent != null) {
            int index = parent.indexOfChild(targetView);
            parent.removeView(targetView);
            loadStateView.addSuccessView(targetView);
            parent.addView(loadStateView, index, targetParams);
        } else {
            if (targetParams != null) {
                loadStateView.setLayoutParams(targetParams);
            }
            loadStateView.addSuccessView(targetView);
        }
        return loadStateView;
    }

    /**
     * @param
     * @return 给一个View添加下拉刷新
     */
    public PtrFrameLayout addPullToRefreshView(View targetView) {
        ViewGroup parent = (ViewGroup) targetView.getParent();
        ViewGroup.LayoutParams targetParams = targetView.getLayoutParams();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        PtrFrameLayout ptr = null;

        if (targetParams == null) {
            targetParams = params;
        }

        targetView.setLayoutParams(params);
        if (parent != null) {
            int index = parent.indexOfChild(targetView);
            parent.removeView(targetView);
            ptr = (PtrFrameLayout) LayoutInflater.from(this)
                    .inflate(R.layout.fragment_base, parent, false);
            FrameLayout content = (FrameLayout) ptr.getContentView();
            content.addView(targetView);
            parent.addView(ptr, index, targetParams);
        } else {
            ptr = (PtrFrameLayout) LayoutInflater.from(this)
                    .inflate(R.layout.fragment_base, null, false);
            FrameLayout content = (FrameLayout) ptr.getContentView();
            ptr.setLayoutParams(targetParams);
            content.addView(targetView);
        }
        return ptr;
    }

    /**
     * 给一个View添加加载的4种状态（成功，失败，超时，网络异常）
     *
     * @param contentView
     * @return
     */
    protected View getLoadingStateView(View contentView) {
        return null;
    }

    public void updateViewState(int state) {
        if (mLoadStateFrameLayout != null) {
            mLoadStateFrameLayout.updateState(state);
        }
    }

    public PwcApplication getPwcApplication() {
        return PwcApplication.getInstance();
    }

    protected View onDealWithContentView(View layoutView) {
        return layoutView;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View layoutView = inflate(layoutResID);
        View loadingView = getLoadingStateView(layoutView);

        if (loadingView != null) {
            mLoadStateFrameLayout = addLoadingStateView(loadingView);
            layoutView = mLoadStateFrameLayout;
        }

        layoutView = onDealWithContentView(layoutView);
        super.setContentView(layoutView);
    }

    public View inflate(int resId) {
        return mInflater.inflate(resId, null);
    }

    public View inflate(int resId, ViewGroup container) {
        return mInflater.inflate(resId, container, false);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsVisible = true;
        mPresenter.subscribe();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsVisible = false;
        mPresenter.onActivityStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unSubscribe();
        WindowUtils.hideSoftKeyboard(getCurrentFocus());
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onActivitySaveInstanceState(outState);
    }

    public boolean isVisible() {
        return mIsVisible;
    }


    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void showShortToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public ProgressDialog showWaitDialog() {
        return showWaitDialog(R.string.loading);
    }


    public ProgressDialog showWaitDialog(int resid) {
        return showWaitDialog(getString(resid));
    }


    public ProgressDialog showWaitDialog(String message) {
        if (mIsVisible) {
            _waitDialog = DialogHelp.getWaitDialog(this, message);
            _waitDialog.setMessage(message);
            _waitDialog.show();
            return _waitDialog;
        }
        return null;
    }

    public void hideWaitDialog() {
        if (mIsVisible && _waitDialog != null) {
            _waitDialog.dismiss();
            _waitDialog = null;
        }
    }

    public AlertDialog showAlertDialog(String message, int resId, View.OnClickListener confirm) {
        return showAlertDialog(message, resId, confirm, null);
    }

    public AlertDialog showAlertDialog(String message, int resId, View.OnClickListener confirm,
                                       View.OnClickListener cancel) {
        return showAlertDialog(message, resId, confirm, cancel, -1, null, null);
    }

    public AlertDialog showAlertDialog(String message, int resId, View.OnClickListener confirm,
                                       View.OnClickListener cancel,
                                       DialogInterface.OnShowListener showListener,
                                       DialogInterface.OnDismissListener dismissListener) {
        return showAlertDialog(message, resId, confirm, cancel, -1, showListener, dismissListener);
    }

    public AlertDialog showAlertDialog(String message, int resId, View.OnClickListener confirm,
                                       View.OnClickListener cancel, int themeId,
                                       DialogInterface.OnShowListener showListener,
                                       DialogInterface.OnDismissListener dismissListener) {

        mDialogCustomView = inflate(resId);
        AlertDialog.Builder builder = null;
        if (themeId > 0) {
            builder = DialogHelp.getMessageDialog(this, mDialogCustomView, themeId);
        } else {
            builder = DialogHelp.getMessageDialog(this, mDialogCustomView);
        }
        mAlertDialog = builder.create();
        View btnConfirm = mDialogCustomView.findViewById(R.id.dialog_btn_confirm);
        if (btnConfirm != null && confirm != null) {
            btnConfirm.setOnClickListener(confirm);
        }
        View btnCancel = mDialogCustomView.findViewById(R.id.dialog_btn_cancel);
        if (btnCancel != null && cancel != null) {
            btnCancel.setOnClickListener(cancel);
        }
        TextView title = (TextView) mDialogCustomView.findViewById(R.id.dialog_tv_title);
        if (message != null && title != null) {
            title.setText(message);
        }
        if (showListener != null) {
            mAlertDialog.setOnShowListener(showListener);
        }
        if (dismissListener != null) {
            mAlertDialog.setOnDismissListener(dismissListener);
        }
        mAlertDialog.show();
        return mAlertDialog;
    }

    public void hideAlertDialog() {
        if (mIsVisible && mAlertDialog != null) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
            mDialogCustomView = null;
        }
    }

    public PopupWindow showPopUpWindow(View contentView, int width, int height, View showAsDrop) {
        return showPopUpWindow(contentView, width, height, showAsDrop, 0, 0);
    }

    public PopupWindow showPopUpWindow(View contentView, int width, int height, View showAsDrop,
                                       int offsetX, int offsetY) {
        if (mIsVisible) {
            _popWindow = new PopupWindow(contentView, width, height);
            _popWindow.showAsDropDown(showAsDrop, offsetX, offsetY);
            return _popWindow;
        }
        return null;
    }

    public Dialog showLoadingProgressDialog() {
        mLoadingProgressDialog = new Dialog(this, R.style.popupDialog);
        mLoadingProgressDialog.setCanceledOnTouchOutside(false);
        mLoadingProgressDialog.setCancelable(false);
        mLoadingProgressDialog.setContentView(R.layout.dialog_progress);
        WindowManager.LayoutParams lay = mLoadingProgressDialog.getWindow().getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Rect rect = new Rect();
        View view = getWindow().getDecorView();//decorView是window中的最顶层view，可以从window中获取到decorView
        view.getWindowVisibleDisplayFrame(rect);
        lay.height = dm.heightPixels - rect.top;
        lay.width = dm.widthPixels;
        mLoadingProgressDialog.show();
        return mLoadingProgressDialog;
    }

    public void hideLoadingProgressDialog() {
        if (mLoadingProgressDialog != null && mLoadingProgressDialog.isShowing()) {
            mLoadingProgressDialog.dismiss();
            mLoadingProgressDialog = null;
        }
    }

    public void hidePopUpWindow() {
        if (_popWindow != null && mIsVisible && _popWindow.isShowing()) {
            _popWindow.dismiss();
            _popWindow = null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        String language = (String) SharedPreHelper
                .get(this, Constants.KEY_LANGUAGE, Constants.LANGUAGE_CHINESE);
        if (TextUtils.equals(language, Constants.LANGUAGE_CHINESE)) {
            newConfig.locale = Locale.CHINESE;
        } else {
            newConfig.locale = Locale.ENGLISH;
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        int backCount = mFragmentManager.getBackStackEntryCount();
        if (backCount > 0) {
            mFragmentManager.popBackStackImmediate();
            return;
        }
        super.onBackPressed();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent message) {
        switch (message.what) {
            case MessageEvent.EVENT_FINISH:
                finish();
                break;
        }
    }

    /**
     * Debug输出Log信息
     *
     * @param msg
     */
    protected void showDebugLog(String msg) {
        TLog.d(msg);
    }

    /**
     * Error输出Log信息
     *
     * @param msg
     */
    protected void showErrorLog(String msg) {
        TLog.e(msg);
    }

    /**
     * Info输出Log信息
     *
     * @param msg
     */
    protected void showInfoLog(String msg) {
        TLog.i(msg);
    }

    protected int getResColor(int resId) {
        return getResources().getColor(resId);
    }


    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 通过Action跳转界面
     **/
    public void startActivity(String action) {
        startActivity(action, null);
    }

    /**
     * 含有Bundle通过Action跳转界面
     **/
    public void startActivity(String action, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 含有Bundle通过Class打开编辑界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    public void addFragment(int container, BaseFragment fragment) {
        if (fragment != null && !fragment.isAdded()) {
            String tag = fragment.getClass().getName();
            mFragmentManager.beginTransaction().add(container, fragment, tag)
                    .commitAllowingStateLoss();
        }
    }

    public void addFragmentToBackStack(int container, BaseFragment fragment) {
        if (fragment != null && !fragment.isAdded()) {
            String tag = fragment.getClass().getSimpleName();
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.activity_right_in, R.anim.activity_left_out,
                            R.anim.activity_left_in, R.anim.activity_right_out)
                    .add(container, fragment, tag).addToBackStack(null).commitAllowingStateLoss();
        }
    }

    public void removeFragment(BaseFragment fragment) {
        if (fragment != null && fragment.isAdded()) {
            mFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
    }

    public void showFragment(BaseFragment fragment) {
        if (fragment != null && fragment.isAdded()) {
            mFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss();
        }
    }

    public void showFragmentToBackStack(BaseFragment fragment) {
        if (fragment != null && fragment.isAdded()) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.activity_right_in, R.anim.activity_left_out,
                            R.anim.activity_left_in, R.anim.activity_right_out).show(fragment)
                    .addToBackStack(null).commitAllowingStateLoss();
        }
    }

    public void hideFragment(BaseFragment fragment) {
        if (fragment != null && fragment.isAdded()) {
            mFragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss();
        }
    }

    public void replaceFragment(int container, BaseFragment fragment) {
        if (fragment != null) {
            mFragmentManager.beginTransaction().replace(container, fragment)
                    .commitAllowingStateLoss();
        }
    }

    public void startFragment(int container, BaseFragment targetFragment, Bundle bundle) {
        if (targetFragment.isAdded()) {
            showFragmentToBackStack(targetFragment);
            if (bundle != null) {
                targetFragment.onNewBundle(bundle);
            }
        } else {
            addFragmentToBackStack(container, targetFragment);
            if (bundle != null) {
                String tag = targetFragment.getClass().getSimpleName();
                bundle.putString(FRAGMENT_MESSAGE_HEADER, tag);
                setTag(bundle);
            }
        }
    }

    public <T extends BaseFragment> T obtainFragment(Class<T> clazz) {
        T fragment = (T) findFragmentByClazz(clazz);
        if (fragment == null) {
            try {
                fragment = clazz.newInstance();
            } catch (Exception e) {
                TLog.exception(e);
                showDebugLog("instance fragment failed" + e.getMessage());
            }
            return fragment;
        }
        return fragment;
    }

    public void sendParcelToFragment(BaseFragment baseFragment, Bundle bundle) {
        bundle.putString(BaseActivity.FRAGMENT_MESSAGE_HEADER,
                baseFragment.getClass().getSimpleName());
        setTag(bundle);
    }

    public BaseFragment findFragmentByClazz(Class<? extends BaseFragment> clazz) {
        String tag = clazz.getSimpleName();
        return (BaseFragment) mFragmentManager.findFragmentByTag(tag);
    }

    public void setTag(Bundle bundle) {
        mObj = bundle;
    }

    public Bundle getTag() {
        return mObj;
    }

    public Bundle obtainBundle() {
        if (mObj == null) {
            mObj = new Bundle();
        } else {
            mObj.clear();
        }
        return mObj;
    }

    protected T getPresenter() {
        return mPresenter;
    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract int getLayoutId();

    protected abstract T instancePresenter();

}
