import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        new JFXPanel();
        Platform.runLater(Main::launch);
    }

    private static int windowWidth = 800;
    private static int windowHeight = 500;

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

        System.out.println(al.toString());
//kys
        AnimationTimer at = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, windowWidth, windowHeight);
                for (Point p : al) {
                    gc.fillRect(p.getPosX(), p.getPosY(), 5, 5);
                    gc.fillText(String.valueOf(al.indexOf(p)), p.getPosX() + 6, p.getPosY() + 6);
                    for (Point a : al) {
                        if (a == p) break;
                        int col = al.indexOf(a);
                        switch (col) {
                            case 0:
                                gc.setStroke(Color.web("#2ec46d"));
                                break;
                            case 1:
                                gc.setStroke(Color.web("#431282"));
                                break;
                            case 2:
                                gc.setStroke(Color.web("#431282"));
                                break;
                            case 3:
                                gc.setStroke(Color.web("#431282"));
                                break;

                        }
                        gc.strokeLine(p.getPosX(), p.getPosY(), a.getPosX(), a.getPosY());
                    }
                    gc.setStroke(Color.BLACK);
                }
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
}
