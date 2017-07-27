package com.pwc.sdc.recruit.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.constants.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by l on 2016/3/9.
 * 自定义View，根据不同的状态显示
 */
public class LoadStateFrameLayout extends FrameLayout {

    @BindView(R.id.view_state_empty)
    TextView iv_empty;

    @BindView(R.id.view_state_error)
    LinearLayout ll_error;

    @BindView(R.id.view_state_loading)
    LinearLayout pb_loading;

    @BindView(R.id.view_tv_state_error)
    TextView tv_error;

    private int state = Constants.STATE_USER;
    private View mSuccessView;

    public LoadStateFrameLayout(Context context) {
        this(context, null);
    }

    public LoadStateFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadStateFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_loading_state, this, true);
        ButterKnife.bind(this);
    }

    public void addSuccessView(View successView) {
        if (successView != null && successView.getParent() == null) {
            addView(successView);
            mSuccessView = successView;
        } else {
            throw new IllegalArgumentException("successView is null or have a parent view");
        }
    }


    /**
     * 根据不同的状态值，更新当前的View
     *
     * @param currentState
     */
    public void updateState(int currentState) {
        switch (currentState) {
            case Constants.STATE_USER:
                mSuccessView.setVisibility(View.VISIBLE);
                iv_empty.setVisibility(View.GONE);
                ll_error.setVisibility(View.GONE);
                pb_loading.setVisibility(View.GONE);
                break;
            case Constants.STATE_ERROR:
                mSuccessView.setVisibility(View.INVISIBLE);
                iv_empty.setVisibility(View.GONE);
                ll_error.setVisibility(View.VISIBLE);
                pb_loading.setVisibility(View.GONE);
                break;
            case Constants.STATE_LOADING:
                mSuccessView.setVisibility(View.INVISIBLE);
                iv_empty.setVisibility(View.GONE);
                ll_error.setVisibility(View.GONE);
                pb_loading.setVisibility(View.VISIBLE);
                break;
            case Constants.STATE_EMPTY:
                mSuccessView.setVisibility(View.INVISIBLE);
                iv_empty.setVisibility(View.VISIBLE);
                ll_error.setVisibility(View.GONE);
                pb_loading.setVisibility(View.GONE);
                break;
            default:
                throw new IllegalArgumentException("can't not recognize view state");
        }

        state = currentState;
    }

    public void setEmptyText(String text, float size, int color){
        iv_empty.setText(text);
        iv_empty.setTextSize(size);
        iv_empty.setTextColor(color);
    }

    public void setEmptyBackground(int color){
        iv_empty.setBackgroundColor(color);
    }

    public void setErrorText(String text, float size, int color){
        tv_error.setText(text);
        tv_error.setTextSize(size);
        tv_error.setTextColor(color);
    }

    public int getState(){
        return state;
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        if (mSuccessView != null) {
            return mSuccessView.canScrollHorizontally(direction);
        }
        return super.canScrollHorizontally(direction);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        if (mSuccessView != null) {
            return mSuccessView.canScrollVertically(direction);
        }
        return super.canScrollVertically(direction);
    }
}
