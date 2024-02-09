package Display;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.*;

import Game.Game;
import Game.GameType;

import java.awt.event.*;

import Players.PlayerController;

/**
 * This class is used to control the display of the game. It is responsible for
 * initialising the display and updating the display when the game state
 * changes.
 * 
 * @see Game
 * @see GameScreen
 * @see GameBoard
 * @see PlayerController
 * @see MouseListener
 * @see MouseAdapter
 * @see MouseMotionListener
 */
public class DisplayController implements MouseListener {

    private static DisplayController instance;
    GameScreen screen;
    GameBoard board;
    int tokenRadius = 25;
    ArrayList<JLabel> tokenCountComponents; // [p1CountToPlace, p2CountToPlace, p1CountRemoved, p2CountRemoved]
    ArrayList<JLabel> turnTextComponents; // [token, text]
    JLabel helperText; // text which displays help to the player
    int NUM_POS = 24;
    int NUM_TOKENS = 9;
    ArrayList<Integer> highlightedPositions = new ArrayList<>(); // stores a list of all positions which are currently
                                                                 // highlighted
    ArrayList<Integer> newlyFormedMill = new ArrayList<>(); // stores a list of all positions which are in the most
                                                            // recently formed mill
    int tokenJustRemoved; // stores the position id of the position which just had its token removed (null
                          // if a token was not just removed)
    ArrayList<Integer> positionsOfPreviousMove = new ArrayList<>();; // stores the initial (if applicable) and final position ids of the
                                                // previous move/place action
    JLabel settingsButton;
    GameType gameType;

    /**
     * Private constructor called by getInstance method when instance is null
     */
    private DisplayController() {

        // initialise the screen and board
        screen = new GameScreen();

        board = new GameBoard(new Color(0xffcc84), Color.black, 800, 1400, 1000);
        settingsButton = board.initialiseSettingsButton();
        board.initialisePositionButtons();
        assignMouseListeners();
        tokenCountComponents = board.initialiseTokenCounts();
        turnTextComponents = board.initialiseTurnText();
        helperText = board.initialiseHelperText();

        screen.add(board);
        screen.setVisible(true);

        // Choosing a game mode
        presentGameModes();
    }

    /**
     * This method is used to get the instance of the DisplayController class.
     * 
     * @return The instance of the DisplayController class.
     */
    public static synchronized DisplayController getInstance() {
        if (instance == null) {
            instance = new DisplayController();
        }
        return instance;
    }

    /**
     * This method resets the display.
     */
    public void resetDisplay() {
        instance = null;
        screen.dispose();
    }

    private void presentGameModes() {
        String[] responses = { "Local Two Player", "AI Player" };
        int answer = JOptionPane.showOptionDialog(
                null,
                "Choose a game mode for your new game!",
                "Start a new game",
                JOptionPane.DEFAULT_OPTION,
                0,
                new ImageIcon(new ImageIcon("main/Display/img/nine_mens_morris.jpg").getImage().getScaledInstance(50,
                        50, -1)),
                responses,
                responses[0]);
        if (answer == 0 || answer == -1) {
            Game.setGameType(GameType.LOCAL_TWO_PLAYER);
        } else if (answer == 1) {
            Game.setGameType(GameType.AI_PLAYER);
        }
    }

    /**
     * This method adds a mouse listener to the position buttons on the board.
     */
    private void assignMouseListeners() {

        ArrayList<PositionButton> positionButtons = board.getPositionButtons();

        for (int i = 0; i < NUM_POS; i++) {
            board.add(positionButtons.get(i));
            positionButtons.get(i).addMouseListener(this);
        }

        settingsButton.addMouseListener(this);
    }

