import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.transform.Translate;

class GoPiece extends Group {
    // default constructor for the class
    public GoPiece(int player) {
        this.player = player;
        piece = new Ellipse();
        t = new Translate();
        if (player == 1)
        {
            piece.setFill(Color.WHITE);
        }
        else if (player == 2){
            piece.setFill(Color.BLACK);
        }
        else {
            piece.setFill(Color.TRANSPARENT);
        }
        piece.getTransforms().add(t);
        this.getChildren().add(piece);
    }

    // overridden version of the resize method to give the piece the correct size
    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        piece.setCenterX(width / 2.0);
        piece.setCenterY(height / 2.0);
        piece.setRadiusX(width / 2.0);
        piece.setRadiusY(height / 2.0);
    }

    // overridden version of the relocate method to position the piece correctly
    @Override
    public void relocate(double x, double y) {
        super.relocate(x, y);
        t.setX(x); t.setY(y);
    }

    // public method that will swap the colour and type of this piece
    public void swapPiece() {
        if (player == 1)
        {
            setPiece(2);
        }
        else
        {
            setPiece(1);
        }

    }

    // method that will set the piece type
    public void setPiece(final int type) {
        if (type == 1)
        {
            piece.setFill(Color.WHITE);
        }
        else if (type == 2){
            piece.setFill(Color.BLACK);
        }
        else {
            piece.setFill(Color.TRANSPARENT);
        }
        player = type;
    }

    // returns the type of this piece
    public int getPiece() {
        // NOTE: this is to keep the compiler happy until you get to this point
        return player;
    }

    // private fields
    private int player;		// the player that this piece belongs to
    private Ellipse piece;	// ellipse representing the player's piece
    private Translate t;	// translation for the player piece
}