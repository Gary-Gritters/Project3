package p3;

import javax.swing.*;
import java.awt.*;

/**
 * This class displays the GUI for the Chess game.
 * @author Gary Gritters, Jacob Dec, and Ross Kuiper
 * @version 1.0
 * @date 3/26/2019
 */

public class ChessGUI {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ChessPanel panel = new ChessPanel();
        frame.getContentPane().add(panel);

        frame.setResizable(true);
        frame.setPreferredSize(new Dimension(800, 637));
        frame.pack();
        frame.setVisible(true);
    }
}
