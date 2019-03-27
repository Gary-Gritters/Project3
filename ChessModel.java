package p3;

import java.util.Random;
import java.util.Stack;

/**
 * This class models the functionality of the actual chess game.
 * It does so by extending IChessModel.
 * @author Gary Gritters, Jacob Dec, and Ross Kuiper
 * @version 1.0
 * @date 3/26/2019
 */

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

    /**
     * This is the constructor for the ChessModel class.
     */

    public ChessModel() {
        status = GUIcodes.NoMessage;
        //Creating the board
        board = new IChessPiece[8][8];
        //Sets the current player to white.
        player = Player.WHITE;
        rand = new Random();

        //Creates all the stacks we'll need
        prevMoves = new Stack<>();
        captureHistory = new Stack<>();
        promoted = new Stack<>();

        //Places white pieces
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

        //Places black pieces
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

    /**
     * This method checks to see if a pawn has been promoted and adds it to a stack if it has.
     * @param f if the pawn has been promoted or not.
     */

    public void pawnPromotionCheck(boolean f){
        promoted.push(f);
    }

    /**
     * This method removes an entry from the pawn promotion stack for undo purposes.
     */

    public void promotePop(){
        promoted.pop();
    }


    /**
     * This method checks to see if the current player has been checkmated.
     * @return if checkmate
     */

    public boolean isComplete() {
        boolean valid = false;

        //If we aren't check, don't even look to see if we can move out
        if(!inCheck(currentPlayer())){
            return false;
        }

        //These first 2 for loops look through the board for every single piece
        for(int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {

                //Checks to make sure that spot isn't null
                //Then checks to make sure that spot is the player in check.
                if(board[r][c] != null) {
                    if (board[r][c].player() == currentPlayer()) {

                        //These two for loops check for every single move those pieces can make
                        for (int rr = 0; rr < 8; rr++) {
                            for (int cc = 0; cc < 8; cc++) {

                                //Creates a theoretical move from the piece at (r, c) to (rr, cc)
                                Move theoMove = new Move(r, c, rr, cc);
                                //If it's a valid move,
                                if (board[r][c].isValidMove(theoMove, board)) {
                                    //Then move that piece. We have to move that piece since inCheck looks for
                                    //the pieces on the board to see if a player is in check
                                    move(theoMove);

                                    //If we aren't in check,  then it isn't checkmate. Undo the theoretical move,
                                    //and return false
                                    if (!inCheck(currentPlayer())) {
                                        undo();
                                        return false;
                                    }

                                    //Else, undo that move and check the next move, unless that piece has tried every move.
                                    //Then, move to the next piece.
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

    /**
     * This method checks whether a move is valid using the given piece.
     * @param move the move that
     * @return if the move is valid.
     */

    public boolean isValidMove(Move move) {
        boolean valid = false;

        if (board[move.fromRow][move.fromColumn] != null)
            if (board[move.fromRow][move.fromColumn].isValidMove(move, board) == true)
                if(board[move.fromRow][move.fromColumn].player()==player)
                    return true;

        return valid;
    }

    /**
     * This method determines if there is a previous move.
     * @return if there is a previous move.
     */

    public Boolean prevMoveExists(){
        return prevMoves.size() != 0;
    }

    /**
     * This method goes backwards one move.
     */

    public void undo(){
        // If a previous move exists
        if(prevMoves.size() > 0){

            // Retrieves previous move from stack and a boolean from resetPromote
            Move prevMove = prevMoves.pop();
            Boolean resetPromote = promoted.pop();
            //For undoing castling. Will only be used if there was a castle. Because of this,
            //it doesn't matter the numbers we put in there. If for some reason it does run through,
            //it will throw an error, since these numbers aren't on the board
            Move castleCheck = new Move(999, 10, 41, 510);
            //Since castling is actually two moves, we check the next move.
            if(prevMoves.size() > 1) {
                castleCheck = prevMoves.peek();
            }

            boolean undoTwice = false;
            //If the last move was the king, and the move before that was a rook, they were the same player
            //and both of them have only moved once. None of these will actually happen if it wasn't a castle.
            //If there wasn't a castle, there would be a black move inbetween them in the move stack.
            if(prevMoves.size() > 1 &&
                    board[prevMove.toRow][prevMove.toColumn].type().equals("p3.King") &&
                    board[castleCheck.toRow][castleCheck.toColumn].type().equals("p3.Rook") &&
                    board[prevMove.toRow][prevMove.toColumn].player() ==
                    board[castleCheck.toRow][castleCheck.toColumn].player() &&
                    board[prevMove.toRow][prevMove.toColumn].getMoveCount() == 1 &&
                    board[castleCheck.toRow][castleCheck.toColumn].getMoveCount() == 1){
                //If it was a castle, undo twice. Effectively undoes both moves
                undoTwice = true;
            }

            if(resetPromote == true){
                //Creates the piece to reset
                IChessPiece undoPromoted;
                if(currentPlayer() == Player.WHITE){
                    undoPromoted = new Pawn(Player.BLACK);
                } else{
                    undoPromoted = new Pawn(Player.WHITE);
                }
                //If the last move was a promotion, replace whatever piece was there with pawn of the
                //appropriate color
                setPiece(prevMove.toRow, prevMove.toColumn, undoPromoted);
            }

            //Moves the piece back
            board[prevMove.fromRow][prevMove.fromColumn]= board[prevMove.toRow][prevMove.toColumn];


            //Sets the previously moved piece's initial condition
            //Basically, since we undid the previous move, that piece has moved -1 times
            board[prevMove.fromRow][prevMove.fromColumn].changeMoveCount(-1);

            //This resets en passant. If the previous move was a pawn, if it moved diagonally,
            //the move before that one moved 2 pieces forward or backward (black/white),
            //and makes sure that pawn has moved more than once. Also makes sure the pawn before
            //moved two rows up. In the scenario that that happened, puts the pawn that had been
            //taken by that move in the spot beneath where he had moved from.
            if(board[prevMove.fromRow][prevMove.fromColumn].type().equals("p3.Pawn") &&
                    Math.abs(prevMove.fromRow-prevMove.toRow) == 1 &&
                    Math.abs(prevMove.toColumn - prevMove.fromColumn) == 1 &&
                    board[prevMove.fromRow][prevMove.fromColumn].getMoveCount() > 1 &&
                    (Math.abs(castleCheck.fromRow - castleCheck.toRow) == 2))
                    {
                        if(currentPlayer() == Player.BLACK) {
                            board[prevMove.toRow + 1][prevMove.toColumn] = captureHistory.pop();
                        }else{
                            board[prevMove.toRow - 1][prevMove.toColumn] = captureHistory.pop();
                        }
                        board[prevMove.toRow][prevMove.toColumn] = null;
            }else {
                // Retrieves and places replaced piece if not an enpassant
                board[prevMove.toRow][prevMove.toColumn] = captureHistory.pop();
            }
            //will undo again if there was a castle. will not go through again, since prevMove will be the rook,
            //not the king
            if(undoTwice == true){
               undo();
            }
        }
    }

    /**
     * This method moves a piece.
     * @param move the move to be made.
     */

    public void move(Move move) {
        //Creates some variables for enPassant that we'll need
        Move enPassant;
        Boolean badEnPassant = false;
        //If it's a pawn moving, who's moving diagonally to an empty square
        if(board[move.fromRow][move.fromColumn].type().equals("p3.Pawn") &&
                board[move.toRow][move.toColumn] == null &&
                Math.abs(move.fromRow-move.toRow) == 1 && Math.abs(move.toColumn - move.fromColumn) == 1){
            //And there are moves in the stack.
            if(prevMoves.size() > 0){
                //Check the previous move
                enPassant = prevMoves.peek();
                //If that previous move wasn't a pawn,  or that pawn hadn't moved twice, or that previous
                //move wasn't right next to the pawn that wasn't enpassant
                if (!board[enPassant.toRow][enPassant.toColumn].type().equals("p3.Pawn") ||
                        !(Math.abs(enPassant.fromRow - enPassant.toRow) == 2) ||
                        Math.abs(enPassant.fromRow - move.toRow) != 1 || enPassant.toColumn != move.toColumn) {
                    //Then it was a bad move, and undo that move. Has to be done at the end of move, or else
                    //things will get mixed up bad
                    badEnPassant = true;
                }
            }
            //But, even if it was a bad move, still make that move. If it was a bad move, we'll just undo it.
            //Otherwise, we'll be able to undo it later if someone decides to.
            if(currentPlayer() == Player.BLACK) {
                captureHistory.push(board[move.toRow - 1][move.toColumn]);
                board[move.toRow - 1][move.toColumn] = null;
            }else{
                captureHistory.push(board[move.toRow + 1][move.toColumn]);
                board[move.toRow + 1][move.toColumn] = null;
            }
        //Basically, if the move wasn't an enpassant, and their move.to isn't empty, push it
        }else if(board[move.toRow][move.toColumn] != null){
            captureHistory.push(board[move.toRow][move.toColumn]);
        }
        //Else if they didn't land on anything, just push null
        else{
            captureHistory.push(null);
        }
        //Push a false since we moved to the stack.
        promoted.push(false);
        //Push the move to the stack.
        prevMoves.push(move);
        //Increase that pieces move count by one.
        board[move.fromRow][move.fromColumn].changeMoveCount(1);
        //Duplicates the piece from (fromRow, fromColumn) onto (toRow, toColumn)
        board[move.toRow][move.toColumn] = board[move.fromRow][move.fromColumn];
        //Removes the piece at (fromRow, fromColumn)
        board[move.fromRow][move.fromColumn] = null;
        //Now, if there was an bad enPassant, undo that move.
        if(badEnPassant){
            setNextPlayer();
            undo();
        }
    }

    /**
     * Determines whether a player is in check.
     * @param p The player which is being checked.
     * @return if the player is in check.
     */

    public boolean inCheck(Player p){

        //First looks for the king, and creates that position
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

        //Checks for if an enemy piece can move onto the king's spot
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                if(board[r][c] != null) {
                    if (board[r][c].player() != p) {
                        Move opponentMove = new Move(r,c, kingPosR, kingPosC);
                        //If they have a valid move onto the king, then it is check
                        if(board[r][c].isValidMove(opponentMove, board)) {
                            return true;
                        }
                        //board[move.toRow][move.toColumn] = board2[0];
                    }
                }

            }
        }
        //else not check
        return false;
    }

    /**
     * This method does not allow a player to end their turn in check
     * It does this by undoing their move if they are in check at the end of their turn.
     */

    public void undoCheck(){
        if (inCheck(currentPlayer())){
            undo();
            setNextPlayer();
            //System.out.println("lol dun goofed");
        }
    }

    /**
     * This method returns the current player.
     * @return current player.
     */

    public Player currentPlayer() {
        return player;
    }

    /**
     * This method returns the number of rows in the chess board.
     * @return 8
     */

    public int numRows() {
        return 8;
    }

    /**
     * This method returns the number of columns in the chess board.
     * @return 8
     */

    public int numColumns() {
        return 8;
    }

    /**
     * This method returns the piece at the given location.
     * @param row on the chess board.
     * @param column on the chess board.
     * @return the piece.
     */

    public IChessPiece pieceAt(int row, int column) {
        return board[row][column];
    }

    /**
     * This methods sets the turn to the other player.
     */

    public void setNextPlayer() {
        player = player.next();
    }

    /**
     * This method changes the piece at the given location to a different type.
     * This is used for promotion.
     * @param row on the chess board.
     * @param column on the chess board
     * @param piece to change to.
     */

    public void setPiece(int row, int column, IChessPiece piece) {
        board[row][column] = piece;
    }

    /**
     * This method runs an AI for one chess player.
     * It uses helper methods to determine which move it should make.
     * The methods are placed in the order of importance.
     * @param compTeam what player the AI is.
     */

    public void AI(Player compTeam) {

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
            setNextPlayer();
            return;
        }

        if(compRandomMove(compTeam)){
            setNextPlayer();
            return;
        }
    }

    //The remaining methods are the AI helper methods.

    /**
     * Check if the AI is in check. If so, move out of check.
     * @param compTeam which player the AI is.
     * @return if the AI moved out of check.
     */

    private boolean compInCheckMove(Player compTeam) {
        //If it isn't checkmate
        if(!isComplete()) {
            //But it is check
            if(inCheck(compTeam)){
                //Look through the entire board
                for(int r = 0; r < 8; r++) {
                    for (int c = 0; c < 8; c++) {
                        //If that piece isn't null, and is his own
                        if(board[r][c] != null) {
                            if (board[r][c].player() == compTeam) {

                                //Check all their moves
                                for (int rr = 0; rr < 8; rr++) {
                                    for (int cc = 0; cc < 8; cc++) {

                                        Move theoMove = new Move(r, c, rr, cc);
                                        //If it's a valid move, then move them
                                        if (board[r][c].isValidMove(theoMove, board) && disableCastlingEnpassantComputer((theoMove))) {
                                            move(theoMove);

                                            //If they aren't in check anymore, leave that piece there.
                                            if(!inCheck(compTeam)){
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

    /**
     * Check if the AI can checkmate the other player. If so, do so.
     * @param compTeam which player the AI is.
     * @return if the AI checkmated the other player.
     */

    private boolean compCheckmateThem(Player compTeam){
        //Looks through the whole board
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                //Make sure there is a piece there and it's my piece
                if(board[r][c] != null) {
                    if (board[r][c].player() == compTeam) {

                        //Check each one of their moves
                        for (int rr = 0; rr < 8; rr++) {
                            for (int cc = 0; cc < 8; cc++) {
                                //If that move is a valid move, make it.
                                Move theoMove = new Move(r, c, rr, cc);
                                if (board[r][c].isValidMove(theoMove, board) && disableCastlingEnpassantComputer(theoMove)) {
                                    //Weird stuff with setNextPlayer, since isComplete checks to see if currentPlayer()
                                    //is in checkmate. Without setNextPlayer, will check if the A. I. is in checkmate
                                    //except, we want to set them in checkmate. If they are in checkmate, leave that
                                    //piece there. Else, make it the A. I.'s turn again, undo, and try again
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

    /**
     * Check if the AI can put the other player in check.
     * @param compTeam which player the AI is.
     * @return if the AI put the other player in check.
     */

    private boolean compPutThemInCheck(Player compTeam){
        //Checks whole board
        for(int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                //Makes sure that spot has a piece on it, and it's my piece
                if (board[r][c] != null) {
                    if (board[r][c].player() == compTeam) {

                        //Check for each of their moves
                        for (int rr = 0; rr < 8; rr++) {
                            for (int cc = 0; cc < 8; cc++) {

                                //Checks for each of those pieces moves
                                Move theoMove = new Move(r, c, rr, cc);
                                //If that move is valid,
                                if(board[r][c].isValidMove(theoMove, board) && disableCastlingEnpassantComputer(theoMove)){
                                    move(theoMove);
                                    //move it. If they are in check, leave that piece there.
                                    //Else, undo it
                                    if(!inCheck(compTeam)) {
                                        if (compTeam == Player.BLACK) {
                                            if (inCheck(Player.WHITE)) {
                                                return true;
                                            }
                                        }
                                        if (compTeam == Player.WHITE) {
                                            if (inCheck(Player.BLACK)) {
                                                return true;
                                            }
                                        }
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

    /**
     * Check if the AI can capture one of the other player's pieces.
     * @param compTeam which player the AI is.
     * @return if the AI captured an enemy piece.
     */

    private boolean compCanCapture(Player compTeam){
        //Look through the whole board
        for(int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                //If that spot has a piece, and is my piece
                if (board[r][c] != null) {
                    if (board[r][c].player() == compTeam) {

                        //Look through each one of that piece's moves
                        for (int rr = 0; rr < 8; rr++) {
                            for (int cc = 0; cc < 8; cc++) {

                                //If the spot they are moving unto isn't null, is the enemy team, and is a valid move
                                Move theoMove = new Move(r, c, rr, cc);
                                if (board[rr][cc] != null && board[rr][cc].player() != compTeam) {
                                    if(board[r][c].isValidMove(theoMove, board) && disableCastlingEnpassantComputer(theoMove)) {
                                        //leave that piece there.
                                        move(theoMove);
                                        if(!inCheck(compTeam)){
                                            return true;
                                        }else{
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

    /**
     * Check if the enemy can take an AI piece.
     * @param compTeam which player the AI is.
     * @return if the AI avoided danger.
     */

    private boolean compIsInDanger(Player compTeam){
        //Look through the whole board
        for(int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                //If that spot has a piece, and is their piece
                if (board[r][c] != null) {
                    if (board[r][c].player() != compTeam) {

                        //Look through each one of that piece's moves
                        for (int rr = 0; rr < 8; rr++) {
                            for (int cc = 0; cc < 8; cc++) {

                                //If the spot they are moving unto isn't null, is my guy, and is a valid move
                                Move theoMove = new Move(r, c, rr, cc);
                                if (board[rr][cc] != null && board[rr][cc].player() == compTeam) {
                                    if (board[r][c].isValidMove(theoMove, board) && disableCastlingEnpassantComputer(theoMove)) {
                                        //Need to move that guy somewhere where he isn't being attacked anymore
                                        //Looks for all his possible valid moves
                                        for(int rrr = 0; rrr < 8; rrr++) {
                                            for (int ccc = 0; ccc < 8; ccc++) {
                                                //If that move moves him,
                                                Move doubleTheoMove = new Move(rr, cc, rrr, ccc);
                                                if(board[rr][cc].isValidMove(doubleTheoMove, board) && disableCastlingEnpassantComputer(doubleTheoMove)) {
                                                    Boolean amISafeHere = true;
                                                    //Check to see if no enemies have a move onto that square. If they don't, full send it
                                                    for(int rrrr = 0; rrrr < 8; rrrr++) {
                                                        for (int cccc = 0; cccc < 8; cccc++) {
                                                            Move tripleTheoMove = new Move(rrrr, cccc, rrr, ccc);
                                                            if(board[rrrr][cccc] != null &&  board[rrrr][cccc].player() != compTeam &&
                                                                    board[rrrr][cccc].isValidMove(tripleTheoMove, board) && disableCastlingEnpassantComputer(tripleTheoMove)){
                                                                //If they do, I'm not safe and keep checking
                                                                amISafeHere = false;
                                                            }
                                                        }
                                                    }
                                                    //IF they don't, check if I'm in check. If I am, keep looking.
                                                    if(amISafeHere){
                                                        move(doubleTheoMove);
                                                        if(inCheck(compTeam)){
                                                            undo();
                                                        }else{
                                                            //If I'm not, good job that was a fantastic move
                                                            return true;
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
        return false;
    }

    /**
     * Move a random piece if nothing else is important.
     * @param compTeam which player the AI is.
     * @return true because it can never return false.
     */

    private boolean compRandomMove(Player compTeam) {
        //Selects random numbers.
        boolean didMove = false;
        int fromR = rand.nextInt(8);
        int fromC = rand.nextInt(8);
        int toR = rand.nextInt(8);
        int toC = rand.nextInt(8);
        Move theoMove = new Move(fromR, fromC, toR, toC);
        //While we didn't make a good move, keep making random numbers
        while(!didMove) {
            //If the spot i'm trying to move has a piece, their move is valid, and it is my piece, make the move
            if(board[fromR][fromC] != null && board[fromR][fromC].isValidMove(theoMove, board)
                    && board[fromR][fromC].player() == compTeam && disableCastlingEnpassantComputer(theoMove)) {
                move(theoMove);
                didMove = true;
                //Else, try again
            } else {
                fromR = rand.nextInt(8);
                fromC = rand.nextInt(8);
                toR = rand.nextInt(8);
                toC = rand.nextInt(8);
                theoMove = new Move(fromR, fromC, toR, toC);
            }
            //Makes sure random move won't put the comp into check
            if(inCheck(compTeam)){
                //Have to reset the move, otherwise it will never hit the else in the if else statement earlier
                //it will get stuck in an endless loop
                undo();
                didMove = false;
                fromR = rand.nextInt(8);
                fromC = rand.nextInt(8);
                toR = rand.nextInt(8);
                toC = rand.nextInt(8);
                theoMove = new Move(fromR, fromC, toR, toC);
            }
        }
        //Will never return false so that the AI always moves
        //However it will not compile without this statement
        return didMove;
    }

    /**
     * This method dissallows castling and enpassant for the AI.
     * @param move the move to check if it should be dissallowed.
     * @return whether or not the move is dissallowed.
     */

    private boolean disableCastlingEnpassantComputer(Move move) {
        boolean valid = true;
        if (board[move.fromRow][move.fromColumn].type().equals("p3.King")) {
            if (Math.abs(move.fromColumn - move.toColumn) > 1) {
                return false;
            }
        }
        if (board[move.fromRow][move.fromColumn].type().equals("p3.Pawn")) {
            if (Math.abs(move.fromRow - move.toRow) == 1 && Math.abs(move.fromColumn - move.toColumn) == 1) {
                if (board[move.toRow][move.toColumn] == null) {
                    return false;
                }
            }
        }
        return valid;
    }
}
