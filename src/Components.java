public class Components {
    static double getXComponent(int x1, int x2, double distance) {
        return Math.abs(x2 - x1) / distance;
    }

    static double getYComponent(int y1, int y2, double distance) {
        return Math.abs(y2 - y1) / distance;
    }
}
