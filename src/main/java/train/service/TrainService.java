package train.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import train.bean.*;
import train.utils.Constants;

/**
 * Created by xie on 17/9/15.
 */
public interface TrainService {

    /**
     * 刷票初始化
     *
     * @return
     */
    @GET("/otn/leftTicket/init")
    Call<String> init();


    /**
     * 查询余票信息
     *
     * @param train_date    火车时间
     * @param from_station  起点站
     * @param to_station    终点站
     * @param purpose_codes 成人/学生
     * @return
     */
    //https://kyfw.12306.cn/otn/leftTicket/queryX?leftTicketDTO.train_date=2017-09-27&leftTicketDTO.from_station=SHH&leftTicketDTO.to_station=YCG&purpose_codes=ADULT
    @Headers({
            "Referer:https://kyfw.12306.cn/otn/leftTicket/init",
            "Host:kyfw.12306.cn",
            "If-Modified-Since:0",
            "X-Requested-With:XMLHttpRequest"
    })
    @GET(Constants.QUERY_X)
    Call<FetchTrain> queryX(@Query("leftTicketDTO.train_date") String train_date,
                            @Query("leftTicketDTO.from_station") String from_station,
                            @Query("leftTicketDTO.to_station") String to_station,
                            @Query("leftTicketDTO.purpose_codes") String purpose_codes);


    /**
     * 验证码验证
     *
     * @param answer
     * @param login_site
     * @param rand
     * @return
     */
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

    /**
     * 获取验证码图片
     *
     * @param login_site
     * @param module
     * @param rand
     * @param end
     * @return
     */
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


    //https://kyfw.12306.cn/otn/leftTicket/queryTicketPrice?train_no=5l000G132140&from_station_no=01&to_station_no=11&seat_types=OM9&train_date=2017-09-27
    @GET("/otn/leftTicket/queryTicketPrice")
    Call<String> queryTicketPrice(@Query("train_no") String train_no,
                                  @Query("from_station_no") String from_station_no,
                                  @Query("to_station_no") String to_station_no,
                                  @Query("seat_types") String seat_types,
                                  @Query("train_date") String train_date);


    @POST("/otn/queryOrder/queryMyOrderNoComplete")
    Call<QueryMyOrder> queryMyOrderNoComplete();

    /**
     * @param queryType:2
     * @param queryStartDate:2017-09-17
     * @param queryEndDate:2017-10-12
     * @param come_from_flag:my_order
     * @param pageSize:8
     * @param pageIndex:0
     * @param query_where:G             历史订单 query_where:H
     * @param sequeue_train_name:
     * @return
     */
    @POST("/otn/queryOrder/queryMyOrder")
    Call<String> queryMyOrder(@Query("queryType") int queryType,
                              @Query("queryStartDate") String queryStartDate,
                              @Query("queryEndDate") String queryEndDate,
                              @Query("come_from_flag") String come_from_flag,
                              @Query("pageSize") int pageSize,
                              @Query("pageIndex") int pageIndex,
                              @Query("query_where") String query_where,
                              @Query("sequeue_train_name") String sequeue_train_name);

    /**
     * 获取城市信息
     *
     * @param station_name 站点名称
     * @param _json_att
     * @return
     */
    @GET("/otn/userCommon/allCitys")
    Call<AllCityDto> allCitys(@Query("station_name") String station_name,
                              @Query("_json_att") String _json_att);


    /**
     * 查询火车列表
     *
     * @param train_no        火车编号
     * @param from_station_no 始发站
     * @param to_station_no   终点站
     * @param seat_types      席别
     * @param train_date      乘车日期
     * @return
     */
    @GET("/otn/czxx/queryByTrainNo")
    Call<QueryByTrainNoDto> queryByTrainNo(@Query("train_no") String train_no,
                                           @Query("from_station_no") String from_station_no,
                                           @Query("to_station_no") String to_station_no,
                                           @Query("seat_types") String seat_types,
                                           @Query("train_date") String train_date);

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @param appid    appId-otn
     * @return
     */
    @FormUrlEncoded
    @POST("/passport/web/login")
    Call<String> login(@Field("username") String username,
                       @Field("password") String password,
                       @Field("appid") String appid);

    /**
     * uamtk
     *
     * @param appid
     * @return appId-otn
     */
    @POST("/passport/web/auth/uamtk")
    Call<UamtkDto> uamtk(@Query("appid") String appid);

    /**
     * uamauthclient
     *
     * @param tk
     * @return
     */
    @POST("/otn/uamauthclient")
    Call<String> uamauthclient(@Query("tk") String tk);


    /**
     * 乘客列表
     *
     * @return
     */
    @POST("/otn/confirmPassenger/getPassengerDTOs")
    Call<GetPassengerDto> getPassengerDTOs();

    /**
     * 检查用户是否登陆
     *
     * @return
     */
    @POST("/otn/login/checkUser")
    Call<CheckUser> checkUser();

    @FormUrlEncoded
    @POST("/otn/leftTicket/submitOrderRequest")
    Call<String> submitOrderRequest(@Field("secretStr") String secretStr,
                                    @Field("train_date") String train_date,
                                    @Field("back_train_date") String back_train_date,
                                    @Field("tour_flag") String tour_flag,
                                    @Field("purpose_codes") String purpose_codes,
                                    @Field("query_from_station_name") String query_from_station_name,
                                    @Field("query_to_station_name") String query_to_station_name,
                                    @Field("undefined") String undefined);
}
