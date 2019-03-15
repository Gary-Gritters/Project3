
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

        //IChessPiece[][] board2 = new IChessPiece[8][8];

        /*
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                board2[r][c] = board[r][c];
            }
        }
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                if(board[r][c] != null) {
                    if (board[r][c].player() != board[move.fromRow][move.fromColumn].player()) {
                        Move opponentMove = new Move(r,c, move.toRow, move.toColumn);

                        board2[move.toRow][move.toColumn] = null;
                        if (!board[r][c].type().equals("p3.King") && board2[r][c] != null && board2[r][c].isValidMove(opponentMove, board)) {
                            valid = false;
                        }
                        //board[move.toRow][move.toColumn] = board2[0];
                    }
                }

            }
        }
        */
        return valid;
    }
}
