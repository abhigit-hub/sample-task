package com.example.sampleapp.di.component;

import android.app.Application;
import android.content.Context;

import com.example.sampleapp.MvpApp;
import com.example.sampleapp.data.DataManager;
import com.example.sampleapp.di.ApplicationContext;
import com.example.sampleapp.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context getContext();

    Application getApplication();

    DataManager getDataManager();

    void inject(MvpApp mvpApp);
}
