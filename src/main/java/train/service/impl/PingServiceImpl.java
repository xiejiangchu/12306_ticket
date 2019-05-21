package train.service.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import train.bean.PingHost;
import train.config.CdnConfig;
import train.config.RetrofitConfig;
import train.service.PingService;
import train.service.TrainService;
import train.utils.Constants;
import train.utils.PingUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by xie on 17/9/15.
 */
@Service
public class PingServiceImpl implements PingService {

    private final static Logger logger = LoggerFactory.getLogger(PingServiceImpl.class);

    private static TreeMap<Integer, PingHost> treeMap = new TreeMap<>();
    private static List<PingHost> pingHostList = new ArrayList<>();
    private static ObservableList<PingHost> list = FXCollections.observableList(pingHostList);

    @Override
    public void initTreeMap() {
        treeMap.clear();
        pingHostList.clear();
        list.clear();
        rx.Observable.from(CdnConfig.getHosts())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.immediate())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        int result = checkStatetion(s);
                        PingHost pingHost = new PingHost();
                        pingHost.setHost(s);
                        pingHost.setPing(result);
                        list.add(pingHost);
                        treeMap.put(result, pingHost);
                    }
                });
    }

    @Override
    public ObservableList<PingHost> getHost() {
        return list;
    }

    @Override
    public String getCurrentHost() {
        if (null == treeMap.firstEntry().getValue().getHost()) {
            return Constants.DOMAIN;
        }
        return treeMap.firstEntry().getValue().getHost();
    }

    @Override
    public List<String> getAllAvailableHost() {
        return null;
    }

    @Override
    public int ping(String host) {
        if (null == host) {
            return Constants.TIME_OUT;
        }
        return PingUtils.ping(host);
    }

    @Override
    public int checkStatetion(String host) {
        Retrofit retrofit = RetrofitConfig.getRetrofit(String.format("http://%s/", host));
        Response<String> response = null;
        try {
            response = retrofit.create(TrainService.class).stationName(null).execute();
            if (response != null && response.isSuccessful()) {
                int time = (int) (response.raw().receivedResponseAtMillis() - response.raw().sentRequestAtMillis());
                logger.info(String.format("CDN：%s 可用 -- %s ms", host, time));
                return time;
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return Constants.TIME_OUT;
    }
}
