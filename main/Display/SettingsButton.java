package Display;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class SettingsButton extends JLabel{
    
    public SettingsButton(){
        this.setBounds(1300, 20, 50, 50);
        this.setVisible(true);
        this.setFocusable(false);
        this.setIcon(new ImageIcon(new ImageIcon("main/Display/img/settings.png").getImage().getScaledInstance(50, 50, -1)));
    }
                
    
    @Override
    public boolean contains(int x, int y) {
        /* Controls which part of the button is clickable. */
        int radius = 25;
        int center = radius;
        int distX = center - x;
        int distY = center - y;
        return (distX * distX + distY * distY) <= (radius * radius);
    }
}
