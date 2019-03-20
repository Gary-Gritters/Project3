public class Knight extends ChessPiece {

    Integer moveCount;

    public Knight(Player player) {
        super(player);
        moveCount = 0;
    }

    public String type() {
        return "p3.Knight";
    }


    public void changeMoveCount(int i){
        moveCount += i;
    }

    public Integer getMoveCount(){
        return moveCount;
    }

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
        if((colChangeABS < 1 && colChangeABS > 2)||(rowChangeABS < 1 && rowChangeABS > 2)){
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
