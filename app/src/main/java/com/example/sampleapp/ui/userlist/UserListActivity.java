package com.example.sampleapp.ui.userlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.example.sampleapp.R;
import com.example.sampleapp.data.db.model.User;
import com.example.sampleapp.ui.base.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListActivity extends BaseActivity implements UserListMvpView {

    @Inject
    UserListMvpPresenter<UserListMvpView> presenter;

    @Inject
    UserListAdapter userListAdapter;

    @Inject
    LinearLayoutManager layoutManager;

    @BindView(R.id.rv_user_list)
    RecyclerView rvUserList;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    public static Intent getStartIntent(Context context) {
        return new Intent(context, UserListActivity.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_list);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        presenter.onAttach(this);

        setUp();
    }

    @Override
    protected void setUp() {
        setUpToolbar();

        setUpAdapter();
    }

    @Override
    protected void retryInternet() {
        presenter.retryInternet();
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.contacts);
        }
    }

    private void setUpAdapter() {
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvUserList.setLayoutManager(layoutManager);
        rvUserList.setItemAnimator(new DefaultItemAnimator());
        rvUserList.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        rvUserList.setAdapter(userListAdapter);
    }

    /*
    *   This scroll listener sends the scroll position change events to the presenter, for it to
    *   decide if the user has reached the bottom of the list.
    * */
    @Override
    public void addScrollListener() {
        rvUserList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                presenter.onScrolledPositionUpdate(
                        layoutManager.findLastVisibleItemPosition(),
                        layoutManager.getItemCount());
            }
        });
    }

    @Override
    public void removeScrollListener() {
        rvUserList.clearOnScrollListeners();
    }

    @Override
    public void updateUserList(List<User> users) {
        userListAdapter.updateListItems(users);
    }

    @Override
    public void appendToUserList(List<User> users) {
        userListAdapter.addListItems(users);
    }

    @Override
    public void updateProgressBarVisibility(boolean isVisible) {
        progressBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
        *   Register the callback with the Adapter such that the Presenter becomes the listener and
        *   deals with all the action triggered in the Recycler View
        * */
        userListAdapter.setCallback((UserListPresenter) presenter);
    }

    @Override
    protected void onPause() {
        userListAdapter.removeCallback();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
