package Players;

import Actions.ActionController;

/**
 * This interface is used to represent a player that can generate an action.
 */
public interface GeneratesAction {
    /**
     * This method is used to generate a action for the player.
     * @param actionController The action controller that is used to execute the action.
     */
    public void generateAction(ActionController actionController);
}
