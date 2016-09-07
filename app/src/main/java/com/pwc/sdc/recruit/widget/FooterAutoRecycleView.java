package com.pwc.sdc.recruit.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.pwc.sdc.recruit.PwcApplication;
import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BaseRecycleAdapter;

/**
 * @author:dongpo 创建时间: 8/26/2016
 * 描述:
 * 修改:
 */
public class FooterAutoRecycleView extends RecyclerView implements ViewTreeObserver.OnGlobalLayoutListener {


    public FooterAutoRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public void startGlobalLayoutListener(){
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public void removeGlobalOnLayoutListener(){
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }


    @Override
    public void onGlobalLayout() {
        boolean canScrollRight = canScrollHorizontally(-1);
        boolean canScrollLeft = canScrollHorizontally(1);
        BaseRecycleAdapter adapter = (BaseRecycleAdapter) getAdapter();
        View footer = adapter.getFooter();
        ViewGroup.LayoutParams params = footer.getLayoutParams();
        int solidSearchWidth = PwcApplication.getInstance().getResources().getDimensionPixelSize(R.dimen.assign_chosen_search_width);
        int searchLeftWidth = footer.getLeft();
        int parentPaddingRight = getPaddingRight();
        int searchWidth = getRight() - searchLeftWidth - parentPaddingRight;
        if (!canScrollLeft && !canScrollRight && params.width != searchWidth) {
            params.width = searchWidth;
            footer.setLayoutParams(params);
        } else if ((canScrollLeft || canScrollRight) && params.width != solidSearchWidth) {
            params.width = solidSearchWidth;
            footer.setLayoutParams(params);
        }
    }
}
