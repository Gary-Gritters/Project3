package p3;

import java.util.Stack;

public class ChessModel implements IChessModel {
    private IChessPiece[][] board;
    private Player player;
    private GUIcodes status;

    // Tracks all previous moves performed
    private Stack<Move> prevMoves;

    // Tracks when pieces are captured
    private Stack<IChessPiece> captureHistory;

    private Stack<Integer> moveCounts;

    // declare other instance variables as needed

    public ChessModel() {
        status = GUIcodes.NoMessage;
        board = new IChessPiece[8][8];
        player = Player.WHITE;

        prevMoves = new Stack<>();
        captureHistory = new Stack<>();
        moveCounts = new Stack<>();

        board[7][0] = new Rook(Player.WHITE);
        board[7][1] = new Knight(Player.WHITE);
        board[7][2] = new Bishop(Player.WHITE);
        board[7][3] = new Queen(Player.WHITE);
        board[7][4] = new King(Player.WHITE);
        board[7][5] = new Bishop(Player.WHITE);
        board[7][6] = new Knight(Player.WHITE);
        board[7][7] = new Rook(Player.WHITE);

        // Places white pawns
        for (int i = 0; i < numColumns(); i++) {
            board[6][i] = new Pawn(Player.WHITE);
        }

        player = Player.BLACK;

        board[0][0] = new Rook(Player.BLACK);
        board[0][1] = new Knight(Player.BLACK);
        board[0][2] = new Bishop(Player.BLACK);
        board[0][3] = new Queen(Player.BLACK);
        board[0][4] = new King(Player.BLACK);
        board[0][5] = new Bishop(Player.BLACK);
        board[0][6] = new Knight(Player.BLACK);
        board[0][7] = new Rook(Player.BLACK);

        // Places black pawns
        for (int i = 0; i < numColumns(); i++) {
            board[1][i] = new Pawn(Player.BLACK);
        }

        player = Player.WHITE;
    }


