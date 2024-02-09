package Positions;

import java.util.ArrayList;

import Display.DisplayController;

/**
 * This class is used to represent the board in the game. It is responsible for initialising the board and managing the
 * positions on the board.
 * @see Position
 * @see PlayerNumber
 */
public class Board {
    private ArrayList<Position> positions;
    private boolean millFormed;
    private int[][] layoutMillList;
    /**
     * This constructor initialises the board.
     */
    public Board(int[][] layoutMillList) {
        /*
         * Board initialiser. This method creates the positions on the board and sets up the mills.
         */
        positions = new ArrayList<Position>();
        // The millList is a list of all the mills on the board. Each mill is an ordered list of the positions in the mill.
        this.layoutMillList = layoutMillList;
        
        millFormed = false;
        int positionIndex = 0;
        for (ArrayList<ArrayList<Integer>> position : setUpPositions()) {
            this.positions.add(new Position(position, positionIndex));
            positionIndex++;
        }
    }

    /**
     * Getter method for millFormed
     * @return millFormed
     */
    public boolean getMillFormed() {
        return millFormed;
    }

    /**
     * Setter method for millFormed
     * @param milBoolean The boolean to set millFormed to
     */
    public void setMillFormed(Boolean milBoolean) {
        millFormed = milBoolean;
    }

    /**
     * This method changes the state of a position on the board.
     * @param positionIndex The index of the position to change the state of.
     * @param state The state to change the position to.
     */
    public void changePositionState(int positionIndex, int playerNum) {
        positions.get(positionIndex).setState(playerNum);
    }

    /**
     * This method returns the state of a position on the board.
     * @param positionIndex The index of the position to get the state of.
     * @return State The state of the position.
     */
    public int getPositionState(int positionIndex) {
        return positions.get(positionIndex).getState();
    }

    /**
     * This method returns the positions ajacent to a given position.
     * @param positionIndex The index of the position to get the ajacent positions of.
     * @return An ArrayList containing the indices of the ajacent positions.
     */
    public ArrayList<Integer> getAdjacentPositions(int positionIndex){
        return positions.get(positionIndex).findAdjacencies();
    }

    /**
     * This method defines the arrangement of the positions on the board.
     * @return An ArrayList of ArrayLists of ArrayLists of Integers. The first ArrayList is the positions on the board. 
     * The second ArrayList is the mills that the position is in. The third ArrayList is the positions in the mill.
     */
    private ArrayList<ArrayList<ArrayList<Integer>>> setUpPositions() {
        ArrayList<ArrayList<ArrayList<Integer>>> positionsSetUp = new ArrayList<ArrayList<ArrayList<Integer>>>();
        for(int i = 0; i < 24; i++) {                                       // 24 is the number of positions
            positionsSetUp.add(new ArrayList<ArrayList<Integer>>());
            for(int j = 0; j < layoutMillList.length; j++) {                      // Iterating through the mills
                for(int k = 0; k < layoutMillList[j].length; k++) {               // Iterating through the positions in the mills
                    if(layoutMillList[j][k] == i) {                               // If the position is in the mill then add the mill to the position
                        ArrayList<Integer> mill = new ArrayList<Integer>();
                        for(int l = 0; l < layoutMillList[j].length; l++) {
                            mill.add(layoutMillList[j][l]);
                        }
                        positionsSetUp.get(i).add(mill);
                    }
                }
            }
        }
        return positionsSetUp;
    }

    /**
     * This method checks if a mill has been formed by a player.
     * @param positionIndex The index of the position to check for a mill.
     * @return A boolean representing if a mill has been formed.
     */
    public boolean checkForMill(Integer positionIndex, boolean checkingForNewMill) {
        ArrayList<ArrayList<Integer>> millList = positions.get(positionIndex).getMillList();

        for(int i=0; i < millList.size(); i++) {
            ArrayList<Integer> potentialMill = millList.get(i);
            if (isMill(potentialMill)) {
                //return true if any potential mill is formed

                //update board to show mill has been formed
                millFormed = true;
                if (checkingForNewMill) {
                    DisplayController.getInstance().updateNewlyFormedMillPositions(potentialMill);
                }
                return isMill(potentialMill);
            }
    
        }
        //no mill was formed
        return false;
    }

    /**
     * This method checks if a set of positions form a mill.
     * @param potentialMill The positions to check if they form a mill.
     * @return A boolean representing if the positions form a mill.
     */
    public boolean isMill(ArrayList<Integer> potentialMill) {
        //checks if the mill passed is formed or not
        
        // check if all the states are the same there is a mill
        for(int i=1; i < potentialMill.size(); i++) {
            Integer currentState = getPositionState(potentialMill.get(i));
            Integer prevState = getPositionState(potentialMill.get(i-1));

            // state of current index against state of previous index
            if (currentState !=  prevState) {
                return false;
            }

        }
        //add to currentMills - if we decide to track mills as we go
        return true;

           
    }

    /**
     * This method returns the indices of the positions on the board that are in a given state.
     * @param state The state to find the positions of.
     * @return An ArrayList containing the indices of the positions in the given state.
     */
    public ArrayList<Integer> findPositionsOfState(int state) {
        ArrayList<Integer> openPositions = new ArrayList<Integer>();
        for(int i = 0; i < positions.size(); i++) {
            if(positions.get(i).getState() == state) {
                openPositions.add(i);
            }
        }
        return openPositions;
    }

    /**
     * This method checks if all the tokens of a player are in a mill.
     * @param playerNum The player to check if all their tokens are in a mill.
     * @return A boolean representing if all the tokens of a player are in a mill.
     */
    public boolean allTokensInMill(int playerNum) {
        ArrayList<Integer> playerTokens = findPositionsOfState(playerNum);
        boolean result = true;
        for (int i=0; i<playerTokens.size(); i++) {
            Integer positionIndex = playerTokens.get(i);
            if (!checkForMill(positionIndex, false)) {
                result = false;
                break;
            }
        }
        return result; 
    }

    /**
     * This method returns the mill layout of the board.
     * @return The mill layout of the board.
     */
    public int[][] getLayoutMillList() {
        return layoutMillList;
    }

    /**
     * This method returns the number of positions on the board.
     * @return The number of positions on the board.
     */
    public Integer numberOfPositions() {
        return positions.size();
    }
}
