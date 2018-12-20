package train.config;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;

import javax.net.ssl.*;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

/**
 * Created by xie on 17/9/20.
 */
public class OkHttpUtils {

    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(OkHttpUtils.class);


    public static OkHttpClient.Builder builder() {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        okHttpBuilder
                .dns(OkHttpDns.getInstance())
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS).build();

        setCookies(okHttpBuilder);
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
//        okHttpBuilder.addInterceptor(loggingInterceptor);
        return okHttpBuilder;
    }


    /**
     * 设置Cookie持久化
     *
     * @param builder
     */
    private static void setCookies(OkHttpClient.Builder builder) {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        builder.cookieJar(new JavaNetCookieJar(cookieManager));
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
        return sslContext.getSocketFactory();
    }
}
