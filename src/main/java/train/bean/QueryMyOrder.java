package train.bean;

/**
 * Created by xie on 17/9/17.
 */
public class QueryMyOrder extends BaseDto {


    private OrderDTO data;

    public OrderDTO getData() {
        return data;
    }

    public void setData(OrderDTO data) {
        this.data = data;
    }

}
