package Display;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;


public class GameBoard extends JPanel {

        /* Attributes dealing with creating the board */
        Color backgroundColor, positionColor;
        int boardLength;
        int frameWidth, frameHeight;
        int boardStartX, boardStartY;
        int[] xCoords, yCoords;
        ArrayList<PositionButton> positionButtons;

        /* Attributes dealing with initialising and updating the token display */
        Color p1TokenColor, p2TokenColor;
        String p1TokenImgPath = "main/Display/img/copper panelled circle.png";
        String p2TokenImgPath = "main/Display/img/cyan circle.png";
        String settingsImgPath = "main/Display/img/settings.png";
        int NUM_TOKENS_TO_PLACE = 9;
        JLabel p1CountToPlace;
        JLabel p2CountToPlace;
        JLabel p1CountRemoved;
        JLabel p2CountRemoved;

        /* Attributes dealing with the turn information */
        JLabel turnText;
        JLabel helperText;

        /**
         * Constructor for the GameBoard class. Creates the board and all of the buttons on it.
         * @param _backgroundColor the background color of the board
         * @param _positionColor the color of the positions on the board
         * @param _boardLength the length of the board
         * @param _frameWidth the width of the frame
         * @param _frameHeight the height of the frame
         */
        GameBoard(Color _backgroundColor, Color _positionColor, int _boardLength, int _frameWidth, int _frameHeight) {

                this.boardLength = _boardLength;
                this.positionColor = _positionColor;
                this.backgroundColor = _backgroundColor;
                this.frameWidth = _frameWidth;
                this.frameHeight = _frameHeight;
                this.setLayout(null);

                // Calculate board boundary
                boardStartX = (frameWidth - boardLength) / 8;
                boardStartY = (frameHeight - boardLength) / 8; // want more space below the board than above it (to
                                                               // display turn
                                                               // info)
                // Used as coordinates for the rest of the method
                xCoords = new int[] { boardStartX + boardLength / 16,
                                boardStartX + 3 * boardLength / 16,
                                boardStartX + 5 * boardLength / 16,
                                boardStartX + boardLength / 2,
                                boardStartX + boardLength - 5 * boardLength / 16,
                                boardStartX + boardLength - 3 * boardLength / 16,
                                boardStartX + boardLength - boardLength / 16 };
                yCoords = new int[] { boardStartY + boardLength / 16,
                                boardStartY + 3 * boardLength / 16,
                                boardStartY + 5 * boardLength / 16,
                                boardStartY + boardLength / 2,
                                boardStartY + boardLength - 5 * boardLength / 16,
                                boardStartY + boardLength - 3 * boardLength / 16,
                                boardStartY + boardLength - boardLength / 16 };

                // inialise token count labels 
                p1CountToPlace = new JLabel();
                p2CountToPlace = new JLabel();
                p1CountRemoved = new JLabel();
                p2CountRemoved = new JLabel();

        }

