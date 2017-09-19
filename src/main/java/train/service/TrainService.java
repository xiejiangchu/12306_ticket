package train.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import train.bean.*;

/**
 * Created by xie on 17/9/15.
 */
public interface TrainService {

    @GET("/otn/leftTicket/init")
    Call<String> init();


    //https://kyfw.12306.cn/otn/resources/merged/login_UAM_js.js?scriptVersion=1.9034
    @Headers({
            "User-Agent:Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36",
            "Access-Control-Allow-Credentials:true"

    })
    @FormUrlEncoded
    @POST("/passport/captcha/captcha-check")
    Call<String> captureCheck(@Field("answer") String answer,
                              @Field("login_site") String login_site,
                              @Field("rand") String rand);

    /**
     * 火车基本信息
     *
     * @return
     */
    @GET("/otn/resources/js/query/train_list.js")
    Call<String> train_list();

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


    @POST("/otn/queryOrder/queryMyOrderNoComplete")
    Call<QueryMyOrder> queryMyOrderNoComplete();


    //    queryType:2
//    queryStartDate:2017-09-17
//    queryEndDate:2017-10-12
//    come_from_flag:my_order
//    pageSize:8
//    pageIndex:0
//    query_where:G   历史订单 query_where:H
//    sequeue_train_name:
    @POST("/otn/queryOrder/queryMyOrder")
    Call<String> queryMyOrder(@Query("queryType") int train_no,
                              @Query("queryStartDate") String queryStartDate,
                              @Query("queryEndDate") String queryEndDate,
                              @Query("come_from_flag") String come_from_flag,
                              @Query("pageSize") int pageSize,
                              @Query("pageIndex") int pageIndex,
                              @Query("query_where") String query_where,
                              @Query("sequeue_train_name") String sequeue_train_name);

    //    station_name:
//    _json_att:
    @GET("/otn/userCommon/allCitys")
    Call<AllCityDto> allCitys(@Query("station_name") String station_name,
                              @Query("_json_att") String _json_att);


    @GET("/otn/czxx/queryByTrainNo")
    Call<QueryByTrainNoDto> queryByTrainNo(@Query("train_no") String train_no,
                                           @Query("from_station_no") String from_station_no,
                                           @Query("to_station_no") String to_station_no,
                                           @Query("seat_types") String seat_types,
                                           @Query("train_date") String train_date);


    @POST("/passport/web/login")
    Call<String> login(@Query("username") String username,
                       @Query("password") String password,
                       @Query("appid") String appid);

    @POST("/passport/web/auth/uamtk")
    Call<UamtkDto> uamtk(@Query("appid") String appid);

    @POST("/otn/uamauthclient")
    Call<String> uamauthclient(@Query("tk") String tk);

    @POST("/otn/confirmPassenger/getPassengerDTOs")
    Call<GetPassengerDto> getPassengerDTOs();
}
