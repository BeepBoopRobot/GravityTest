import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Test {
    public static void main(String[] args) {
        new JFXPanel();
        Platform.runLater(Test::launch);
    }

    private static void launch() {
        Stage stage = new Stage();
        stage.setWidth(100);
        stage.setHeight(100);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.show();

        Group group = new Group();
        Scene scene = new Scene(group);
        stage.setScene(scene);

        Canvas canvas = new Canvas();
        canvas.setWidth(stage.getWidth());
        canvas.setHeight(stage.getHeight());
        group.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        AnimationTimer at = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long r = now / 10000; // y = (y2-y1) * t + y1, where y2 = 0xFF0000 and y1 = 0x0000FF
                double colour = (0xFF0000 - 0x0000FF) * ((r % stage.getWidth())/stage.getWidth()) + 0x0000FF;
                System.out.println(colour);
                //gc.setFill(Color.web(String.valueOf(colour)));
                gc.fillRect(0,0,100,100);
            }
        };
        at.start();
    }
}
