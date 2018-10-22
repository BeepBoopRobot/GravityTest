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

/*
soeirierjoew#r
kwe
prk#[ewkrpwke
rkwe#[r#
*/
public class Main {
    public static void main(String[] args) {
        Trig.generateSin();
        System.out.println(Math.sin(Math.PI));
        System.out.println(Trig.sin(Short.MAX_VALUE));
        new JFXPanel();
        Platform.runLater(Main::launch);
    }

    private static double frameRate = 60;

    private static int windowWidth = 1000;
    private static int windowHeight = 1000;
    private static int maxLength = (int) Math.hypot(windowWidth, windowHeight);
    private static boolean showLines = false, showDist = false, showVector = false;

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

        int points = 200;
        for (int i = 0; i < points; i++) {
            al.add(new Point(rnd.nextDouble() * 0.5 - 0.5,
                    rnd.nextDouble() * 0.5 - 0.5,
                    (int) Math.ceil(Math.random() * windowWidth),
                    (int) Math.ceil(Math.random() * windowHeight),
                    (long) Math.ceil(Math.random() * 100_000_000 + 50_000_000)));
        }


        final int[] stop = {0};
        AnimationTimer at = new AnimationTimer() {
            private long last = 0;

            @Override
            public void handle(long now) {
                double fps = 1 / frameRate;
                if (now - last >= (fps * Math.pow(10, 6)) && stop[0] <50000) {
                    render(al, gc);
                    last = now;
                    stop[0]++;
                }
            }
        };
        at.start();
        scene.setOnMousePressed(me -> at.stop());
        scene.setOnMouseReleased(me -> at.start());
    }

    private static void render(ArrayList<Point> al, GraphicsContext gc) {
        gc.clearRect(0, 0, windowWidth, windowHeight);
        drawLines(al, gc);
        for (Point p : al) {
            gc.fillOval(p.getPosX() - 10, p.getPosY() - 10, 20, 20);
            // gc.fillText(String.valueOf(Math.hypot(p.getDy(), p.getDx())), p.getPosX() + 6, p.getPosY() + 6);
            // gc.fillText(String.valueOf(al.indexOf(p)), p.getPosX() + 6, p.getPosY() + 15);
            if (showVector)
                gc.strokeLine(p.getPosX(), p.getPosY(), p.getPosX() + p.getDx() * 50, p.getPosY() + p.getDy() * 50);
        }
        for (Point p : al) p.update();
    }

    private static void drawLines(ArrayList<Point> al, GraphicsContext gc) {
        double frameLength = 1 / frameRate;
        for (Point p : al) {
            double x1 = p.getPosX();
            double y1 = p.getPosY();

            for (Point a : al) {
                double x2 = a.getPosX();
                double y2 = a.getPosY();
                double distance = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

                if (distance != 0 && distance > 20) {
                    double A = getA(a.getMass(), distance);
                    if (x1 > x2) {
                        double newDx = p.getDx() - (A * frameLength * 1_000_000 * (Math.abs(x2 - x1) / distance));
                        p.setDx(newDx);
                    } else {
                        double newDx = p.getDx() + (A * frameLength * 1_000_000 * (Math.abs(x2 - x1) / distance));
                        p.setDx(newDx);

                    }

                    if (y1 > y2) {
                        double newDy = p.getDy() - (A * frameLength * 1_000_000 * (Math.abs(y2 - y1) / distance));
                        p.setDy(newDy);
                    } else {
                        double newDy = p.getDy() + (A * frameLength * 1_000_000 * (Math.abs(y2 - y1) / distance));
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

    private static double G = 6.67408 * Math.pow(10, -11);

    private static double getA(long mass, double distance) {
        double top = G * mass;
        double bottom = distance * distance;
        return top / bottom;
    }


}