        /**
         * This method initialises the labels that display information about which player's turn it is 
         * and places them on the screen
         * @return an ArrayList of the JLabels that display the turn information
         */
        public ArrayList<JLabel> initialiseTurnText() {
                JPanel turnTextPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, boardLength/32, boardLength/32));
                /* Adding the token image */
                JLabel p1Token = new JLabel();
                p1Token.setIcon(new ImageIcon(p1TokenImgPath));
                turnTextPanel.add(p1Token);
                /* Adding the turn text */
                turnText = new JLabel("Player 1's Turn");
                turnText.setFont(new Font("Georgia", Font.ITALIC, 35));
                turnTextPanel.add(turnText);
                /* Positioning the token information on the frame */
                turnTextPanel.setBounds(frameWidth/16, boardStartY+boardLength+frameHeight/64, frameWidth*7/8, frameHeight/8);
                this.add(turnTextPanel);

                ArrayList<JLabel> turnTextComponents = new ArrayList<JLabel>();
                turnTextComponents.add(p1Token);
                turnTextComponents.add(turnText);

                return turnTextComponents;
        }

        /**
         * This method initialises the labels that display information about how many tokens each player has left to place
         * and places them on the screen.  
         * @return an ArrayList of the JLabels that display the token information
         */
        public JLabel initialiseHelperText() {
                JPanel helperTextPanel = new JPanel(new BorderLayout());
                helperText = new JLabel();
                helperText.setFont(new Font("Georgia", Font.ITALIC, 35));
                helperTextPanel.setBounds(boardStartX + boardLength + boardLength/32, boardStartY + boardLength/4, boardLength/2, boardLength/2);
                helperTextPanel.add(helperText);
                helperTextPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.black));
                this.add(helperTextPanel);
                return helperText;
        }

        /**
         * This method initialises the buttons for each position on the board and places them on the screen.
         */
        public void initialisePositionButtons() {
                /* Positions (numbered from 0-23 by top-bottom -> left-right) */
                int positionRadius = boardLength / 32;
                positionButtons = new ArrayList<PositionButton>();

                positionButtons.add(new PositionButton(xCoords[0], yCoords[0], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[3], yCoords[0], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[6], yCoords[0], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[1], yCoords[1], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[3], yCoords[1], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[5], yCoords[1], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[2], yCoords[2], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[3], yCoords[2], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[4], yCoords[2], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[0], yCoords[3], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[1], yCoords[3], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[2], yCoords[3], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[4], yCoords[3], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[5], yCoords[3], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[6], yCoords[3], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[2], yCoords[4], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[3], yCoords[4], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[4], yCoords[4], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[1], yCoords[5], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[3], yCoords[5], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[5], yCoords[5], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[0], yCoords[6], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[3], yCoords[6], positionRadius,
                                positionColor, backgroundColor));
                positionButtons.add(new PositionButton(xCoords[6], yCoords[6], positionRadius,
                                positionColor, backgroundColor));
        }

        /**
         * This method initialises the labels that display the number of tokens each player has left to place and places them on the screen.
         * @return an ArrayList of the JLabels that display the token information
         */
        public ArrayList<JLabel> initialiseTokenCounts() {
                
                JPanel tokenCountToPlacePanel = new JPanel(new GridLayout(2, 1));
                tokenCountToPlacePanel.setBounds(boardStartX + boardLength + boardLength/32, boardStartY, boardLength/2, boardLength/4);
                JPanel tokenCountToPlaceDisplay = createCounterDisplay(boardStartY, NUM_TOKENS_TO_PLACE, p1CountToPlace, p2CountToPlace);
                JLabel tokenCountToPlaceText = new JLabel("Number of tokens to place");
                tokenCountToPlaceText.setFont(new Font("Georgia", Font.ITALIC, 30));
                tokenCountToPlaceText.setHorizontalAlignment(JLabel.CENTER);
                tokenCountToPlaceText.setVerticalAlignment(JLabel.TOP);
                tokenCountToPlacePanel.add(tokenCountToPlaceDisplay);
                tokenCountToPlacePanel.add(tokenCountToPlaceText);
                this.add(tokenCountToPlacePanel);

                JPanel tokenCountRemovedPanel = new JPanel(new GridLayout(2, 1));
                tokenCountRemovedPanel.setBounds(boardStartX + boardLength + boardLength/32, boardStartY + boardLength - boardLength/4, boardLength/2, boardLength/4);
                JPanel tokenCountRemovedDisplay = createCounterDisplay(boardStartY, 0, p1CountRemoved, p2CountRemoved);
                JLabel tokenCountRemovedText = new JLabel("Number of tokens removed");
                tokenCountRemovedText.setFont(new Font("Georgia", Font.ITALIC, 30));
                tokenCountRemovedText.setHorizontalAlignment(JLabel.CENTER);
                tokenCountRemovedText.setVerticalAlignment(JLabel.BOTTOM);
                tokenCountRemovedPanel.add(tokenCountRemovedText);
                tokenCountRemovedPanel.add(tokenCountRemovedDisplay);
                this.add(tokenCountRemovedPanel);

                ArrayList<JLabel> tokenCountComponents = new ArrayList<JLabel>();
                tokenCountComponents.add(p1CountToPlace);
                tokenCountComponents.add(p2CountToPlace);
                tokenCountComponents.add(p1CountRemoved);
                tokenCountComponents.add(p2CountRemoved);

                return tokenCountComponents;
        }

        /**
         * This method creates the JPanel that displays the number of tokens each player has left to place or has removed.
         * @param yStartPosition the y-coordinate of the top of the JPanel
         * @param startingCount the number of tokens the player has left to place or has removed
         * @param p1Count the JLabel that displays the number of tokens Player 1 has left to place or has removed
         * @param p2Count the JLabel that displays the number of tokens Player 2 has left to place or has removed
         * @return
         */
        private JPanel createCounterDisplay(int yStartPosition, int startingCount, JLabel p1Count, JLabel p2Count) {
                JPanel tokenCount = new JPanel(new FlowLayout(FlowLayout.CENTER, boardLength/32, boardLength/32));
                /* Creating Player 1's items */
                JLabel p1Token = new JLabel();
                p1Token.setIcon(new ImageIcon(p1TokenImgPath));
                tokenCount.add(p1Token);
                p1Count.setText(String.valueOf(startingCount)+"/"+NUM_TOKENS_TO_PLACE);
                p1Count.setFont(new Font("Georgia", Font.ITALIC, 40));
                tokenCount.add(p1Count);
                /* Creating Player 2's items */
                JLabel p2Token = new JLabel();
                p2Token.setIcon(new ImageIcon(p2TokenImgPath));
                tokenCount.add(p2Token);
                p2Count.setText(String.valueOf(startingCount)+"/"+NUM_TOKENS_TO_PLACE);
                p2Count.setFont(new Font("Georgia", Font.ITALIC, 40));
                tokenCount.add(p2Count);
                /* Positioning the token information on the frame */
                tokenCount.setBounds(boardStartX + boardLength + boardLength/32, yStartPosition, boardLength/2, boardLength/8);
                return tokenCount;
        }

        @Override
        protected void paintComponent(Graphics g) {

                Graphics2D g2D = (Graphics2D) g;

                /* Board background */
                g2D.setPaint(backgroundColor);
                g2D.fillRect(boardStartX, boardStartY, boardLength, boardLength);

                /* Sets color of lines and positions */
                g2D.setPaint(positionColor);
                g2D.setStroke(new BasicStroke(5));

                /* Board outer ring */
                g2D.drawRect(xCoords[0], yCoords[0], boardLength - boardLength / 8, boardLength - boardLength / 8);

                /* Board middle ring */
                g2D.drawRect(xCoords[1], yCoords[1], boardLength - 3 * boardLength / 8,
                                boardLength - 3 * boardLength / 8);

                /* Board inner ring */
                g2D.drawRect(xCoords[2], yCoords[2], boardLength - 5 * boardLength / 8,
                                boardLength - 5 * boardLength / 8);

                /* Perpendicular lines */
                g2D.drawLine(xCoords[3], yCoords[2], xCoords[3], yCoords[0]); // top
                g2D.drawLine(xCoords[3], yCoords[4], xCoords[3], yCoords[6]); // bottom
                g2D.drawLine(xCoords[0], yCoords[3], xCoords[2], yCoords[3]); // left
                g2D.drawLine(xCoords[4], yCoords[3], xCoords[6], yCoords[3]); // right

        }

        /**
         * This method returns the ArrayList of PositionButtons that are on the board.
         * @return the ArrayList of PositionButtons that are on the board
         */
        public ArrayList<PositionButton> getPositionButtons() {
                return positionButtons;
        }


        public JLabel initialiseSettingsButton(){
                JLabel settingsButton = new SettingsButton();
                this.add(settingsButton);
                return settingsButton;
        }
}
