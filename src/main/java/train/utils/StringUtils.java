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
            stringBuilder.append("O");
            stringBuilder.append(",0,");
            stringBuilder.append(passengers[i].getPassenger_type());
            stringBuilder.append(",");
            stringBuilder.append(passengers[i].getPassenger_name());
            stringBuilder.append(",");
            stringBuilder.append(passengers[i].getPassenger_id_type_code());
            stringBuilder.append(",");
            stringBuilder.append(passengers[i].getPassenger_id_no());
            stringBuilder.append(",");
            stringBuilder.append(passengers[i].getMobile_no() == null ? "" : passengers[i].getMobile_no());
            stringBuilder.append(",");
            stringBuilder.append("N");
            temp[i] = stringBuilder.toString();
        }
        return String.join("_", temp);
    }

    public static String joinoldPassanger(Passenger... passengers) {
        String[] temp = new String[passengers.length];
        for (int i = 0; i < passengers.length; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(passengers[i].getPassenger_name());
            stringBuilder.append(",");
            stringBuilder.append(passengers[i].getPassenger_id_type_code());
            stringBuilder.append(",");
            stringBuilder.append(passengers[i].getPassenger_id_no());
            stringBuilder.append(",");
            stringBuilder.append(passengers[i].getPassenger_type());
            temp[i] = stringBuilder.toString();
        }
        return String.join("_", temp);
    }


    //字符串转换unicode
    public static String stringToUnicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);  // 取出每一个字符
            unicode.append("\\u" + Integer.toHexString(c));// 转换为unicode
        }
        return unicode.toString();
    }

    //unicode 转字符串
    public static String unicodeToString(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            int data = Integer.parseInt(hex[i], 16);// 转换出每一个代码点
            string.append((char) data);// 追加成string
        }
        return string.toString();
    }

    public static String match(String source, String target) {
        int position_ypInfoDetail = source.indexOf(target);
        int position_start_ypInfoDetail = source.indexOf("'", position_ypInfoDetail + target.length()) + 1;
        int position_end_ypInfoDetail = source.indexOf("'", position_start_ypInfoDetail);
        String token_ypInfoDetail = String.valueOf(source.subSequence(position_start_ypInfoDetail, position_end_ypInfoDetail));
        return token_ypInfoDetail;
    }

    public static String match2(String source, String target) {
        int position_ypInfoDetail = source.indexOf(target, source.indexOf("ypInfoDetail"));
        int position_start_ypInfoDetail = source.indexOf("'", position_ypInfoDetail + target.length()) + 1;
        int position_end_ypInfoDetail = source.indexOf("'", position_start_ypInfoDetail);
        String token_ypInfoDetail = String.valueOf(source.subSequence(position_start_ypInfoDetail, position_end_ypInfoDetail));
        return token_ypInfoDetail;
    }

}
