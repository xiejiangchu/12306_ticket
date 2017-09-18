package train.enums;

/**
 * Created by xie on 17/9/18.
 */
public enum OrderScopeType {
    所有("my_order"),可改签("my_resign"),可变更到站("my_cs_resign"),可退票("my_refund");

    private String val;

    OrderScopeType(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
