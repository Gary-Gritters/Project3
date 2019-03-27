package p3;

/**
 * This class models the functionality of the queen in the chess game
 * It does this by extending the ChessPiece class.
 * @author Gary Gritters, Jacob Dec, and Ross Kuiper
 * @version 1.0
 * @date 3/26/2019
 */

public class Queen extends ChessPiece {

    Integer moveCount;

    /**
     * This is the constructor for the queen class.
     * @param player determines which player the queen is for.
     */

    public Queen(Player player) {
        super(player);
        moveCount = 0;
    }

    /**
     * This method tells what type of piece the queen is.
     * @return p3.Queen
     */

    public String type() {
        return "p3.Queen";
    }

    /**
     * This method changes the number of moves the queen has done.
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
     * This method determines what a valid move is for the queen.
     * This is done by treating the queen as both a rook and a bishop..
     * @param move The move which the queen is trying to make.
     * @param board The current board state.
     * @return Whether or not it is a valid move.
     */

    public boolean isValidMove(Move move, IChessPiece[][] board) {
        Bishop move1 =
                new Bishop(board[move.fromRow][move.fromColumn].
                        player());
        Rook move2 =
                new Rook(board[move.fromRow][move.fromColumn].
                        player());
        return (move1.isValidMove(move, board) ||
                move2.isValidMove(move, board));
    }
}
