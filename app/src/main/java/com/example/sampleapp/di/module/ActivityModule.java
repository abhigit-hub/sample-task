package com.example.sampleapp.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.example.sampleapp.data.db.model.User;
import com.example.sampleapp.di.ActivityContext;
import com.example.sampleapp.di.PerActivity;
import com.example.sampleapp.ui.splash.SplashMvpPresenter;
import com.example.sampleapp.ui.splash.SplashMvpView;
import com.example.sampleapp.ui.splash.SplashPresenter;
import com.example.sampleapp.ui.userlist.UserListAdapter;
import com.example.sampleapp.ui.userlist.UserListMvpPresenter;
import com.example.sampleapp.ui.userlist.UserListMvpView;
import com.example.sampleapp.ui.userlist.UserListPresenter;
import com.example.sampleapp.utils.rx.AppSchedulerProvider;
import com.example.sampleapp.utils.rx.SchedulerProvider;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;


@Module
public class ActivityModule {

    private final AppCompatActivity activity;

    public ActivityModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return activity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return activity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    SchedulerProvider provideScheduleProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager() {
        return new LinearLayoutManager(activity);
    }

    @Provides
    @PerActivity
    SplashMvpPresenter<SplashMvpView> provideSplashPresenter(SplashPresenter<SplashMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    UserListMvpPresenter<UserListMvpView> provideUserListPresenter(UserListPresenter<UserListMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    UserListAdapter providesUserListAdapter() {
        return new UserListAdapter(new ArrayList<User>());
    }
}
