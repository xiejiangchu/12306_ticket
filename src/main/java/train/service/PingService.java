package train.service;

import java.util.List;
import java.util.Map;

/**
 * Created by xie on 17/9/15.
 */
public interface PingService {

    int ping(String host);

    Map<String, Integer> ping(List<String> hosts);
}
