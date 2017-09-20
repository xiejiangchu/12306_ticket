package train.config;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import train.utils.Constants;

import javax.annotation.PostConstruct;
import javax.net.ssl.*;
import java.io.*;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by xie on 17/9/20.
 */
public class OkHttpUtils {

    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(OkHttpUtils.class);

    private static HashMap<String, String> cookieMap = new HashMap<>();

    private final static String COOKIE_FILE = "cookie.properties";


    public static OkHttpClient.Builder builder() {
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

//        /**
//         * log  拦截器
//         */
//        // Log信息拦截器
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);//这里可以选择拦截级别
//
//        //设置 Debug Log 模式
//        okHttpBuilder.addInterceptor(loggingInterceptor);


        /**
         * 插入cookie
         */
        okHttpBuilder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                final Request.Builder builder = chain.request().newBuilder();
                String url = chain.request().url().toString();
                if (!url.contains(Constants.QUERY_X)) {
                    //最近在学习RxJava,这里用了RxJava的相关API大家可以忽略,用自己逻辑实现即可
                    Observable.just(cookieMap)
                            .subscribe(new Action1<HashMap<String, String>>() {
                                @Override
                                public void call(HashMap<String, String> map) {
                                    StringBuilder cookie = new StringBuilder();

                                    for (Map.Entry<String, String> entry : map.entrySet()) {
                                        cookie.append(entry.getKey());
                                        cookie.append("=");
                                        cookie.append(entry.getValue());
                                        cookie.append(";");
                                    }
                                    cookie.append("RAIL_DEVICEID=Yr5ltfnHvq_BPruONq3k7ox9hKu_1n6gBTHFA0hrPwf-Pez1nmig7JscI-ZZQoPaVwDnKJjzZtVfDry57xXLK7tW5pEX7mm7-A_W02dg5HIfXtk_LlwxkxxTBG0lZdSyhggnyzH3UQnr1l8Je7rGXYD8PVDb2pV9;");
                                    builder.addHeader("Cookie", cookie.toString());
                                }
                            });

                } else {
                    builder.addHeader("Cookie", "RAIL_DEVICEID=Yr5ltfnHvq_BPruONq3k7ox9hKu_1n6gBTHFA0hrPwf-Pez1nmig7JscI-ZZQoPaVwDnKJjzZtVfDry57xXLK7tW5pEX7mm7-A_W02dg5HIfXtk_LlwxkxxTBG0lZdSyhggnyzH3UQnr1l8Je7rGXYD8PVDb2pV9;");
                }
                return chain.proceed(builder.build());
            }
        });

        /**
         * 保存cookie
         */
        okHttpBuilder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response originalResponse = chain.proceed(chain.request());
                if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                    Observable.from(originalResponse.headers("Set-Cookie"))
                            .map(new Func1<String, String[]>() {
                                @Override
                                public String[] call(String s) {
                                    logger.info("=====================     Set-Cookie   =====================");
                                    logger.info(s);
                                    return s.split(";");
                                }
                            })
                            .subscribe(new Action1<String[]>() {
                                @Override
                                public void call(String[] cookie) {
                                    for (int i = 0; i < cookie.length; i++) {
                                        String[] name_value = cookie[i].split("=");
                                        if (name_value.length == 2) {
                                            cookieMap.put(name_value[0], name_value[1]);
                                        }
                                    }
                                    saveToFile();
                                }
                            });
                }
                return originalResponse;
            }
        });
        return okHttpBuilder;
    }


    private static SSLSocketFactory getSSLSocketFactory() throws Exception {
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

    private static void saveToFile() {
        File file = new File(COOKIE_FILE);

        try {
            OutputStream out = new FileOutputStream(file);
            Properties properties = new Properties();
            for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
                properties.put(entry.getKey(), entry.getValue());
            }
            properties.put("RAIL_DEVICEID", "b5MkrO5u2zTPmz-JlEbCccu5rJ68F-oJ0tk1WSYXKn9DaVDisPEQKSx6cApl-aPccKToOQGkJPPXbtNkltQVl_yYzMrmIYhjU5-Y6yCdIDldkrTL582N-rnViCzOlsfUQsPzqU-oD-1ZsXGV5qkYK8Vh_O8wdhYd");
            properties.store(out, DateTime.now().toString());
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }

    }


    @PostConstruct
    private static void loadFromFile() {
        File file = new File(COOKIE_FILE);
        try {
            InputStream in = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(in);
            Set<Map.Entry<Object, Object>> entrySet = properties.entrySet();//返回的属性键值对实体
            for (Map.Entry<Object, Object> entry : entrySet) {
                cookieMap.put((String) entry.getKey(), (String) entry.getValue());
            }
            logger.info("加载cookie:" + cookieMap.toString());
        } catch (FileNotFoundException e) {
            logger.info("加载cookie失败");
        } catch (IOException e) {
            logger.info("加载cookie失败");
        }

    }
}
