package com.example.sampleapp.ui.base;


import com.example.sampleapp.data.DataManager;
import com.example.sampleapp.utils.rx.SchedulerProvider;

import io.reactivex.disposables.CompositeDisposable;


public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private SchedulerProvider schedulerProvider;
    private CompositeDisposable compositeDisposable;
    private DataManager dataManager;

    private V mvpView;

    public BasePresenter(SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable,
                         DataManager dataManager) {
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = compositeDisposable;
        this.dataManager = dataManager;
    }

    @Override
    public void onAttach(V mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void onDetach() {
        compositeDisposable.dispose();
        this.mvpView = null;
    }

    public V getMvpView() {
        return mvpView;
    }

    public boolean isViewAttached() {
        return mvpView != null;
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public SchedulerProvider getSchedulerProvider() {
        return schedulerProvider;
    }
}
