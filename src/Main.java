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

//ToDO: Maybe add multithreading?
public class Main {
    public static void main(String[] args) {
        new JFXPanel();
        Platform.runLater(Main::launch);
    }

    private static int windowWidth = 500;
    private static int windowHeight = 500;
    private static int maxLength = (int) Math.hypot(windowWidth, windowHeight);
    private static boolean showLines = false, showDist = false;
    private static FrameRegulator fr = new FrameRegulator();

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
        for (int i = 0; i < 5; i++) {
            al.add(new Point(Math.ceil(rnd.nextDouble() * 100 - 50),
                    rnd.nextDouble() * 100 - 50,
                    (int) Math.ceil(Math.random() * windowWidth),
                    (int) Math.ceil(Math.random() * windowHeight),
                    (long) Math.ceil(Math.random() * 100000000 + 50000000)));
        }
        AnimationTimer at = new AnimationTimer() {
            private long last = 0;

            @Override
            public void handle(long now) {
                if (now - last >= 16_000_000) {
                    render(now, al, gc);
                    last = now;
                }
            }
        };
        at.start();
        scene.setOnMousePressed(me -> at.stop());
        scene.setOnMouseReleased(me -> at.start());
    }

    private static void render(long now, ArrayList<Point> al, GraphicsContext gc) {
        gc.clearRect(0, 0, windowWidth, windowHeight);
        drawLines(al, gc);
        for (Point p : al) {
            gc.fillRect(p.getPosX(), p.getPosY(), 5, 5);
            gc.fillText(String.valueOf(Math.hypot(p.getDy(), p.getDx())), p.getPosX() + 6, p.getPosY() + 6);
            gc.fillText(String.valueOf(al.indexOf(p)), p.getPosX() + 6, p.getPosY() + 15);
            gc.strokeLine(p.getPosX(), p.getPosY(), p.getPosX() + p.getDx(), p.getPosY() + p.getDy());
        }
        for (Point p : al) p.update(fr);
        fr.updateFPS(now, gc);
    }

    private static void drawLines(ArrayList<Point> al, GraphicsContext gc) {
        for (Point p : al) {

            double x1 = p.getPosX();
            double y1 = p.getPosY();

            for (Point a : al) {
                double x2 = a.getPosX();
                double y2 = a.getPosY();
                double distance = Math.hypot(x2 - x1, y2 - y1);

                if (distance != 0) {
                    double A = Physics.getA(a.getMass(), distance);
                    if (x1 > x2) {
                        double newDx = p.getDx() - (A * fr.getFrameLength() * 100000000 * (Math.abs(x2 - x1) / distance));
                        p.setDx(newDx);
                    } else {
                        double newDx = p.getDx() + (A * fr.getFrameLength() * 100000000 * (Math.abs(x2 - x1) / distance));
                        p.setDx(newDx);
                    }

                    if (y1 > y2) {
                        double newDy = p.getDy() - (A * fr.getFrameLength() * 100000000 * (Math.abs(y2 - y1) / distance));
                        p.setDy(newDy);
                    } else {
                        double newDy = p.getDy() + (A * fr.getFrameLength() * 100000000 * (Math.abs(y2 - y1) / distance));
                        p.setDy(newDy);
                    }
                }

                if (showLines) {
                    int redRat = (int) ((1 - (distance / maxLength)) * 255);
                    int blueRat = (int) ((distance / maxLength) * 255);
                    gc.setStroke(Color.rgb(redRat, 0, blueRat));
                    gc.strokeLine(x1, y1, x2, y2);
                    if (showDist) {
                        gc.fillText(String.valueOf(Physics.calcForce(p.getMass(), a.getMass(), distance)),
                                x1 + ((x2 - x1) / 2),
                                y1 + ((y2 - y1) / 2));
                    }
                }
            }
            //stack.pop();
        }
    }


}
