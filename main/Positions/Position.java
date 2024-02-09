package Positions;

import java.util.ArrayList;

/**
 * This class is used to represent a position on the board. It is responsible for storing the state of the position and
 * the mills that the position is a part of.
 * @see PlayerNumber
 */
public class Position {

    private ArrayList<ArrayList<Integer>> millList = new ArrayList<ArrayList<Integer>>();
    private int playerNum;
    private int index;

    /**
     * This constructor initialises the position.
     * @param millList The mills that the position is a part of.
     * @param index The index of the position.
     */
    public Position(ArrayList<ArrayList<Integer>> millList, int index) {
        for (ArrayList<Integer> millRaw : millList) {
            ArrayList<Integer> mill = new ArrayList<Integer>();
            for (Integer positionIndex : millRaw) {
                mill.add(positionIndex);
            }
            this.millList.add(mill);
        }
        this.playerNum = -1;
        this.index = index;
    }

    /**
     * This method returns the state of the position.
     * @return The state of the position.
     */
    public int getState() {
        return  playerNum;
    }

    /**
     * This method sets the state of the position.
     * @param state The state to set the position to.
     */
    public void setState(int state) {
        this.playerNum = state;
    }

    /**
     * This method returns the mills that the position is a part of.
     * @return The mills that the position is a part of.
     */
    public ArrayList<ArrayList<Integer>> getMillList() {
        return millList;
    }

    /**
     * This method returns the index of the positions this position is adjacent to.
     * @return The indices of the positions this position is adjacent to.
     */
    public ArrayList<Integer> findAdjacencies() {
        ArrayList<Integer> adjacencies = new ArrayList<Integer>();
        for (ArrayList<Integer> mill : millList) {
            if(mill.get(1) == index){
                adjacencies.add(mill.get(0));
                adjacencies.add(mill.get(2));
            }
            else{
                adjacencies.add(mill.get(1));
            }
        }
        return adjacencies;
    }
}
