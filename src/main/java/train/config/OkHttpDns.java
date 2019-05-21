package train.config;

import okhttp3.Dns;
import org.slf4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class OkHttpDns implements Dns {
    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(OkHttpUtils.class);
    private static OkHttpDns instance = null;

    private OkHttpDns() {
    }

    public static OkHttpDns getInstance() {
        if (instance == null) {
            synchronized (OkHttpDns.class) {
                instance = new OkHttpDns();
            }
        }
        return instance;
    }

    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        //通过异步解析接口获取ip
        return Dns.SYSTEM.lookup(hostname);
    }
}
