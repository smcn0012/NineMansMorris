package Actions.Moves;

import java.util.ArrayList;

import Positions.Board;

/**
 * This class handles all the moves in phase two of the game. It is responsible for creating the  slide move actions.  It also handles
 * the logic for checking if a position is valid for a slide as well as the execution of the slide.
 * @see MoveController
 * @see MoveAction
 * @see Board
 */
public class PhaseTwoMoveController extends MoveController {

    static PhaseTwoMoveController instance = null;

    /**
     * Constructor for the MoveController class. Initialises the controller with the game board.
     * @param _board The board on which the game is being played
     */
    private PhaseTwoMoveController(Board _board) {
        /*
         * Standard initialiser for PhaseTwoMoveConstructor.
         */
        super(_board);
    }

    /**
     * This method is used to get an instance of the PhaseTwoMoveController. It implements the singleton pattern.
     * @param _board The board on which the game is being played
     * @return The instance of the PhaseTwoMoveController
     */
    public static PhaseTwoMoveController getInstance(Board _board) {
        if (instance == null) {
            instance = new PhaseTwoMoveController(_board);
        }
        return instance;
    }

    @Override
    public ArrayList<Integer> findLegalDestinationsFrom(int positionIndex){
        ArrayList<Integer> legalDestinations = new ArrayList<Integer>();
        
        for (Integer position : getBoard().getAdjacentPositions(positionIndex)) {
            if (getBoard().getPositionState(position) == -1) {
                legalDestinations.add(position);
            }
        }
        return legalDestinations;
    }

    @Override
    public String getHelperTextBase() {
       return "<html>Move one of your tokens to an adjacent empty position!<html>";
    }

    @Override
    public boolean legalActionsRemain(int playerIndex){
        for (Integer position : getBoard().findPositionsOfState(playerIndex)) {
            if (findLegalDestinationsFrom(position).size() > 0) {
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

        if (startPosition == -1) {
            // gets a list of positions which have the current player's token on them and can be moved
            ArrayList<Integer> possibleValidPositions = getBoard().findPositionsOfState(playerIndex);
            ArrayList<Integer> validPositions = new ArrayList<Integer>();
            for (Integer position : possibleValidPositions) {
                if(findLegalDestinationsFrom(position).size() > 0) {
                    validPositions.add(position);
                }
            } 
            return validPositions;
        }
        else {
            return findLegalDestinationsFrom(startPosition);     
        }
        
    }
}
