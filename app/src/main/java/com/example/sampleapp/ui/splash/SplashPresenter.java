package com.example.sampleapp.ui.splash;


import com.example.sampleapp.data.DataManager;
import com.example.sampleapp.ui.base.BasePresenter;
import com.example.sampleapp.utils.rx.SchedulerProvider;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class SplashPresenter<V extends SplashMvpView> extends BasePresenter<V> implements SplashMvpPresenter<V> {

    @Inject
    SplashPresenter(SchedulerProvider schedulerProvider,
                    CompositeDisposable compositeDisposable,
                    DataManager dataManager) {
        super(schedulerProvider, compositeDisposable, dataManager);
    }

    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);

        startActivityWithDelay();
    }


    /*
    *   Opens the main screen of the app with a delay of 1 second for the Splash screen
    * */
    private void startActivityWithDelay() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getMvpView().openUserListActivity();
            }
        }, 1000);
    }
}
