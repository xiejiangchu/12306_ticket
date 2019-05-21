package train.bean;

public class LoginDto {


    /**
     * result_message : 登录成功
     * uamtk : aI-ANQc8kEojKgJ09rCuq7ph8_LvlS_Mozx1x0
     * result_code : 0
     */

    private String result_message;
    private String uamtk;
    private int result_code;

    public String getResult_message() {
        return result_message;
    }

    public void setResult_message(String result_message) {
        this.result_message = result_message;
    }

    public String getUamtk() {
        return uamtk;
    }

    public void setUamtk(String uamtk) {
        this.uamtk = uamtk;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }
}
