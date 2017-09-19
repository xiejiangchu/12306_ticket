package train.enums;

/**
 * Created by xie on 17/9/19.
 */
public enum TourFlag {
    单程("dc"),往返("wf");

    private String val;

    TourFlag(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
