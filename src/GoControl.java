import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

class GoControl extends Control {
    // constructor for the class
    public GoControl() {
        setSkin(new GoControlSkin(this));
        rb_board = new GoBoard();
        this.getChildren().add(rb_board);
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                rb_board.placePiece(event.getX(), event.getY());
            }
        });
        this.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.SPACE) {
                    rb_board.resetGame();
                }
            }
        });
    }

    // overridden version of the resize method
    @Override
    public void resize(double width, double height) {
        width = width - 50;
        height = height - 50;
        super.resize(width, height);
        rb_board.resize(width, height);
    }

    // private fields of a reversi board
    GoBoard rb_board;
}