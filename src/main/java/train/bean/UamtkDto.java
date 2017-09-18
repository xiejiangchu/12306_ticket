package train.bean;

import java.io.Serializable;

/**
 * Created by xie on 17/9/17.
 */
public class UamtkDto implements Serializable{


    /**
     * result_message : 验证通过
     * result_code : 0
     * apptk : null
     * newapptk : FtQXii-HQVbnhlaPA79q9xMF9fLL0-DN64x1x0
     */

    private String result_message;
    private int result_code;
    private Object apptk;
    private String newapptk;

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

    public Object getApptk() {
        return apptk;
    }

    public void setApptk(Object apptk) {
        this.apptk = apptk;
    }

    public String getNewapptk() {
        return newapptk;
    }

    public void setNewapptk(String newapptk) {
        this.newapptk = newapptk;
    }
}
