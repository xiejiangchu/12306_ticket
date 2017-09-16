package train.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import javax.net.ssl.*;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.security.cert.CertificateException;

/**
 * Created by xie on 17/9/15.
 */
@Configuration
public class RetrofitConfig {

    private static StringBuffer cookieBuffer = new StringBuffer();

    @Bean("retrofit")
    public Retrofit retrofitInit() {

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
         * 插入cookie
         */
        okHttpBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                final Request.Builder builder = chain.request().newBuilder();

                //最近在学习RxJava,这里用了RxJava的相关API大家可以忽略,用自己逻辑实现即可
                Observable.just(cookieBuffer)
                        .subscribe(new Action1<StringBuffer>() {
                            @Override
                            public void call(StringBuffer cookie) {
                                //添加cookie
                                cookie.append(";RAIL_DEVICEID=QSTF_Pp5d0KigjzCtBW6mwr7-27Djped6eybGe-F6Onbm25_9Zb_xovg740-xeYIynXIwzrm" +
                                        "KKXZW5lthyM0PBWJgZNYy4Jj6OhPTd90jXUwornOW5QgbDacaAvfcHD1sCwdB3Ux9bMhPZFlh4zFOquQFvwc15h_");
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
                    cookieBuffer.setLength(0);
                    Observable.from(originalResponse.headers("Set-Cookie"))
                            .map(new Func1<String, String>() {
                                @Override
                                public String call(String s) {
                                    String[] cookieArray = s.split(";");
                                    return cookieArray[0];
                                }
                            })
                            .subscribe(new Action1<String>() {
                                @Override
                                public void call(String cookie) {
                                    System.out.println("=====================     cookie   =====================");
                                    System.out.println(cookie);
                                    cookieBuffer.append(cookie).append(";");
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