    public boolean isComplete() {
        if(!inCheck(currentPlayer())){
            return false;
        }

        for(int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if(board[r][c] != null) {
                    if (board[r][c].player() == currentPlayer()) {

                        for (int rr = 0; rr < 8; rr++) {
                            for (int cc = 0; cc < 8; cc++) {

                                Move theoMove = new Move(r, c, rr, cc);
                                if (board[r][c].isValidMove(theoMove, board)) {
                                    move(theoMove);

                                    if (!inCheck(currentPlayer())) {
                                        undo();
                                        return false;
                                    }

                                    undo();

                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public boolean isValidMove(Move move) {
        if (board[move.fromRow][move.fromColumn] != null)
            if (board[move.fromRow][move.fromColumn].isValidMove(move, board) == true)
                if(board[move.fromRow][move.fromColumn].player()==player)
                    return true;
        return false;
    }

    public Boolean prevMoveExists(){
        return prevMoves.size() != 0;
    }

    public void undo(){
        // If a previous move exists
        if(prevMoves.size() > 0){

            // Retrieves previous move from stack
            Move prevMove = prevMoves.pop();
            //For undoing castling
            Move castleCheck = new Move(3, 10, 41, 2);
            if(prevMoves.size() > 1) {
                castleCheck = prevMoves.peek();
            }
            boolean undoTwice = false;
            if(prevMoves.size() > 1 && board[prevMove.toRow][prevMove.toColumn].player() ==
                    board[castleCheck.toRow][castleCheck.toColumn].player() &&
                    inCheck(board[prevMove.toRow][prevMove.toColumn].player())){
                undoTwice = true;
            }

            board[prevMove.fromRow][prevMove.fromColumn]= board[prevMove.toRow][prevMove.toColumn];


            // Sets the previously moved piece's initial condition
            board[prevMove.fromRow][prevMove.fromColumn].changeMoveCount(-1);


            // Retrieves and places replaced piece
            board[prevMove.toRow][prevMove.toColumn] = captureHistory.pop();
            if(undoTwice == true){
                undo();
            }
        }
    }

    public void move(Move move) {
        if(board[move.toRow][move.toColumn] != null){
            captureHistory.push(board[move.toRow][move.toColumn]);
        }
        else{
            //if en passant
            // else push null
            captureHistory.push(null);
        }
        prevMoves.push(move);
        board[move.fromRow][move.fromColumn].changeMoveCount(1);
        board[move.toRow][move.toColumn] = board[move.fromRow][move.fromColumn];
        board[move.fromRow][move.fromColumn] = null;


    }


    public boolean inCheck(Player p){

        int kingPosR = 0;
        int kingPosC = 0;
        for(int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[r][c] != null && board[r][c].type().equals("p3.King") && board[r][c].player() == p) {
                    kingPosR = r;
                    kingPosC = c;
                }
            }
        }

        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                if(board[r][c] != null) {
                    if (board[r][c].player() != p) {
                        Move opponentMove = new Move(r,c, kingPosR, kingPosC);
                        if(board[r][c].isValidMove(opponentMove, board)) {
                            return true;
                        }
                        //board[move.toRow][move.toColumn] = board2[0];
                    }
                }

            }
        }
        return false;
    }

    public void undoCheck(){
        if (inCheck(currentPlayer())){
            undo();
            setNextPlayer();
            //System.out.println("lol dun goofed");
        }
    }

    public Player currentPlayer() {
        return player;
    }

    public int numRows() {
        return 8;
    }

    public int numColumns() {
        return 8;
    }

    public IChessPiece pieceAt(int row, int column) {
        return board[row][column];
    }

    public void setNextPlayer() {
        player = player.next();
    }

    public void setPiece(int row, int column, IChessPiece piece) {
        board[row][column] = piece;
    }

    public void AI(Player p) {
        /*
         * Write a simple AI set of rules in the following order.
         * a. Check to see if you are in check.
         * 		i. If so, get out of check by moving the king or placing a piece to block the check
         *
         * b. Attempt to put opponent into check (or checkmate).
         * 		i. Attempt to put opponent into check without losing your piece
         *		ii. Perhaps you have won the game.
         *
         *c. Determine if any of your pieces are in danger,
         *		i. p3.Move them if you can.
         *		ii. Attempt to protect that piece.
         *
         *d. p3.Move a piece (pawns first) forward toward opponent king
         *		i. check to see if that piece is in danger of being removed, if so, move a different piece.
         */
        Player AI = p;
        Player other;
        if(AI == Player.BLACK) {
            other = Player.WHITE;
        } else {
            other = Player.BLACK;
        }
        if(inCheck(AI)) {
            int kingPosRB = 0;
            int kingPosCB = 0;
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    if (board[r][c] != null && board[r][c].type().equals("p3.King") && board[r][c].player() == AI) {
                        kingPosRB = r;
                        kingPosCB = c;
                    }
                }
            }
            for(int r = 0; r < 8; r++) {
                for(int c = 0; c < 8; c++) {
                    if (board[r][c] != null && board[r][c].type().equals("p3.King") && board[r][c].player() == AI) {
                        Move outOfCheck = new Move(kingPosRB, kingPosCB, r, c);
                        if (isValidMove(outOfCheck)) {
                            move(outOfCheck);
                            setNextPlayer();
                        }
                    } else if (board[r][c] != null && board[r][c].player() == AI){
                        for(int rr = 0; rr < 8; rr++) {
                            for(int cc = 0; cc < 8; cc++) {
                                Move blockCheck = new Move(r, c, rr, cc);
                                if(isValidMove(blockCheck)){
                                    move(blockCheck);
                                    setNextPlayer();
                                }
                            }
                        }
                    }
                }
            }
        } else {
            boolean didIMove = false;
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    for (int rr = 0; rr < 8; rr++) {
                        for (int cc = 0; cc < 8; cc++) {
                            if (board[r][c] != null && board[r][c].player() == AI) {
                                Move putInCheck = new Move(r, c, rr, cc);
                                if (isValidMove(putInCheck)) {
                                    move(putInCheck);
                                    setNextPlayer();
                                    didIMove = true;
                                    if (!inCheck(other)) {
                                        undo();
                                        didIMove = false;
                                    } else {
                                        for (int rrr = 0; rrr < 8; rrr++) {
                                            for (int ccc = 0; ccc < 8; ccc++) {
                                                if (board[r][c] != null && board[r][c].player() == other) {
                                                    Move stopCheck = new Move(rrr, ccc, rr, cc);
                                                    if (isValidMove(stopCheck)) {
                                                        move(stopCheck);
                                                        setNextPlayer();
                                                        if (!inCheck(other)) {
                                                            undo();
                                                            didIMove = false;
                                                        }
                                                        undo();
                                                    }
                                                } else {
                                                    for (int rrrr = 0; rrrr < 8; rrrr++) {
                                                        for (int cccc = 0; cccc < 8; cccc++) {
                                                            if (board[r][c] != null && board[r][c].player() == other) {
                                                                Move blockCheck = new Move(rrr, ccc, rrrr, cccc);
                                                                if (isValidMove(blockCheck)) {
                                                                    move(blockCheck);
                                                                    setNextPlayer();
                                                                    if (!inCheck(other)) {
                                                                        undo();
                                                                        didIMove = false;
                                                                    }
                                                                    undo();
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!didIMove) {
                for (int r = 0; r < 8; r++) {
                    for (int c = 0; c < 8; c++) {
                        for (int rr = 0; rr < 8; rr++) {
                            for (int cc = 0; cc < 8; cc++) {
                                if (board[r][c] != null && board[r][c].player() == other) {
                                    if (board[rr][cc] != null && board[rr][cc].player() == AI) {
                                        Move amIInDanger = new Move(r, c, rr, cc);
                                        if (isValidMove(amIInDanger)) {
                                            for (int rrr = 0; rrr < 8; rrr++) {
                                                for (int ccc = 0; ccc < 8; ccc++) {
                                                    Move outOfDanger = new Move(rr, cc, rrr, ccc);
                                                    if (isValidMove(outOfDanger)) {
                                                        move(outOfDanger);
                                                        setNextPlayer();
                                                        didIMove = true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!didIMove) {
                for (int r = 0; r < 8; r++) {
                    for (int c = 0; c < 8; c++) {
                        for (int rr = 0; rr < 8; rr++) {
                            for (int cc = 0; cc < 8; cc++) {
                                if (board[r][c] != null && board[r][c].player() == AI) {
                                    Move random = new Move(r, c, rr, cc);
                                    if (isValidMove(random)) {
                                        move(random);
                                        setNextPlayer();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}