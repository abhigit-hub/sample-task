package com.example.sampleapp.data.network;


import com.example.sampleapp.data.network.model.UserListResponse;

import io.reactivex.Observable;

public interface ApiHelper {

    Observable<UserListResponse> doUserListRequest(int page, int perPage);
}
