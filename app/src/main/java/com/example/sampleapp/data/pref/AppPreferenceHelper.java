package com.example.sampleapp.data.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sampleapp.di.ApplicationContext;
import com.example.sampleapp.di.PreferenceInfo;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class AppPreferenceHelper implements PreferenceHelper {

    private final long defaultValue = 0l;

    private static final String PREF_KEY_TOTAL_PAGE_COUNT = "PREF_KEY_TOTAL_PAGE_COUNT";


    private SharedPreferences prefs;

    @Inject
    AppPreferenceHelper(@ApplicationContext Context context,
                        @PreferenceInfo String prefName) {
        prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    @Override
    public void setTotalPageCount(long id) {
        prefs.edit().putLong(PREF_KEY_TOTAL_PAGE_COUNT, id).apply();
    }

    @Override
    public long getTotalPageCount() {
        return prefs.getLong(PREF_KEY_TOTAL_PAGE_COUNT, defaultValue);
    }
}
