package train.service.impl;

import train.service.PingService;
import train.utils.PingUtils;
import org.springframework.stereotype.Service;
import rx.functions.Action1;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by xie on 17/9/15.
 */
@Service
public class PingServiceImpl implements PingService {

    @Override
    public int ping(String host) {
        return PingUtils.ping(host);
    }

    @Override
    public Map<String, Integer> ping(List<String> hosts) {
        Map<String, Integer> map = new TreeMap<>();
        rx.Observable.from(hosts).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                int time = PingUtils.ping(s);
                map.put(s, time);
            }
        });
        return map;
    }
}
