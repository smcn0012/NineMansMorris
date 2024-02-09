package Actions.Placements;

import java.util.ArrayList;

import Actions.Action;
import Actions.ActionController;
import Positions.Board;

/**
 * This class handles all the moves in phase one of the game. It is responsible for creating the place actions.  It also handles
 * the logic for checking if a position is valid for a placement as well as the execution of the placement.
 * @see ActionController
 * @see PlaceAction
 * @see Board
 */
public class PlaceActionController extends ActionController{

    static PlaceActionController instance = null;

    /**
     * Constructor for the MoveController class. Initialises the controller with the game board.
     * @param _board The board on which the game is being played
     */
    private PlaceActionController(Board _board) {
        super(_board);
    }

    /**
     * This method is used to get an instance of the PlaceActionController. It implements the singleton pattern.
     * @param _board The board on which the game is being played
     * @return The instance of the PlaceActionController
     */
    public static PlaceActionController getInstance(Board _board) {
        if (instance == null) {
            instance = new PlaceActionController(_board);
        }
        return instance;
    }

    @Override
    public Action getAction(int positionIndexStart, int positionIndexEnd, int playerIndex)
    {
        return new PlaceAction(positionIndexEnd, playerIndex);
    }

    /**
     * This method is used to check if a position is valid for a placement.
     * @param positionIndex The index of the position to check
     * @return True if the position is valid, false otherwise
     */
    public boolean isValidPosition(int positionIndex) {
        return getBoard().getPositionState(positionIndex) == -1;  // only valid if the position is empty
    }


    @Override
    public boolean handlePositionInteraction(int positionIndex, int playerIndex) {
        /* Called when a player in phase one clicks on a position */
        if (!isValidPosition(positionIndex)) { 
            return false; // could provide user with feedback here
        }
        
        /* if valid, perform the move */
        Action move = getAction(-1, positionIndex, playerIndex);
        move.execute(getBoard());
        return true;  // turn is over, so return true
    }

    @Override
    public String getHelperTextBase() {
        return "<html>Place one of your tokens onto an empty position!<html>";
    }

    @Override
    public boolean legalActionsRemain(int playerIndex) {
        return getBoard().findPositionsOfState(-1).size() > 0;
    }

    @Override
    public void reset() {
        instance = null;
    }

    @Override
    public ArrayList<Integer> getValidPositions(int playerIndex) {
        return getBoard().findPositionsOfState(-1); // return a list of all the empty positions
    }
}
