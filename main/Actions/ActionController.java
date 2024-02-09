package Actions;

import java.util.ArrayList;

import Positions.Board;

/**
 * This class is used to represent a move action in the game. It implements the Action interface, and is responsible for
 * executing the move on the board. It is used by the various move controllers, which are responsible for creating the move
 * action and passing it to the board to be executed.
 * @see Action
 * @see Board
 */
public abstract class ActionController {

    private Board board;

    /**
     * Constructor for the MoveController class. Initialises the controller with the game board.
     * @param _board The board on which the game is being played
     */
    protected ActionController(Board _board) {
        board = _board;
    }

    /**
     * This method is used to get an action for the player.
     * @param positionIndexStart The index of the start position
     * @param positionIndexEnd The index of the end position
     * @param player The player who is making the action
     * @return The action
     */
    public abstract Action getAction(int positionIndexStart, int positionIndexEnd, int playerIndex);

    
    /**
     * This method is used to handle a position clicked by the user.
     * @param positionIndex The index of the position that was clicked
     * @param player The player who's turn it is
     * @return  True if the turn is over, false if still awaiting clicks from user
     */
    public abstract boolean handlePositionInteraction(int positionIndex, int playerIndex);

    /**
     * This method is used to get the game board.
     * @return The game board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * This method is used to reset the controller.
     */
    public abstract void reset();

    /**
     * This method is used to get the helper text for the controller which is used in the display.
     * @return The helper text
     */
    public abstract String getHelperTextBase();

    /**
     * This method returns true if a legal action remains for the player, false otherwise.
     * @param playerIndex The index of the player
     * @return True if a legal action remains, false otherwise
     */
    public abstract boolean legalActionsRemain(int playerIndex);

    /**
     * This method returns the list of valid positions for the player excecute an action on.
     * @param playerIndex The index of the player
     * @return The list of valid positions
     */
    public abstract ArrayList<Integer> getValidPositions(int playerIndex);

}
