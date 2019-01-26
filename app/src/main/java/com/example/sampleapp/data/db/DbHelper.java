package com.example.sampleapp.data.db;

import com.example.sampleapp.data.db.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;


public interface DbHelper {

    //User
    Observable<Long> insertUser(User user);

    Observable<List<Long>> insertUserList(List<User> users);

    Observable<List<User>> getAllUsers();

    Completable wipeUserData();
}
