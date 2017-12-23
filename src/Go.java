import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.SkinBase;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


//class defnition for go game
public class Go extends Application {
    // overridden init method
    public void init() {
        sp_mainlayout = new StackPane();
        rc_go = new GoControl();
        Image board = new Image("/resources/board.png");
        ImageView boardView = new ImageView();
        boardView.setImage(board);
        boardView.setFitHeight(800);
        boardView.setFitWidth(800);
        boardView.setPreserveRatio(true);
        sp_mainlayout.getChildren().add(boardView);
        sp_mainlayout.getChildren().add(rc_go);
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
    private GoControl rc_go;

}

//class definition for a custom reversi control

//class definition for a skin for the reversi control
//NOTE: to keep JavaFX happy we dont use the skin here



//class definition for a reversi piece