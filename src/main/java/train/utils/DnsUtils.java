package train.utils;

import com.alibaba.dcm.DnsCacheManipulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xie on 17/9/12.
 */
public class DnsUtils {

    private final static Logger logger = LoggerFactory.getLogger(DnsUtils.class);

    private final static String DOMAIN = "kyfw.12306.cn";

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

    public static void main(String[] args) throws Exception {
        try {
            logger.info(InetAddress.getByName("kyfw.12306.cn").getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        Map<String, String> addressMap = new HashMap<>();
        addressMap.put(DOMAIN, HOSTS.get(0));

        DnsCacheManipulator.setDnsCache("kyfw.12306.cn", "115.239.210.27");

        rx.Observable.from(HOSTS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.immediate())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        DnsCacheManipulator.setDnsCache("kyfw.12306.cn", s);
                        try {
                            logger.info(InetAddress.getByName("kyfw.12306.cn").getHostAddress());
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                    }
                });


        Thread.currentThread().sleep(5000);

//        Observable.from(HOSTS).map(new Func1<String, String>() {
//            @Override
//            public String call(String s) {
//                return s+" - "+PingUtils.ping(s);
//            }
//        }).subscribeOn(Schedulers.newThread()).subscribe(new Action1<String>() {
//            @Override
//            public void call(String integer) {
//                System.out.println(integer);
//            }
//        });
    }
}
