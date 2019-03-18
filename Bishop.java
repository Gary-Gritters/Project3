package p3;

public class Bishop extends ChessPiece {

    public Bishop(Player player) {
        super(player);
    }

    public String type() {
        return "p3.Bishop";
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
