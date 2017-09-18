package train.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import javax.net.ssl.*;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by xie on 17/9/15.
 */
@Configuration
public class RetrofitConfig {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(RetrofitConfig.class);

    private static HashMap<String, String> cookieMap = new HashMap<>();

    private final static String COOKIE_FILE = "cookie.properties";


    private void saveToFile() {
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


    private void loadFromFile() {
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


    @Bean("retrofit")
    public Retrofit retrofitInit() {
        loadFromFile();
        Gson gson = new GsonBuilder()
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
            public Response intercept(Chain chain) throws IOException {
                final Request.Builder builder = chain.request().newBuilder();

                logger.info(chain.request().url().toString());

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
                                }
                                cookie.append(";RAIL_DEVICEID=b5MkrO5u2zTPmz-JlEbCccu5rJ68F-oJ0tk1WSYXKn9DaVDisPEQKSx6cApl-aPccKToOQGkJPPXbtNkltQVl_yYzMrmIYhjU5-Y6yCdIDldkrTL582N-rnViCzOlsfUQsPzqU-oD-1ZsXGV5qkYK8Vh_O8wdhYd");
                                builder.addHeader("Cookie", cookie.toString());
                            }
                        });
                return chain.proceed(builder.build());
            }
        });

        /**
         * 保存cookie
         */
        okHttpBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                    Observable.from(originalResponse.headers("Set-Cookie"))
                            .map(new Func1<String, String[]>() {
                                @Override
                                public String[] call(String s) {
                                    logger.info("=====================     cookie   =====================");
                                    logger.info(s);
                                    return s.split(";");
                                }
                            })
                            .subscribe(new Action1<String[]>() {
                                @Override
                                public void call(String[] cookie) {
                                    for (int i = 0; i < cookie.length; i++) {
                                        String[] name_value = cookie[i].split("=");
                                        cookieMap.put(name_value[0], name_value[1]);
                                    }

                                    saveToFile();
                                }
                            });
                }
                return originalResponse;
            }
        });

        return new Retrofit.Builder()
                .client(okHttpBuilder.build())
                .baseUrl("https://kyfw.12306.cn/")
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }


    private static class StringConverter implements Converter<ResponseBody, String> {

        private static final StringConverter INSTANCE = new StringConverter();

        @Override
        public String convert(ResponseBody value) throws IOException {
            return value.string();
        }
    }

    private static class StringConverterFactory extends Converter.Factory {

        private static final StringConverterFactory INSTANCE = new StringConverterFactory();

        private static StringConverterFactory create() {
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

}
