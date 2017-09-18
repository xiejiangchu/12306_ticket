package train.bean;

/**
 * Created by xie on 17/9/17.
 */
public class PassengerDTO {


    /**
     * passenger_name : 谢江初
     * passenger_id_type_code : 1
     * passenger_id_type_name : 二代身份证
     * passenger_id_no : 362201198910185014
     * total_times : 98
     */

    private String passenger_name;
    private String passenger_id_type_code;
    private String passenger_id_type_name;
    private String passenger_id_no;
    private String total_times;

    public String getPassenger_name() {
        return passenger_name;
    }

    public void setPassenger_name(String passenger_name) {
        this.passenger_name = passenger_name;
    }

    public String getPassenger_id_type_code() {
        return passenger_id_type_code;
    }

    public void setPassenger_id_type_code(String passenger_id_type_code) {
        this.passenger_id_type_code = passenger_id_type_code;
    }

    public String getPassenger_id_type_name() {
        return passenger_id_type_name;
    }

    public void setPassenger_id_type_name(String passenger_id_type_name) {
        this.passenger_id_type_name = passenger_id_type_name;
    }

    public String getPassenger_id_no() {
        return passenger_id_no;
    }

    public void setPassenger_id_no(String passenger_id_no) {
        this.passenger_id_no = passenger_id_no;
    }

    public String getTotal_times() {
        return total_times;
    }

    public void setTotal_times(String total_times) {
        this.total_times = total_times;
    }
}
