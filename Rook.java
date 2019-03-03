package p3;

public class Rook extends ChessPiece {

    public Rook(Player player) {

        super(player);

    }

    public String type() {

        return "p3.Rook";

    }

    // determines if the move is valid for a rook piece
    public boolean isValidMove(Move move, IChessPiece[][] board) {

        boolean valid = true;
        // More code is needed

        if (((move.fromRow == move.toRow) && (move.fromColumn == move.toColumn)) == false) {

            // Prevents capturing same color pieces
            if (board[move.toRow][move.toColumn] != null) {
                if (board[move.toRow][move.toColumn].player() == board[move.fromRow][move.fromColumn].player()) {
                    valid = false;
                }
            }


//            System.out.println(board[move.toRow][move.toColumn].player());

            // Prevents non-horizontal or non-vertical movement
            if (!((move.fromRow == move.toRow) ^ (move.fromColumn == move.toColumn))) {
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
                    for (int i = move.fromColumn - 1; i > move.toColumn; i--) {
                        if (board[move.toRow][i] != null) {
                            valid = false;
                        }
                    }
                }

                // Checks path from low from col to high to col
                else if (move.fromColumn < move.toColumn) {
                    for (int i = move.fromColumn + 1; i < move.toColumn; i++) {
                        if (board[move.toRow][i] != null) {
                            valid = false;
                        }
                    }
                }

            }


        }
        // Prevents capturing itself
        if ((move.fromRow == move.toRow) && (move.fromColumn == move.toColumn)) {
            valid = false;
        }


        return valid;

    }

}
