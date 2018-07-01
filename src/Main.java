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
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        new JFXPanel();
        Platform.runLater(Main::launch);
    }

    private static int windowWidth = 1000;
    private static int windowHeight = 1000;

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
            }
        });

        canvas.setWidth(windowWidth);
        canvas.setHeight(windowHeight);
        group.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setFont(new Font("Comic Sans MS", 8));

        ArrayList<Point> al = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            al.add(new Point((int) Math.ceil(Math.random() * 5),
                    (int) Math.ceil(Math.random() * 5),
                    (int) Math.ceil(Math.random() * windowWidth),
                    (int) Math.ceil(Math.random() * windowHeight)));
        }

        AnimationTimer at = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, windowWidth, windowHeight);
                for (Point p : al) {
                    gc.fillRect(p.getPosX(), p.getPosY(), 5, 5);
                    gc.fillText(String.valueOf(al.indexOf(p)), p.getPosX() + 6, p.getPosY() + 6);
                }
                drawLines(al, gc);
                for (Point p : al) p.update();

            }
        };

        at.start();
        scene.setOnMousePressed(me -> {
            at.stop();
        });

        scene.setOnMouseReleased(me -> {
            at.start();
        });
    }

    private static void drawLines(ArrayList<Point> temp, GraphicsContext gc) {
        Stack<Point> stack = new Stack<>();
        for(Point p: temp) stack.push(p);
        while (stack.size() != 1) {
            Point p = stack.peek();
            for (Point a : stack) {
                if (a == p) break;
                gc.strokeLine(p.getPosX(), p.getPosY(), a.getPosX(), a.getPosY());
                double distance = Math.hypot(a.getPosX() - p.getPosX(), a.getPosY() - p.getPosY());

                gc.fillText(String.valueOf((float)distance),
                        p.getPosX() + ((a.getPosX() - p.getPosX())/2),
                        p.getPosY() + ((a.getPosY() - p.getPosY())/2));
            }
            stack.pop();
        }
    }
}
