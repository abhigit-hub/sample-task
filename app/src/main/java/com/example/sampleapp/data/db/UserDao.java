package com.example.sampleapp.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.sampleapp.data.db.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    Long insertUser(User user);

    @Insert
    List<Long> insertUserList(List<User> users);

    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("DELETE FROM user")
    void nukeUserTable();
}
