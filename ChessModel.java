

import java.util.Stack;

public class ChessModel implements IChessModel {
    private IChessPiece[][] board;
    private Player player;

    // Tracks all previous moves performed
    private Stack<Move> prevMoves;

    // Tracks when pieces are captured
    private Stack<IChessPiece> captureHistory;

    // declare other instance variables as needed

    public ChessModel() {
        board = new IChessPiece[8][8];
        player = Player.WHITE;

        prevMoves = new Stack<>();
        captureHistory = new Stack<>();

        board[7][0] = new Rook(Player.WHITE);
        board[7][1] = new Knight(Player.WHITE);
        board[7][2] = new Bishop(Player.WHITE);
        board[7][3] = new Queen(Player.WHITE);
        board[7][4] = new King(Player.WHITE);
        board[7][5] = new Bishop(Player.WHITE);
        board[7][6] = new Knight(Player.WHITE);
        board[7][7] = new Rook(Player.WHITE);

        // Places white pawns
        for (int i = 0; i < numColumns(); i++) {
            board[6][i] = new Pawn(Player.WHITE);
        }

        player = Player.BLACK;

        board[0][0] = new Rook(Player.BLACK);
        board[0][1] = new Knight(Player.BLACK);
        board[0][2] = new Bishop(Player.BLACK);
        board[0][3] = new Queen(Player.BLACK);
        board[0][4] = new King(Player.BLACK);
        board[0][5] = new Bishop(Player.BLACK);
        board[0][6] = new Knight(Player.BLACK);
        board[0][7] = new Rook(Player.BLACK);

        // Places black pawns
        for (int i = 0; i < numColumns(); i++) {
            board[1][i] = new Pawn(Player.BLACK);
        }

        player = Player.WHITE;
    }


    public boolean isComplete() {
        boolean valid = false;
        return valid;
    }

    public boolean isValidMove(Move move) {
        boolean valid = false;

        if (board[move.fromRow][move.fromColumn] != null)
            if (board[move.fromRow][move.fromColumn].isValidMove(move, board) == true)
                if(board[move.fromRow][move.fromColumn].player()==player)
                    return true;

        return valid;
    }

    public Boolean prevMoveExisits(){
        return prevMoves.size() != 0;
    }

    public void undo(){
        // If a previous move exists
        if(prevMoves.size() > 0){

            // Retrieves previous move from stack
            Move prevMove = prevMoves.pop();
            board[prevMove.fromRow][prevMove.fromColumn]= board[prevMove.toRow][prevMove.toColumn];

            // Retrieves and places replaced piece
            board[prevMove.toRow][prevMove.toColumn] = captureHistory.pop();
        }
    }

    public void move(Move move) {
        if(board[move.toRow][move.toColumn] != null){
            captureHistory.push(board[move.toRow][move.toColumn]);
        }
        else{
            captureHistory.push(null);
        }
        prevMoves.push(move);

        board[move.toRow][move.toColumn] = board[move.fromRow][move.fromColumn];
        board[move.fromRow][move.fromColumn] = null;

    }

    public boolean inCheck(Player p){return false;}
   /*public boolean inCheck(Player p) {
        int kingPosR;
        int kingPosC;
        // find king first
        for(int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if(board[r][c].type().equals("p3.King"));
            }
        }

         Move test = new Move(r,c, move.toRow, move.toColumn);

                if(board[r][c] != null) {
                    if (board[r][c].player() != board[move.fromRow][move.fromColumn].player()) {
                        if (!board[r][c].type().equals("p3.king") && board[r][c].isValidMove(test, board)) {
                            valid = false;
                        }
                    }
                }
            }
        }

        boolean valid = false;
        return valid;
    }*/


    public Player currentPlayer() {
        return player;
    }

    public int numRows() {
        return 8;
    }

    public int numColumns() {
        return 8;
    }

    public IChessPiece pieceAt(int row, int column) {
        return board[row][column];
    }

    public void setNextPlayer() {
        player = player.next();
    }

    public void setPiece(int row, int column, IChessPiece piece) {
        board[row][column] = piece;
    }

    public void AI() {
        /*
         * Write a simple AI set of rules in the following order.
         * a. Check to see if you are in check.
         * 		i. If so, get out of check by moving the king or placing a piece to block the check
         *
         * b. Attempt to put opponent into check (or checkmate).
         * 		i. Attempt to put opponent into check without losing your piece
         *		ii. Perhaps you have won the game.
         *
         *c. Determine if any of your pieces are in danger,
         *		i. p3.Move them if you can.
         *		ii. Attempt to protect that piece.
         *
         *d. p3.Move a piece (pawns first) forward toward opponent king
         *		i. check to see if that piece is in danger of being removed, if so, move a different piece.
         */

    }
}
