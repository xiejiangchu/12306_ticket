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
    @GET("/otn/index/init")
    Call<String> test();

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
            "X-Requested-With:XMLHttpRequest",
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
    })
    @GET(Constants.QUERY_X)
    Call<FetchTrain> queryX(@Query("leftTicketDTO.train_date") String train_date,
                            @Query("leftTicketDTO.from_station") String from_station,
                            @Query("leftTicketDTO.to_station") String to_station,
                            @Query("purpose_codes") String purpose_codes);


    /**
     * 验证码验证
     *
     * @param answer
     * @param login_site
     * @param rand
     * @return
     */
    @Headers({
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36",
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
    Call<UamAuthClientBean> uamauthclient(@Query("tk") String tk);


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
    @Headers({
            "Cache-Control:no-cache",
            "If-Modified-Since:0",
            "X-Requested-With:XMLHttpRequest",
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
    })
    @POST("/otn/login/checkUser")
    Call<CheckUser> checkUser();

    @Headers({
            "Cache-Control:no-cache",
            "If-Modified-Since:0",
            "X-Requested-With:XMLHttpRequest",
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
    })
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


    /**
     * 获取 var globalRepeatSubmitToken = '20463d0de07ae8536f4752ef4c4590eb';
     *
     * @return
     */
    @GET("/otn/confirmPassenger/initDc")
    Call<String> initDc();

    /**
     * 在上面的信息中座位编号指的是，一等座、二等座等的编码，从ticketInfoForPassengerForm.limitBuySeatTicketDTO.seat_type_codes属性中选择获取。
     * 票类型指的是，成人票，学生票等的编码，可以从ticketInfoForPassengerForm.limitBuySeatTicketDTO.ticket_type_codes属性中选择获取。
     * <p>
     * 证件类型指的是二代身份证，学生证，签证等的编码，可以从ticketInfoForPassengerForm.cardTypes属性中选择获取。
     *
     * @param cancel_flag
     * @param bed_level_order_num
     * @param passengerTicketStr  座位编号,0,票类型,乘客名,证件类型,证件号,手机号码,保存常用联系人(Y或N)  'M,0,1,谢江初,1,362201198910185014,,N_M,0,1,谭斌,1,36220119900203563X,,N',
     * @param oldPassengerStr     乘客名,证件类型,证件号,乘客类型  '谢江初,1,362201198910185014,1_谭斌,1,36220119900203563X,1_',
     * @param tour_flag           'dc'
     * @param randCode
     * @param whatsSelect
     * @param _json_att
     * @param REPEAT_SUBMIT_TOKEN add13c27f533769120f7f8ad68d051e6
     * @return
     */
    @FormUrlEncoded
    @POST("/otn/confirmPassenger/checkOrderInfo")
    Call<String> checkOrderInfo(@Field("cancel_flag") String cancel_flag,
                                @Field("bed_level_order_num") String bed_level_order_num,
                                @Field("passengerTicketStr") String passengerTicketStr,
                                @Field("oldPassengerStr") String oldPassengerStr,
                                @Field("tour_flag") String tour_flag,
                                @Field("randCode") String randCode,
                                @Field("whatsSelect") String whatsSelect,
                                @Field("_json_att") String _json_att,
                                @Field("REPEAT_SUBMIT_TOKEN") String REPEAT_SUBMIT_TOKEN);

    /**
     * @param train_date
     * @param train_no
     * @param stationTrainCode
     * @param seatType
     * @param fromStationTelecode
     * @param toStationTelecode
     * @param leftTicket
     * @param purpose_codes
     * @param train_location
     * @param isCheckOrderInfo
     * @return
     */
    @FormUrlEncoded
    @POST("/otn/confirmPassenger/getQueueCount")
    Call<String> getQueueCount(@Field("train_date") String train_date,
                               @Field("train_no") String train_no,
                               @Field("stationTrainCode") String stationTrainCode,
                               @Field("seatType") String seatType,
                               @Field("fromStationTelecode") String fromStationTelecode,
                               @Field("toStationTelecode") String toStationTelecode,
                               @Field("leftTicket") String leftTicket,
                               @Field("purpose_codes") String purpose_codes,
                               @Field("train_location") String train_location,
                               @Field("isCheckOrderInfo") String isCheckOrderInfo);

    /**
     * @param passengerTicketStr
     * @param oldPassengerStr
     * @param randCode
     * @param purpose_codes
     * @param key_check_isChange
     * @param leftTicketStr
     * @param train_location
     * @param choose_seats
     * @param seatDetailType
     * @param whatsSelect
     * @param dwAll              N/Y
     * @return
     */
    @POST("/otn/confirmPassenger/confirmSingleForQueue")
    Call<String> confirmSingleForQueue(@Field("passengerTicketStr") String passengerTicketStr,
                                       @Field("oldPassengerStr") String oldPassengerStr,
                                       @Field("randCode") String randCode,
                                       @Field("purpose_codes") String purpose_codes,
                                       @Field("key_check_isChange") String key_check_isChange,
                                       @Field("leftTicketStr") String leftTicketStr,
                                       @Field("train_location") String train_location,
                                       @Field("choose_seats") String choose_seats,
                                       @Field("seatDetailType") String seatDetailType,
                                       @Field("whatsSelect") String whatsSelect,
                                       @Field("dwAll") String dwAll);

    /**
     * @param random   Date().getTime()
     * @param tourFlag
     * @return
     */
    @FormUrlEncoded
    @POST("/otn/confirmPassenger/queryOrderWaitTime")
    Call<String> queryOrderWaitTime(@Query("random") String random,
                                    @Field("tourFlag") String tourFlag);
}
