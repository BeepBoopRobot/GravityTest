class Physics {
    static double calcForce(long m1, long m2, double distance) {
        double G = 6.67408 * Math.pow(10,-11);
        double top = m1 * m2 * G;
        double bottom = Math.pow(distance, 2);

        return top/bottom;
    }
}
