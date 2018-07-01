public class Point {
    private int dx;
    private int dy;
    private int posX;

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    private int posY;

    Point(int dx, int dy, int posX, int posY) {
        this.dx = dx;
        this.dy = dy;
        this.posX = posX;
        this.posY = posY;
    }

    void update() {
        int nextX = posX + dx;
        if(nextX >= Main.getWindowWidth() || nextX <= 0) {
            nextX = posX;
            dx = -dx;
        }
        posX = nextX;
        int nextY = posY + dy;
        if(nextY >= Main.getWindowHeight() || nextY <= 0) {
            nextY = posY;
            dy = -dy;
        }
        posY = nextY;
    }
}
