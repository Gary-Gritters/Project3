package p3;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/*****************************************************************
 * Unit testing for ChessModel class.
 *
 * @author Jacob Dec, Ross Kuiper, Gary Gritters
 * @Verson Winter 2019
 *****************************************************************/
public class ChessModelTest {

    /******************************************************************
     * Tests the ChessModel default constructor
     ******************************************************************/
    @Test
    public void modelConstructor() {

        // Creates new ChessModel
        ChessModel model;
        model = new ChessModel();

        // Creates new move, moving white pawn by 2
        Move testmove = new Move(6,6,4,6);

        // Checks if this is a valid move
        assertTrue(model.isValidMove(testmove));
    }


    /******************************************************************
     * Tests the ChessModel move method
     ******************************************************************/
    @Test
    public void move() {
        ChessModel model;
        model = new ChessModel();

        // Creates new move, moving white pawn by 2
        Move testmove = new Move(6,6,4,6);

        // Checks if this is a valid move
        assertTrue(model.isValidMove(testmove));

        // Makes the move
        model.move(testmove);

        // Changes player
        model.setNextPlayer();

        // Checks if repeating this is a valid move
        assertFalse(model.isValidMove(testmove));
    }


    /******************************************************************
     * Tests the en Passant move and undo functionality
     ******************************************************************/
    @Test
    public void enPassantTest() {
        // Creates new ChessModel
        ChessModel model;
        model = new ChessModel();

        // Series of moves that results in a White En Passant
        Move testmove = new Move(6,6,4,6);
        model.move(testmove);
        model.setNextPlayer();

        testmove = new Move(1,1,2,1);
        model.move(testmove);
        model.setNextPlayer();

        testmove = new Move(4,6,3,6);
        model.move(testmove);
        model.setNextPlayer();

        testmove = new Move(1,5,3,5);
        model.move(testmove);
        model.setNextPlayer();

        testmove = new Move(3,6,2,5);
        model.move(testmove);
        model.setNextPlayer();

        // Undoes the en passant move
        model.undo();


        // Resets the ChessModel
        model = new ChessModel();

        // Series of moves that results in a Black En Passant
        testmove = new Move(6,6,4,6);
        model.move(testmove);
        model.setNextPlayer();

        testmove = new Move(1,1,3,1);
        model.move(testmove);
        model.setNextPlayer();

        testmove = new Move(4,6,3,6);
        model.move(testmove);
        model.setNextPlayer();

        testmove = new Move(3,1,4,1);
        model.move(testmove);
        model.setNextPlayer();

        testmove = new Move(6,2,4,2);
        model.move(testmove);
        model.setNextPlayer();

        testmove = new Move(4,1,5,2);
        model.move(testmove);
        model.setNextPlayer();

        // Undoes the en passant move
        model.undo();
    }


    /******************************************************************
     * Tests the ChessModel AI method
     ******************************************************************/
    @Test
    public void testAI() {

        // Creates new ChessModel
        ChessModel model;
        model = new ChessModel();

        // Counts the number of moves taken
        int counter = 0;

        //Runs through the first 6 moves of 500 AI ran games
        for(int i=0; i < 500; i++){

            //Continues while not in checkmate or met counter limit
            while (!model.isComplete() && counter < 6) {

                // While White is not in checkmate
                if (!model.isComplete()) {
                    model.AI(Player.WHITE);
                }

                // While Black is not in checkmate
                if (!model.isComplete()) {
                    model.AI(Player.BLACK);
                }

                counter++;
            }

            counter = 0;
        }
    }


    /******************************************************************
     * Tests the ChessModel prevMoveExists method
     ******************************************************************/
    @Test
    public void prevMoveExists() {

        // Creates new ChessModel
        ChessModel model;
        model = new ChessModel();

        assertFalse(model.prevMoveExists());

        Move testmove = new Move(0,1,0,2);
        model.move(testmove);

        assertTrue(model.prevMoveExists());
    }


