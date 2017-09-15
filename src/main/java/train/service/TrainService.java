package train.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by xie on 17/9/15.
 */
public interface TrainService {

    @POST("/passport/captcha/captcha-check")
    Call<String> captureCheck();

    @GET("/otn/HttpZF/GetJS")
    Call<String> getJs();

    @GET("/passport/captcha/captcha-image")
    Call<ResponseBody> captchaImage(@Query("login_site") String login_site,
                                    @Query("module") String module,
                                    @Query("rand") String rand,
                                    @Query("end") String end);
}
