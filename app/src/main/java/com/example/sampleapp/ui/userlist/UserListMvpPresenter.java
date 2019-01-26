package com.example.sampleapp.ui.userlist;

import com.example.sampleapp.di.PerActivity;
import com.example.sampleapp.ui.base.MvpPresenter;

@PerActivity
public interface UserListMvpPresenter<V extends UserListMvpView> extends MvpPresenter<V> {
    void onScrolledPositionUpdate(int lastVisibleItemPosition, int itemCount);

    void retryInternet();
}
