package p3;

import java.util.Random;
import java.util.Stack;

public class ChessModel implements IChessModel {
    private IChessPiece[][] board;
    private Player player;
    private GUIcodes status;

    // Tracks all previous moves performed
    private Stack<Move> prevMoves;

    // Tracks when pieces are captured
    private Stack<IChessPiece> captureHistory;

    private Stack<Boolean> promoted;

    // declare other instance variables as needed
    private Random rand;

    public ChessModel() {
        status = GUIcodes.NoMessage;
        board = new IChessPiece[8][8];
        player = Player.WHITE;
        rand = new Random();

        prevMoves = new Stack<>();
        captureHistory = new Stack<>();
        promoted = new Stack<>();

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

    public void pawnPromotionCheck(boolean f){
        promoted.push(f);
    }

    public void promotePop(){
        promoted.pop();
    }


    public boolean isComplete() {
        boolean valid = false;

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
        boolean valid = false;

        if (board[move.fromRow][move.fromColumn] != null)
            if (board[move.fromRow][move.fromColumn].isValidMove(move, board) == true)
                if(board[move.fromRow][move.fromColumn].player()==player)
                    return true;

        return valid;
    }

    public Boolean prevMoveExists(){
        return prevMoves.size() != 0;
    }

    public void undo(){
        // If a previous move exists
        if(prevMoves.size() > 0){

            // Retrieves previous move from stack
            Move prevMove = prevMoves.pop();
            Boolean resetPromote = promoted.pop();
            //For undoing castling
            Move castleCheck = new Move(3, 10, 41, 2);
            if(prevMoves.size() > 1) {
                castleCheck = prevMoves.peek();
            }
            boolean undoTwice = false;
            if(prevMoves.size() > 1 &&
                    board[prevMove.toRow][prevMove.toColumn].type().equals("p3.King") &&
                    board[castleCheck.toRow][castleCheck.toColumn].type().equals("p3.Rook") &&
                    board[prevMove.toRow][prevMove.toColumn].player() ==
                    board[castleCheck.toRow][castleCheck.toColumn].player() &&
                    board[prevMove.toRow][prevMove.toColumn].getMoveCount() == 1 &&
                    board[castleCheck.toRow][castleCheck.toColumn].getMoveCount() == 1){
                undoTwice = true;
            }


            IChessPiece undoPromoted;
            if(currentPlayer() == Player.WHITE){
                undoPromoted = new Pawn(Player.BLACK);
            } else{
                undoPromoted = new Pawn(Player.WHITE);
            }


            if(resetPromote == true){
                setPiece(prevMove.toRow, prevMove.toColumn, undoPromoted);
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
        promoted.push(false);
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

    public void AI(Player compTeam) {
        /*
            See if I'm in check
                -First possible move to prevent check
            Put them into checkmate
                -Doesn't display a message until white tries to move
            Put them into check
                -Doesn't account for dumb move and losing a piece
            Take their piece
                -Will currently sacrifice a queen for a pawn
            *Protect my piece
                -Doesn't exist
            *Move a piece
                -Doesn't exist
        */
        if(compInCheckMove(compTeam)){
            setNextPlayer();
            return;
        }
        if(compCheckmateThem(compTeam)){
            setNextPlayer();
            return;
        }

        if(compPutThemInCheck(compTeam)){
           setNextPlayer();
           return;
        }

        if(compCanCapture(compTeam)){
            setNextPlayer();
            return;
        }

        if(compIsInDanger(compTeam)){

        }

        if(compRandomMove(compTeam)) {
            setNextPlayer();
            return;
        }
    }

    private boolean compInCheckMove(Player compTeam) {
        if(!isComplete()) {
            if(inCheck(compTeam)){
                for(int r = 0; r < 8; r++) {
                    for (int c = 0; c < 8; c++) {
                        if(board[r][c] != null) {
                            if (board[r][c].player() == compTeam) {

                                for (int rr = 0; rr < 8; rr++) {
                                    for (int cc = 0; cc < 8; cc++) {

                                        Move theoMove = new Move(r, c, rr, cc);
                                        if (board[r][c].isValidMove(theoMove, board)) {
                                            move(theoMove);

                                            if (!inCheck(currentPlayer())) {
                                                return true;
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
        return false;
    }

    private boolean compCheckmateThem(Player compTeam){
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                if(board[r][c] != null) {
                    if (board[r][c].player() == compTeam) {

                        for (int rr = 0; rr < 8; rr++) {
                            for (int cc = 0; cc < 8; cc++) {
                                Move theoMove = new Move(r, c, rr, cc);
                                if (board[r][c].isValidMove(theoMove, board)) {
                                    //Weird stuff with setNextPlayer, since isComplete checks to see if currentPlayer()
                                    //is in checkmate. Without setNextPlayer, will check if the A. I. is in checkmate
                                    //except, we want to set them in checkmate
                                    move(theoMove);
                                    setNextPlayer();
                                    if (isComplete()) {
                                        setNextPlayer();
                                        return true;
                                    } else {
                                        setNextPlayer();
                                        undo();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean compPutThemInCheck(Player compTeam){
        //Checks whole board for own pieces
        for(int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[r][c] != null) {
                    if (board[r][c].player() == compTeam) {

                        for (int rr = 0; rr < 8; rr++) {
                            for (int cc = 0; cc < 8; cc++) {

                                //Checks for each of those pieces moves
                                Move theoMove = new Move(r, c, rr, cc);
                                //If that move is valid,
                                if(board[r][c].isValidMove(theoMove, board)){
                                    move(theoMove);
                                    //From here, look if I have a move onto the enemy king.
                                    //If I do, stay here, it's check. Otherwise, go back.
                                    if(inCheck(Player.WHITE)){
                                        return true;
                                    }
                                    undo();

                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean compCanCapture(Player compTeam){
        for(int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[r][c] != null) {
                    if (board[r][c].player() == compTeam) {

                        for (int rr = 0; rr < 8; rr++) {
                            for (int cc = 0; cc < 8; cc++) {

                                Move theoMove = new Move(r, c, rr, cc);
                                if (board[rr][cc] != null && board[rr][cc].player() != compTeam) {
                                    if(board[r][c].isValidMove(theoMove, board)) {
                                        move(theoMove);
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean compIsInDanger(Player compTeam){
        return false;
    }

    private boolean compRandomMove(Player compTeam) {
        boolean didMove = false;
        int fromR = rand.nextInt(8);
        int fromC = rand.nextInt(8);
        int toR = rand.nextInt(8);
        int toC = rand.nextInt(8);
        Move theoMove = new Move(fromR, fromC, toR, toC);
        while(!didMove) {
            if(board[fromR][fromC] != null && board[fromR][fromC].isValidMove(theoMove, board) && board[fromR][fromC].player() == compTeam) {
                move(theoMove);
                System.out.println(theoMove);
                didMove = true;
                return didMove;
            } else {
                fromR = rand.nextInt(8);
                fromC = rand.nextInt(8);
                toR = rand.nextInt(8);
                toC = rand.nextInt(8);
                theoMove = new Move(fromR, fromC, toR, toC);
            }
        }
        return false;
    }
}
