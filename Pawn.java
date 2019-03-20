

public class Pawn extends ChessPiece {

    Integer moveCount;

    public Pawn(Player player) {
        super(player);
        moveCount = 0;
    }

    public String type() {
        return "p3.Pawn";
    }

    public void changeMoveCount(int i){
        moveCount += i;
    }

    public Integer getMoveCount(){
        return moveCount;
    }

    // determines if the move is valid for a pawn piece
    public boolean isValidMove(Move move, IChessPiece[][] board) {
        boolean valid = true;
        boolean homerow = false;


        int colChangeABS;
        int rowChangeABS;

        int rowChange = move.fromRow - move.toRow;
        int colChange = move.toColumn - move.fromColumn;

        // Calculates ABS of colChange
        if(colChange < 0){
            colChangeABS = -colChange;
        }
        else colChangeABS = colChange;

        // Calculates ABS of rowChange
        if(rowChange < 0){
            rowChangeABS = -rowChange;
        }
        else rowChangeABS = rowChange;

        // Sets homerow for each pawn color
        if(board[move.fromRow][move.fromColumn].player()==Player.BLACK){
            if(move.fromRow == 1){
                homerow = true;
            }
        }
        else{
            if(move.fromRow == 6){
                homerow = true;
            }
        }

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

        // Prevents capture when 2 spaces are moved
        if(rowChangeABS == 2 && colChangeABS != 0){
            valid = false;
        }

        // Cases where destination cell is populated
        if (board[move.toRow][move.toColumn] != null) {

            // Prevents more that one horizontal movement
            if(colChangeABS != 1 || rowChangeABS == 0){
                valid = false;
            }

            // Prevents capturing same color pieces
            if (board[move.toRow][move.toColumn].player() == board[move.fromRow][move.fromColumn].player()) {
                valid = false;
            }

            //Prevents capturing pieces directly ahead
            if(move.toColumn == move.fromColumn){
                valid = false;
            }
        }

        // Prevents white from pawns moving down
        if(board[move.fromRow][move.fromColumn].player()
                == Player.WHITE && rowChange < 0){
            valid = false;
        }

        // Prevents black from pawns moving up
        if(board[move.fromRow][move.fromColumn].player()
                == Player.BLACK && rowChange > 0){
            valid = false;
        }

        // Absolute value of path Length
        if(rowChange < 0){
            rowChange = -rowChange;
        }

        // Prevents moving more than 2 spaces
        if(rowChange > 2){
            valid = false;
        }

        // Prevents moving 2 unless on homerow
        else if(rowChange == 2 && !homerow){
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

        return valid;
    }
}
