package com.example.sampleapp.ui.userlist;

import com.example.sampleapp.data.db.model.User;
import com.example.sampleapp.ui.base.MvpView;

import java.util.List;

public interface UserListMvpView extends MvpView {
    void updateUserList(List<User> users);

    void appendToUserList(List<User> users);

    void updateProgressBarVisibility(boolean isVisible);

    void addScrollListener();

    void removeScrollListener();
}
