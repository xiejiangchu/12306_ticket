package train.service;

import javafx.collections.ObservableList;
import train.bean.PingHost;

import java.util.List;

/**
 * Created by xie on 17/9/15.
 */
public interface PingService {

    void initTreeMap();

    ObservableList<PingHost> getHost();

    int ping(String host);

    int checkStatetion(String host);

    String getCurrentHost();

    List<String> getAllAvailableHost();
}
