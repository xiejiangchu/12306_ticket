package train.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xie on 17/9/17.
 */
public class AllCityDto extends BaseDto implements Serializable {

    private List<CityDto> data;

    public List<CityDto> getData() {
        return data;
    }

    public void setData(List<CityDto> data) {
        this.data = data;
    }
}
