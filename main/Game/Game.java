package Game;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;

import Actions.*;
import Actions.Mills.RemoveActionController;
import Actions.Moves.PhaseThreeMoveController;
import Actions.Moves.PhaseTwoMoveController;
import Actions.Placements.PlaceActionController;
import Display.DisplayController;
import Players.*;
import Positions.Board;

/**
 * This class is the main class for the game. It is a singleton class, and is
 * responsible for initialising the game and
 * managing the game state. It also contains the main game loop, which is
 * responsible for running the game.
 * 
 * @see DisplayController
 * @see Board
 * @see Player
 * @see ActionController
 */
public class Game {

    private static Game instance = null;
    private DisplayController displayController = null;
    private HashMap<PhaseIndicator, ActionController> moveControllerMap = new HashMap<PhaseIndicator, ActionController>(); // used to fetch the appropriate move controller given the current player's phase
    private ActionController currentActionController = null; // the action controller relevant to the current player's game phase
    private Board board = null;
    private String[] tokenImgPathList = new String[] { "main/Display/img/copper panelled circle.png", "main/Display/img/cyan circle.png" };
    private int NUM_PLAYERS = 2;
    private int currentPlayer = -1; // the player who's turn it is
    private PlayerController playerController = null;
    private static GameType gameType;
    private boolean disableInteractions;

    /**
     * Constructor for the Game class. Initialises the game state.
     */
    private Game() {
        /* Initialise display */
        displayController = DisplayController.getInstance();

        /* The default layout is a list of all the mills on the board for a regular game of nine men's morris. Each mill is an ordered list of the positions in the mill. */
        int[][] defaultLayout = new int[][] { {0,1,2}, {0,9,21}, {1,4,7}, {2,14,23}, {3,4,5}, {3,10,18}, {5,13,20}, {6,7,8}, {6,11,15}, {8,12,17}, {9,10,11}, {12,13,14}, {15,16,17}, {16,19,22}, {18,19,20}, {21,22,23} };
        /* Initialise board with the default layout */
        board = new Board(defaultLayout);
        disableInteractions = false;
        /* Initialise players */
        initialisePlayers();

        /* Initialise moveControllerMap */
        moveControllerMap.put(PhaseIndicator.PHASE_ONE, PlaceActionController.getInstance(board));
        moveControllerMap.put(PhaseIndicator.PHASE_TWO, PhaseTwoMoveController.getInstance(board));
        moveControllerMap.put(PhaseIndicator.PHASE_THREE, PhaseThreeMoveController.getInstance(board));
        currentActionController = moveControllerMap.get(playerController.getGamePhase(currentPlayer)); // sets the move controller to be relevant for player 1
        displayController.changeHelperText(currentActionController.getHelperTextBase());
        /* Update the display to display the current player's valid moves */
        ArrayList<Integer> validPositions = currentActionController.getValidPositions(currentPlayer);
        displayController.updateHighlightedPositions(validPositions);

    }

    /**
     * Returns the singleton instance of the Game class.
     * 
     * @return The singleton instance of the Game class
     */
    public static synchronized Game getInstance() {
        if (instance == null) {
                instance = new Game();
        }
        return instance;
    }

    public static void setGameType(GameType gameType) {
        Game.gameType = gameType;
    }

    /**
     * Checks if the game has ended
     * 
     * @return True if the game has ended, false otherwise
     */
    public void checkEndCondition() {
        // if (playerController.findWinner() == -1) { // if there is no winner currently
        for (int i = 0; i < NUM_PLAYERS; i++) {
            if (playerController.getTokensRemaining(i) < 3 && playerController.getGamePhase(i) != PhaseIndicator.PHASE_ONE) { // if a player has < 3 tokens remaining and is not in the first phase
                playerController.setPlayerLost(i, true); // set the player to have lost
            }
        }
        if (!currentActionController.legalActionsRemain(currentPlayer)) { // if the current player has legal actions remaining
            playerController.setPlayerLost(currentPlayer, true); // set the player to have lost
        }
        if (playerController.findWinner() != -1) { // if there is a winner now that all the conditions have been checked
            endGame(playerController.findWinner());
        }
    }

    /**
     * Initises the player instances and adds them to the player controller based in the game type.
     */
    private void initialisePlayers() {
        playerController = PlayerController.getInstance(); 
        switch(Game.gameType) {
            
            case LOCAL_TWO_PLAYER:     
                playerController.addHumanPlayer(tokenImgPathList[0]);
                playerController.addHumanPlayer(tokenImgPathList[1]);
                break;
            case AI_PLAYER:
                playerController.addHumanPlayer(tokenImgPathList[0]);
                playerController.addAiPlayer(tokenImgPathList[1]);
                break;
            default:
                System.out.println("Invalid game type");
            }
            
            currentPlayer = 0; // set player 1 for first turn
    }

