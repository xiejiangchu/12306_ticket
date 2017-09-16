package train.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import train.bean.FetchTrain;

/**
 * Created by xie on 17/9/15.
 */
public interface TrainService {

    @GET("/otn/leftTicket/init")
    Call<String> init();

    @POST("/passport/captcha/captcha-check")
    Call<String> captureCheck();

    @GET("/otn/HttpZF/GetJS")
    Call<String> getJs();

    @GET("/passport/captcha/captcha-image")
    Call<ResponseBody> captchaImage(@Query("login_site") String login_site,
                                    @Query("module") String module,
                                    @Query("rand") String rand,
                                    @Query("end") double end);

    /**
     * 获取站信息
     *
     * @param station_version
     * @return
     */
    //https://kyfw.12306.cn/otn/resources/js/framework/station_name.js?station_version=1.9025
    @GET("/otn/resources/js/framework/station_name.js")
    Call<String> stationName(@Query("station_version") String station_version);


    //https://kyfw.12306.cn/otn/leftTicket/queryX?leftTicketDTO.train_date=2017-09-27&leftTicketDTO.from_station=SHH&leftTicketDTO.to_station=YCG&purpose_codes=ADULT
    @Headers({
            "Referer:https://kyfw.12306.cn/otn/leftTicket/init",
            "Host:kyfw.12306.cn",
            "If-Modified-Since:0",
            "X-Requested-With:XMLHttpRequest"
    })
    @GET("/otn/leftTicket/queryX")
    Call<FetchTrain> queryX(@Query("leftTicketDTO.train_date") String train_date,
                            @Query("leftTicketDTO.from_station") String from_station,
                            @Query("leftTicketDTO.to_station") String to_station,
                            @Query("leftTicketDTO.purpose_codes") String purpose_codes);

    //https://kyfw.12306.cn/otn/leftTicket/queryTicketPrice?train_no=5l000G132140&from_station_no=01&to_station_no=11&seat_types=OM9&train_date=2017-09-27
    @GET("/otn/leftTicket/queryTicketPrice")
    Call<String> queryTicketPrice(@Query("train_no") String train_no,
                                  @Query("from_station_no") String from_station_no,
                                  @Query("to_station_no") String to_station_no,
                                  @Query("seat_types") String seat_types,
                                  @Query("train_date") String train_date);
}
