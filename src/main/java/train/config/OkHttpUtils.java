package train.config;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
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
import java.util.concurrent.TimeUnit;

/**
 * Created by xie on 17/9/20.
 */
public class OkHttpUtils {

    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(OkHttpUtils.class);

    private static HashMap<String, String> cookieMap = new HashMap<>();

    private final static String COOKIE_FILE = "cookie.properties";

    private final static String COOKIE_DEFAULT = "";


    public static OkHttpClient.Builder builder() {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        okHttpBuilder.connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS).build();
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

        /**
         * log  拦截器
         */
        // Log信息拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//这里可以选择拦截级别

        //设置 Debug Log 模式
        okHttpBuilder.addInterceptor(loggingInterceptor);


        /**
         * 插入cookie
         */
        okHttpBuilder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                final Request.Builder builder = chain.request().newBuilder();
                String url = chain.request().url().toString();
                if (!url.contains(Constants.QUERY_X)) {
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
                                    cookie.append(COOKIE_DEFAULT);
                                    builder.addHeader("Cookie", cookie.toString());
                                }
                            });

                } else {
                    builder.addHeader("Cookie", COOKIE_DEFAULT);
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
                                    for (int i = 0; i < 1; i++) {
                                        String[] name_value = cookie[i].split("=");
                                        if (name_value.length == 2 && !name_value[0].equalsIgnoreCase("path")) {
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
