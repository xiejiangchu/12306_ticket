package train.bean;

/**
 * Created by xie on 17/9/17.
 */
public class TrainBase {


    /**
     * station_train_code : D1(北京-沈阳)
     * train_no : 24000000D10R
     */

    private String station_train_code;
    private String train_no;

    public String getStation_train_code() {
        return station_train_code;
    }

    public void setStation_train_code(String station_train_code) {
        this.station_train_code = station_train_code;
    }

    public String getTrain_no() {
        return train_no;
    }

    public void setTrain_no(String train_no) {
        this.train_no = train_no;
    }
}
