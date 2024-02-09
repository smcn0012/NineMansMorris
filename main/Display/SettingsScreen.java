package Display;

import javax.swing.*;

import Game.Game;

public class SettingsScreen extends JFrame {
    
    SettingsScreen(){
		String[] responses = {"Cancel","Confirm"};
		int answer = JOptionPane.showOptionDialog(
				null,
				"Please confirm whether you would like to restart the game?", 
				"Start a new game", 
				JOptionPane.DEFAULT_OPTION, 
				0, 
				new ImageIcon(new ImageIcon("main/Display/img/nine_mens_morris.jpg").getImage().getScaledInstance(50, 50, -1)), 
				responses, 
				responses[0]);
		if (answer == 0) {
            return;
        }
        else if (answer == 1) {
            Game.getInstance().resetGame();
        }
    }
}

