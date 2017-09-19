package train.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xie on 17/9/19.
 */
public class PassengerDataDto  implements Serializable{

    /**
     * isExist : true
     * exMsg :
     * two_isOpenClick : ["93","95","97","99"]
     * other_isOpenClick : ["91","93","98","99","95","97"]
     */

    private boolean isExist;
    private String exMsg;
    private List<String> two_isOpenClick;
    private List<String> other_isOpenClick;
    private List<Passenger> normal_passengers;

    public boolean isIsExist() {
        return isExist;
    }

    public void setIsExist(boolean isExist) {
        this.isExist = isExist;
    }

    public String getExMsg() {
        return exMsg;
    }

    public void setExMsg(String exMsg) {
        this.exMsg = exMsg;
    }

    public List<String> getTwo_isOpenClick() {
        return two_isOpenClick;
    }

    public void setTwo_isOpenClick(List<String> two_isOpenClick) {
        this.two_isOpenClick = two_isOpenClick;
    }

    public List<String> getOther_isOpenClick() {
        return other_isOpenClick;
    }

    public void setOther_isOpenClick(List<String> other_isOpenClick) {
        this.other_isOpenClick = other_isOpenClick;
    }

    public List<Passenger> getNormal_passengers() {
        return normal_passengers;
    }

    public void setNormal_passengers(List<Passenger> normal_passengers) {
        this.normal_passengers = normal_passengers;
    }
}
