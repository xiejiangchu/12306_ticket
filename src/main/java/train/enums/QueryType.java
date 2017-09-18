package train.enums;

/**
 * Created by xie on 17/9/18.
 */
public enum QueryType {
    订票日期(1),乘车日期(2);

    private int val;

    QueryType(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }
}
