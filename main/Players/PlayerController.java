package Players;

import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * This class is used to controll the players in the game. It is responsible for storing the players and their number.
 * @see Player
 * @see HumanPlayer
 * @see AiPlayer
 */
public class PlayerController {
    ArrayList<Player> players;
    private static PlayerController instance;
    private Integer numPlayers;

    /**
     * Private constructor called by getInstance method when instance is null
     */
    private PlayerController() {
        players = new ArrayList<Player>();
        numPlayers = 0;
    }
    
    /**
     * This method is used to get the instance of the PlayerController class.
     * @return The instance of the PlayerController class.
     */
    public static PlayerController getInstance() {
        if (instance == null) {
            instance = new PlayerController();
        }
        return instance;
    }

    /**
     * This method is to add a human player to the game.
     * @param tokenImgPath The path to the player's token image.
     * @return The player's number.
     */
    public int addHumanPlayer(String tokenImgPath) {
        players.add(new HumanPlayer(numPlayers, tokenImgPath));
        int playerIndex = numPlayers;
        numPlayers += 1;
        return playerIndex;
    }

    /**
     * This method is to add an AI player to the game.
     * @param tokenImgPath The path to the player's token image.
     * @return The player's number.
     */
    public int addAiPlayer(String tokenImgPath) {
        players.add(new AiPlayer(numPlayers, tokenImgPath));
        int playerIndex = numPlayers;
        numPlayers += 1;
        return playerIndex;
    }

    /**
     * This method is used to get the number of players in the game.
     * @param playerNum The player's number.
     * @return The number of players in the game.
     */
    public ImageIcon getTokenIcon(int playerNum) {
        return players.get(playerNum).getTokenIcon();
    }

    /**
     * This method is used to get the phase of the specified player.
     * @param playerNum The player's number.
     * @return The phase of the specified player.
     */
    public PhaseIndicator getGamePhase(int playerNum) {
        return players.get(playerNum).getGamePhase();
    }

    /**
     * This method is used to set the phase of the specified player.
     * @param playerNum The player's number.
     * @param phase The phase to set the player's phase to.
     */
    public void setGamePhase(int playerNum, PhaseIndicator phase) {
        players.get(playerNum).setGamePhase(phase);
    }

    /**
     * This method is used to get the number of tokens on the board for the specified player.
     * @param playerNum The player's number.
     * @return The number of tokens on the board for the specified player.
     */
    public int getTokensRemaining(int playerNum) {
        return players.get(playerNum).getTokensRemaining();
    }

    /**
     * This method is used to set the number of tokens that the specified player has on the board.
     * @param playerNum The player's number.
     */
    public void setTokensRemaining(int playerNum, int _tokensRemaining) {
        players.get(playerNum).setTokensRemaining(_tokensRemaining);
    }

    /**
     * This method is used to set the number of tokens that the specified player has to place on the board.
     * @param playerNum The player's number.
     * @return The number of tokens that the specified player has to place on the board.
     */
    public int getNumTokensToPlace(int playerNum) {
        return players.get(playerNum).getNumTokensToPlace();
    }

    /**
     * This method is used to decrement the number of tokens that the specified player has to place on the board.
     * @param playerNum The player's number.
     */
    public void decrementNumTokensToPlace(int playerNum) {
        players.get(playerNum).decrementNumTokensToPlace();
    }

    /**
     * This method is used to get the number of players in the game.
     * @return The number of players in the game.
     */
    public int getNumberOfPlayers() {
        return numPlayers;
    }

    /**
     * This method is used to get the specified player's name.
     * @param playerNum The player's number.
     * @return The specified player's name.
     */
    public boolean hasPlayerLost(int playerNum) {
        return players.get(playerNum).getHasLost();
    }

    /**
     * This method is used to set the specified player as having lost the game.
     * @param playerNum The player's number.
     */
    public void setPlayerLost(int playerNum, boolean hasLost) {
        players.get(playerNum).setHasLost(hasLost);
    }

    /**
     * This method returns -1 if there is no winner, otherwise it returns the player number of the winner.
     * @return The player number of the winner or -1 if there is no winner.
     */
    public int findWinner() {
        
        boolean foundWinner = false;
        int noWinner = -1;
        for (int i = 0; i < numPlayers; i++) {
            if (players.get(i).getHasLost()) {
                foundWinner = true;
                break;
            }
        }
        if (!foundWinner) { return noWinner; }

        for (int i = 0; i < numPlayers; i++) { // this loop is only run if a player has lost the game
            if (!players.get(i).getHasLost()) {    // if the i'th player hasn't lost the game, they've won
                return i;
            }
        }
        return noWinner;  // this line should never be called if method works correctly but Java doesn't like conditional returns

    }

    /**
     * This method is used to get the specified player.
     * @param playerId The player's number.
     * @return The specified player.
     */
    public Player getPlayerById(int playerId) {
        return players.get(playerId);
    }
    
    /**
     * This method is used to reset the instance of the PlayerController class.
     */
    public void reset() {
        instance = null;
    }
}
