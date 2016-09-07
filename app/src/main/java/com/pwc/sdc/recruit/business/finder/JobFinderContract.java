package com.pwc.sdc.recruit.business.finder;

import android.database.Cursor;

import com.pwc.sdc.recruit.base.interf.ActivityPresenter;
import com.pwc.sdc.recruit.base.interf.ViewLayer;

/**
 * Created by lhuang126 on 7/12/2016.
 */
public interface JobFinderContract {
    interface View extends ViewLayer {
        void initSuggestionCursor();

        void updateSuggestionCursor();

        void updateAdapterCursor();
        void finishSearch(String result);
    }

    interface Presenter extends ActivityPresenter {

    }

    interface AdapterPresenter {
        void updateCursor(Cursor cursor);
        void updateQueryString(String columnName);
        void updateCardViewClickAble(boolean able);
        void onCardViewClick(boolean isCardViewClickAble);
        void addOnCardViewClickListener(OnCardViewClickListener listener);
        void removeOnCardViewClickListener(OnCardViewClickListener listener);
        interface OnCardViewClickListener{
            void onClick(android.view.View view,int position,boolean isCardViewClickAble);
        }
    }


}
