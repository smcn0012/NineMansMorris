package Actions.Moves;

import Players.Player;
import Players.PlayerController;
import Positions.Board;

import java.util.ArrayList;
import java.util.Arrays;

import Actions.Action;
import Display.DisplayController;

/**
 * This class is used to represent a move action in the game. It implements the Action interface, and is responsible for
 * executing the move on the board. It is used by the various move controllers, which are responsible for creating the move
 * action and passing it to the board to be executed.
 * @see Action
 * @see Board
 * @see Player
 */
public class MoveAction implements Action {
    private int positionIndexStart;
    private int positionIndexEnd;
    private int playerIndex;
    
    /**
     * Constructor for the MoveAction class. Initialises the move action with the given parameters.
     * @param positionIndexStart The index of the position from which the piece is to be moved
     * @param positionIndexEnd The index of the position to which the piece is to be moved
     * @param playerIndex The player who is making the move
     */
    public MoveAction(int positionIndexStart, int positionIndexEnd, int playerIndex) {
        this.positionIndexStart = positionIndexStart;
        this.positionIndexEnd = positionIndexEnd;
        this.playerIndex = playerIndex;
    }

    @Override
    public void execute(Board board) {
    
        // update the gamestate 
        board.changePositionState(positionIndexStart, -1); // Remove the piece from the start position
        board.changePositionState(positionIndexEnd, this.playerIndex);

        // make the position display the correct token
        DisplayController dispCont = DisplayController.getInstance();
        dispCont.setPositionToken(positionIndexEnd, PlayerController.getInstance().getTokenIcon(playerIndex));
        dispCont.removePositionToken(positionIndexStart);

        // change the positions indicators
        dispCont.updateTokenRemovalIndicator(-1);
        dispCont.updatePreviousMoveIndicators( new ArrayList<Integer>( Arrays.asList(positionIndexStart, positionIndexEnd) ) );

        board.checkForMill(positionIndexEnd, true);
    }

    
}
        
