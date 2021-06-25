package nl.ritogames.shared.dto.gameobject;

/**
 * A 2D Vector that is used to store the position of objects in the game.
 */
public class ASDVector {
    private int x;
    private int y;

    public ASDVector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public ASDVector() {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ASDVector that = (ASDVector) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        int sum = x + y;
        return sum * (sum + 1) / 2 + x;
    }

    @Override
    public String toString() {
        return "ASDVector{" +
            "X=" + x +
            ", Y=" + y +
            '}';
    }
}
