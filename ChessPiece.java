package p3;

/**
 * This abstract class determines what methods each chess piece must have.
 * @author Gary Gritters, Jacob Dec, and Ross Kuiper
 * @version 1.0
 * @date 3/26/2019
 */

public abstract class ChessPiece implements IChessPiece {

    private Player owner;

    /**
     * This is the constructor for the ChessPiece class.
     * @param player what player the piece is for.
     */

    protected ChessPiece(Player player) {
        this.owner = player;
    }

    /**
     * Determine what type of piece it is.
     * @return type.
     */

    public abstract String type();

    /**
     * Determine which player owns the piece.
     * @return owner
     */

    public Player player() {
        return owner;
    }

    /**
     * Determine whether a move is valid.
     * @param move the move to be made.
     * @param board the current state of the board.
     * @return if the move is valid.
     */

    public boolean isValidMove(Move move, IChessPiece[][] board) {
        boolean valid = false;

        if (((move.fromRow == move.toRow) && (move.fromColumn == move.toColumn)) == false)
            return valid;

        return false;
    }
}
