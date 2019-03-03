package p3;

public class Pawn extends ChessPiece {

    private boolean firstMove = true;

    public Pawn(Player player) {
        super(player);
    }

    public String type() {
        return "p3.Pawn";
    }

    // determines if the move is valid for a pawn piece
    public boolean isValidMove(Move move, IChessPiece[][] board) {
        boolean valid = true;
        int pathLength = move.fromRow - move.toRow;

        // Prevents movement through other pieces vertically
        if(move.fromRow != move.toRow){

            // Checks path from high from row to low to row
            if(move.fromRow > move.toRow){
                for(int i = move.fromRow - 1; i > move.toRow; i--){
                    if(board[i][move.fromColumn] != null){
                        valid = false;
                    }
                }
            }
            // Checks path from low from row to high to row
            if(move.fromRow < move.toRow){
                for(int i = move.fromRow + 1; i < move.toRow; i++){
                    if(board[i][move.fromColumn] != null){
                        valid = false;
                    }
                }
            }
        }

        // Cases where destination cell is populated
        if (board[move.toRow][move.toColumn] != null) {

            // Prevents capturing same color pieces
            if (board[move.toRow][move.toColumn].player() == board[move.fromRow][move.fromColumn].player()) {
                valid = false;
            }
            //Prevents capturing pieces directly ahead
            if(move.toColumn == move.fromColumn){
                valid = false;
            }
        }


        // Prevents white pawns moving down
        if(board[move.fromRow][move.fromColumn].player()
                == Player.WHITE && pathLength < 0){
            valid = false;
        }

        // Prevents white pawns moving up
        if(board[move.fromRow][move.fromColumn].player()
                == Player.BLACK && pathLength > 0){
            valid = false;
        }

        // Absolute value of path Length
        if(pathLength < 0){
            pathLength = -pathLength;
        }

        // Prevents moving more than 2 spaces
        if(pathLength > 2){
            valid = false;
        }
        // Prevents moving 2 unless on first move
        else if(pathLength == 2 && !firstMove){
            valid = false;
        }

        // Prevents capturing itself
        if ((move.fromRow == move.toRow) && (move.fromColumn == move.toColumn)) {
            valid = false;
        }

        // Prevents horizontal movement
        if(!(move.fromColumn == move.toColumn)){
            if(board[move.toRow][move.toColumn] == null){
                valid = false;
            }
        }

        // Must be right before return, tracks first move
        if(valid){
            firstMove = false;
        }
        return valid;
    }
}
