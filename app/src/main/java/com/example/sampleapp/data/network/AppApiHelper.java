package com.example.sampleapp.data.network;

import com.example.sampleapp.data.network.model.UserListResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class AppApiHelper implements ApiHelper {

    private ApiCall apiCall;

    @Inject
    AppApiHelper(ApiCall apiCall) {
        this.apiCall = apiCall;
    }

    @Override
    public Observable<UserListResponse> doUserListRequest(int page, int perPage) {
        return apiCall.makeUserListRequest(page, perPage);
    }
}
