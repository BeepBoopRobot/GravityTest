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
// Rewrite the calcForce function
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        new JFXPanel();
        Platform.runLater(Main::launch);
    }

    private static int windowWidth = 1000;
    private static int windowHeight = 1000;
    private static int maxLength = (int) Math.hypot(windowWidth, windowHeight);
    private static boolean showLines = false, showDist = false;

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
        gc.setFont(new Font("Comic Sans MS", 8));
        Random rnd = new Random();
        ArrayList<Point> al = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            al.add(new Point(Math.ceil(rnd.nextDouble() * 1000 - 500),
                    rnd.nextDouble() * 1000 - 500,
                    (int) Math.ceil(Math.random() * windowWidth),
                    (int) Math.ceil(Math.random() * windowHeight),
                    (int) Math.ceil(Math.random() * 100000 + 50000)));
        }
        FrameRegulator fr = new FrameRegulator();
        calcForce(al.get(0), al.get(1));

        AnimationTimer at = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, windowWidth, windowHeight);
                if (showLines) drawLines(al, gc);
                for (Point p : al) {
                    gc.fillRect(p.getPosX(), p.getPosY(), 5, 5);
                    gc.fillText(String.valueOf(al.indexOf(p)), p.getPosX() + 6, p.getPosY() + 6);
                }
                for (Point p : al) p.update(fr);
                fr.updateFPS(now, gc);
            }
        };

        at.start();
        scene.setOnMousePressed(me -> at.stop());

        scene.setOnMouseReleased(me -> at.start());
    }

    private static void drawLines(ArrayList<Point> temp, GraphicsContext gc) {
        Stack<Point> stack = new Stack<>();
        for (Point p : temp) stack.push(p);
        while (stack.size() != 1) {
            Point p = stack.peek();
            for (Point a : stack) {
                if (a == p) break;
                double distance = Math.hypot(a.getPosX() - p.getPosX(), a.getPosY() - p.getPosY());
                int redRat = (int) ((1 - (distance / maxLength)) * 255);
                int blueRat = (int) ((distance / maxLength) * 255);
                gc.setStroke(Color.rgb(redRat, 0, blueRat));
                gc.strokeLine(p.getPosX(), p.getPosY(), a.getPosX(), a.getPosY());

                if(showDist) {gc.fillText(String.valueOf((int) distance),
                        p.getPosX() + ((a.getPosX() - p.getPosX()) / 2),
                        p.getPosY() + ((a.getPosY() - p.getPosY()) / 2));
                }
            }
            stack.pop();
        }
    }

    private static void calcForce(Point m1, Point m2) {
        System.out.println(4000 * (6.67408 * Math.pow(10, -11)));
        System.out.println("M1 Mass: " + m1.getMass() + ", M2 mass: " + m2.getMass() + ", Distance: " + Math.hypot(m2.getPosX() - m1.getPosX(), m2.getPosY() - m1.getPosY()));
        double top = m1.getMass() * m2.getMass() * (6.67408 * Math.pow(10, -11));
        System.out.println("Top: " + top);
        double bottom = Math.pow(Math.hypot(m2.getPosX() - m1.getPosX(), m2.getPosY() - m1.getPosY()), 2);
        System.out.println("Bottom: " + bottom);
        System.out.println(top / bottom);
    }
}
