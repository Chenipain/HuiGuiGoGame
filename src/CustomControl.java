import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

class CustomControl extends Control {
    // constructor for the class
    public CustomControl() {
        setSkin(new CustomControlSkin(this));
        rb_board = new GoBoard();
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                rb_board.placePiece(event.getX(), event.getY());
            }
        });
        this.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.R) {
                    rb_board.resetGame();
                }
            }
        });
        rb_board.passButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (rb_board.pass)
                    rb_board.determineWinner();
                else {
                    rb_board.pass = true;
                    rb_board.swapPlayers();
                    rb_board.updateScores();
                }
            }
        });
        this.getChildren().add(rb_board);
    }

    // overridden version of the resize method
    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        rb_board.resize(width, height);
    }

    // private fields of a reversi board
    GoBoard rb_board;
}