    /**
     * Returns the next player in the player list that hasn't lost.
     * 
     * @return The next player in the player list that hasn't lost.
     */
    public int getNextPlayer() {
        int currentPlayerId = (currentPlayer + 1) % NUM_PLAYERS;
        while (PlayerController.getInstance().hasPlayerLost(currentPlayer)) {
            currentPlayerId = (currentPlayerId + 1) % NUM_PLAYERS;
        }
        return currentPlayerId;
    }

    /**
     * Checks if a player implements the GeneratesMove interface and if so, calls the generateMove method.
     */
    public void checkMoveGenerator() {
        GeneratesActionController.getInstance().generateAction(playerController.getPlayerById(currentPlayer), currentActionController);
    }

    /**
     * Ends the current turn and checks for end game conditions, before setting up
     * the next turn.
     */
    public void endTurn() {
        /* ran at the conclusion of each turn, sets up the next turn */
        if (!board.getMillFormed()) {
            currentPlayer = getNextPlayer(); // sets the next player
            if (GeneratesActionController.getInstance().checkPlayerGeratesAction(playerController.getPlayerById(currentPlayer))) {
                setInteractionDisabled(true);
            }
            else {
                setInteractionDisabled(false);
            }
            currentActionController = moveControllerMap.get(playerController.getGamePhase(currentPlayer)); // sets the
                                                                                                           // game
                                                                                                           // controller
                                                                                                           // to be
                                                                                                           // relevant
                                                                                                           // to the
                                                                                                           // next
                                                                                                           // player's
                                                                                                           // phase
            int playerNum = currentPlayer + 1; // playerId (zero indexed) -> playerNum (not zero indexed)
            displayController.changeTurnText(playerController.getTokenIcon(currentPlayer),
                    "Player " + playerNum + "'s Turn");
            checkEndCondition();
        } else {
            // let the current player remove one of the opponents tokens
            currentActionController = RemoveActionController.getInstance(board);
        }
        if(!GeneratesActionController.getInstance().checkPlayerGeratesAction(PlayerController.getInstance().getPlayerById(currentPlayer))) {
            // do not update the helper text for the AI's turn, only for human players
            displayController.changeHelperText(currentActionController.getHelperTextBase());
        }
        
        checkMoveGenerator(); // checks whether the next turn's player generates moves, if they do generate a move
    }

    /**
     * Returns true if the current player has any moves remaining, false otherwise.
     * 
     * @return True if the current player has any moves remaining, false otherwise
     */
    public boolean actionsRemaining() {
        return currentActionController.legalActionsRemain(currentPlayer);
    }

    /**
     * Handles a position click by the current player.
     * 
     * @param positionId The id of the position that was clicked
     */
    public boolean handlePositionClick(int positionId) {
        boolean turnOver = currentActionController.handlePositionInteraction(positionId, currentPlayer);
        if (turnOver) { // if the turn is over
            endTurn();
        }
        // if not, wait for this method to be called again
        /* Update the display to display the current player's valid moves */
        ArrayList<Integer> validPositions = currentActionController.getValidPositions(currentPlayer);
        if (Game.getInstance().isInteractionDisabled()) {
            validPositions = new ArrayList<Integer>();
        }
        displayController.updateHighlightedPositions(validPositions);

        return turnOver;
    }

    /**
     * Ends the game.
     */
    public void endGame(int winner) {
        String[] responses = { "Play Again" };
        String losingQuote = LosingQuotes.getRandomQuote();
        String endGameText = "Player " + (winner + 1) + " has won this match!"; 
        if (GeneratesActionController.getInstance().checkPlayerGeratesAction(PlayerController.getInstance().getPlayerById(winner))) {
            // if the winner was an AI player
            endGameText += "\n\n" + losingQuote;
        }
        int answer = JOptionPane.showOptionDialog(
                null,
                endGameText,
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                0,
                playerController.getTokenIcon(winner),
                responses,
                responses[0]);
        if (answer == 0) {
            // if the user wants to play again
            resetGame();
        }
        else if (answer == -1) {
            resetGame(); // if we want other functionality to happen if the user exits the dialogue box instead of clicking play again 
        }
    }

    /**
     * Resets the game.
     */
    public void resetGame() {
        displayController.resetDisplay();
        playerController.reset();
        for (HashMap.Entry<PhaseIndicator, ActionController> pair : moveControllerMap.entrySet()) {
            pair.getValue().reset(); // loops through the action controllers in the HashMap and resets them
        };
        RemoveActionController.getInstance(board).reset();
        currentPlayer = 0;
        instance = null;
        Game.getInstance();
    }

    /**
     * returns the isInteractionDisabled boolean
     * @return the isInteractionDisabled boolean
     */
    public boolean isInteractionDisabled() {
        return disableInteractions;
    }

    /**
     * sets the isInteractionDisabled boolean
     * @param disableInteractions the boolean to set isInteractionDisabled to
     */
    public void setInteractionDisabled(boolean disableInteractions) {
        this.disableInteractions = disableInteractions;
    }
}
