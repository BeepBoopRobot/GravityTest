import java.util.ArrayList;

class Physics {
    private static double G = 6.67408 * Math.pow(10, -11);

    static double calcForce(long m1, long m2, double distance) {
        double top = m1 * m2 * G;
        double bottom = Math.pow(distance, 2);
        return top / bottom;
    }

    static double getA(long mass, double distance) {
        double top = G * mass;
        double bottom = distance * distance;
        return top / bottom;
    }

    static double getResultant(ArrayList<Point> al) {
        return 0;
    }
}
