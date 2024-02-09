package Players;

/**
 * This class is used to represent a human player in the game. It extends the Player class, and is responsible for
 * storing the player's number and token image path.
 * @see Player
 */
public class HumanPlayer extends Player{
    public HumanPlayer(int playerNum, String tokenImgPath) {
        super(playerNum, tokenImgPath);
    }
}
