package train.bean;

/**
 * Created by xie on 17/9/15.
 */
public class Station {

    private String abbr;
    private String abbrL;
    private String name;
    private String code;
    private String py;
    private int index;

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
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

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getAbbrL() {
        return abbrL;
    }

    public void setAbbrL(String abbrL) {
        this.abbrL = abbrL;
    }

    @Override
    public String toString() {
        return "Station{" +
                "abbr='" + abbr + '\'' +
                ", abbrL='" + abbrL + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", py='" + py + '\'' +
                ", index=" + index +
                '}';
    }
}
