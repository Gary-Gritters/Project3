package p3;

public class King extends ChessPiece {

    public King(Player player) {
        super(player);
    }

    public String type() {
        return "p3.King";
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

        // Prevents movement other than surrounding cells
        if(rowChangeABS > 1 || colChangeABS > 1){
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
