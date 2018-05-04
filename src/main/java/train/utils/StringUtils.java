package train.utils;

import train.bean.Passenger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xie on 17/9/15.
 */
public class StringUtils {
    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static String joinPassanger(Passenger... passengers) {
        String[] temp = new String[passengers.length];
        for (int i = 0; i < passengers.length; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("M");
            stringBuilder.append(passengers[i].getPassenger_flag());
            stringBuilder.append(passengers[i].getPassenger_type());
            stringBuilder.append(passengers[i].getPassenger_name());
            stringBuilder.append(passengers[i].getPassenger_id_type_code());
            stringBuilder.append(passengers[i].getPassenger_id_no());
            temp[i] = stringBuilder.toString();
        }
        return String.join("N_", temp);
    }

    public static String joinoldPassanger(Passenger... passengers) {
        String[] temp = new String[passengers.length];
        for (int i = 0; i < passengers.length; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(passengers[i].getPassenger_name());
            stringBuilder.append(passengers[i].getPassenger_id_type_code());
            stringBuilder.append(passengers[i].getPassenger_id_no());
            temp[i] = stringBuilder.toString();
        }
        return String.join("1_", temp);
    }
}
