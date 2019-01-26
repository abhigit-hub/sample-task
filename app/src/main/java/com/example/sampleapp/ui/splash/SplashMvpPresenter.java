package com.example.sampleapp.ui.splash;


import com.example.sampleapp.di.PerActivity;
import com.example.sampleapp.ui.base.MvpPresenter;

@PerActivity
public interface SplashMvpPresenter<V extends SplashMvpView> extends MvpPresenter<V> {
}
