import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

//class defnition for go game
public class Go extends Application {
    // overridden init method
    public void init() {
        sp_mainlayout = new StackPane();
        rc_go = new CustomControl();
        sp_mainlayout.getChildren().add(rc_go);
    }

    // overridden start method
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Go");
        primaryStage.setScene(new Scene(sp_mainlayout, 900, 900 ));
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
    private CustomControl rc_go;

}

//class definition for a custom reversi control

//class definition for a skin for the reversi control
//NOTE: to keep JavaFX happy we dont use the skin here



//class definition for a reversi piece