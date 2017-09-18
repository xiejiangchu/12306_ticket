package train.bean;

/**
 * Created by xie on 17/9/17.
 */
public class TicketDto {


    /**
     * ticket_no : E974495554104001C
     * sequence_no : E974495554
     * batch_no : 1
     * train_date : 2017-10-08 00:00:00
     * coach_no : 04
     * coach_name : 04
     * seat_no : 001C
     * seat_name : 01C号
     * seat_flag : 0
     * seat_type_code : O
     * seat_type_name : 二等座
     * ticket_type_code : 1
     * ticket_type_name : 成人票
     * reserve_time : 2017-09-09 14:30:45
     * limit_time : 2017-09-09 14:30:45
     * lose_time : 2017-09-09 15:00:45
     * pay_limit_time : 2017-09-09 15:00:45
     * ticket_price : 47800.0
     * print_eticket_flag : Y
     * resign_flag : 1
     * return_flag : Y
     * confirm_flag : N
     * pay_mode_code : N
     * ticket_status_code : a
     * ticket_status_name : 已支付
     * cancel_flag : N
     * amount_char : 0
     * trade_mode : E
     * start_train_date_page : 2017-10-08 13:40
     * str_ticket_price_page : 478.0
     * come_go_traveller_ticket_page : N
     * return_deliver_flag : N
     * deliver_fee_char :
     * is_need_alert_flag : false
     * is_deliver : N
     * dynamicProp :
     * fee_char :
     * insure_query_no :
     */

    private String ticket_no;
    private String sequence_no;
    private String batch_no;
    private String train_date;
    private String coach_no;
    private String coach_name;
    private String seat_no;
    private String seat_name;
    private String seat_flag;
    private String seat_type_code;
    private String seat_type_name;
    private String ticket_type_code;
    private String ticket_type_name;
    private String reserve_time;
    private String limit_time;
    private String lose_time;
    private String pay_limit_time;
    private double ticket_price;
    private String print_eticket_flag;
    private String resign_flag;
    private String return_flag;
    private String confirm_flag;
    private String pay_mode_code;
    private String ticket_status_code;
    private String ticket_status_name;
    private String cancel_flag;
    private int amount_char;
    private String trade_mode;
    private String start_train_date_page;
    private String str_ticket_price_page;
    private String come_go_traveller_ticket_page;
    private String return_deliver_flag;
    private String deliver_fee_char;
    private boolean is_need_alert_flag;
    private String is_deliver;
    private String dynamicProp;
    private String fee_char;
    private String insure_query_no;

    private StationTrainDTO stationTrainDTO;
    private PassengerDTO passengerDTO;

    public StationTrainDTO getStationTrainDTO() {
        return stationTrainDTO;
    }

    public void setStationTrainDTO(StationTrainDTO stationTrainDTO) {
        this.stationTrainDTO = stationTrainDTO;
    }

    public PassengerDTO getPassengerDTO() {
        return passengerDTO;
    }

    public void setPassengerDTO(PassengerDTO passengerDTO) {
        this.passengerDTO = passengerDTO;
    }

    public String getTicket_no() {
        return ticket_no;
    }

    public void setTicket_no(String ticket_no) {
        this.ticket_no = ticket_no;
    }

    public String getSequence_no() {
        return sequence_no;
    }

