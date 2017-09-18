package train.bean;

import java.util.List;

/**
 * Created by xie on 17/9/17.
 */
public class OrderDTO {


    /**
     * sequence_no : E974495554
     * order_date : 2017-09-09 14:30:45
     * ticket_totalnum : 2
     * ticket_price_all : 95600.0
     * cancel_flag : N
     * resign_flag : 1
     * return_flag : Y
     * print_eticket_flag : Y
     * pay_flag : N
     * pay_resign_flag : N
     * confirm_flag : N
     */

    private String sequence_no;
    private String order_date;
    private int ticket_totalnum;
    private double ticket_price_all;
    private String cancel_flag;
    private String resign_flag;
    private String return_flag;
    private String print_eticket_flag;
    private String pay_flag;
    private String pay_resign_flag;
    private String confirm_flag;

    private List<TicketDto> tickets;

    public List<TicketDto> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketDto> tickets) {
        this.tickets = tickets;
    }

    public String getSequence_no() {
        return sequence_no;
    }

    public void setSequence_no(String sequence_no) {
        this.sequence_no = sequence_no;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public int getTicket_totalnum() {
        return ticket_totalnum;
    }

    public void setTicket_totalnum(int ticket_totalnum) {
        this.ticket_totalnum = ticket_totalnum;
    }

    public double getTicket_price_all() {
        return ticket_price_all;
    }

    public void setTicket_price_all(double ticket_price_all) {
        this.ticket_price_all = ticket_price_all;
    }

    public String getCancel_flag() {
        return cancel_flag;
    }

    public void setCancel_flag(String cancel_flag) {
        this.cancel_flag = cancel_flag;
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

    public String getPrint_eticket_flag() {
        return print_eticket_flag;
    }

    public void setPrint_eticket_flag(String print_eticket_flag) {
        this.print_eticket_flag = print_eticket_flag;
    }

    public String getPay_flag() {
        return pay_flag;
    }

    public void setPay_flag(String pay_flag) {
        this.pay_flag = pay_flag;
    }

    public String getPay_resign_flag() {
        return pay_resign_flag;
    }

    public void setPay_resign_flag(String pay_resign_flag) {
        this.pay_resign_flag = pay_resign_flag;
    }

    public String getConfirm_flag() {
        return confirm_flag;
    }

    public void setConfirm_flag(String confirm_flag) {
        this.confirm_flag = confirm_flag;
    }
}
