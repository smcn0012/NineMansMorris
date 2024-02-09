package Actions.Mills;

import java.util.ArrayList;

import Actions.Action;
import Actions.ActionController;
import Positions.Board;
import Players.PlayerController;

/**
 * This class handles the remove actions in the game. It is responsible for creating the remove RemoveTokenActions.  It also handles
 * the logic for checking if a position is valid for a remove as well as the execution of the remove.
 * @see ActionController
 * @see RemoveTokenAction
 * @see Board
 */
public class RemoveActionController extends ActionController {

    static RemoveActionController instance = null;

    /**
     * Constructor for the MoveController class. Initialises the controller with the
     * game board.
     * 
     * @param _board The board on which the game is being played
     */
    private RemoveActionController(Board _board) {
        super(_board);
    }

    /**
     * This method is used to get an instance of the RemoveActionController. It implements the singleton pattern.
     * @param _board The board on which the game is being played
     * @return The instance of the RemoveActionController
     */
    public static RemoveActionController getInstance(Board _board) {
        if (instance == null) {
            instance = new RemoveActionController(_board);
        }
        return instance;
    }

    @Override
    public Action getAction(int positionIndex, int positionIndexEnd, int playerIndex) {
        return new RemoveTokenAction(positionIndexEnd);
    }

    /**
     * This method is used to check if a position is valid for a placement.
     * 
     * @param positionIndex The index of the position to check
     * @return True if the position is valid, false otherwise
     */
    public boolean isValidPosition(int positionIndex, int playerIndex) {
        // returns true if the position is not the current player's token and is not empty and is not opponent's token that's forming a mill 
        // (i.e. returns true if the position has an opponent's token on it which does not form a mill)

        boolean isOnlyMills = getBoard().allTokensInMill(1 - playerIndex); // need to check for the player who's token we are removing
        boolean positionInMill = getBoard().checkForMill(positionIndex, false);
        int positionState = getBoard().getPositionState(positionIndex);
        return positionState != -1 && positionState != playerIndex && (!positionInMill || isOnlyMills);
    }

    @Override
    public boolean handlePositionInteraction(int positionIndex, int playerIndex) {
        /* Called when a player in phase one clicks on a position */
        if (!isValidPosition(positionIndex, playerIndex)) { 
            return false; // could provide user with feedback here
        }

        /* if valid, perform the move */
        Action move = getAction(-1, positionIndex, playerIndex);
        move.execute(getBoard());
        return true; // turn is over, so return true
    }

    @Override
    public String getHelperTextBase() {
        return "<html>You've formed a mill! Remove a token from your opponent which is not forming a mill<html>";
    }

    @Override
    public boolean legalActionsRemain(int playerIndex) {
        // loop through players
        for (int i = 0; i < PlayerController.getInstance().getNumberOfPlayers(); i++) {
            if (i != playerIndex && PlayerController.getInstance().getTokensRemaining(i) > 2) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void reset() {
        instance = null;
    }

    @Override
    public ArrayList<Integer> getValidPositions(int playerIndex) {

        ArrayList<Integer> opponentsTokens = getBoard().findPositionsOfState(1 - playerIndex); // gets a list of positions which have the opponent's token on them
        ArrayList<Integer> validPositions = new ArrayList<Integer>();
        
        // loop through the opponents tokens
        for (int i=0; i<opponentsTokens.size(); i++) {
            if (isValidPosition(opponentsTokens.get(i), playerIndex)) {
                validPositions.add(opponentsTokens.get(i)); // if the i'th opponent's token is millable, add it to movable positions
            }
        }

        return validPositions;
    }
}
