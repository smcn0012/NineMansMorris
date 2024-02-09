package Players;

import java.util.ArrayList;

import Actions.ActionController;

/**
 * This class manages all the players that can generate an action.
 */
public class GeneratesActionController {
    private static GeneratesActionController instance;
    private ArrayList<GeneratesAction> actionGenerators;

    /**
     * This constructor is used to create a GeneratesActionController object.
     */
    private GeneratesActionController() {
        actionGenerators = new ArrayList<GeneratesAction>();
    }

    /**
     * This method is used to get the instance of the GeneratesActionController.
     * @return The instance of the GeneratesActionController.
     */
    public static GeneratesActionController getInstance() {
        if (instance == null) {
            instance = new GeneratesActionController();
        }
        return instance;
    }

    /**
     * This method is used to add a action generator to the list of action generators.
     * @param actionGenerator The action generator to add to the list.
     */
    public void addActionGenerator(GeneratesAction actionGenerator) {
        actionGenerators.add(actionGenerator);
    }

    /**
     * This method is used to generate a move for the player if that player can generate a move.
     * @param player The player to generate a move for.
     * @param actionController The action controller that is used to execute the move.
     */
    public void generateAction(Player player, ActionController actionController) {
        // get the player from the list and generate a move
        if (checkPlayerGeratesAction(player)) {
            int moveGenerator_index = actionGenerators.indexOf(player);
            actionGenerators.get(moveGenerator_index).generateAction(actionController);
        }

    }

    /**
     * This method is used to check if a player can generate an action.
     * @param player The player to check if they can generate an action.
     * @return True if the player can generate an action, false otherwise.
     */
    public boolean checkPlayerGeratesAction(Player player) {
        // checks to see if player can generate action
        return actionGenerators.contains(player);
    }
}
