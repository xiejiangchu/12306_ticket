package train.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xie on 17/9/16.
 */
public class TrainType {

    private String name;
    private String code;

    public TrainType(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static List<TrainType> getAllType() {
        return new ArrayList<TrainType>() {
            {
                add(new TrainType("高速动车", "G"));
                add(new TrainType("快速", "K"));
                add(new TrainType("空调特快", "T"));
                add(new TrainType("动车组", "D"));
                add(new TrainType("直达特快", "Z"));
                add(new TrainType("其他", "Q"));
            }
        };
    }
}
