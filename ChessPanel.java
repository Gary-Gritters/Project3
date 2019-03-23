package p3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/*
 * Initial condition stack before piece moves
 *      En Passant
 *
 * AI
 * JUnits
 */
public class ChessPanel extends JPanel {

    private JButton[][] board;
    private JButton undo;
    private ChessModel model;



    private ImageIcon wRook, wBishop, wQueen, wKing, wPawn, wKnight;
    private ImageIcon bRook, bBishop, bQueen, bKing, bPawn, bKnight;


    private boolean firstTurnFlag;
    private int fromRow;
    private int toRow;
    private int fromCol;
    private int toCol;
    // declare other instance variables as needed

    private listener listener;
    private undoListener undoListener;

    public ChessPanel() {

        model = new ChessModel();
        board = new JButton[model.numRows()][model.numColumns()];
        listener = new listener();
        undoListener = new undoListener();
        createIcons();

        JPanel boardpanel = new JPanel();
        JPanel buttonpanel = new JPanel();
        boardpanel.setLayout(new GridLayout(model.numRows(), model.numColumns(), 1, 1));

        for (int r = 0; r < model.numRows(); r++) {
            for (int c = 0; c < model.numColumns(); c++) {
                if (model.pieceAt(r, c) == null) {
                    board[r][c] = new JButton("", null);
                    board[r][c].addActionListener(listener);
                } else if (model.pieceAt(r, c).player() == Player.WHITE) {
                    placePieces(r, c);
                } else if (model.pieceAt(r, c).player() == Player.BLACK) {
                    placePieces(r, c);
                }

                setBackGroundColor(r, c);
                boardpanel.add(board[r][c]);
            }
        }
        add(boardpanel, BorderLayout.WEST);
        boardpanel.setPreferredSize(new Dimension(600, 600));
        add(buttonpanel, BorderLayout.SOUTH);

        undo = new JButton("Undo");
        undo.addActionListener(undoListener);
        buttonpanel.add(undo);
        firstTurnFlag = true;


    }

    private void setBackGroundColor(int r, int c) {
        if ((c % 2 == 1 && r % 2 == 0) || (c % 2 == 0 && r % 2 == 1)) {
            board[r][c].setBackground(Color.LIGHT_GRAY);
        } else if ((c % 2 == 0 && r % 2 == 0) || (c % 2 == 1 && r % 2 == 1)) {
            board[r][c].setBackground(Color.WHITE);
        }
    }

    private void placePieces(int r, int c) {

        if (model.pieceAt(r, c).type().equals("p3.Pawn")) {
            if(model.pieceAt(r, c).player() == Player.WHITE) {
                board[r][c] = new JButton(null, wPawn);
                board[r][c].addActionListener(listener);
            }
            else {
                board[r][c] = new JButton(null, bPawn);
                board[r][c].addActionListener(listener);
            }
        }

        if (model.pieceAt(r, c).type().equals("p3.Rook")) {
            if(model.pieceAt(r, c).player() == Player.WHITE) {
                board[r][c] = new JButton(null, wRook);
                board[r][c].addActionListener(listener);
            }
            else {
                board[r][c] = new JButton(null, bRook);
                board[r][c].addActionListener(listener);
            }
        }

        if (model.pieceAt(r, c).type().equals("p3.Knight")) {
            if(model.pieceAt(r, c).player() == Player.WHITE) {
                board[r][c] = new JButton(null, wKnight);
                board[r][c].addActionListener(listener);
            }
            else {
                board[r][c] = new JButton(null, bKnight);
                board[r][c].addActionListener(listener);
            }
        }

        if (model.pieceAt(r, c).type().equals("p3.Bishop")) {
            if(model.pieceAt(r, c).player() == Player.WHITE) {
                board[r][c] = new JButton(null, wBishop);
                board[r][c].addActionListener(listener);
            }
            else {
                board[r][c] = new JButton(null, bBishop);
                board[r][c].addActionListener(listener);
            }
        }

        if (model.pieceAt(r, c).type().equals("p3.Queen")) {
            if(model.pieceAt(r, c).player() == Player.WHITE) {
                board[r][c] = new JButton(null, wQueen);
                board[r][c].addActionListener(listener);
            }
            else {
                board[r][c] = new JButton(null, bQueen);
                board[r][c].addActionListener(listener);
            }
        }

        if (model.pieceAt(r, c).type().equals("p3.King")) {
            if(model.pieceAt(r, c).player() == Player.WHITE) {
                board[r][c] = new JButton(null, wKing);
                board[r][c].addActionListener(listener);
            }
            else {
                board[r][c] = new JButton(null, bKing);
                board[r][c].addActionListener(listener);
            }
        }
    }

