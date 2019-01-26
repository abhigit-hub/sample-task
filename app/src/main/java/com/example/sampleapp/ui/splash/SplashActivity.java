package com.example.sampleapp.ui.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.sampleapp.R;
import com.example.sampleapp.ui.base.BaseActivity;
import com.example.sampleapp.ui.userlist.UserListActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;


public class SplashActivity extends BaseActivity implements SplashMvpView {


    @Inject
    SplashMvpPresenter<SplashMvpView> presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        presenter.onAttach(SplashActivity.this);

        setUp();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void setUp() {
    }

    @Override
    protected void retryInternet() {
    }

    @Override
    public void openUserListActivity() {
        startActivity(UserListActivity.getStartIntent(getApplicationContext()));
        finish();
    }
}
