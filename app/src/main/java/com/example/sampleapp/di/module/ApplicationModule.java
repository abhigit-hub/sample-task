package com.example.sampleapp.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.sampleapp.R;
import com.example.sampleapp.data.AppDataManager;
import com.example.sampleapp.data.DataManager;
import com.example.sampleapp.data.db.AppDatabase;
import com.example.sampleapp.data.db.AppDbHelper;
import com.example.sampleapp.data.db.DbHelper;
import com.example.sampleapp.data.network.ApiCall;
import com.example.sampleapp.data.network.ApiHelper;
import com.example.sampleapp.data.network.AppApiHelper;
import com.example.sampleapp.data.pref.AppPreferenceHelper;
import com.example.sampleapp.data.pref.PreferenceHelper;
import com.example.sampleapp.di.ApplicationContext;
import com.example.sampleapp.di.DatabaseInfo;
import com.example.sampleapp.di.PreferenceInfo;
import com.example.sampleapp.utils.AppConstants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @ApplicationContext
    Context providesContext() {
        return application;
    }

    @Provides
    Application providesApplication() {
        return application;
    }

    @Provides
    @DatabaseInfo
    String providesDatabaseName() {
        return AppConstants.DB_NAME;
    }

    @Provides
    @DatabaseInfo
    Integer providesDatabaseVersion() {
        return AppConstants.DB_VERSION;
    }

    @Provides
    @PreferenceInfo
    String providesSharedPrefName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @Singleton
    DataManager providesDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    DbHelper providesDbHelper(AppDbHelper appDbHelper) {
        return appDbHelper;
    }

    @Provides
    @Singleton
    ApiHelper providesApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    @Singleton
    PreferenceHelper providesPreferenceHelper(AppPreferenceHelper appPreferenceHelper) {
        return appPreferenceHelper;
    }

    @Provides
    @Singleton
    ApiCall providesApiCall() {
        return ApiCall.Factory.create();
    }

    @Provides
    @Singleton
    AppDatabase providesAppDatabase(@ApplicationContext Context context,
                                    @DatabaseInfo String dbName) {
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                dbName).build();
    }

    @Provides
    @Singleton
    CalligraphyConfig providesCalligraphyConfig() {
        return new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Google-Sans-Font/GoogleSans-Medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build();
    }
}
