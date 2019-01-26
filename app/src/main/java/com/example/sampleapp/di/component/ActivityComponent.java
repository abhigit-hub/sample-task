package com.example.sampleapp.di.component;


import com.example.sampleapp.di.PerActivity;
import com.example.sampleapp.di.module.ActivityModule;
import com.example.sampleapp.ui.splash.SplashActivity;
import com.example.sampleapp.ui.userlist.UserListActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(SplashActivity splashActivity);

    void inject(UserListActivity userListActivity);
}
