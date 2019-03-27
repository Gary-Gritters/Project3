
package p3;

import org.junit.Test;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;



public class ChessModelTest {



    /******************************************************************
     *
     ******************************************************************/
    @Test
    public void modelConstructor() {
        ChessModel model;

        model = new ChessModel();

    }

    /******************************************************************
     *
     ******************************************************************/
    @Test
    public void move() {
        ChessModel model;

        model = new ChessModel();

        Move testmove = new Move(0,1,0,2);
        model.move(testmove);

    }

    /******************************************************************
     *
     ******************************************************************/
    @Test
    public void prevMoveExists() {
        ChessModel model;

        model = new ChessModel();

        assertFalse(model.prevMoveExists());

        Move testmove = new Move(0,1,0,2);
        model.move(testmove);

        assertTrue(model.prevMoveExists());

    }

    /******************************************************************
     *
     ******************************************************************/
    @Test
    public void check() {
        ChessModel model;

        model = new ChessModel();

        assertFalse(model.inCheck(model.currentPlayer()));
        System.out.println(model.inCheck(model.currentPlayer()));

        //White King to (2,0)
        Move testmove = new Move(7,4,2,0);
        model.move(testmove);

        assertTrue(model.inCheck(model.currentPlayer()));

        model.undoCheck();
    }

    /******************************************************************
     *
     ******************************************************************/
    @Test
    public void isValidMove() {
        ChessModel model;

        model = new ChessModel();

        assertFalse(model.isValidMove(new Move(0,1,1,0)));

        assertTrue(model.isValidMove(new Move(6,0,5,0)));

    }

    /******************************************************************
     *
     *
     *
     ******************************************************************/
    @Test
    public void isComplete() {
        ChessModel model;

        model = new ChessModel();

        assertFalse(model.isComplete());;


        Move testmove = new Move(6,5,5,5);
        model.move(testmove);

        testmove = new Move(6,6,4,6);
        model.move(testmove);

        model.setNextPlayer();

        testmove = new Move(0,3,4,7);
        model.move(testmove);

        model.setNextPlayer();


        assertTrue(model.isComplete());

        model.setNextPlayer();
        model.setNextPlayer();

        testmove = new Move(7,2,6,7);
        model.move(testmove);

        assertFalse(model.isComplete());



    }

    /******************************************************************
     *
     ******************************************************************/
    @Test
    public void rowscolumns() {
        ChessModel model;

        model = new ChessModel();

        assertTrue(model.numRows()==8);
        assertTrue(model.numColumns()==8);

    }

    /******************************************************************
     *
     ******************************************************************/
    @Test
    public void setpiece() {
        ChessModel model;

        model = new ChessModel();

        assertTrue(model.pieceAt(4,4)==null);

        model.setPiece(4,4,new Bishop(Player.WHITE));

        assertTrue(model.pieceAt(4,4).toString().substring(0,9).equals("p3.Bishop"));
    }



}
