package train.enums;

/**
 * Created by xie on 17/9/16.
 */
public enum Seat {
    高级软卧("gr"),
    占位1("zw1"),
    软卧("rw"),
    占位2("zw2"),
    占位3("zw3"),
    无座("wz"),
    占位4("zw4"),
    硬卧("yw"),
    硬座("yz"),
    二等座("ze"),
    一等座("zy"),
    商务座("swz"),
    动卧("dw"),
    软座("rz"),
    其他("qt");

    private String val;

    Seat(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}