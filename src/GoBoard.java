import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Translate;

import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.lang.reflect.Array;
import java.util.Vector;

class GoBoard extends Pane {
    // default constructor for the class
    public GoBoard() {
        Image board = new Image("/resources/board.png");
        boardView = new ImageView();
        boardView.setImage(board);
        this.getChildren().add(boardView);
        render = new GoPiece[7][7];
        initialiseRender();
        resetGame();
        previous_states = new Vector<GoBoardStorage>();
    }

    // public method that will try to place a piece in the given x,y coordinate
    public void placePiece(final double x, final double y) {

        if (!in_play)
            return;
        int indexx = (int) (x / cell_width);
        int indexy = (int) (y / cell_height);
        if (getPiece(indexx, indexy) != 0)
            return;
        render[indexx][indexy].setPiece(current_player);
        if (!validateMove(indexx, indexy))
            return;
        checkChains(indexx, indexy);
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


    public boolean validateMove(int indexx, int indexy)
    {
        if (!checkSuicide(indexx, indexy))
        {
            render[indexx][indexy].resetPiece();
            return false;
        }
        if (!checkKo())
        {
            render[indexx][indexy].resetPiece();
            return false;
        }
        previous_states.add(new GoBoardStorage(render));
        return true;
    }

    public boolean checkKo()
    {
        for (int i = 0; i < previous_states.size(); i++)
        {
            if (previous_states.elementAt(i).isSame(render))
            {
                for (int j = 0; j < 7; j++)
                {
                    for (int k = 0; k < 7; k++)
                    {
                        render[j][k].setPiece(previous_states.lastElement().board[j][k]);
                    }
                }
                return false;
            }
        }

        return true;
    }

    private boolean checkSuicide(int indexx, int indexy)
    {
        checkChains(indexx, indexy);
        Vector<GoPiece> chain = new Vector<>();
        chainMaking(indexx, indexy, chain, current_player);
        return chainCheck(chain,false);
    }

    public void checkChains(int indexx, int indexy)
    {
        if (getPiece(indexx + 1, indexy) == opposing)
        {
            Vector<GoPiece> chain = new Vector<GoPiece>();
            chainMaking(indexx + 1, indexy, chain, opposing);
            chainCheck(chain,true);
        }
        if (getPiece(indexx - 1, indexy) == opposing)
        {
            Vector<GoPiece> chain = new Vector<GoPiece>();
            chainMaking(indexx - 1, indexy, chain, opposing);
            chainCheck(chain,true);
        }
        if (getPiece(indexx, indexy - 1) == opposing)
        {
            Vector<GoPiece> chain = new Vector<GoPiece>();
            chainMaking(indexx, indexy - 1, chain, opposing);
            chainCheck(chain, true);
        }
        if (getPiece(indexx, indexy + 1) == opposing)
        {
            Vector<GoPiece> chain = new Vector<GoPiece>();
            chainMaking(indexx, indexy + 1, chain, opposing);
            chainCheck(chain, true);
        }
    }

    private boolean chainCheck(Vector<GoPiece> chain, boolean destroy)
    {
        boolean safe = false;
        for (int i = 0; i < chain.size(); i++)
        {
            if (checkPieceIntersec(chain.elementAt(i).indexx, chain.elementAt(i).indexy) == 1)
            {
                safe = true;
            }
        }
        if (!safe)
        {
            if (destroy)
            {
                for (int i = 0; i < chain.size(); i++) {
                    chain.elementAt(i).resetPiece();
                }
                chain.removeAllElements();
            }
            return false;
        }
        return true;
    }

    private void chainMaking(int indexx, int indexy, Vector<GoPiece> chain, int player)
    {
        chain.add(render[indexx][indexy]);
        if (getPiece(indexx + 1, indexy) == player && existingInChain(chain, indexx + 1, indexy) == 0)
        {
            chainMaking(indexx + 1, indexy, chain, player);
        }
        if (getPiece(indexx - 1, indexy) == player && existingInChain(chain, indexx - 1, indexy) == 0)
        {
            chainMaking(indexx - 1, indexy, chain, player);
        }
        if (getPiece(indexx, indexy - 1) == player && existingInChain(chain, indexx, indexy - 1) == 0)
        {
            chainMaking(indexx, indexy - 1, chain, player);
        }
        if (getPiece(indexx, indexy + 1) == player && existingInChain(chain, indexx, indexy + 1) == 0)
        {
            chainMaking(indexx, indexy + 1, chain, player);
        }
    }

    private int existingInChain(Vector<GoPiece> chain, int indexx, int indexy)
    {
        for (int i = 0; i < chain.size(); i++)
        {
            if (chain.elementAt(i).indexx == indexx && chain.elementAt(i).indexy == indexy)
            {
                return 1;
            }
        }
        return 0;
    }

    public int checkPieceIntersec(int indexx, int indexy)
    {
        if (getPiece(indexx + 1, indexy) == 0)
        {
            return 1;
        }
        else if (getPiece(indexx - 1, indexy) == 0)
        {
            return 1;
        }
        else if (getPiece(indexx, indexy - 1) == 0)
        {
            return 1;
        }
        else if (getPiece(indexx, indexy + 1) == 0)
        {
            return 1;
        }
        return 0;
    }

    // overridden version of the resize method to give the board the correct size
    @Override
    public void resize(double width, double height) {
        boardView.setFitWidth(width);
        boardView.setFitHeight(height);
        super.resize(width, height);
        cell_height = height / 7.0;
        cell_width = width / 7.0;
        pieceResizeRelocate();
    }

    // public method for resetting the game
    public void resetGame() {
        resetRenders();
        in_play = true;
        current_player = 2;
        opposing = 1;
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
        Text display = new Text("\tPlayer 1 : " + player1_score + "\t|\tCurrently Playing : Player " + current_player + "\t|\tPlayer 2 : " + player2_score);
        display.setFont(new Font(20));
        display.setY(50);
        this.getChildren().add(display);
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
        if (false)
        {
            swapPlayers();
            if (false) {
                in_play = false;
                determineWinner();
            }
        }
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
                render[i][j] = new GoPiece(0, i, j);
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
    private ImageView boardView;
    private Vector<GoBoardStorage> previous_states;
}
