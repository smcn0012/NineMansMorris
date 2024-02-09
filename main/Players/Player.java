package Players;

import javax.swing.ImageIcon;

/**
 * This abstract class is used to represent a player in the game. It responsible for storing the player's number, token 
 * image path, number of tokens to place, number of tokens on the board, and game phase.
 * @see HumanPlayer
 * @see AiPlayer
 * @see PlayerNumber
 * @see PhaseIndicator
 * @see ImageIcon
 */
public abstract class Player {
    private int numTokensToPlace = 9;
    private int tokensRemaining = 0;
    private int playerNumber;
    private PhaseIndicator gamePhase = PhaseIndicator.PHASE_ONE;
    private ImageIcon tokenIcon;
    private boolean hasLost = false;

    /**
     * This constructor is used to create a player object.
     * @param playerNum The player's number.
     * @param tokenImgPath The path to the player's token image.
     */
    Player(int playerNum, String tokenImgPath) {
        playerNumber = playerNum;
        tokenIcon = new ImageIcon(tokenImgPath);
    }

    /**
     * Getter for the number or remaining tokens to place.
     * @return The number of remaining tokens to place.
     */
    public int getNumTokensToPlace() {
        return numTokensToPlace;
    }

    /**
     * Decrements the number of remaining tokens to place by one.
     */
    public void decrementNumTokensToPlace() {
        numTokensToPlace -= 1;
    }

    /**
     * Getter for the number of tokens on the board.
     * @return The number of tokens on the board.
     */
    public int getTokensRemaining() {
        return tokensRemaining;
    }

    /**
     * Sets the number which the player understands to be the number of tokens on the board.
     * @param _tokensRemaining
     */
    public void setTokensRemaining(int _tokensRemaining) {
        tokensRemaining = _tokensRemaining;
    }

    /**
     * Getter for the player's number.
     * @return The player's number.
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * Setter for the player's game phase.
     * @param _gamePhase The player's new game phase.
     */
    public void setGamePhase(PhaseIndicator _gamePhase) {
        gamePhase = _gamePhase;
    }

    /**
     * Getter for the player's game phase.
     * @return The player's game phase.
     */
    public PhaseIndicator getGamePhase() {
        return gamePhase;
    }

    /**
     * Getter for the player's token image.
     * @return The player's token image.
     */
    public ImageIcon getTokenIcon() {
        return tokenIcon;
    }

    /**
     * Getter for the player's loss status.
     * @return True if the player has lost, false otherwise.
     */
    public boolean getHasLost() {
        return hasLost;
    }

    /**
     * Setter for the player's loss status.
     * @param _hasLost True if the player has lost, false otherwise.
     */
    public void setHasLost(boolean _hasLost) {
        hasLost = _hasLost;
    }
}
