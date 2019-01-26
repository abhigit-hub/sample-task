package com.example.sampleapp.data.db;

import com.example.sampleapp.data.db.model.User;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Singleton
public class AppDbHelper implements DbHelper {

    private AppDatabase appDatabase;

    @Inject
    AppDbHelper(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    //USER
    @Override
    public Observable<Long> insertUser(User user) {
        return Observable.fromCallable(() -> appDatabase.userDao().insertUser(user));
    }

    @Override
    public Observable<List<Long>> insertUserList(List<User> users) {
        return Observable.fromCallable(() -> appDatabase.userDao().insertUserList(users));
    }

    @Override
    public Observable<List<User>> getAllUsers() {
        return Observable.fromCallable(() -> appDatabase.userDao().getAllUsers());
    }

    @Override
    public Completable wipeUserData() {
        return Completable.fromAction(() -> appDatabase.userDao().nukeUserTable());
    }
}