    /**
     * This method adds the token's image to the specified position.
     * 
     * @param positionId The position to add the token image to.
     * @param token      The token's image.
     */
    public void setPositionToken(int positionId, ImageIcon token) {
        /*
         * Used to set the token displayed on a specific position
         */
        PositionButton position = board.getPositionButtons().get(positionId);
        position.setIsCurrentToken(true); // tells the PositionButton that there is a token to render so that it won't
                                          // paint the button ontop of it
        /*
         * Create a new label for the player's token and place it ontop of the selected
         * position
         */
        JLabel positionToken = new JLabel();
        positionToken.setIcon(token);
        int[] tokenCoords = position.getCoords();
        positionToken.setBounds(tokenCoords[0] - tokenRadius, tokenCoords[1] - tokenRadius, 2 * tokenRadius,
                2 * tokenRadius);
        position.setToken(positionToken);
        board.add(positionToken);
        position.setIsHovering(false);
        position.repaint(); // updates the position as soon as a token is added, not just when the mouse
                            // leaves
    }

    /**
     * This method removes the token image from the specified position.
     * 
     * @param positionId The position to remove the token image from.
     */
    public void removePositionToken(int positionId) {
        /*
         * Used to remove the (if any) token at the specified position
         */
        PositionButton position = board.getPositionButtons().get(positionId);
        position.setIsCurrentToken(false); // tells the PositionButton that there is no token there, so paint the button
        board.remove(position.getToken());
        position.repaint();
    }

    /**
     * This method initialises the turn text components.
     * 
     * @param _turnTextComponents The turn text components.
     */
    public void initialiseTurnTextComponents(ArrayList<JLabel> _turnTextComponents) {
        /* Used to transfer the turn text components from Board to DisplayController */
        turnTextComponents = _turnTextComponents;
    }

    /**
     * This method changes the the token icon and the text to appropriatly reflect
     * the new player's turn.
     * 
     * @param token          the relative path to the image used for the current
     *                       player's token ("main/Display/X.png")
     * @param turnTextString the text to be displayed in the label, e.g. "Player 2's
     *                       Turn"
     */
    public void changeTurnText(ImageIcon token, String turnTextString) {
        /* Change turn token */
        JLabel turnToken = turnTextComponents.get(0);
        turnToken.setIcon(token);
        /* Change turn text */
        JLabel turnText = turnTextComponents.get(1);
        turnText.setText(turnTextString);
    }

    /**
     * This method changes the helper text to the specified string.
     * 
     * @param newHelperText The new helper text.
     */
    public void changeHelperText(String newHelperText) {
        helperText.setText(newHelperText);
    }

    /**
     * Decrements the token count displaying how many tokens each player has left to
     * place. Player's internal
     * count must be decremented before calling this method.
     * 
     * @param playerIndex The player's index.
     */
    public void decrementTokensToPlaceCount(int playerIndex) {
        JLabel tokenCountLabel = tokenCountComponents.get(playerIndex);
        int tokenCount = PlayerController.getInstance().getNumTokensToPlace(playerIndex);
        tokenCountLabel.setText(String.valueOf(tokenCount) + "/" + NUM_TOKENS);
    }

    /**
     * Increments the token count displaying how many tokens each player has had
     * removed. Player's internal
     * count must be decremented before calling this method.
     * 
     * @param playerIndex The player's index.
     */
    public void incrementTokensRemovedCount(int playerIndex) {
        JLabel tokenCountLabel = tokenCountComponents.get(2 + playerIndex);
        int tokenCount = PlayerController.getInstance().getTokensRemaining(playerIndex)
                + PlayerController.getInstance().getNumTokensToPlace(playerIndex);
        tokenCountLabel.setText(String.valueOf(NUM_TOKENS - tokenCount) + "/" + NUM_TOKENS);
    }

    /**
     * This method updates the highlighted positions on the board.
     * 
     * @param positionsToHighlight The positions to highlight.
     */
    public void updateHighlightedPositions(ArrayList<Integer> positionsToHighlight) {
        /* Remove highlights from old positions */
        PositionButton position;
        for (int positionId : highlightedPositions) {
            position = board.getPositionButtons().get(positionId);
            position.setIsHighlighted(false);
            position.repaint();
        }
        /* Create highlights for new positions */
        highlightedPositions = positionsToHighlight;
        for (int positionId : highlightedPositions) {
            position = board.getPositionButtons().get(positionId);
            position.setIsHighlighted(true);
            position.repaint();
        }
    }