    private void createIcons() {
        // Sets the Image for white player pieces
        wRook = new ImageIcon(getClass().getResource("wRook.png"));
        wBishop = new ImageIcon(getClass().getResource("wBishop.png"));
        wQueen = new ImageIcon(getClass().getResource("wQueen.png"));
        wKing = new ImageIcon(getClass().getResource("wKing.png"));
        wPawn = new ImageIcon(getClass().getResource("wPawn.png"));
        wKnight = new ImageIcon(getClass().getResource("wKnight.png"));

        bRook = new ImageIcon(getClass().getResource("bRook.png"));
        bBishop = new ImageIcon(getClass().getResource("bBishop.png"));
        bQueen = new ImageIcon(getClass().getResource("bQueen.png"));
        bKing = new ImageIcon(getClass().getResource("bKing.png"));
        bPawn = new ImageIcon(getClass().getResource("bPawn.png"));
        bKnight = new ImageIcon(getClass().getResource("bKnight.png"));
    }

    // method that updates the board
    private void displayBoard() {

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++)
                if (model.pieceAt(r, c) == null)
                    board[r][c].setIcon(null);
                else if(model.pieceAt(r, c).player() == Player.WHITE) {
                    if (model.pieceAt(r, c).type().equals("p3.Pawn"))
                        board[r][c].setIcon(wPawn);

                    if (model.pieceAt(r, c).type().equals("p3.Rook"))
                        board[r][c].setIcon(wRook);

                    if (model.pieceAt(r, c).type().equals("p3.Knight"))
                        board[r][c].setIcon(wKnight);

                    if (model.pieceAt(r, c).type().equals("p3.Bishop"))
                        board[r][c].setIcon(wBishop);

                    if (model.pieceAt(r, c).type().equals("p3.Queen"))
                        board[r][c].setIcon(wQueen);

                    if (model.pieceAt(r, c).type().equals("p3.King"))
                        board[r][c].setIcon(wKing);
                }
                else if(model.pieceAt(r, c).player() == Player.BLACK) {
                    if (model.pieceAt(r, c).type().equals("p3.Pawn"))
                        board[r][c].setIcon(bPawn);

                    if (model.pieceAt(r, c).type().equals("p3.Rook"))
                        board[r][c].setIcon(bRook);

                    if (model.pieceAt(r, c).type().equals("p3.Knight"))
                        board[r][c].setIcon(bKnight);

                    if (model.pieceAt(r, c).type().equals("p3.Bishop"))
                        board[r][c].setIcon(bBishop);

                    if (model.pieceAt(r, c).type().equals("p3.Queen"))
                        board[r][c].setIcon(bQueen);

                    if (model.pieceAt(r, c).type().equals("p3.King"))
                        board[r][c].setIcon(bKing);
                }

        }
        repaint();
    }

    private class undoListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if(undo == event.getSource()){

                if(model.prevMoveExists()){
                    // Undoes the previous move
                    model.undo();

                    // Toggles Players
                    model.setNextPlayer();

                    displayBoard();
                }

            }

        }
    }



    // inner class that represents action listener for buttons
    private class listener implements ActionListener {
        public void actionPerformed(ActionEvent event) {


            for (int r = 0; r < model.numRows(); r++)
                for (int c = 0; c < model.numColumns(); c++)
                    if (board[r][c] == event.getSource())
                        if (firstTurnFlag == true) {
                            fromRow = r;
                            fromCol = c;
                            firstTurnFlag = false;

                            // Sets back ground of selected tile
                            board[r][c].setBackground(Color.GREEN);

                            // Piece movement diagnostic
                            for (int rr = 0; rr < model.numRows(); rr++) {
                                for (int cc = 0; cc < model.numColumns(); cc++) {
                                    Move m = new Move(r, c, rr,cc);

                                    if(model.isValidMove(m)){
                                        board[rr][cc].setBackground(Color.BLUE);
                                    }
                                }
                                }

                        } else {
                            toRow = r;
                            toCol = c;
                            int lookingForCastle = c;
                            firstTurnFlag = true;
                            Move m = new Move(fromRow, fromCol, toRow, toCol);

                            if ((model.isValidMove(m)) == true){
                                System.out.println(model.pieceAt(fromRow,fromCol).getMoveCount());
                                if(m.toColumn != lookingForCastle){
                                    if (m.toRow == 7 && m.toColumn == 6 && model.pieceAt(r, c).type().equals("p3.Rook")){
                                        Move castleMove = new Move(7, 7, 7, 5);
                                        model.move(castleMove);
                                    }
                                }
                                if(m.toColumn != lookingForCastle){
                                    if (m.toRow == 7 && m.toColumn == 2 && model.pieceAt(r, c).type().equals("p3.Rook")){
                                        Move castleMove = new Move(7, 0, 7, 3);
                                        model.move(castleMove);
                                    }
                                }
                                if(m.toColumn != lookingForCastle){
                                    if (m.toRow == 0 && m.toColumn == 6 && model.pieceAt(r, c).type().equals("p3.Rook")){
                                        Move castleMove = new Move(0, 7, 0, 5);
                                        model.move(castleMove);
                                    }
                                }
                                if(m.toColumn != lookingForCastle){
                                    if (m.toRow == 0 && m.toColumn == 2 && model.pieceAt(r, c).type().equals("p3.Rook")){
                                        Move castleMove = new Move(0, 0, 0, 3);
                                        model.move(castleMove);
                                    }
                                }
                                model.move(m);
                                model.undoCheck();
                                //Promotion!
                                String promotion = "";
                                boolean didIPromote = false;
                                if(!model.inCheck(model.currentPlayer())) {
                                    if (model.currentPlayer() == Player.WHITE) {
                                        if (model.pieceAt(m.toRow, m.toColumn).type().equals("p3.Pawn")) {
                                            if (m.toRow == 0){
                                                while(!promotion.equals("Queen") && !promotion.equals("Knight") && !promotion.equals("Rook") && !promotion.equals("Bishop")) {
                                                    try {
                                                        promotion = JOptionPane.showInputDialog(null,
                                                                "What new piece do you want?\n" +
                                                                        "Queen, Knight, Rook, Bishop");
                                                        if (promotion.length() > 2) {
                                                            promotion = promotion.substring(0, 1).toUpperCase() + promotion.substring(1).toLowerCase();
                                                        }
                                                    } catch (IllegalArgumentException e) {
                                                        throw e;
                                                    }
                                                    if (!promotion.equals("Queen") && !promotion.equals("Knight") && !promotion.equals("Rook") && !promotion.equals("Bishop")) {
                                                        JOptionPane.showMessageDialog(null, "Give me a proper answer");
                                                    }
                                                }
                                                didIPromote = true;
                                            }
                                        }
                                    } else {
                                        if (model.pieceAt(m.toRow, m.toColumn).type().equals("p3.Pawn")) {
                                            if (m.toRow == 7){
                                                while(!promotion.equals("Queen") && !promotion.equals("Knight") && !promotion.equals("Rook") && !promotion.equals("Bishop")) {
                                                    try {
                                                        promotion = JOptionPane.showInputDialog(null,
                                                                "What new piece do you want?\n" +
                                                                        "Queen, Knight, Rook, Bishop");
                                                        if(promotion.length() > 2) {
                                                            promotion = promotion.substring(0, 1).toUpperCase() + promotion.substring(1).toLowerCase();
                                                        }
                                                    }catch(IllegalArgumentException e){
                                                        throw e;
                                                    }
                                                    if(!promotion.equals("Queen") && !promotion.equals("Knight") && !promotion.equals("Rook") && !promotion.equals("Bishop")){
                                                        JOptionPane.showMessageDialog(null, "Give me a proper answer");
                                                    }
                                                }
                                                didIPromote = true;

                                            }
                                        }
                                    }
                                }
                                if(didIPromote == true) {
                                    IChessPiece promotedPawn = new King(model.currentPlayer());
                                    if (promotion.equals("Queen")) {
                                        promotedPawn = new Queen(model.currentPlayer());
                                    }
                                    if (promotion.equals("Knight")) {
                                        promotedPawn = new Knight(model.currentPlayer());
                                    }
                                    if (promotion.equals("Rook")) {
                                        promotedPawn = new Rook(model.currentPlayer());
                                    }
                                    if (promotion.equals("Bishop")) {
                                        promotedPawn = new Bishop(model.currentPlayer());
                                    }
                                    model.setPiece(m.toRow, m.toColumn, promotedPawn);
                                }
                                model.pawnPromotionCheck(didIPromote);
                                model.setNextPlayer();

                                if(model.isComplete()) {
                                    JOptionPane.showMessageDialog(null, "Checkmate!");

                                }
                            }

                            // Resets selected background
                            setBackGroundColor(fromRow,fromCol);


                            // Piece movement diagnostic
                            for (int rr = 0; rr < model.numRows(); rr++) {
                                for (int cc = 0; cc < model.numColumns(); cc++) {
                                    setBackGroundColor(rr,cc);
                                }
                                }

                            displayBoard();
                        }
        }
    }
}
