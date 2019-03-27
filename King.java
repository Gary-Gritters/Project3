package p3;

/**
 * This class models the functionality of the king in the chess game
 * It does this by extending the ChessPiece class.
 * @author Gary Gritters, Jacob Dec, and Ross Kuiper
 * @version 1.0
 * @date 3/26/2019
 */

public class King extends ChessPiece {

    Integer moveCount;

    /**
     * This is the constructor for the king class
     * @param player determines which player the king is for
     */

    public King(Player player) {
        super(player);
        moveCount = 0;
    }

    /**
     * This method tells what type of piece the king is.
     * @return p3.King
     */

    public String type() {
        return "p3.King";
    }

    /**
     * This method changes the number of moves the king has done.
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
     * This method determines what a valid move is for the king.
     * @param move The move which the king is trying to make.
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

        // Prevents movement other than surrounding cells

        //Checks for white castling at both locations. Makes sure neither have moved, and you selected it's piece
        if(board[move.fromRow][move.fromColumn].player() == Player.WHITE && (move.toRow == 7 && move.toColumn == 0) &&
        ((moveCount == 0) && (board[7][0] != null) && (board[7][0].getMoveCount() == 0))){
            for(int i = 1; i < 4; i++){
                if(board[7][i] != null){
                    valid = false;
                }
            }
            move.toColumn = 2;
        }else if(board[move.fromRow][move.fromColumn].player() == Player.WHITE && (move.toRow == 7 && move.toColumn == 7) &&
                ((moveCount == 0) && (board[7][7] != null) && (board[7][7].getMoveCount() == 0))){
            for(int i = 6; i < 4; i--){
                if(board[7][i] != null){
                    valid = false;
                }
            }
            move.toColumn = 6;

        }
        //Checks for black castling at both locations.
        else if(board[move.fromRow][move.fromColumn].player() == Player.BLACK && (move.toRow == 0 && move.toColumn == 0) &&
                ((moveCount == 0) && (board[0][0] != null) && (board[0][0].getMoveCount() == 0))){
            for(int i = 1; i < 4; i++){
                if(board[0][i] != null){
                    valid = false;
                }
            }
            move.toColumn = 2;

        }else if(board[move.fromRow][move.fromColumn].player() == Player.BLACK && (move.toRow == 0 && move.toColumn == 7) &&
                ((moveCount == 0) && (board[0][7] != null) && (board[0][7].getMoveCount() == 0))){
            for(int i = 6; i < 4; i--){
                if(board[0][i] != null){
                    valid = false;
                }
            }
            move.toColumn = 6;

        }
        //Checks for normal king movement
        else if(rowChangeABS > 1 || colChangeABS > 1){
            valid  = false;
        }

        // Prevents capturing itself
        if ((move.fromRow == move.toRow) && (move.fromColumn == move.toColumn)) {
            valid = false;
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
