package p3;

/**
 * This class models the functionality of the bishop in the chess game
 * It does this by extending the ChessPiece class.
 * @author Gary Gritters, Jacob Dec, and Ross Kuiper
 * @version 1.0
 * @date 3/26/2019
 */

public class Bishop extends ChessPiece {

    Integer moveCount;

    /**
     * This is the constructor for the bishop class.
     * @param player determines which player the bishop is for.
     */

    public Bishop(Player player) {
        super(player);
        moveCount = 0;
    }

    /**
     * This method tells what type of piece the bishop is.
     * @return p3.Bishop
     */

    public String type() {
        return "p3.Bishop";
    }

    /**
     * This method changes the number of moves the bishop has done.
     * @param i number of moves.
     */

    public void changeMoveCount(int i){
        moveCount += i;
    }

    /**
     * This method returns the move count.
     * @return moveCount
     */

    public Integer getMoveCount(){
        return moveCount;
    }

    /**
     * This method determines what a valid move is for the bishop.
     * @param move The move which the bishop is trying to make.
     * @param board The current board state.
     * @return Whether or not it is a valid move.
     */

    public boolean isValidMove(Move move, IChessPiece[][] board) {

        boolean valid = true;

        int rowChange = move.toRow - move.fromRow;
        int colChange = move.toColumn - move.fromColumn;

        int rowChangeABS;
        int colChangeABS;

        // Finds absolute value of row change
        if(rowChange < 0){
            rowChangeABS = -rowChange;
        }
        else rowChangeABS = rowChange;

        // Finds absolute value of col change
        if(colChange < 0){
            colChangeABS = -colChange;
        }
        else colChangeABS = colChange;

        if(rowChangeABS != colChangeABS){
            valid = false;
        }

        // Prevents non-diagonal movements
        if(move.fromRow == move.toRow || move.fromColumn == move.toColumn){
            valid = false;
        }

        if(colChangeABS != rowChangeABS){
            valid = false;
        }

        // Checks for pieces in down right path
        else if(rowChange > 0 && colChange > 0) {
            int pathlength = move.toRow - move.fromRow;

            for (int i = 1; i < pathlength; i++) {
                if (board[move.fromRow + i][move.fromColumn + i] != null) {
                    valid = false;
                }
            }
        }

        // Checks for pieces in up right path
        else if(rowChange < 0 && colChange > 0) {
            int pathlength =  move.fromRow - move.toRow;

            for (int i = 1; i < pathlength; i++) {
                if (board[move.fromRow - i][move.fromColumn + i] != null) {
                    valid = false;
                }
            }
        }

        // Checks for pieces in down left path
        else if(rowChange > 0 && colChange < 0) {
            int pathlength = move.toRow - move.fromRow;

            for (int i = 1; i < pathlength; i++) {
                if (board[move.fromRow + i][move.fromColumn - i] != null) {
                    valid = false;
                }
            }
        }

        // Checks for pieces in up left path
        else if(rowChange < 0 && colChange < 0) {
            int pathlength = move.fromRow - move.toRow;

            for (int i = 1; i < pathlength; i++) {
                if (board[move.fromRow - i][move.fromColumn - i] != null) {
                    valid = false;
                }
            }
        }

        // Prevents capturing same color pieces
        if (board[move.toRow][move.toColumn] != null) {
            if (board[move.toRow][move.toColumn].player() == board[move.fromRow][move.fromColumn].player()) {
                valid = false;
            }
        }

        return valid;
    }
}
