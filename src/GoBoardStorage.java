import java.util.Arrays;

public class GoBoardStorage {

    public GoBoardStorage(Stone[][] initialBoard)
    {
        board = new char[7][7];
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                board[i][j] = (char)initialBoard[i][j].getPiece();
                if (initialBoard[i][j].getPiece() != 0)
                {
                    size++;
                }
            }
        }
    }

    public boolean isSame(Stone[][] initialBoard)
    {
        char[][] new_board = new char[7][7];
        int initialBoardSize = 0;
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                new_board[i][j] = (char)initialBoard[i][j].getPiece();
                if (initialBoard[i][j].getPiece() != 0)
                {
                    initialBoardSize++;
                }
            }
        }
        if (initialBoardSize != size)
        {
            return false;
        }
        return (Arrays.deepEquals(new_board, board));
    }


    public char[][] board;
    private int size;
}
