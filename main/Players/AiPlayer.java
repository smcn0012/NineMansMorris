package Players;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.SwingUtilities;

import Actions.ActionController;
import Game.Game;
import Positions.Board;

/**
 * This class is used to represent an AI player in the game. It extends the Player class, and is responsible for
 * storing the player's number and token image path.
 * @see Player
 * @see GeneratesAction
 */
public class AiPlayer extends Player implements GeneratesAction {

    int TIMEOUT_LENGTH = 1000;
    ActionController currentActionController;
    ArrayList<Integer> legalPositionsList;
    ArrayList<Integer> searchedPositions;
    Board board;

    /**
     * This constructor creates a new AI player with the given player number and token image path.
     * @param playerNum the player number of the AI player
     * @param tokenImgPath the token image path of the AI player
     */
    public AiPlayer(int playerNum, String tokenImgPath) {
        super(playerNum, tokenImgPath);
        GeneratesActionController.getInstance().addActionGenerator(this);
    }

    @Override
    public void generateAction(ActionController actionController) {
        currentActionController = actionController;
        board = currentActionController.getBoard();
        
        SwingUtilities.invokeLater(() -> {
            new Sleep(TIMEOUT_LENGTH);
            boolean turnOver = false;
            while (!turnOver) {
                legalPositionsList = currentActionController.getValidPositions(getPlayerNumber());
                turnOver = Game.getInstance().handlePositionClick(chooseAction());
            }
        });        
    }

    /**
     * This method choses the action that the AI will make
     * @return the position that the AI will take the action on
     */
    private int chooseAction(){
        int positionChoice = -1;
        int[][] layoutMillList = board.getLayoutMillList();

        if (board.getPositionState(legalPositionsList.get(0))  == -1 || board.getPositionState(legalPositionsList.get(0))  == getPlayerNumber()){
            for (int[] mill : layoutMillList) {
                boolean potentialMill = true; // can this be a mill
                int emptyPosition = -1; // which position is the empty position
    
                for (int millPosition : mill) {
                    if(board.getPositionState(millPosition) == -1){
                        if (emptyPosition != -1){
                            potentialMill = false;
                            break;
                        }
                        emptyPosition = millPosition;
                    }
                    else if (board.getPositionState(millPosition) != getPlayerNumber()){ 
                        potentialMill = false;
                        break;
                    }
                }
                if (emptyPosition == -1){
                    potentialMill = false;
                }
                if (potentialMill){
                    if (legalPositionsList.contains(emptyPosition)){
                        positionChoice = emptyPosition;
                        break; // best move is the make a mill
                    }
                    else if (findPieceToMoveTo(emptyPosition, mill) != -1) {
                        positionChoice = findPieceToMoveTo(emptyPosition, mill);
                        break;
                    }
                    else if (getTokensRemaining() == 3 && board.getPositionState(legalPositionsList.get(0)) == getPlayerNumber()){
                        ArrayList<Integer> piecesInPotentialMill = new ArrayList<Integer>();
                        for (int millPosition : mill) {
                            if (legalPositionsList.contains(millPosition)){
                                piecesInPotentialMill.add(millPosition);
                            }
                        }
                        for (Integer legalPosition : legalPositionsList) {
                            if (! piecesInPotentialMill.contains(legalPosition)){
                                positionChoice = legalPosition;
                                break;
                            }
                        }
                        if (positionChoice != -1){
                            break;
                        }
                    }
                }
            }
        }

        // If no obvious mill, then look for a good heuristic move, if no heuristic move, then choose a random move
        if (positionChoice == -1){
            ArrayList<Integer> heuristicMoves = getHeuristicMove();
            if (heuristicMoves.size() > 0){
                Collections.shuffle(heuristicMoves);
                positionChoice = heuristicMoves.get(0);
            }
            else{
                Collections.shuffle(legalPositionsList);
                positionChoice = legalPositionsList.get(0);
            }
        }

        return positionChoice;
    }

    /**
     * This method finds a piece that can be moved to the target position
     * @param targetPosition the position that the piece will be moved to
     * @param potentialMill the potential mill that the target position is in that 
     * @return the position of the piece that can be moved to the target position
     */
    private int findPieceToMoveTo(int targetPosition, int[] potentialMill){
        int[][] layoutMillList = board.getLayoutMillList();
        ArrayList<int[]> millsContainingTarget = new ArrayList<int[]>();
        for (Integer millIndex : getPositionToMillIndex(layoutMillList).get(targetPosition)) {
            if (layoutMillList[millIndex] != potentialMill){
                millsContainingTarget.add(layoutMillList[millIndex]);
            }
        } 
        for (int[] mill : millsContainingTarget) {
            int targetIndex = 0;
            for (int position : mill) {
                if (position == targetPosition){
                    break;
                }
                else{
                    targetIndex++;
                }
            }

            int currentIndex = 0;
            for (int position : mill) {
                if (currentIndex == targetIndex-1 || currentIndex == targetIndex+1){
                    if(board.getPositionState(position) == getPlayerNumber() && legalPositionsList.contains(position)){
                        return position;
                    }
                }
                currentIndex++;
            }
        }

        return -1;
    }

