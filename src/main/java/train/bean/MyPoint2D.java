package train.bean;

/**
 * Created by xie on 17/9/16.
 */
public class MyPoint2D {
    private int x;
    private int y;

    public MyPoint2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return x + "," + (y - 30);
    }
}
