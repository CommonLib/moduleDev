package com.pwc.sdc.recruit.business.finder;

import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.pwc.sdc.recruit.R;
import com.pwc.sdc.recruit.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by lhuang126 on 7/12/2016.
 */
public class JobFinderActivity extends BaseActivity<JobFinderPresenter> implements JobFinderContract.View {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.search_bar)
    SearchView mSearchView;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    Cursor suggestionCursor;
    JobFinderAdapter mAdapter;
    SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        simpleCursorAdapter = new SimpleCursorAdapter(this,
                R.layout.job_finder_suggestion_layout, suggestionCursor, new String[]{JobFinderModel.JOB_FINDER_NAME},
                new int[]{R.id.textview});
        initSuggestionCursor();
        mSearchView.setSuggestionsAdapter(simpleCursorAdapter);
        updateSuggestionCursor();
        updateAdapterCursor();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new JobFinderAdapter(this);
        mAdapter.setDataSource(getPresenter().dataSource);
        mAdapter.addOnCardViewClickListener(new JobFinderContract.AdapterPresenter.OnCardViewClickListener() {
            @Override
            public void onClick(View view, int position, boolean isCardViewClickAble) {
                if (!isCardViewClickAble)
                    mSearchView.clearFocus();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mSearchView.clearFocus();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_job_finder;
    }

    @Override
    protected JobFinderPresenter instancePresenter() {
        return new JobFinderPresenter(this, new JobFinderDataSource(this));
    }

    @Override
    public void initSuggestionCursor() {
        getPresenter().dataSource.getJobFinderNameCursor(null, new JobFinderModel.LoadJobNameCallback() {
            @Override
            public void onJobNameLoaded(Cursor cursor) {
                suggestionCursor = cursor;
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void updateSuggestionCursor() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getPresenter().dataSource.getJobFinderNameCursor(query, new JobFinderModel.LoadJobNameCallback() {
                    @Override
                    public void onJobNameLoaded(Cursor cursor) {
                        suggestionCursor = cursor;
                        simpleCursorAdapter.changeCursor(suggestionCursor);
                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                });
                if (mAdapter != null) {
                    mAdapter.updateQueryString(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getPresenter().dataSource.getJobFinderNameCursor(newText, new JobFinderModel.LoadJobNameCallback() {
                    @Override
                    public void onJobNameLoaded(Cursor cursor) {
                        suggestionCursor = cursor;
                        simpleCursorAdapter.changeCursor(suggestionCursor);
                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                });
                if (mAdapter != null) {
                    mAdapter.updateQueryString(newText);
                }
                return false;
            }
        });
    }

    @Override
    public void updateAdapterCursor() {
        mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {

               String queryString=null;
                //TODO 这里要把选择的字串拿到。
                //queryString=getStringByPosition(position);
                finishSearch(queryString);
                return false;
            }
        });
        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mAdapter.updateCardViewClickAble(!hasFocus);
            }
        });

    }

    @Override
    public void finishSearch(String result) {
        mSearchView.clearFocus();
        mAdapter.updateQueryString(result);
    }
}
