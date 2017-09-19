package train.bean;

/**
 * Created by xie on 17/9/19.
 */
public class GetPassengerDto extends BaseDto {


    private PassengerDataDto data;

    public PassengerDataDto getData() {
        return data;
    }

    public void setData(PassengerDataDto data) {
        this.data = data;
    }
}
