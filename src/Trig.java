import java.util.Arrays;

class Trig {

    private static double[] sineTable = new double[65535];

    static void generateSin() {
        for(int i = 0; i< 65534; i++) {
           double a = Math.sin(((double)i/65535) * 2 * Math.PI);
           sineTable[i] = a;
        }
       // System.out.println(Arrays.toString(sineTable));
    }

    static double sin(short x) {return sineTable[x];}
}
