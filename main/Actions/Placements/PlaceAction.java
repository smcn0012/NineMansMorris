package Actions.Placements;

import java.util.ArrayList;
import java.util.Arrays;

import Actions.Action;
import Display.DisplayController;
import Players.PhaseIndicator;
import Players.Player;
import Players.PlayerController;
import Positions.Board;

/**
 * This class is used to represent a place action in the game. It implements the Action interface, and is responsible for
 * executing the place on the board. It is used by the phase one move controller.
 * @see Action
 * @see Board
 * @see Player
 * @see PlaceActionController
 */
public class PlaceAction implements Action {
    private int positionIndexEnd;
    private int playerIndex;

    /**
     * Constructor for the PlaceAction class. Initialises the action with the end position and the player who is making the action.
     * @param positionIndexEnd
     * @param playerIndex
     */
    public PlaceAction(int positionIndexEnd, int playerIndex) {
        this.positionIndexEnd = positionIndexEnd;
        this.playerIndex = playerIndex;
    }

    /**
     * This method is used to execute the place action on the board.
     */
    public void execute(Board board) {
        // update the gamestate 
        board.changePositionState(positionIndexEnd, this.playerIndex);
        // make the position display the correct token
        DisplayController dispCont = DisplayController.getInstance();
        dispCont.setPositionToken(positionIndexEnd, PlayerController.getInstance().getTokenIcon(playerIndex));
        // change the positions indicators
        dispCont.updateTokenRemovalIndicator(-1);
        dispCont.updatePreviousMoveIndicators( new ArrayList<Integer>( Arrays.asList(positionIndexEnd) ) );

        // decrease player's tokens to place counter attribute
        PlayerController.getInstance().decrementNumTokensToPlace(playerIndex);
        // decrease the displayed counter for number of tokens to place (this must be done after the counter attribute is decremented)
        dispCont.decrementTokensToPlaceCount(playerIndex);
        // increment number of tokens on board for player
        int currentTokenCount = PlayerController.getInstance().getTokensRemaining(playerIndex);
        PlayerController.getInstance().setTokensRemaining(playerIndex, currentTokenCount + 1);

        // change phase if there are no more tokens to place
        if (PlayerController.getInstance().getNumTokensToPlace(playerIndex) == 0) {
            PlayerController.getInstance().setGamePhase(playerIndex, PhaseIndicator.PHASE_TWO);
        }

        board.checkForMill(positionIndexEnd, true);
    }
}
