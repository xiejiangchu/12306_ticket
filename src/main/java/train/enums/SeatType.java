package train.enums;

/**
 * Created by xie on 17/9/15.
 */
public enum SeatType {
    商务座("A9"), 一等座("M"), 二等座("O");
    private String val;

    SeatType(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
