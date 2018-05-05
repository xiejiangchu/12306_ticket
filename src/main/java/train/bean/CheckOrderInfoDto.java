package train.bean;

public class CheckOrderInfoDto extends BaseDto {

    /**
     * validateMessagesShowId : _validatorMessage
     * status : true
     * httpstatus : 200
     * data : {"ifShowPassCode":"N","canChooseBeds":"N","canChooseSeats":"Y","choose_Seats":"O","isCanChooseMid":"N","ifShowPassCodeTime":"3617","submitStatus":true,"smokeStr":""}
     * messages : []
     * validateMessages : {}
     */
    private DataBean data;

    public static class DataBean {
        /**
         * ifShowPassCode : N
         * canChooseBeds : N
         * canChooseSeats : Y
         * choose_Seats : O
         * isCanChooseMid : N
         * ifShowPassCodeTime : 3617
         * submitStatus : true
         * smokeStr :
         */

        private String ifShowPassCode;
        private String canChooseBeds;
        private String canChooseSeats;
        private String choose_Seats;
        private String isCanChooseMid;
        private String ifShowPassCodeTime;

        private String isCheckOrderInfo;

        public String getIsCheckOrderInfo() {
            return isCheckOrderInfo;
        }

        public void setIsCheckOrderInfo(String isCheckOrderInfo) {
            this.isCheckOrderInfo = isCheckOrderInfo;
        }

        /**
         * submitStatus : true
         * smokeStr :
         */



        private boolean submitStatus;
        private String smokeStr;


        public String getIfShowPassCode() {
            return ifShowPassCode;
        }

        public void setIfShowPassCode(String ifShowPassCode) {
            this.ifShowPassCode = ifShowPassCode;
        }

        public String getCanChooseBeds() {
            return canChooseBeds;
        }

        public void setCanChooseBeds(String canChooseBeds) {
            this.canChooseBeds = canChooseBeds;
        }

        public String getCanChooseSeats() {
            return canChooseSeats;
        }

        public void setCanChooseSeats(String canChooseSeats) {
            this.canChooseSeats = canChooseSeats;
        }

        public String getChoose_Seats() {
            return choose_Seats;
        }

        public void setChoose_Seats(String choose_Seats) {
            this.choose_Seats = choose_Seats;
        }

        public String getIsCanChooseMid() {
            return isCanChooseMid;
        }

        public void setIsCanChooseMid(String isCanChooseMid) {
            this.isCanChooseMid = isCanChooseMid;
        }

        public String getIfShowPassCodeTime() {
            return ifShowPassCodeTime;
        }

        public void setIfShowPassCodeTime(String ifShowPassCodeTime) {
            this.ifShowPassCodeTime = ifShowPassCodeTime;
        }

        public boolean isSubmitStatus() {
            return submitStatus;
        }

        public void setSubmitStatus(boolean submitStatus) {
            this.submitStatus = submitStatus;
        }

        public String getSmokeStr() {
            return smokeStr;
        }

        public void setSmokeStr(String smokeStr) {
            this.smokeStr = smokeStr;
        }
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }
}
