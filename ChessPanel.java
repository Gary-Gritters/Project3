package p3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessPanel extends JPanel {

    private JButton[][] board;
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

    public ChessPanel() {
        model = new ChessModel();
        board = new JButton[model.numRows()][model.numColumns()];
        listener = new listener();
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
        add(buttonpanel);
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
        wRook = new ImageIcon(getClass().getResource("/p3/wRook.png"));
        wBishop = new ImageIcon(getClass().getResource("/p3/wBishop.png"));
        wQueen = new ImageIcon(getClass().getResource("/p3/wQueen.png"));
        wKing = new ImageIcon(getClass().getResource("/p3/wKing.png"));
        wPawn = new ImageIcon(getClass().getResource("/p3/wPawn.png"));
        wKnight = new ImageIcon(getClass().getResource("/p3/wKnight.png"));

        bRook = new ImageIcon(getClass().getResource("/p3/bRook.png"));
        bBishop = new ImageIcon(getClass().getResource("/p3/bBishop.png"));
        bQueen = new ImageIcon(getClass().getResource("/p3/bQueen.png"));
        bKing = new ImageIcon(getClass().getResource("/p3/bKing.png"));
        bPawn = new ImageIcon(getClass().getResource("/p3/bPawn.png"));
        bKnight = new ImageIcon(getClass().getResource("/p3/bKnight.png"));
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

                        } else {
                            toRow = r;
                            toCol = c;
                            firstTurnFlag = true;
                            Move m = new Move(fromRow, fromCol, toRow, toCol);

                            if ((model.isValidMove(m)) == true) {
                                model.move(m);
                            }

                            // Resets selected background
                            setBackGroundColor(fromRow,fromCol);
                            displayBoard();
                        }
        }
    }
}
