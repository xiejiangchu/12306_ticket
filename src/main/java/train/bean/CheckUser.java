package train.bean;

/**
 * Created by xie on 17/9/19.
 */
public class CheckUser extends BaseDto {

    private CheckUserBean data;

    class CheckUserBean {
        private boolean flag;

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }
    }

    public CheckUserBean getData() {
        return data;
    }

    public void setData(CheckUserBean data) {
        this.data = data;
    }
}
