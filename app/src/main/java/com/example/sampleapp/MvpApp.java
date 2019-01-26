package com.example.sampleapp;

import android.app.Application;

import com.example.sampleapp.di.component.ApplicationComponent;
import com.example.sampleapp.di.component.DaggerApplicationComponent;
import com.example.sampleapp.di.module.ApplicationModule;
import com.facebook.stetho.Stetho;

import javax.inject.Inject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MvpApp extends Application {

    @Inject
    CalligraphyConfig calligraphyConfig;

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        //Instantiate ApplicationComponent
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        applicationComponent.inject(this);

        CalligraphyConfig.initDefault(calligraphyConfig);

        Stetho.initializeWithDefaults(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
