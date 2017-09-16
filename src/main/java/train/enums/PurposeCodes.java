package train.enums;

/**
 * Created by xie on 17/9/15.
 */
public enum PurposeCodes {
    学生("0X00"),成人("ADULT");

    PurposeCodes(String val) {
        this.val = val;
    }

    private String val;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
