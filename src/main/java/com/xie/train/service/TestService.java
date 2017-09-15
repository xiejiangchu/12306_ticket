package com.xie.train.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by xie on 17/9/13.
 */
public interface TestService {

    @GET("/otn/HttpZF/GetJS")
    Call<String> getJs(@Header("Remote Address") String contentRange);
}
