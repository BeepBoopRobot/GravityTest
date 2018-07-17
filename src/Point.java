import java.util.HashMap;

public class Point {
    private double dx;
    private double dy;
    private double posX;
    private long mass;

    long getMass() {
        return mass;
    }

    public void setMass(long mass) {
        this.mass = mass;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    private double posY;

    Point(double dx, double dy, int posX, int posY, long mass) {
        this.dx = dx;
        this.dy = dy;
        this.posX = posX;
        this.posY = posY;
        this.mass = mass;
    }

    void update(FrameRegulator fr) {
        double nextX = posX + (dx * fr.getFrameLength());
        if (nextX >= Main.getWindowWidth() || nextX <= 0) {
            nextX = posX;
            dx = -dx;
        }
        posX = nextX;
        double nextY = posY + (dy * fr.getFrameLength());
        if (nextY >= Main.getWindowHeight() || nextY <= 0) {
            nextY = posY;
            dy = -dy;
        }
        posY = nextY;
    }
}
