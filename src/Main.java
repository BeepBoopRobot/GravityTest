import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Random;

//Idea: Add a "stick" function, so that when two  particles collide with enough speed, they stick together and share velocity vectors and collision. How do rotation? (intrinsic?)
//ToDo: sort out scales and trig functions
//ToDo: Remove the line drawing section and separate out the calculations

public class Main {
    public static void main(String[] args) {
        Trig.generateSin();
        System.out.println(Math.sin(Math.PI));
        System.out.println(Trig.sin(Short.MAX_VALUE));
        new JFXPanel();
        Platform.runLater(Main::launch);
    }

    private static double frameRate = 60;
    private static int points = 5;

    private static int windowWidth = 1000;
    private static int windowHeight = 1000;
    private static int maxLength = (int) Math.hypot(windowWidth, windowHeight);
    private static boolean showLines = false, showDist = false, showVector = true;

    static int getWindowWidth() {
        return windowWidth;
    }

    static int getWindowHeight() {
        return windowHeight;
    }

    private static void launch() {
        Stage stage = new Stage();
        stage.setWidth(windowWidth);
        stage.setHeight(windowHeight);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);


        Group group = new Group();
        Scene scene = new Scene(group);
        stage.setScene(scene);
        stage.show();

        Canvas canvas = new Canvas();
        scene.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ESCAPE)) {
                System.exit(0);
            } else if (ke.getCode().equals(KeyCode.W)) {
                showLines = !showLines;
            } else if (ke.getCode().equals(KeyCode.S) && showLines) {
                showDist = !showDist;
            } else if (ke.getCode().equals(KeyCode.A)) {
                showVector = !showVector;
            }
        });

        canvas.setWidth(windowWidth);
        canvas.setHeight(windowHeight);
        group.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.setFont(new Font("Comic Sans MS", 8));
        Random rnd = new Random();
        ArrayList<Point> al = new ArrayList<>();

        for (int i = 0; i < points; i++) {
            al.add(new Point(rnd.nextDouble() * 0.5 - 0.5,
                    rnd.nextDouble() * 0.5 - 0.5,
                    (int) Math.ceil(Math.random() * windowWidth),
                    (int) Math.ceil(Math.random() * windowHeight),
                    (long) Math.ceil(Math.random() * 1_000_000 + 500_000)));
        }


        AnimationTimer at = new AnimationTimer() {
            private long last = 0;

            @Override
            public void handle(long now) {
                double fps = 1 / frameRate;
                if (now - last >= 16_000_000) {
                    render(al, gc);
                    calculate(al, gc);
                    last = now;
                }
            }
        };
        at.start();
        scene.setOnMousePressed(me -> at.stop());
        scene.setOnMouseReleased(me -> at.start());
    }

    private static void render(ArrayList<Point> al, GraphicsContext gc) {
        gc.clearRect(0, 0, windowWidth, windowHeight);
        for (Point p : al) {
            gc.fillOval(p.getPosX() - 10, p.getPosY() - 10, 20, 20);
            gc.fillText(String.valueOf(time), 0, 40);
            // gc.fillText(String.valueOf(Math.hypot(p.getDy(), p.getDx())), p.getPosX() + 6, p.getPosY() + 6);
            // gc.fillText(String.valueOf(al.indexOf(p)), p.getPosX() + 6, p.getPosY() + 15);
            if (showVector)
                gc.strokeLine(p.getPosX(), p.getPosY(), p.getPosX() + p.getDx() * 1, p.getPosY() + p.getDy() * 1);
        }
        for (Point p : al) p.update();
    }

    private static double time;

    private static void bonk(Point a, Point b) {
        long m1 = a.getMass();
        long m2 = b.getMass();

        //calculate y velocities
        double v_y1 = a.getDy();
        double v_y2 = b.getDy();
        b.setDy(((float) (2 * m1) / (m1 + m2)) * v_y1 - ((float) (m1 - m2) / (m1 + m2)) * v_y2);
        a.setDy(((float) (m1 - m2) / (m1 + m2)) * v_y1 - ((float) (2 * m2) / (m1 + m2)) * v_y2);

        //calculate x velocities
        double v_x1 = a.getDx();
        double v_x2 = b.getDx();
        b.setDx(((float) (2 * m1) / (m1 + m2)) * v_x1 - ((float) (m1 - m2) / (m1 + m2)) * v_x2);
        a.setDx(((float) (m1 - m2) / (m1 + m2)) * v_x1 - ((float) (2 * m2) / (m1 + m2)) * v_x2);

    }

    private static void calculate(ArrayList<Point> al, GraphicsContext gc) {
        for (int i = 0; i < 1; i++) {
            double frameLength = (1 / frameRate);
            for (Point p : al) {
                double x1 = p.getPosX();
                double y1 = p.getPosY();

                for (Point a : al) {
                    double x2 = a.getPosX();
                    double y2 = a.getPosY();
                    double distance = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
                    /*Collision detection will need to:
                 - Handle realistic momentum transfers on each collision (
                 - Not allow points to be stuck into each other
                 - Be able to support the coalition feature*/

                    if (distance <= 20) {
                       // bonk(a,p);
                    } else {
                        double A = getA(a.getMass(), distance);
                        if (x1 > x2) {
                            double newDx = p.getDx() - (A * frameLength * (Math.abs(x2 - x1) / distance));
                            p.setDx(newDx);
                        } else {
                            double newDx = p.getDx() + (A * frameLength * (Math.abs(x2 - x1) / distance));
                            p.setDx(newDx);

                        }

                        if (y1 > y2) {
                            double newDy = p.getDy() - (A * frameLength * (Math.abs(y2 - y1) / distance));
                            p.setDy(newDy);
                        } else {
                            double newDy = p.getDy() + (A * frameLength * (Math.abs(y2 - y1) / distance));
                            p.setDy(newDy);
                        }
                    }

                    if (showLines) {
                        int redRat = (int) ((1 - (distance / maxLength)) * 255);
                        int blueRat = (int) ((distance / maxLength) * 255);
                        gc.setStroke(Color.rgb(redRat, 0, blueRat));
                        gc.strokeLine(x1, y1, x2, y2);
                        if (showDist) {
                            gc.fillText(String.valueOf(distance),
                                    x1 + ((x2 - x1) / 2),
                                    y1 + ((y2 - y1) / 2));
                        }
                    }
                }
            }
        }
    }

    private static double G = 6.67408e-4;

    private static double getA(long mass, double distance) {
        double top = G * mass;
        double bottom = distance * distance;
        return top / bottom;
    }


}
