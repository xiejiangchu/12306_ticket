package train.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import train.bean.*;
import train.utils.Constants;

import java.util.Map;

/**
 * Created by xie on 17/9/15.
 */
public interface TrainService {

    /**
     * 360验证码识别
     */
    @Headers({
            "Host:check.huochepiao.360.cn",
            "Upgrade-Insecure-Requests:1",
            "User-Agent:Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36 QIHU 360EE",
            "Content-Type: application/json"})
    @POST("http://47.98.124.142:9082/api/v2/getCheck")
    Call<String> getCheck360(@Body Check360Dto check360Dto);

    @Headers({
            "Host:check.huochepiao.360.cn",
            "Upgrade-Insecure-Requests:1",
            "User-Agent:Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36 QIHU 360EE",
            "Content-Type: application/json"})
    @POST("http://check.huochepiao.360.cn/img_vcode")
    Call<String> getPositionBy360(@Body Position360Dto position360Dto);

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
     * 登录初始化
     *
     * @return
     */
    @GET("/otn/login/conf")
    Call<LoginConfDto> loginConf();

    /**
     * 登录初始化
     *
     * @return
     */
    @GET("/otn/login/init")
    Call<String> loginInit();

    /**
     * 检查登录状态 登录流程
     *
     * @return
     */
    @Headers({
            "Referer:https://kyfw.12306.cn/otn/index/init",
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
    })
    @FormUrlEncoded
    @POST("/passport/web/auth/uamtk-static")
    Call<UamtkStaticDto> uamtkStatic(@Field(value = "appid", encoded = true) String appid);


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
            "Referer:https://kyfw.12306.cn/otn/resources/login.html"
    })
    @GET("/passport/captcha/captcha-check")
    Call<String> captureCheck(@Query("answer") String answer,
                              @Query("login_site") String login_site,
                              @Query("rand") String rand);

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
     * base64生成验证码
     *
     * @param login_site
     * @param module
     * @param rand
     * @param timeStamp
     * @return
     */
    @Headers({
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36",
            "Referer:https://kyfw.12306.cn/otn/leftTicket/init"
    })
    @GET("/passport/captcha/captcha-image64")
    Call<Image64Dto> captchaImage64(@Query("login_site") String login_site,
                                    @Query("module") String module,
                                    @Query("rand") String rand,
                                    @Query("_") long timeStamp);

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
    @Headers({
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36",
            "Referer:https://kyfw.12306.cn/otn/resources/login.html"
    })
    @FormUrlEncoded
    @POST("/passport/web/login")
    Call<String> login(@Field(value = "username", encoded = true) String username,
                       @Field(value = "password", encoded = true) String password,
                       @Field(value = "appid", encoded = true) String appid,
                       @Field(value = "answer", encoded = true) String answer);

    /**
     * uamtk
     *
     * @param appid
     * @return appId-otn
     */
    @Headers({
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36",
            "Referer:https://kyfw.12306.cn/otn/resources/login.html"
    })
    @FormUrlEncoded
    @POST("/passport/web/auth/uamtk")
    Call<UamtkDto> uamtk(@Field(value = "appid", encoded = true) String appid);

    /**
     * uamauthclient
     *
     * @param tk
     * @return
     */
    @FormUrlEncoded
    @POST("/otn/uamauthclient")
    Call<UamAuthClientBean> uamauthclient(@Field(value = "tk", encoded = true) String tk);


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

    @GET("/otn/passcodeNew/getPassCodeNew")
    Call<ResponseBody> getPassCodeNew(@Query("module") String module,
                                      @Query("rand") String rand);

    @FormUrlEncoded
    @POST("/otn/login/checkUser")
    Call<CheckUser> checkUser(@Field(value = "_json_att", encoded = true) String _json_att);

    @Headers({
            "Cache-Control:no-cache",
            "If-Modified-Since:0",
            "X-Requested-With:XMLHttpRequest",
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
    })
    @FormUrlEncoded
    @POST("/otn/leftTicket/submitOrderRequest")
    Call<BaseDto> submitOrderRequest(@Field(value = "secretStr", encoded = true) String secretStr,
                                     @Field(value = "train_date", encoded = true) String train_date,
                                     @Field(value = "back_train_date", encoded = true) String back_train_date,
                                     @Field(value = "tour_flag", encoded = true) String tour_flag,
                                     @Field(value = "purpose_codes", encoded = true) String purpose_codes,
                                     @Field(value = "query_from_station_name", encoded = true) String query_from_station_name,
                                     @Field(value = "query_to_station_name", encoded = true) String query_to_station_name,
                                     @Field(value = "undefined", encoded = true) String undefined);


    @POST("/otn/leftTicket/submitOrderRequest")
    Call<String> submitOrderRequest2(@Field(value = "secretStr", encoded = true) String secretStr,
                                     @Field(value = "train_date", encoded = true) String train_date,
                                     @Field(value = "back_train_date", encoded = true) String back_train_date,
                                     @Field(value = "tour_flag", encoded = true) String tour_flag,
                                     @Field(value = "purpose_codes", encoded = true) String purpose_codes,
                                     @Field(value = "query_from_station_name", encoded = true) String query_from_station_name,
                                     @Field(value = "query_to_station_name", encoded = true) String query_to_station_name,
                                     @Field(value = "undefined", encoded = true) String undefined);


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
    @Headers({
            "Cache-Control:no-cache",
            "If-Modified-Since:0",
            "X-Requested-With:XMLHttpRequest",
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
    })
    @FormUrlEncoded
    @POST("/otn/confirmPassenger/checkOrderInfo")
    Call<CheckOrderInfoDto> checkOrderInfo(@Field(value = "cancel_flag", encoded = true) String cancel_flag,
                                           @Field(value = "bed_level_order_num", encoded = true) String bed_level_order_num,
                                           @Field(value = "passengerTicketStr", encoded = true) String passengerTicketStr,
                                           @Field(value = "oldPassengerStr", encoded = true) String oldPassengerStr,
                                           @Field(value = "tour_flag", encoded = true) String tour_flag,
                                           @Field(value = "randCode", encoded = true) String randCode,
                                           @Field(value = "whatsSelect", encoded = true) String whatsSelect,
                                           @Field(value = "_json_att", encoded = true) String _json_att,
                                           @Field(value = "REPEAT_SUBMIT_TOKEN", encoded = true) String REPEAT_SUBMIT_TOKEN);

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
    @Headers({
            "Cache-Control:no-cache",
            "X-Requested-With:XMLHttpRequest",
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
    })
    @FormUrlEncoded
    @POST("/otn/confirmPassenger/getQueueCount")
    Call<String> getQueueCount(@Field(value = "train_date", encoded = true) String train_date,
                               @Field(value = "train_no", encoded = true) String train_no,
                               @Field(value = "stationTrainCode", encoded = true) String stationTrainCode,
                               @Field(value = "seatType", encoded = true) String seatType,
                               @Field(value = "fromStationTelecode", encoded = true) String fromStationTelecode,
                               @Field(value = "toStationTelecode", encoded = true) String toStationTelecode,
                               @Field(value = "leftTicket", encoded = true) String leftTicket,
                               @Field(value = "purpose_codes", encoded = true) String purpose_codes,
                               @Field(value = "train_location", encoded = true) String train_location,
                               @Field(value = "isCheckOrderInfo", encoded = true) String isCheckOrderInfo);

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
    @Headers({
            "Cache-Control:no-cache",
            "If-Modified-Since:0",
            "X-Requested-With:XMLHttpRequest",
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
    })
    @POST("/otn/confirmPassenger/confirmSingleForQueue")
    Call<String> confirmSingleForQueue(@Field(value = "passengerTicketStr", encoded = true) String passengerTicketStr,
                                       @Field(value = "oldPassengerStr", encoded = true) String oldPassengerStr,
                                       @Field(value = "randCode", encoded = true) String randCode,
                                       @Field(value = "purpose_codes", encoded = true) String purpose_codes,
                                       @Field(value = "key_check_isChange", encoded = true) String key_check_isChange,
                                       @Field(value = "leftTicketStr", encoded = true) String leftTicketStr,
                                       @Field(value = "train_location", encoded = true) String train_location,
                                       @Field(value = "choose_seats", encoded = true) String choose_seats,
                                       @Field(value = "seatDetailType", encoded = true) String seatDetailType,
                                       @Field(value = "whatsSelect", encoded = true) String whatsSelect,
                                       @Field(value = "dwAll", encoded = true) String dwAll);

    /**
     * @param random   Date().getTime()
     * @param tourFlag
     * @return
     */
    @FormUrlEncoded
    @POST("/otn/confirmPassenger/queryOrderWaitTime")
    Call<String> queryOrderWaitTime(@Query(value = "random", encoded = true) String random,
                                    @Field(value = "tourFlag", encoded = true) String tourFlag);


    /**
     * 订票时检测用户是否登录
     *
     * @param req
     * @return
     */
    @POST("/otn/login/checkUser")
    public Call<String> checkUser(@QueryMap Map<String, String> req);
}
