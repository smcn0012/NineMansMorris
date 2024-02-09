package Actions.Moves;

import java.util.ArrayList;

import Actions.Action;
import Positions.Board;

/**
 * This class handles the moves in phase three of the game. It is responsible for creating the fly MoveActions.  It also handles
 * the logic for checking if a position is valid for a fly as well as the execution of the fly.
 * @see MoveController
 * @see MoveAction
 * @see Board

 */
public class PhaseThreeMoveController extends MoveController {
    
    static PhaseThreeMoveController instance = null;
    
    /**
     * Constructor for the MoveController class. Initialises the controller with the game board.
     * @param _board The board on which the game is being played
     */
    private PhaseThreeMoveController(Board _board) {
        super(_board);
    }

    /**
     * This method is used to get an instance of the PhaseThreeMoveController. It implements the singleton pattern.
     * @param _board The board on which the game is being played
     * @return The instance of the PhaseThreeMoveController
     */
    public static PhaseThreeMoveController getInstance(Board _board) {
        if (instance == null) {
            instance = new PhaseThreeMoveController(_board);
        }
        return instance;
    }

    @Override
    public Action getAction(int positionIndexStart, int positionIndexEnd, int playerIndex)
    {
        return new MoveAction(positionIndexStart, positionIndexEnd, playerIndex);
    }

    @Override
    public ArrayList<Integer> findLegalDestinationsFrom(int positionIndex){
        //Finding empty positions
        return getBoard().findPositionsOfState(-1);
    }

    @Override
    public String getHelperTextBase() {
        return "<html>Move one of your tokens to an empty position!<html>";
    }

    @Override
    public boolean legalActionsRemain(int playerIndex){
        return getBoard().findPositionsOfState(playerIndex).size() > 0 && getBoard().findPositionsOfState(-1).size() > 0;
    }

    @Override
    public void reset() {
        instance = null;
    }
    
    @Override
    public ArrayList<Integer> getValidPositions(int playerIndex) {

        if (startPosition == -1) {
            return getBoard().findPositionsOfState(playerIndex); // gets a list of positions which have the current player's token on them
        }
        else {
            return findLegalDestinationsFrom(startPosition);     
        }
        
    }
}
