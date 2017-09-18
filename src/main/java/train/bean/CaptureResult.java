package train.bean;

import java.io.Serializable;

/**
 * Created by xie on 17/9/17.
 */
public class CaptureResult implements Serializable{


    /**
     * result_message : 登录成功
     * result_code : 0
     * uamtk : 5p5dsjV2NzYD0IsqCsVjCyxTkdAMVwRMsV-98XGGPhcPaNfpub5150
     */

    private String result_message;
    private int result_code;
    private String uamtk;

    public String getResult_message() {
        return result_message;
    }

    public void setResult_message(String result_message) {
        this.result_message = result_message;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public String getUamtk() {
        return uamtk;
    }

    public void setUamtk(String uamtk) {
        this.uamtk = uamtk;
    }
}