    /**
     * This method finds the best heuristic move for the AI to make
     * @return the position that the AI will take the action on
     */
    private ArrayList<Integer> getHeuristicMove(){
        ArrayList<Integer> goodHeuristicMoves;
        if (board.getPositionState(legalPositionsList.get(0))  == -1 ){
            // Open positions are the available moves
            goodHeuristicMoves = createGoodHeuristicActions(getPlayerNumber());
        }
        else {
            // removing either one of the  oponents pieces
            goodHeuristicMoves = createGoodHeuristicActions(-1);
        }

        return goodHeuristicMoves;
    }

    /**
     * This method creates a list of good heuristic actions that the AI can take
     * @param positionStateResultingFromAction the state that the position will be once the AI takes the action
     * @return the list of good heuristic actions that the AI can take
     */
    private ArrayList<Integer> createGoodHeuristicActions(Integer positionStateResultingFromAction){
        ArrayList<Integer> actionHeuristics = new ArrayList<Integer>(); // will be used to determine which position has the best benefit

        for (int i = 0; i < legalPositionsList.size(); i++) {
            actionHeuristics.add(0);
        }

        int[][] layoutMillList = board.getLayoutMillList();
        ArrayList<ArrayList<Integer>> positionToMillIndex = getPositionToMillIndex(layoutMillList);
        ArrayList<Integer> goodHeuristicMoves = new ArrayList<Integer>();
        
        Integer legalPositionIndex = 0;
        for (Integer legalPosition : legalPositionsList) {
            ArrayList<Integer> millsToBeExamined = positionToMillIndex.get(legalPosition);
            Integer actionScore = 0;
            
            for (Integer indexOfMillToBeExamined : millsToBeExamined) {
                int[] futureStateMill = {-1, -1, -1};
                int[] currentStateMill = {-1, -1, -1};
                int[] millBeingExamined = layoutMillList[indexOfMillToBeExamined];
                int copyIndex = 0;
                int targetPositionIndex = -1;
                for (int millPosition : millBeingExamined) {
                    if (millPosition == legalPosition){
                        targetPositionIndex = copyIndex;
                    }
                    futureStateMill[copyIndex] = board.getPositionState(millPosition);
                    currentStateMill[copyIndex] = board.getPositionState(millPosition);
                    copyIndex++;
                }

                futureStateMill[targetPositionIndex] = positionStateResultingFromAction;

                actionScore += getHeuristicScoreOfMill(futureStateMill, millBeingExamined) - getHeuristicScoreOfMill(currentStateMill, millBeingExamined);
                
            }
            if (positionStateResultingFromAction == -1 ){
                if (board.getPositionState(legalPositionsList.get(0)) == getPlayerNumber()){
                    Integer proximityToCreatingMill = getShortestPathToCreateMill(legalPosition, legalPosition, -1, getPlayerNumber()).size();
                    if (proximityToCreatingMill == 1){
                        actionScore += 200;
                    }
                    else if (proximityToCreatingMill == 2){
                        actionScore += 70;
                    }
                    else if (proximityToCreatingMill >= 3){
                        actionScore += 50 - (proximityToCreatingMill-3)*10;
                    }
                    ArrayList<Integer> emptyAdjacentPositions = new ArrayList<Integer>();
                    ArrayList<Integer> notEmptyAdjacentPositions = new ArrayList<Integer>();
                    for (Integer adjacentPosition : board.getAdjacentPositions(legalPosition)) {
                        if (board.getPositionState(adjacentPosition) == -1){
                            emptyAdjacentPositions.add(adjacentPosition);
                        }
                        else{
                            notEmptyAdjacentPositions.add(adjacentPosition);
                        }
                    }
    
                    Integer bestScoreChange = -1;
                    for (Integer emptyPosition : emptyAdjacentPositions) {
                        Integer positionScoreChange = 0;
                        for (Integer notEmptyPosition : notEmptyAdjacentPositions) {
                            Integer posOrNeg = 1;
                            if (board.getPositionState(notEmptyPosition) != getPlayerNumber()){
                                posOrNeg = -1;
                            }
    
                            proximityToCreatingMill = getShortestPathToCreateMill(notEmptyPosition, legalPosition, emptyPosition, getPlayerNumber()).size();
    
                            if (proximityToCreatingMill == 1){
                                positionScoreChange += 200 * posOrNeg;
                            }
                            else if (proximityToCreatingMill == 2){
                                positionScoreChange += 70 * posOrNeg;
                            }
                            else if (proximityToCreatingMill >= 3){
                                positionScoreChange += (50 - (proximityToCreatingMill-3)*10) * posOrNeg;
                            }
                        }
                        if (bestScoreChange == -1 || positionScoreChange > bestScoreChange){
                            bestScoreChange = positionScoreChange;
                        }
                        
                    }
                    actionScore += bestScoreChange;
                }
                else{
                    Integer proximityToCreatingMill = getShortestPathToCreateMill(legalPosition, legalPosition, -1, board.getPositionState(legalPositionsList.get(0))).size();
                    if (proximityToCreatingMill == 1){
                        actionScore += 300;
                    }
                    else if (proximityToCreatingMill == 2){
                        actionScore += 100;
                    }
                    else if (proximityToCreatingMill >= 3){
                        actionScore += 50 - (proximityToCreatingMill-3)*10;
                    }
                }
            }
            actionHeuristics.set(legalPositionIndex, actionScore);
            legalPositionIndex++;
        }

        for (int i = 0; i < actionHeuristics.size(); i++) {
            if (actionHeuristics.get(i) - Collections.max(actionHeuristics) == 0){
                goodHeuristicMoves.add(legalPositionsList.get(i));
            }
        }
        return goodHeuristicMoves;
    }

