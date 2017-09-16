package train.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xie on 17/9/15.
 */
public class StrUtils {

    public static String stationName(){
        return "1.9025";
    }

    public static String formatDate(Date date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }
}
