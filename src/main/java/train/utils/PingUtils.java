package train.utils;

import java.net.InetAddress;
import java.util.GregorianCalendar;

/**
 * Created by xie on 17/9/15.
 */
public class PingUtils {

    public static int ping(String host) {
        try {
            InetAddress inet = InetAddress.getByName(host);
            long finish = 0;
            long start = new GregorianCalendar().getTimeInMillis();
            if (inet.isReachable(Constants.TIME_OUT)) {
                finish = new GregorianCalendar().getTimeInMillis();
                return (int) (finish - start);
            } else {
                return Constants.TIME_OUT;
            }
        } catch (Exception e) {
            return Constants.TIME_OUT;
        }
    }
}
