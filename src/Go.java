import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.SkinBase;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

//class defnition for reversi game
public class Go extends Application {
    // overridden init method
    public void init() {
        sp_mainlayout = new StackPane();
        rc_reversi = new GoControl();
        sp_mainlayout.getChildren().add(rc_reversi);
    }

    // overridden start method
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Go");
        primaryStage.setScene(new Scene(sp_mainlayout, 800, 800 ));
        primaryStage.show();
    }

    // overridden stop method
    public void stop() {

    }

    // entry point into our program for launching our javafx applicaton
    public static void main(String[] args) {
        launch(args);
    }

    // private fields for a stack pane and a reversi control
    private StackPane sp_mainlayout;
    private GoControl rc_reversi;

}

//class definition for a custom reversi control

//class definition for a skin for the reversi control
//NOTE: to keep JavaFX happy we dont use the skin here



//class definition for a reversi piece