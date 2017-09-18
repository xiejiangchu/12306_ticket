package train.bean;

/**
 * Created by xie on 17/9/17.
 */
public class QueryMyOrder extends BaseDto {

    private OrderPageDto data;

    public OrderPageDto getData() {
        return data;
    }

    public void setData(OrderPageDto data) {
        this.data = data;
    }
}
