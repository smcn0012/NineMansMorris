package Display;

import javax.swing.*;

/**
 * This class is used to create the game screen.
 * @see JFrame
 */
public class GameScreen extends JFrame {

    /**
     * This constructor is used to create the game screen.
     */
    GameScreen() {
        this.setTitle("Nine Men's Morris"); // sets title of frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit out of application
        this.setResizable(false); // prevent frame from being resized
        this.setSize( 1400, 1000 ); //Set the width and height of the JFrame.
        ImageIcon image = new ImageIcon("main/Display/img/nine_mens_morris.jpg"); // create an ImageIcon
        this.setIconImage(image.getImage()); // change icon of frame
    }
}