    public void updateNewlyFormedMillPositions(ArrayList<Integer> positionsInMill) {
        /* Remove styling from old positions */
        PositionButton position;
        for (int positionId : newlyFormedMill) {
            position = board.getPositionButtons().get(positionId);
            position.setIsInMill(false);
            position.repaint();
        }
        /* Create styling for new positions */
        newlyFormedMill = positionsInMill;
        for (int positionId : newlyFormedMill) {
            position = board.getPositionButtons().get(positionId);
            position.setIsInMill(true);
            position.repaint();
        }
    }

    public void updatePreviousMoveIndicators(ArrayList<Integer> _positionsOfPreviousMove) {
        PositionButton position;
        /* Remove styling from old positions */
        for (int positionId : positionsOfPreviousMove) {
            position = board.getPositionButtons().get(positionId);
            position.setWasPreviousMove(false);
            position.repaint();
        }
        positionsOfPreviousMove = _positionsOfPreviousMove;
        /* Create styling for new positions */
        for (int positionId : positionsOfPreviousMove) {
            position = board.getPositionButtons().get(positionId);
            position.setWasPreviousMove(true);
            position.repaint();
        }
    }

    /**
     * Updates the removed token indicator
     * 
     * @param _tokenJustRemoved The index of the token which was just removed. -1 if
     *                          no token was just removed.
     */
    public void updateTokenRemovalIndicator(int _tokenJustRemoved) {
        /* Remove styling from old positions */
        PositionButton position; 
        if (tokenJustRemoved != -1) {
            position = board.getPositionButtons().get(tokenJustRemoved);
            position.setWasJustRemoved(false);
            position.repaint();
        }

        tokenJustRemoved = _tokenJustRemoved;
        /* Add styling to the new position */
        if (tokenJustRemoved == -1) {
            return;
        }
        position = board.getPositionButtons().get(tokenJustRemoved);
        position.setWasJustRemoved(true);
        position.repaint();

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (Game.getInstance().isInteractionDisabled()) {
            return;
        }
        if (e.getSource() == settingsButton) {
            new SettingsScreen();
            return;
        }
        ArrayList<PositionButton> positionButtons = board.getPositionButtons();
        /* Find the position that was pressed */
        int positionId = -1;
        for (int i = 0; i < NUM_POS; i++) {
            if (e.getSource() == positionButtons.get(i)) {
                positionId = i;
                break;
            }
        }

        /* Perform desired action */
        Game.getInstance().handlePositionClick(positionId);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

        /*
         * used to change the button's appearence if you hover over it
         */
        if (e.getSource() == settingsButton) {
            return;
        }
        ArrayList<PositionButton> positionButtons = board.getPositionButtons();
        /* Find the position that was pressed */
        int positionId = -1;
        for (int i = 0; i < NUM_POS; i++) {
            if (e.getSource() == positionButtons.get(i)) {
                positionId = i;
                break;
            }
        }
        /* Perform desired action */
        PositionButton positionButton = positionButtons.get(positionId);
        positionButton.setIsHovering(true);
        positionButton.repaint();

    }

    @Override
    public void mouseExited(MouseEvent e) {
        /*
         * used to change the button's appearance back if you stop hovering over it
         */
        if (e.getSource() == settingsButton) {
            return;
        }
        ArrayList<PositionButton> positionButtons = board.getPositionButtons();
        /* Find the position that was pressed */
        int positionId = -1;
        for (int i = 0; i < NUM_POS; i++) {
            if (e.getSource() == positionButtons.get(i)) {
                positionId = i;
                break;
            }
        }
        /* Perform desired action */
        PositionButton positionButton = positionButtons.get(positionId);
        positionButton.setIsHovering(false);
        positionButton.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * Used to set the selected position to a different colour
     * 
     * @param positionId The position to change the colour of
     * @param isSelected Whether the position is selected or not
     */
    public void setSelected(int positionId, boolean isSelected) {
        ArrayList<PositionButton> positionButtons = board.getPositionButtons();
        PositionButton positionButton = positionButtons.get(positionId);
        positionButton.setIsSelected(isSelected);
        positionButton.repaint();
    }
}
