package com.pwc.sdc.recruit.base;

import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.interf.ActivityPresenter;

import butterknife.BindView;

/**
 * baseActionBar Activity
 *
 * @author
 * @created
 */
public abstract class ToolBarActivity<T extends ActivityPresenter> extends BaseActivity<T> {

    @BindView(R.id.common_tl_toolbar)
    public Toolbar mToolbar;
    private ActionBar mActionBar;
    @BindView(R.id.common_tv_title)
    public TextView mTvToolBarCenter;

    @Override
    protected void initView() {
        if(mToolbar == null){
            mToolbar = (Toolbar) findViewById(R.id.common_tl_toolbar);
            mTvToolBarCenter = (TextView) mToolbar.findViewById(R.id.common_tv_title);
        }
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        initActionBar();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

    }

    @Override
    protected View onDealWithContentView(View contentView) {
        LinearLayout newContent = new LinearLayout(this);
        newContent.setOrientation(LinearLayout.VERTICAL);
        mInflater.from(this).inflate(R.layout.view_tool_bar, newContent, true);
        newContent.addView(contentView);

        LinearLayout.LayoutParams contentViewLayoutParams = (LinearLayout.LayoutParams) contentView.getLayoutParams();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        contentView.setLayoutParams(params);
        newContent.setLayoutParams(contentViewLayoutParams);
        return newContent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setBackButtonEnable(boolean enable) {
        mActionBar.setDisplayHomeAsUpEnabled(enable);
    }


    protected void initActionBar() {
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
    }

    public void setActionBarTitle(int resId) {
        if (resId != 0) {
            setActionBarTitle(getString(resId));
        }
    }

    public void setActionBarCenterTitleEnable(boolean enable) {
        if (enable == false) {
            mTvToolBarCenter.setVisibility(View.GONE);
        }
    }

    public void setActionBarTitleEnable(boolean enable) {
        if (enable == false) {
            mActionBar.setDisplayShowTitleEnabled(false);
        }
    }

    public void setActionBarTitle(String title) {
        mActionBar.setTitle(title);
    }

    public void setActionBarBackButtonIcon(int resId) {
        mToolbar.setNavigationIcon(resId);
    }

    public void setActionBarTitleColor(int color) {
        mToolbar.setTitleTextColor(getResources().getColor(color));
    }

    public void setActionBarCenterTitle(int resId) {
        mTvToolBarCenter.setText(resId);
    }

    public void setActionBarCenterTextColor(int resId) {
        mTvToolBarCenter.setTextColor(getResources().getColor(resId));
    }

    public void setActionBarCenterTextSize(float size) {
        mTvToolBarCenter.setTextSize(size);
    }

    public void addActionBarCustomView(int layoutId) {
        mInflater.inflate(layoutId, mToolbar, true);
    }

}
