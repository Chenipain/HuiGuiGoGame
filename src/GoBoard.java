import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import java.util.Vector;

class GoBoard extends Pane {
    // default constructor for the class
    public GoBoard() {
        Image board = new Image("/resources/board.png");
        passButton = new Button("Pass");
        passButton.setLayoutX(this.getWidth());
        passButton.setLayoutY(this.getHeight());
        winnerLabel = new Text();
        boardView = new ImageView();
        boardView.setImage(board);
        this.getChildren().add(boardView);
        render = new Stone[7][7];
        display = new Text();
        previous_states = new Vector<GoBoardStorage>();
        initialiseRender();
        resetGame();
        this.getChildren().add(display);
        this.getChildren().add(winnerLabel);
        this.getChildren().add(passButton);
    }

    // public method that will try to place a piece in the given x,y coordinate
    public void placePiece(final double x, final double y) {

        if (!in_play)
            return;
        int indexx = (int) (x / cell_width);
        int indexy = (int) (y / cell_height);
        if (getPiece(indexx, indexy) != 0) {
            return;
        }
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
        pass = false;
    }

    //Check if a move is possible
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

    //Check the Ko rule
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

    //check the Suicide rule
    private boolean checkSuicide(int indexx, int indexy)
    {
        checkChains(indexx, indexy);
        Vector<Stone> chain = new Vector<>();
        chainMaking(indexx, indexy, chain, current_player);
        return chainCheck(chain,false);
    }

    //check if chains have free intersection and destroy them if necessary
    public void checkChains(int indexx, int indexy)
    {
        if (getPiece(indexx + 1, indexy) == opposing)
        {
            Vector<Stone> chain = new Vector<Stone>();
            chainMaking(indexx + 1, indexy, chain, opposing);
            chainCheck(chain,true);
        }
        if (getPiece(indexx - 1, indexy) == opposing)
        {
            Vector<Stone> chain = new Vector<Stone>();
            chainMaking(indexx - 1, indexy, chain, opposing);
            chainCheck(chain,true);
        }
        if (getPiece(indexx, indexy - 1) == opposing)
        {
            Vector<Stone> chain = new Vector<Stone>();
            chainMaking(indexx, indexy - 1, chain, opposing);
            chainCheck(chain, true);
        }
        if (getPiece(indexx, indexy + 1) == opposing)
        {
            Vector<Stone> chain = new Vector<Stone>();
            chainMaking(indexx, indexy + 1, chain, opposing);
            chainCheck(chain, true);
        }
    }

    //check intersection for a specific chain and destroy it if necessary
    private boolean chainCheck(Vector<Stone> chain, boolean destroy)
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
                if (current_player == 1)
                {
                    capturedWhite += chain.size();
                }
                else
                {
                    capturedBlack += chain.size();
                }
                chain.removeAllElements();
            }
            return false;
        }
        return true;
    }

    //Create chains from group of pieces
    private void chainMaking(int indexx, int indexy, Vector<Stone> chain, int player)
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

    //check if a piece already exists in a chain
    private int existingInChain(Vector<Stone> chain, int indexx, int indexy)
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

    //Check if at least one intersection is free for a piece
    private int checkPieceIntersec(int indexx, int indexy)
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
        previous_states.clear();
        resetRenders();
        capturedWhite = 0;
        capturedBlack = 0;
        in_play = true;
        current_player = 2;
        opposing = 1;
        winnerLabel.setVisible(false);
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
    public void swapPlayers() {
        int save;
        save = current_player;
        current_player = opposing;
        opposing = save;
    }

    // private method for updating the player scores
    public void updateScores() {
        player1_score = capturedWhite;
        player2_score = capturedBlack;
        countTer();
        resetCounting();
        String text = new String("\t\tP1 : Captured(" + capturedWhite + ") / Owned(" + (player1_score - capturedWhite) + ")\t|\tCurrently Playing : Player " + opposing + "\t|\tP2 : Captured(" + capturedBlack + ") / Owned(" + (player2_score - capturedBlack) + ")");
        display.setText(text);
        display.setFont(new Font(15));
        display.relocate(10, 35);
        display.setFill(Color.GRAY);
    }

    //Count territories from each player
    private void countTer()
    {
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                if (getPiece(i, j) == 0) {
                    hasBlack = false;
                    hasWhite = false;
                    Vector<Stone> chain = new Vector<>();
                    chainMaking(i, j, chain, 0);
                    for (int k = 0; k < chain.size(); k++) {
                        checkSurrounding(chain.elementAt(k).indexx, chain.elementAt(k).indexy);
                        // Avoiding recounting previous 0 piece
                        chain.elementAt(k).setPiece(3);
                    }
                    if (hasBlack && !hasWhite)
                    {
                        player2_score += chain.size();
                    }
                    else if (hasWhite && !hasBlack)
                    {
                        player1_score += chain.size();
                    }
                }
            }
        }
    }

    //replace the 3 by 0 in the board
    private void resetCounting()
    {
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                if (getPiece(i, j) == 3)
                {
                    render[i][j].setPiece(0);
                }
            }
        }
    }

    //check all intersections around the piece
    private void checkSurrounding(int x, int y)
    {
        checkThePiece(x + 1, y);
        checkThePiece(x - 1, y);
        checkThePiece(x, y + 1);
        checkThePiece(x, y + 1);
    }

    //check the presence of white or black in surrounding
    private void checkThePiece(int x, int y)
    {
        if (getPiece(x, y) == 1)
        {
            hasWhite = true;
        }
        else if (getPiece(x , y) == 2)
        {
            hasBlack = true;
        }
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

    // private method that determines who won the game
    public void determineWinner() {
        player2_score += 1.5;
        if (player1_score < player2_score)
        {
            winnerLabel.setText("Player 2 wins !");
            System.out.println("Player 2 wins !");
        }
        else if (player1_score > player2_score)
        {
            winnerLabel.setText("Player 1 wins !");
            System.out.println("Player 1 wins !");
        }
        else
        {
            winnerLabel.setText("WOW THAT IS A DRAW !");
            System.out.println("WOW THAT IS A DRAW !");
        }
        winnerLabel.setFont(new Font(100));
        winnerLabel.setVisible(true);
        winnerLabel.setY(200);
        winnerLabel.setFill(Color.GRAY);
    }

    // private method that will initialise everything in the render array
    private void initialiseRender() {
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                render[i][j] = new Stone(0, i, j);
                getChildren().add(render[i][j]);
            }
        }
    }

    private Stone[][] render;
    // the current player who is playing and who is his opposition
    private int current_player;
    private int opposing;
    // is the game currently in play
    private boolean in_play;
    // current scores of player 1 and player 2
    private double player1_score;
    private double player2_score;
    // the width and height of a cell in the board
    private double cell_width;
    private double cell_height;
    // 3x3 array that holds the pieces that surround a given piece
    private int[][] surrounding;
    // 3x3 array that determines if a reverse can be made in any direction
    private boolean[][] can_reverse;
    private Text winnerLabel;
    // Image of the goban
    private ImageView boardView;
    //Storage for previous game stages
    private Vector<GoBoardStorage> previous_states;
    //boolean used to check free territories
    private boolean hasBlack;
    private boolean hasWhite;
    //total of captured stones
    private int capturedBlack;
    private int capturedWhite;
    // boolean to determine if the last turn has been passed
    public boolean pass;
    public Text display;
    public Button passButton;
}
