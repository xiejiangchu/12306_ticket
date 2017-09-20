package train.task;

import com.alibaba.dcm.DnsCacheManipulator;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import train.bean.FetchTrain;
import train.config.RetrofitConfig;
import train.service.TrainService;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xie on 17/9/19.
 */
@Component
public class TaskTest {

    private final static Logger logger = LoggerFactory.getLogger(TaskTest.class);

    private final static String DOMAIN = "kyfw.12306.cn";

    private Retrofit retrofit;

    private TrainService trainService;

    private static List<String> HOSTS = Arrays.asList("182.243.62.48", "27.148.151.17",
            "219.147.233.232", "59.44.30.36", "183.58.18.36", "14.18.201.47", "42.123.105.35",
            "14.215.9.83", "59.49.42.252", "58.222.19.61", "101.227.66.207", "125.90.206.182",
            "61.156.243.247", "27.159.182.50", "222.180.166.229", "202.98.156.62", "218.92.221.15",
            "218.92.209.74", "180.97.217.77", "183.62.114.247", "183.134.9.58", "61.147.227.247", "58.222.19.72",
            "222.245.77.74", "220.162.97.136", "182.140.236.27", "61.136.167.78", "117.23.2.85", "222.218.45.216",
            "42.81.9.47", "125.46.22.126", "101.227.102.199", "182.106.194.107", "220.162.97.209", "111.206.186.244",
            "58.222.42.9", "123.128.14.201", "115.231.74.76", "153.35.174.108", "221.202.204.253", "150.138.169.233",
            "220.194.200.232", "113.207.77.192", "61.155.237.56", "124.164.8.65", "61.167.54.109", "139.215.210.59",
            "111.62.244.177", "123.138.157.109", "117.148.165.154", "117.23.6.81", "113.107.57.43");

    @Scheduled(fixedDelay = 50000)
    public void taskTest() {

        System.out.println("开始");

        try {
            logger.info(InetAddress.getByName("kyfw.12306.cn").getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        Observable.from(HOSTS)
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        Observable.just(s)
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        System.out.println("rank is :" + Thread.currentThread().getId());

                                        DnsCacheManipulator.setDnsCache("kyfw.12306.cn", s);
                                        String ip = null;
                                        try {
                                            ip = InetAddress.getByName("kyfw.12306.cn").getHostAddress();
                                        } catch (UnknownHostException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            logger.info(ip);
                                            retrofit = new RetrofitConfig().retrofitInit();
                                            trainService = retrofit.create(TrainService.class);
                                            Response<FetchTrain> response = trainService.queryX("2017-09-21", "YCG", "AOH", "ADULT").execute();
                                            if (response.body() != null) {
                                                System.out.println(JSON.toJSONString(response.body().getData().getResult().get(0)));
                                            }
                                        } catch (IOException e) {
                                        }

                                    }
                                });
                        return Observable.just("");
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("结束");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String o) {
                        System.out.println("");

                    }
                });
    }


    //@Scheduled(cron = "0/10 * * * * ? ")   //每5秒执行一次

    public void testCron() throws IOException {

        try {
            logger.info(InetAddress.getByName("kyfw.12306.cn").getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        System.out.println(trainService.test().execute().body());

        DnsCacheManipulator.setDnsCache("kyfw.12306.cn", "103.213.249.247");

        retrofit = new RetrofitConfig().retrofitInit();
        trainService = retrofit.create(TrainService.class);
        System.out.println(trainService.test().execute().body());

        try {
            logger.info(InetAddress.getByName("kyfw.12306.cn").getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


    }


}
