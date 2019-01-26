package com.example.sampleapp.data;

import android.content.Context;

import com.example.sampleapp.data.db.DbHelper;
import com.example.sampleapp.data.db.model.User;
import com.example.sampleapp.data.network.ApiHelper;
import com.example.sampleapp.data.network.model.UserListResponse;
import com.example.sampleapp.data.pref.PreferenceHelper;
import com.example.sampleapp.di.ApplicationContext;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Singleton
public class AppDataManager implements DataManager {


    private final Context context;
    private final DbHelper dbHelper;
    private final PreferenceHelper preferenceHelper;
    private final ApiHelper apiHelper;

    @Inject
    AppDataManager(@ApplicationContext Context context,
                   DbHelper dbHelper,
                   PreferenceHelper preferenceHelper,
                   ApiHelper apiHelper) {

        this.context = context;
        this.dbHelper = dbHelper;
        this.preferenceHelper = preferenceHelper;
        this.apiHelper = apiHelper;
    }

    //DATABASE
    @Override
    public Observable<Long> insertUser(User user) {
        return dbHelper.insertUser(user);
    }

    @Override
    public Observable<List<Long>> insertUserList(List<User> users) {
        return dbHelper.insertUserList(users);
    }

    @Override
    public Observable<List<User>> getAllUsers() {
        return dbHelper.getAllUsers();
    }

    @Override
    public Completable wipeUserData() {
        return dbHelper.wipeUserData();
    }

    //SHARED PREFERENCE
    @Override
    public void setTotalPageCount(long id) {
        preferenceHelper.setTotalPageCount(id);
    }

    @Override
    public long getTotalPageCount() {
        return preferenceHelper.getTotalPageCount();
    }

    //NETWORK
    @Override
    public Observable<UserListResponse> doUserListRequest(int page, int perPage) {
        return apiHelper.doUserListRequest(page, perPage);
    }
}
