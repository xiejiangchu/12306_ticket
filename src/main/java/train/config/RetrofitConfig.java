package train.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import train.utils.Constants;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by xie on 17/9/15.
 */
@Configuration
public class RetrofitConfig {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(RetrofitConfig.class);


    @Bean("retrofit")
    public Retrofit retrofitInit() {

        return new Retrofit.Builder()
                .client(OkHttpUtils.builder().build())
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(initGson()))
                .build();
    }

    private static Gson initGson() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
        return gson;
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


}
