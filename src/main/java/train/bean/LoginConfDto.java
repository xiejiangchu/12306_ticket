package train.bean;

public class LoginConfDto extends BaseDto {


    /**
     * validateMessagesShowId : _validatorMessage
     * status : true
     * httpstatus : 200
     * data : {"isstudentDate":true,"is_login_passCode":"Y","is_sweep_login":"Y","psr_qr_code_result":"N","login_url":"resources/login.html","studentDate":["2019-06-01","2019-09-30","2019-12-01","2019-12-31","2019-01-01","2019-03-31"],"stu_control":30,"is_uam_login":"Y","is_login":"N","other_control":30}
     * messages : []
     * validateMessages : {}
     */
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean {
        /**
         * isstudentDate : true
         * is_login_passCode : Y
         * is_sweep_login : Y
         * psr_qr_code_result : N
         * login_url : resources/login.html
         * studentDate : ["2019-06-01","2019-09-30","2019-12-01","2019-12-31","2019-01-01","2019-03-31"]
         * stu_control : 30
         * is_uam_login : Y
         * is_login : N
         * other_control : 30
         */

        private boolean isstudentDate;
        private String is_login_passCode;
        private String is_sweep_login;
        private String psr_qr_code_result;
        private String login_url;
        private int stu_control;

        public boolean isIsstudentDate() {
            return isstudentDate;
        }

        public void setIsstudentDate(boolean isstudentDate) {
            this.isstudentDate = isstudentDate;
        }

        public String getIs_login_passCode() {
            return is_login_passCode;
        }

        public void setIs_login_passCode(String is_login_passCode) {
            this.is_login_passCode = is_login_passCode;
        }

        public String getIs_sweep_login() {
            return is_sweep_login;
        }

        public void setIs_sweep_login(String is_sweep_login) {
            this.is_sweep_login = is_sweep_login;
        }

        public String getPsr_qr_code_result() {
            return psr_qr_code_result;
        }

        public void setPsr_qr_code_result(String psr_qr_code_result) {
            this.psr_qr_code_result = psr_qr_code_result;
        }

        public String getLogin_url() {
            return login_url;
        }

        public void setLogin_url(String login_url) {
            this.login_url = login_url;
        }

        public int getStu_control() {
            return stu_control;
        }

        public void setStu_control(int stu_control) {
            this.stu_control = stu_control;
        }
    }

    public static class ValidateMessagesBean {
    }
}
