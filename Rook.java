package p3;

/**
 * This class models the functionality of the rook in the chess game
 * It does this by extending the ChessPiece class.
 * @author Gary Gritters, Jacob Dec, and Ross Kuiper
 * @version 1.0
 * @date 3/26/2019
 */

public class Rook extends ChessPiece {

    Integer moveCount;

    /**
     * This is the constructor for the rook class.
     * @param player determines which player the rook is for.
     */

    public Rook(Player player) {

        super(player);
        moveCount = 0;

    }

    /**
     * This method tells what type of piece the rook is.
     * @return p3.Rook
     */

    public String type() {
        return "p3.Rook";
    }

    /**
     * This method changes the number of moves the rook has done.
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
     * This method determines what a valid move is for the rook.
     * @param move The move which the rook is trying to make.
     * @param board The current board state.
     * @return Whether or not it is a valid move.
     */

    public boolean isValidMove(Move move, IChessPiece[][] board) {

        boolean valid = true;
        // More code is needed

        if (((move.fromRow == move.toRow) &&
                (move.fromColumn == move.toColumn)) == false) {

            // Prevents capturing same color pieces
            if (board[move.toRow][move.toColumn] != null) {
                if (board[move.toRow][move.toColumn].player() ==
                        board[move.fromRow][move.fromColumn].player()) {
                    valid = false;
                }
            }

            // Prevents non-horizontal or non-vertical movement
            if (!((move.fromRow == move.toRow) ^
                    (move.fromColumn == move.toColumn))) {
                valid = false;
            }

            // Prevents movement through other pieces vertically
            if (move.fromRow != move.toRow) {

                // Checks path from high from row to low to row
                if (move.fromRow > move.toRow) {
                    for (int i = move.fromRow - 1; i > move.toRow; i--) {
                        if (board[i][move.fromColumn] != null) {
                            valid = false;
                        }
                    }
                }

                // Checks path from low from row to high to row
                if (move.fromRow < move.toRow) {
                    for (int i = move.fromRow + 1; i < move.toRow; i++) {
                        if (board[i][move.fromColumn] != null) {
                            valid = false;
                        }
                    }
                }
            }

            // Prevents movement through other pieces horizontally
            else if ((move.fromColumn != move.toColumn)) {

                // Checks path from high from col to low to col
                if (move.fromColumn > move.toColumn) {
                    for (int i = move.fromColumn - 1;
                         i > move.toColumn; i--) {
                        if (board[move.toRow][i] != null) {
                            valid = false;
                        }
                    }
                }

                // Checks path from low from col to high to col
                else if (move.fromColumn < move.toColumn) {
                    for (int i = move.fromColumn + 1;
                         i < move.toColumn; i++) {
                        if (board[move.toRow][i] != null) {
                            valid = false;
                        }
                    }
                }
            }
        }

        // Prevents capturing itself
        if ((move.fromRow == move.toRow) &&
                (move.fromColumn == move.toColumn)) {
            valid = false;
        }

        return valid;

    }

}
