package train.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import train.service.PingService;
import train.utils.Constants;
import train.utils.PingUtils;

/**
 * Created by xie on 17/9/15.
 */
@Service
public class PingServiceImpl implements PingService {

    private final static Logger logger = LoggerFactory.getLogger(PingServiceImpl.class);

    @Override
    public int ping(String host) {
        if (null == host) {
            return Constants.TIME_OUT;
        }
        return PingUtils.ping(host);
    }
}
