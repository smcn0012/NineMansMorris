package Actions;

import Positions.Board;

/**
 * This interface is used to represent a action in the game. It is implemented by the various move classes, which are
 * responsible for executing the action on the board.
 * @see Board
 */
public interface Action {
    /**
     * This method is used to execute the action on the board.
     * @param board The board on which the action is to be executed
     */
    public abstract void execute(Board board);

}
