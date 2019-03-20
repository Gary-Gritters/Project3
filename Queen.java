
public class Queen extends ChessPiece {

    Integer moveCount;

    public Queen(Player player) {
        super(player);

        moveCount = 0;
    }

    public String type() {
        return "p3.Queen";

    }

    public void changeMoveCount(int i){
        moveCount += i;
    }

    public Integer getMoveCount(){
        return moveCount;
    }

    public boolean isValidMove(Move move, IChessPiece[][] board) {
        Bishop move1 = new Bishop(board[move.fromRow][move.fromColumn].player());
        Rook move2 = new Rook(board[move.fromRow][move.fromColumn].player());
        return (move1.isValidMove(move, board) || move2.isValidMove(move, board));
    }
}
