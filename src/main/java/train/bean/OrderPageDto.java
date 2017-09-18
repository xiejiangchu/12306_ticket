package train.bean;

import java.util.List;

/**
 * Created by xie on 17/9/18.
 */
public class OrderPageDto {



    private List<OrderDTO> OrderDTODataList;
    /**
     * order_total_number : 2
     * show_catering_button : true
     */

    private String order_total_number;
    private boolean show_catering_button;

    public List<OrderDTO> getOrderDTODataList() {
        return OrderDTODataList;
    }

    public void setOrderDTODataList(List<OrderDTO> orderDTODataList) {
        OrderDTODataList = orderDTODataList;
    }

    public String getOrder_total_number() {
        return order_total_number;
    }

    public void setOrder_total_number(String order_total_number) {
        this.order_total_number = order_total_number;
    }

    public boolean isShow_catering_button() {
        return show_catering_button;
    }

    public void setShow_catering_button(boolean show_catering_button) {
        this.show_catering_button = show_catering_button;
    }
}
