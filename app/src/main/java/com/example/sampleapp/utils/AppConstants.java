package com.example.sampleapp.utils;

public final class AppConstants {

    public static String BASE_URL = "https://reqres.in/api/";

    private AppConstants() {
        //PC
    }

    public static final String DB_NAME = "SampleApp.db";
    public static final int DB_VERSION = 1;
    public static final String PREF_NAME = "SampleApp_pref";
    public static final int PAGE_START_INDEX = 1;

    /*
    *  Maximum item in a paged result
    * */
    public static final int PER_PAGE_MAX_COUNT = 8;
}
