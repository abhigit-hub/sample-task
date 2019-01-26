package com.example.sampleapp.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.sampleapp.data.db.model.User;

import javax.inject.Singleton;


@Database(entities = {User.class}, version = 1, exportSchema = false)
@Singleton
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
}
