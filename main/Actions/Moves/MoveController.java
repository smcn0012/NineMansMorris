package Actions.Moves;

import java.util.ArrayList;

import Actions.Action;
import Actions.ActionController;
import Display.DisplayController;
import Positions.Board;

/**
 * This abstract class handles all moves in the game. It is responsible for creating the move actions.  It also handles
 * the logic for checking if a position is valid for a move as well as the execution of the move.
 * @see ActionController
 * @see MoveAction
 * @see Board
 * @see PhaseTwoMoveController
 * @see PhaseThreeMoveController
 */
public abstract class MoveController extends ActionController{
    protected int startPosition = -1; //if start is -1 it means an initial position has not been clicked
    //if it is another value we are selecting anew square to move to

    /**
     * Constructor for the MoveController class. Initialises the controller with the game board.
     * @param _board The board on which the game is being played
     */
    protected MoveController(Board _board) {
        /*
         * Standard initialiser for PhaseTwoMoveConstructor.
         */
        super(_board);
    }

    @Override
    public Action getAction(int positionIndexStart, int positionIndexEnd, int playerIndex) {
        /*
         * This method is used to get a move from the player.
         * @param positionIndexStart The index of the start position
         * @param positionIndexEnd The index of the end position
         * @param player The player who is making the move
         * @return The move that the player has made
         */
        return new MoveAction(positionIndexStart, positionIndexEnd, playerIndex);
    }

    @Override
    public boolean handlePositionInteraction(int positionIndex, int playerIndex) {
        /*
         * @return : true if the turn is over, false if the turn is not over
         */
        /* Called when a player in phase one clicks on a position */        
        if (checkForFriend(positionIndex, playerIndex)) {
            //player selects new token to move
            handleTokenSelection(positionIndex, playerIndex);
            return false;
        }
        else if (startPosition == -1) {
            //player has not selected a token to move yet
            return false;
        }
        else {
            //player has selected a token to move
            return handleMoveExecution(positionIndex, playerIndex);
        }
        
    }

    /**
     * This method checks if the position that the player has clicked on is a valid token that the current player owns.
     * @param positionIndex The index of the position that the player has clicked on
     * @param playerIndex The player who is making the selection
     * @return True if the position is a valid token owned by the current player, false otherwise
     */
    public boolean checkForFriend(int positionIndex, int playerIndex) {
        return getBoard().getPositionState(positionIndex) == playerIndex;
    }

    /**
     * Helper function that handles the process of selecting the token in which you want to move
     * @param positionIndex The index of the position that the player has clicked on
     * @param player The player who is making the move
     */
    public void handleTokenSelection(int positionIndex, int playerIndex) {
        if (startPosition != -1) {
            //we have already selected a token to move
            DisplayController.getInstance().setSelected(startPosition, false);
            if (startPosition == positionIndex) {
                // if you click on the position you've selected already, deselect it
                startPosition = -1;
                return;
            }
        }
        if (findLegalDestinationsFrom(positionIndex).size() > 0) {
            // if there are any valid moves originating from the clicked position
            startPosition = positionIndex; // set start position to positionIndex
            DisplayController.getInstance().setSelected(positionIndex, true);
        }
    }

    /**
     * This method handles the execution of the move. It checks if the move is valid and if it is it executes the move.
     * @param positionIndex The index of the position that the player has clicked on
     * @param player The player who is making the move
     * @return True if the move is valid and has been executed, false otherwise
     */
    public boolean handleMoveExecution(int positionIndex, int playerIndex) {
        //check if the position selected is free
        if (findLegalDestinationsFrom(startPosition).contains(positionIndex)) {
           //move the piece and return true
           Action move = getAction(startPosition, positionIndex, playerIndex);
           move.execute(getBoard());
           
           DisplayController.getInstance().setSelected(startPosition, false);

           //reset startPosition to -1
           startPosition = -1;
           return true;
        }
        else {
           //square is not free to move
           return false;
        }

   }

    /**
     * Finds all the legal destinations from a given position
     * @param positionIndex The index of the position to find the legal destinations from
     * @return An ArrayList of the legal destinations
     */
    public abstract ArrayList<Integer> findLegalDestinationsFrom(int positionIndex);
}