    /******************************************************************
     * Tests the ChessModel check method
     ******************************************************************/
    @Test
    public void check() {

        // Creates new ChessModel
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
     * Tests the ChessModel handling of Castling
     ******************************************************************/
    @Test
    public void castling() {

        // Creates new ChessModel
        ChessModel model;
        model = new ChessModel();

        // Series of moves resulting in a castling move
        Move testmove = new Move(6,6,5,6);
        model.move(testmove);
        model.setNextPlayer();

        testmove = new Move(0,1,2,0);
        model.move(testmove);
        model.setNextPlayer();

        testmove = new Move(7,5,6,6);
        model.move(testmove);
        model.setNextPlayer();

        testmove = new Move(2,0,0,1);
        model.move(testmove);
        model.setNextPlayer();

        testmove = new Move(7,6,5,7);
        model.move(testmove);
        model.setNextPlayer();

        testmove = new Move(0,1,2,0);
        model.move(testmove);
        model.setNextPlayer();

        testmove = new Move(7,4,7,7);
        assertTrue(model.isValidMove(testmove));
        model.move(testmove);
        model.setNextPlayer();

        // Undoes Castling
        model.undo();
    }


    /******************************************************************
     * Tests the ChessModel handling of promotion
     ******************************************************************/
    @Test
    public void promotion() {

        // Creates new ChessModel
        ChessModel model;

        model = new ChessModel();

        // White moves
        Move testmove = new Move(6,5,0,6);
        model.move(testmove);

        assertTrue(model.pieceAt(0,6).type().equals("p3.Pawn"));

        // Moves white pawn to last black row and promotes
        model.setPiece(0,6, model.pieceAt(7,3));
        model.promotePop();
        model.pawnPromotionCheck(true);
        model.setNextPlayer();

        assertTrue(model.pieceAt(0,6).type().equals("p3.Queen"));

        // Undoes Promotion
        model.undo();

        assertTrue(model.pieceAt(0,6).type().equals("p3.Knight"));

        model.pawnPromotionCheck(true);
        model.promotePop();


        //Creates new model for black promotion
        ChessModel model1 = new ChessModel();


        // Moves black pawn to last white row and promotes
        assertTrue(model1.pieceAt(7,2).type().equals("p3.Bishop"));
        testmove = new Move(1,2,7,2);

        model1.move(testmove);
        model1.setPiece(7,2, model1.pieceAt(0,3));
        assertTrue(model1.pieceAt(7,2).type().equals("p3.Queen"));

        // Promotes Pawn
        model1.promotePop();
        model1.pawnPromotionCheck(true);
        model1.setNextPlayer();

        //Undoes Promotion
        model1.undo();
        assertTrue(model1.pieceAt(7,2).type().equals("p3.Bishop"));

        model1.pawnPromotionCheck(true);
        model1.promotePop();
    }


    /******************************************************************
     * Tests the ChessModel isValidMove method
     ******************************************************************/
    @Test
    public void isValidMove() {

        // Creates new ChessModel
        ChessModel model;
        model = new ChessModel();

        assertFalse(model.isValidMove(new Move(0,1,1,0)));

        assertTrue(model.isValidMove(new Move(6,0,5,0)));
    }


    /******************************************************************
     * Tests the ChessModel isComplete method
     ******************************************************************/
    @Test
    public void isComplete() {

        // Creates new ChessModel
        ChessModel model;
        model = new ChessModel();

        assertFalse(model.isComplete());

        // Series of moves resulting in checkmate
        Move testmove = new Move(6,5,5,5);
        model.move(testmove);

        testmove = new Move(6,6,4,6);
        model.move(testmove);

        model.setNextPlayer();

        testmove = new Move(0,3,4,7);
        model.move(testmove);

        model.setNextPlayer();

        // Checks for checkmate
        assertTrue(model.isComplete());

        model.setNextPlayer();
        model.setNextPlayer();

        testmove = new Move(7,2,6,7);
        model.move(testmove);

        assertFalse(model.isComplete());
    }


    /******************************************************************
     * Tests the ChessModel numRows method
     ******************************************************************/
    @Test
    public void testNumRows() {

        // Creates new ChessModel
        ChessModel model;
        model = new ChessModel();

        assertTrue(model.numRows()==8);
    }


    /******************************************************************
     * Tests the ChessModel numColumns method
     ******************************************************************/
    @Test
    public void testNumCols() {

        // Creates new ChessModel
        ChessModel model;
        model = new ChessModel();

        assertTrue(model.numColumns()==8);
    }


    /******************************************************************
     * Tests the ChessModel setPiece method
     ******************************************************************/
    @Test
    public void setpiece() {

        // Creates new ChessModel
        ChessModel model;
        model = new ChessModel();

        assertTrue(model.pieceAt(4,4)==null);

        model.setPiece(4,4,new Bishop(Player.WHITE));

        assertTrue(model.pieceAt(4,4).type().equals("p3.Bishop"));
    }
}
