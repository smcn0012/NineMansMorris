package Actions.Mills;

import java.util.ArrayList;

import Actions.Action;
import Display.DisplayController;
import Players.PhaseIndicator;
import Players.PlayerController;
import Positions.Board;

/**
 * This class is used to represent a remove token action in the game. It implements the Action interface, and is responsible for
 * executing the remove token on the board. It is used by the phase remove action controller.
 * @see Action
 * @see Board
 * @see RemoveActionController
 */
public class RemoveTokenAction implements Action{
    private int positionIndex;

    /**
     * Constructor for the RemoveTokenAction class. Initialises the remove token action with the given parameters.
     * @param positionIndex The index of the position from which the piece is to be removed
     */
    public RemoveTokenAction(int positionIndex) {
        this.positionIndex = positionIndex;
    }

    @Override
    public  void execute(Board board) {
        //update the board so that it no longer thinks a mill was formed
        board.setMillFormed(false);

        // update the gamestate 
        int playerNum = board.getPositionState(positionIndex);
        board.changePositionState(positionIndex, -1);

        //decrease the number tokens the opponent has
        int currentTokensRemaining = PlayerController.getInstance().getTokensRemaining(playerNum);
        PlayerController.getInstance().setTokensRemaining(playerNum, currentTokensRemaining - 1);

        // decrease token count on display 
        DisplayController dispCont = DisplayController.getInstance();
        dispCont.incrementTokensRemovedCount(playerNum);

        // make the position display no token
        dispCont.removePositionToken(positionIndex);

        // add the removed token styline
        dispCont.updateTokenRemovalIndicator( positionIndex );

        // remove the mill's styling
        dispCont.updateNewlyFormedMillPositions(new ArrayList<>());

        // change phase if there are no more tokens to place
        if (PlayerController.getInstance().getTokensRemaining(playerNum) == 3 && PlayerController.getInstance().getGamePhase(playerNum) != PhaseIndicator.PHASE_ONE) {
            PlayerController.getInstance().setGamePhase(playerNum, PhaseIndicator.PHASE_THREE);
        }
    }
}
