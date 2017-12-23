import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

import javafx.scene.text.Text;
import javafx.scene.text.Font;

class GoBoard extends Pane {
    // default constructor for the class
    public GoBoard() {
        render = new GoPiece[7][7];
        initialiseRender();
        resetGame();
    }

    // public method that will try to place a piece in the given x,y coordinate
    public void placePiece(final double x, final double y) {

        if (!in_play)
            return;
        int indexx = (int) (x / cell_width);
        int indexy = (int) (y / cell_height);
        if (getPiece(indexx, indexy) != 0)
            return;
        determineSurrounding(indexx, indexy);
        if (!adjacentOpposingPiece())
        {
            return;
        }
        if (!determineReverse(indexx, indexy))
        {
            return;
        }
        placeAndReverse(indexx, indexy);
        updateScores();
        if (current_player == 1) {

            System.out.println("Player 1 : " + player1_score);
            System.out.println("Player 2 : " + player2_score);
            System.out.println("Player 2 turn");
        }
        if (current_player == 2) {
            System.out.println("Player 1 : " + player1_score);
            System.out.println("Player 2 : " + player2_score);
            System.out.println("Ready Player One");
        }
        swapPlayers();
        determineEndGame();
    }

    // overridden version of the resize method to give the board the correct size
    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        cell_height = height / 7.0;
        cell_width = width / 7.0;
        pieceResizeRelocate();
    }

    // public method for resetting the game
    public void resetGame() {
        resetRenders();
        render[6][6].setPiece(1);
        render[0][0].setPiece(1);
        render[3][3].setPiece(1);
        render[1][0].setPiece(1);
        render[0][1].setPiece(1);
        in_play = true;
        current_player = 1;
        opposing = 2;
        swapPlayers();
        updateScores();
    }

    // private method that will reset the renders
    private void resetRenders() {
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                render[i][j].setPiece(0);
            }
        }
    }

    // private method for swapping the players
    private void swapPlayers() {
        int save;
        save = current_player;
        current_player = opposing;
        opposing = save;
        Text turn = new Text("Currently Playing : Player " + current_player);
        turn.setFont(new Font(20));
        turn.setY(35);
        turn.setX(520);
        this.getChildren().add(turn);
    }

    // private method for updating the player scores
    private void updateScores() {
        player2_score = 0;
        player1_score = 0;
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                if (getPiece(i, j) == 1)
                    player1_score++;
                if (getPiece(i, j) == 2)
                    player2_score++;
            }
        }
        Text score1 = new Text("Player 1 : " + player1_score);
        score1.setFont(new Font(20));
        score1.setY(35);
        this.getChildren().add(score1);
        Text score2 = new Text("Player 2 : " + player2_score);
        score2.setFont(new Font(20));
        score2.setY(745);
        this.getChildren().add(score2);
    }

    // private method for resizing and relocating all the pieces
    private void pieceResizeRelocate() {
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                render[i][j].resize(cell_width, cell_height);
                render[i][j].relocate(i * cell_width, j  * cell_height);
            }
        }
    }

    // private method for determining which pieces surround x,y will update the
    // surrounding array to reflect this
    private void determineSurrounding(final int x, final int y) {
        for (int i = -1; i < 2; i++)
        {
            for (int j = -1; j < 2; j++)
            {
                if (j == 0 && i == 0)
                {
                    surrounding[i + 1][j + 1] = 0;
                }
                else
                {
                    surrounding[i + 1][j + 1] = getPiece(x + i, j + y);
                }
            }
        }
    }

    // private method for determining if a reverse can be made will update the can_reverse
    // array to reflect the answers will return true if a single reverse is found
    private boolean determineReverse(final int x, final int y) {
        // NOTE: this is to keep the compiler happy until you get to this part
        boolean isOne = false;
        for (int i = -1; i < 2; i++)
        {
            for (int j = -1; j < 2; j++)
            {
                if (i != 0 || j != 0)
                {
                    can_reverse[i + 1][j + 1] = isReverseChain(x, y, i, j, current_player);
                    if (can_reverse[i + 1][j + 1])
                    {
                        isOne = true;
                    }
                }
            }
        }
        can_reverse[1][1] = false;
        return isOne;
    }

    // private method for determining if a reverse can be made from a position (x,y) for
    // a player piece in the given direction (dx,dy) returns true if possible
    // assumes that the first piece has already been checked
    private boolean isReverseChain(final int x, final int y, final int dx, final int dy, final int player) {
        // NOTE: this is to keep the compiler happy until you get to this part
        int idx = x + dx;
        int idy = y + dy;
        boolean passed = false;
        while (getPiece(idx, idy) == opposing)
        {
            idx += dx;
            idy += dy;
            passed = true;
        }
        if (getPiece(idx, idy) == player)
            return (passed);
        return false;
    }

    // private method for determining if any of the surrounding pieces are an opposing
    // piece. if a single one exists then return true otherwise false
    private boolean adjacentOpposingPiece() {
        // NOTE: this is to keep the compiler happy until you get to this part
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if (surrounding[i][j] == opposing)
                {
                    return true;
                }
            }
        }
        return false;
    }

    // private method for placing a piece and reversing pieces
    private void placeAndReverse(final int x, final int y) {

        render[x][y].setPiece(current_player);
        for (int i = -1; i < 2; i++)
        {
            for (int j = -1; j < 2; j++)
            {
                if (can_reverse[i + 1][j + 1])
                {
                    reverseChain(x, y, i, j);
                }
            }
        }
    }

    // private method to reverse a chain
    private void reverseChain(final int x, final int y, final int dx, final int dy) {
        int idx = x + dx;
        int idy = y + dy;
        while (getPiece(idx, idy) == opposing)
        {
            render[idx][idy].swapPiece();
            idx += dx;
            idy += dy;
        }
    }

    // private method for getting a piece on the board. this will return the board
    // value unless we access an index that doesnt exist. this is to make the code
    // for determing reverse chains much easier
    private int getPiece(final int x, final int y) {
        // NOTE: this is to keep the compiler happy until you get to this point
        if (x < 0 || x > 6 || y < 0 || y > 6)
            return -1;
        return render[x][y].getPiece();
    }

    // private method that will determine if the end of the game has been reached
    private void determineEndGame() {
        if (!canMove())
        {
            swapPlayers();
            if (!canMove()) {
                in_play = false;
                determineWinner();
            }
        }
    }

    // private method to determine if a player has a move available
    private boolean canMove() {
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                if (getPiece(i, j) == 0)
                {
                    determineSurrounding(i, j);
                    if (adjacentOpposingPiece() && determineReverse(i, j))
                        return true;
                }
            }
        }
        // NOTE: this is to keep the compiler happy until you get to this part
        return false;
    }

    // private method that determines who won the game
    private void determineWinner() {
        if (player1_score < player2_score)
        {
            winnerLabel = new Text("Player 2 wins !");
            System.out.println("Player 2 wins !");
        }
        else if (player1_score > player2_score)
        {
            winnerLabel = new Text("Player 1 wins !");
            System.out.println("Player 1 wins !");
        }
        else
        {
            winnerLabel = new Text("WOW THAT IS A DRAW !");
            System.out.println("WOW THAT IS A DRAW !");
        }
        winnerLabel.setFont(new Font(100));
        winnerLabel.setVisible(true);
        winnerLabel.setY(200);
        this.getChildren().add(winnerLabel);
    }

    // private method that will initialise everything in the render array
    private void initialiseRender() {
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                render[i][j] = new GoPiece(0);
                getChildren().add(render[i][j]);
            }
        }
    }

    private GoPiece[][] render;
    // the current player who is playing and who is his opposition
    private int current_player;
    private int opposing;
    // is the game currently in play
    private boolean in_play;
    // current scores of player 1 and player 2
    private int player1_score;
    private int player2_score;
    // the width and height of a cell in the board
    private double cell_width;
    private double cell_height;
    // 3x3 array that holds the pieces that surround a given piece
    private int[][] surrounding;
    // 3x3 array that determines if a reverse can be made in any direction
    private boolean[][] can_reverse;
    private Text winnerLabel;
}
