package train.bean;

import java.io.Serializable;

/**
 * Created by xie on 17/9/17.
 */
public class CityDto implements Serializable {


    /**
     * chineseName : 哈尔滨
     * allPin :
     * simplePin : heb
     * stationTelecode : 0034
     */

    private String chineseName;
    private String allPin;
    private String simplePin;
    private String stationTelecode;

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getAllPin() {
        return allPin;
    }

    public void setAllPin(String allPin) {
        this.allPin = allPin;
    }

    public String getSimplePin() {
        return simplePin;
    }

    public void setSimplePin(String simplePin) {
        this.simplePin = simplePin;
    }

    public String getStationTelecode() {
        return stationTelecode;
    }

    public void setStationTelecode(String stationTelecode) {
        this.stationTelecode = stationTelecode;
    }
}
