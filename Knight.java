package p3;

/**
 * This class models the functionality of the knight in the chess game
 * It does this by extending the ChessPiece class.
 * @author Gary Gritters, Jacob Dec, and Ross Kuiper
 * @version 1.0
 * @date 3/26/2019
 */

public class Knight extends ChessPiece {

    Integer moveCount;

    /**
     * This is the constructor for the knight class.
     * @param player determines which player the knight is for.
     */

    public Knight(Player player) {
        super(player);
        moveCount = 0;
    }

    /**
     * This method tells what type of piece the knight is.
     * @return p3.Knight
     */

    public String type() {
        return "p3.Knight";
    }

    /**
     * This method changes the number of moves the knight has done.
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
     * This method determines what a valid move is for the knight.
     * @param move The move which the knight is trying to make.
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

        // Checks col & row ChangeABS are in the correct range
        if((colChangeABS < 1 && colChangeABS > 2)||
                (rowChangeABS < 1 && rowChangeABS > 2)){
            valid = false;
        }

        if(colChangeABS == 3 || rowChangeABS == 3){
            valid = false;
        }

        // Checks the piece total move distance is 3 squares
        if((rowChangeABS + colChangeABS) != 3){
            valid = false;
        }


        // Prevents capturing itself
        if ((move.fromRow == move.toRow) &&
                (move.fromColumn == move.toColumn)) {
            valid = false;
        }

        // Prevents capturing same color pieces
        if (board[move.toRow][move.toColumn] != null) {
            if (board[move.toRow][move.toColumn].player() ==
                    board[move.fromRow][move.fromColumn].player()) {
                valid = false;
            }
        }

        return valid;

    }

}
