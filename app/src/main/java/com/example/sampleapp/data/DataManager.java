package com.example.sampleapp.data;


import com.example.sampleapp.data.db.DbHelper;
import com.example.sampleapp.data.network.ApiHelper;
import com.example.sampleapp.data.pref.PreferenceHelper;

public interface DataManager extends DbHelper, PreferenceHelper, ApiHelper {

}