    public void setSequence_no(String sequence_no) {
        this.sequence_no = sequence_no;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getTrain_date() {
        return train_date;
    }

    public void setTrain_date(String train_date) {
        this.train_date = train_date;
    }

    public String getCoach_no() {
        return coach_no;
    }

    public void setCoach_no(String coach_no) {
        this.coach_no = coach_no;
    }

    public String getCoach_name() {
        return coach_name;
    }

    public void setCoach_name(String coach_name) {
        this.coach_name = coach_name;
    }

    public String getSeat_no() {
        return seat_no;
    }

    public void setSeat_no(String seat_no) {
        this.seat_no = seat_no;
    }

    public String getSeat_name() {
        return seat_name;
    }

    public void setSeat_name(String seat_name) {
        this.seat_name = seat_name;
    }

    public String getSeat_flag() {
        return seat_flag;
    }

    public void setSeat_flag(String seat_flag) {
        this.seat_flag = seat_flag;
    }

    public String getSeat_type_code() {
        return seat_type_code;
    }

    public void setSeat_type_code(String seat_type_code) {
        this.seat_type_code = seat_type_code;
    }

    public String getSeat_type_name() {
        return seat_type_name;
    }

    public void setSeat_type_name(String seat_type_name) {
        this.seat_type_name = seat_type_name;
    }

    public String getTicket_type_code() {
        return ticket_type_code;
    }

    public void setTicket_type_code(String ticket_type_code) {
        this.ticket_type_code = ticket_type_code;
    }

    public String getTicket_type_name() {
        return ticket_type_name;
    }

    public void setTicket_type_name(String ticket_type_name) {
        this.ticket_type_name = ticket_type_name;
    }

    public String getReserve_time() {
        return reserve_time;
    }

    public void setReserve_time(String reserve_time) {
        this.reserve_time = reserve_time;
    }

    public String getLimit_time() {
        return limit_time;
    }

    public void setLimit_time(String limit_time) {
        this.limit_time = limit_time;
    }

    public String getLose_time() {
        return lose_time;
    }

    public void setLose_time(String lose_time) {
        this.lose_time = lose_time;
    }

    public String getPay_limit_time() {
        return pay_limit_time;
    }

    public void setPay_limit_time(String pay_limit_time) {
        this.pay_limit_time = pay_limit_time;
    }

    public double getTicket_price() {
        return ticket_price;
    }

    public void setTicket_price(double ticket_price) {
        this.ticket_price = ticket_price;
    }

    public String getPrint_eticket_flag() {
        return print_eticket_flag;
    }

    public void setPrint_eticket_flag(String print_eticket_flag) {
        this.print_eticket_flag = print_eticket_flag;
    }

    public String getResign_flag() {
        return resign_flag;
    }

    public void setResign_flag(String resign_flag) {
        this.resign_flag = resign_flag;
    }

    public String getReturn_flag() {
        return return_flag;
    }

    public void setReturn_flag(String return_flag) {
        this.return_flag = return_flag;
    }

    public String getConfirm_flag() {
        return confirm_flag;
    }

    public void setConfirm_flag(String confirm_flag) {
        this.confirm_flag = confirm_flag;
    }

    public String getPay_mode_code() {
        return pay_mode_code;
    }

    public void setPay_mode_code(String pay_mode_code) {
        this.pay_mode_code = pay_mode_code;
    }

    public String getTicket_status_code() {
        return ticket_status_code;
    }

    public void setTicket_status_code(String ticket_status_code) {
        this.ticket_status_code = ticket_status_code;
    }

    public String getTicket_status_name() {
        return ticket_status_name;
    }

    public void setTicket_status_name(String ticket_status_name) {
        this.ticket_status_name = ticket_status_name;
    }

    public String getCancel_flag() {
        return cancel_flag;
    }

    public void setCancel_flag(String cancel_flag) {
        this.cancel_flag = cancel_flag;
    }

    public int getAmount_char() {
        return amount_char;
    }

    public void setAmount_char(int amount_char) {
        this.amount_char = amount_char;
    }

    public String getTrade_mode() {
        return trade_mode;
    }

    public void setTrade_mode(String trade_mode) {
        this.trade_mode = trade_mode;
    }

    public String getStart_train_date_page() {
        return start_train_date_page;
    }

    public void setStart_train_date_page(String start_train_date_page) {
        this.start_train_date_page = start_train_date_page;
    }

    public String getStr_ticket_price_page() {
        return str_ticket_price_page;
    }

    public void setStr_ticket_price_page(String str_ticket_price_page) {
        this.str_ticket_price_page = str_ticket_price_page;
    }

    public String getCome_go_traveller_ticket_page() {
        return come_go_traveller_ticket_page;
    }

    public void setCome_go_traveller_ticket_page(String come_go_traveller_ticket_page) {
        this.come_go_traveller_ticket_page = come_go_traveller_ticket_page;
    }

    public String getReturn_deliver_flag() {
        return return_deliver_flag;
    }

    public void setReturn_deliver_flag(String return_deliver_flag) {
        this.return_deliver_flag = return_deliver_flag;
    }

    public String getDeliver_fee_char() {
        return deliver_fee_char;
    }

    public void setDeliver_fee_char(String deliver_fee_char) {
        this.deliver_fee_char = deliver_fee_char;
    }

    public boolean isIs_need_alert_flag() {
        return is_need_alert_flag;
    }

    public void setIs_need_alert_flag(boolean is_need_alert_flag) {
        this.is_need_alert_flag = is_need_alert_flag;
    }

    public String getIs_deliver() {
        return is_deliver;
    }

    public void setIs_deliver(String is_deliver) {
        this.is_deliver = is_deliver;
    }

    public String getDynamicProp() {
        return dynamicProp;
    }

    public void setDynamicProp(String dynamicProp) {
        this.dynamicProp = dynamicProp;
    }

    public String getFee_char() {
        return fee_char;
    }

    public void setFee_char(String fee_char) {
        this.fee_char = fee_char;
    }

    public String getInsure_query_no() {
        return insure_query_no;
    }

    public void setInsure_query_no(String insure_query_no) {
        this.insure_query_no = insure_query_no;
    }
}