    /**
     * Gets the heuristic score of a mill
     * @param millOfStates the states of the positions in the mill
     * @return the heuristic score of the mill
     */
    private Integer getHeuristicScoreOfMill(int[] millOfStates, int[] millOfPositions){
        Integer heuristicScore = 0;
        int playerCount = 0;
        int opponentCount = 0;
        int emptyCount = 0;
        int playerIndex = -1;
        int emptyIndex = -1;
        int indexOfMill = 0;
        for (int millPosition : millOfStates) {
            if (millPosition == getPlayerNumber()){
                playerCount++;
                playerIndex = indexOfMill;
            }
            else if (millPosition == -1){
                emptyCount++;
                emptyIndex = indexOfMill;
            }
            else{
                opponentCount++;
            }
            indexOfMill++;
        }

        if (opponentCount == 0){
            heuristicScore += 100 * playerCount;
            if (emptyCount == 1){
                heuristicScore += 100;
                if (emptyCount == 1){
                    if (getNumTokensToPlace() > 0){
                        Integer positionOfOpen = millOfPositions[emptyIndex];
                        ArrayList<Integer> positionsAdjacentToBlocker = board.getAdjacentPositions(positionOfOpen);
                        for (Integer millPosition : millOfPositions) {
                            if (positionsAdjacentToBlocker.contains(millPosition)){
                                positionsAdjacentToBlocker.remove(millPosition);
                            }
                        }
    
                        for (Integer positionAdjacentToBlocker : positionsAdjacentToBlocker) {
                            if (board.getPositionState(positionAdjacentToBlocker) == getPlayerNumber()){
                                heuristicScore += 150;
                                break;
                            }
                        }
                    }
                    else{
                        heuristicScore -= 200;
                    }
                }
            }
        }
        else if (playerCount == 0){
            heuristicScore -= 70*opponentCount;
            if (emptyCount == 1){
                if (getNumTokensToPlace() > 0){
                    Integer positionOfOpen = millOfPositions[emptyIndex];
                    ArrayList<Integer> positionsAdjacentToBlocker = board.getAdjacentPositions(positionOfOpen);
                    for (Integer millPosition : millOfPositions) {
                        if (positionsAdjacentToBlocker.contains(millPosition)){
                            positionsAdjacentToBlocker.remove(millPosition);
                        }
                    }

                    for (Integer positionAdjacentToBlocker : positionsAdjacentToBlocker) {
                        if (board.getPositionState(positionAdjacentToBlocker) != -1 && board.getPositionState(positionAdjacentToBlocker) != getPlayerNumber()){
                            heuristicScore -= 200;
                            break;
                        }
                    }
                }
                else{
                    heuristicScore -= 200;
                }
                
            }
        }
        else if (playerCount == 1 && opponentCount > 0){
            heuristicScore += 50*opponentCount;
            if (emptyCount == 0){
                if (getNumTokensToPlace() > 0){
                    Integer positionOfBlocker = millOfPositions[playerIndex];
                    ArrayList<Integer> positionsAdjacentToBlocker = board.getAdjacentPositions(positionOfBlocker);
                    for (Integer millPosition : millOfPositions) {
                        if (positionsAdjacentToBlocker.contains(millPosition)){
                            positionsAdjacentToBlocker.remove(millPosition);
                        }
                    }

                    for (Integer positionAdjacentToBlocker : positionsAdjacentToBlocker) {
                        if (board.getPositionState(positionAdjacentToBlocker) != -1 && board.getPositionState(positionAdjacentToBlocker) != getPlayerNumber()){
                            heuristicScore += 150;
                            break;
                        }
                    }
                }
                else{
                    heuristicScore += 150;
                }
            }
        }
        else if (opponentCount == 1 && playerCount > 1){
            heuristicScore -= 30*playerCount;
        }
        
        return heuristicScore;
    }


