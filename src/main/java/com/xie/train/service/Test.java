package com.xie.train.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xie.train.controller.BaseFXController;
import com.xie.train.utils.PingUtils;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Action1;

import javax.net.ssl.*;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by xie on 17/9/13.
 */
public class Test {

    private static final Logger logger = LoggerFactory.getLogger(BaseFXController.class);

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

    public static void main(String args[]) throws IOException {

        Gson gson = new GsonBuilder()
                //配置你的Gson
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        try {
            okHttpBuilder.sslSocketFactory(getSSLSocketFactory()).hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpBuilder.build())
                .baseUrl("https://kyfw.12306.cn/")
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        TestService service = retrofit.create(TestService.class);


        rx.Observable.from(HOSTS).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(PingUtils.ping(s));
            }
        });
    }


    public static boolean addressCache(Map<String, String> addressMap) throws NoSuchMethodException {

        // 以下内容修改缓存失效时间,单位秒(-1,永久缓存;0,不缓存;其它>0的值为缓存的秒数)
        Class<?> iacp = sun.net.InetAddressCachePolicy.class;

        Field cachePolicyFiled = null;
        Field setEnable = null;
        try {
            cachePolicyFiled = iacp.getDeclaredField("cachePolicy");
            setEnable = iacp.getDeclaredField("set");
        } catch (NoSuchFieldException e) {
            logger.error("Get cachePolicyFiled or setEnable failed !", e);
        }
        cachePolicyFiled.setAccessible(true);
        setEnable.setAccessible(true);

        try {
            cachePolicyFiled.set(null, -1);
            setEnable.set(null, true);
        } catch (IllegalArgumentException e) {
            logger.error("Set cachePolicyFiled and setEnable failed !", e);
        } catch (IllegalAccessException e) {
            logger.error("Access cachePolicyFiled and setEnable failed !", e);
        }
        // 以下内容修改DNS的缓存数据，实现域名动态绑定
        Class<?> inetAddressClass = java.net.InetAddress.class;
        Field cacheField = null;
        Object addressCache = null;
        InetAddress ia = null;
        try {
            cacheField = inetAddressClass.getDeclaredField("addressCache");
        } catch (SecurityException e) {
            logger.error("Get addressCache security failed !", e);
        } catch (NoSuchFieldException e) {
            logger.error("Get addressCache field failed !", e);
        }

        cacheField.setAccessible(true);

        try {
            addressCache = cacheField.get(inetAddressClass);
        } catch (IllegalArgumentException e) {
            logger.error("Get cacheField IllegalArgument failed !", e);
        } catch (IllegalAccessException e) {
            logger.error("Get cacheField Access failed !", e);
        }
        Class<?> cacheClazz = addressCache.getClass();

        Method m_put = cacheClazz.getMethod("put", new Class<?>[]{String.class, Object.class});
        m_put.setAccessible(true);

        try {
            for (String domain : addressMap.keySet()) {
                ia = Inet4Address.getByName(addressMap.get(domain));
                m_put.invoke(addressCache, new Object[]{domain, new InetAddress[]{ia}});
            }

        } catch (IllegalArgumentException e) {
            logger.error("Put addressCache IllegalArgument failed !", e);
        } catch (IllegalAccessException e) {
            logger.error("Put addressCache IllegalAccess failed !", e);
        } catch (InvocationTargetException e) {
            logger.error("Put addressCache InvocationTarget failed !", e);
        } catch (UnknownHostException e) {
            logger.error("Put addressCache UnknownHost failed !", e);
        }
        // 修改缓存数据结束
        return true;
    }


    public static class StringConverter implements Converter<ResponseBody, String> {

        public static final StringConverter INSTANCE = new StringConverter();

        @Override
        public String convert(ResponseBody value) throws IOException {
            return value.string();
        }
    }

    public static class StringConverterFactory extends Converter.Factory {

        public static final StringConverterFactory INSTANCE = new StringConverterFactory();

        public static StringConverterFactory create() {
            return INSTANCE;
        }

        // 我们只关实现从ResponseBody 到 String 的转换，所以其它方法可不覆盖
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            if (type == String.class) {
                return StringConverter.INSTANCE;
            }
            //其它类型我们不处理，返回null就行
            return null;
        }
    }

    public static SSLSocketFactory getSSLSocketFactory() throws Exception {
        //创建一个不验证证书链的证书信任管理器。
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }
        }};

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts,
                new java.security.SecureRandom());
        // Create an ssl socket factory with our all-trusting manager
        return sslContext
                .getSocketFactory();
    }
}
