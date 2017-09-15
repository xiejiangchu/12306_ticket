package com.xie.train.retrofit;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by xie on 17/9/15.
 */
public interface train {

    @POST("/passport/captcha/captcha-check")
    Call<String> captureCheck();
}