    /**
     * Gets the shortest path to create a mill
     * @param startPosition the position to start from
     * @param positionThatIsNowOpen the position that is now open
     * @param positionThatIsNowPlayer the position that is now the player
     * @param playerNumber the player number
     * @return
     */
    private ArrayList<Integer> getShortestPathToCreateMill(Integer startPosition, Integer positionThatIsNowOpen, Integer positionThatIsNowPlayer, Integer playerNumber){
        searchedPositions = new ArrayList<Integer>();
        return getShortestPathToCreateMillRecursive(startPosition, positionThatIsNowOpen, positionThatIsNowPlayer, playerNumber, new ArrayList<Integer>(), 0);
    }


    /**
     * Gets the shortest path to create a mill recursively using breadth first search
     * @param startPosition the position to start from
     * @param positionThatIsNowOpen the position that is now open
     * @param positionThatIsNowPlayer the position that is now the player
     * @param playerNumber the player number
     * @param currentPath the current path
     * @param depth the depth of the search
     * @return the shortest path to create a mill
     */
    private ArrayList<Integer> getShortestPathToCreateMillRecursive(Integer startPosition, Integer positionThatIsNowOpen, Integer positionThatIsNowPlayer, Integer playerNumber, ArrayList<Integer> currentPath, Integer depth){
        int[][] layoutMillList = board.getLayoutMillList();
        ArrayList<ArrayList<Integer>> positionToMillIndex = getPositionToMillIndex(layoutMillList);
        boolean millFound = false;
        ArrayList<Integer> adjacentPositions = board.getAdjacentPositions(startPosition);
        ArrayList<Integer> adjacentEmptyUnexaminedPositions = new ArrayList<Integer>();
        currentPath.add(startPosition);

        for (Integer adjacentPosition : adjacentPositions) {
            if ((board.getPositionState(adjacentPosition) == -1 || adjacentPosition == positionThatIsNowOpen) && adjacentPosition != positionThatIsNowPlayer){
                if (!searchedPositions.contains(adjacentPosition)){
                    adjacentEmptyUnexaminedPositions.add(adjacentPosition);
                }
                ArrayList<Integer> millsToBeExamined = positionToMillIndex.get(adjacentPosition);
                for (Integer indexOfMillToBeExamined : millsToBeExamined) {
                    int[] millToBeExamined = layoutMillList[indexOfMillToBeExamined];
                    int emptyCount = 0;
                    int opponentCount = 0;
                    for (int millPosition : millToBeExamined) {
                        if (millPosition == positionThatIsNowPlayer){}
                        else if (board.getPositionState(millPosition) == -1 || millPosition == positionThatIsNowOpen){
                            emptyCount++;
                        }
                        else if (board.getPositionState(millPosition) != playerNumber){
                            opponentCount++;
                        }
                    }
                    if (opponentCount == 0 && emptyCount == 1){
                        millFound = true;
                    }
                }
            }
        }

        ArrayList<Integer> bestPath = new ArrayList<Integer>();
        if (!millFound && depth < 7){
            Integer bestPathLength = -1;
            for (Integer position : adjacentEmptyUnexaminedPositions) {
                ArrayList<Integer> pathBeingEvaluated = getShortestPathToCreateMillRecursive(position, positionThatIsNowOpen, positionThatIsNowPlayer, playerNumber, currentPath, depth + 1);
                if (bestPathLength == -1 || pathBeingEvaluated.size() < bestPathLength){
                    bestPath = pathBeingEvaluated;
                    bestPathLength = bestPath.size();
                }
            }
        }
        else{
            bestPath = currentPath;
        }
        return bestPath;
    }

    /**
     * This method gets the position to mill index, which is a list of lists that contains the index of the mill that each position is in
     * @param layoutMillList the layout mill list
     * @return the position to mill index
     */
    private ArrayList<ArrayList<Integer>> getPositionToMillIndex(int[][] layoutMillList){
        ArrayList<ArrayList<Integer>> positionToMillIndex = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < board.numberOfPositions(); i++) {
            positionToMillIndex.add(new ArrayList<Integer>());
        }
        Integer millIndex = 0;
        for (int[] mill : layoutMillList) {
            for (int millPosition : mill) {
                positionToMillIndex.get(millPosition).add(millIndex);
            }
            millIndex++;
        }
        return positionToMillIndex;
    }
}
