package Display;

import javax.swing.*;
import java.awt.*;

/**
 * This class is represents a position on the board. It handles the display of
 * the position as well as clicks from the user.
 * 
 * @see JLabel
 */
public class PositionButton extends JLabel {

    Color positionColor;
    int positionRadius;
    int[] coords;
    boolean isCurrentToken = false;
    boolean isHovering = false;
    boolean isHighlighted = false;
    boolean isSelected = false;
    boolean isInMill = false;
    boolean wasPreviousMove = false;
    boolean wasJustRemoved = false;
    JLabel token;

    /**
     * Constructor for the PositionButton class. Initialises the button with the
     * given parameters.
     * 
     * @param xCoord          the x coordinate of the position
     * @param yCoord          the y coordinate of the position
     * @param _positionRadius the radius of the position
     * @param _positionColor  the color of the position
     * @param boardColor      the color of the board
     */
    public PositionButton(int xCoord, int yCoord, int _positionRadius, Color _positionColor, Color boardColor) {

        this.positionColor = _positionColor;
        this.positionRadius = _positionRadius;
        this.coords = new int[] { xCoord, yCoord };
        this.setBackground(boardColor);
        this.setForeground(positionColor);
        this.setBounds(xCoord - 2 * positionRadius, yCoord - 2 * positionRadius, xCoord + 2 * positionRadius,
                yCoord + 2 * positionRadius);
        this.setSize(4 * _positionRadius, 4 * _positionRadius);
    }

    @Override
    protected void paintComponent(Graphics g) {
        /* Draws the button onto the board */
        Graphics2D g2D = (Graphics2D) g;

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isInMill) {
            g2D.setPaint(new GradientPaint(positionRadius - 5, positionRadius - 5, new Color(133, 199, 151, 100),
                    2 * positionRadius + 9, 2 * positionRadius + 9, new Color(50, 168, 82, 200)));
            g2D.setStroke(new BasicStroke(10));
            g2D.drawOval(positionRadius - 5, positionRadius - 5, 2 * positionRadius + 9, 2 * positionRadius + 9);
        } else if (wasPreviousMove) {
            g2D.setPaint(new GradientPaint(positionRadius - 5, positionRadius - 5, new Color(150, 150, 150, 100),
                    2 * positionRadius + 9, 2 * positionRadius + 9, new Color(255, 255, 255, 200)));
            g2D.setStroke(new BasicStroke(10));
            g2D.drawOval(positionRadius - 5, positionRadius - 5, 2 * positionRadius + 9, 2 * positionRadius + 9);
        } else if (wasJustRemoved) {
            g2D.setPaint(new GradientPaint(positionRadius - 5, positionRadius - 5, new Color(199, 133, 133, 100),
                    2 * positionRadius + 9, 2 * positionRadius + 9, new Color(168, 50, 50, 200)));
            g2D.setStroke(new BasicStroke(10));
            g2D.drawOval(positionRadius - 5, positionRadius - 5, 2 * positionRadius + 9, 2 * positionRadius + 9);
        }

        if (!isCurrentToken) {
            g2D.setColor(positionColor);
            g2D.fillOval(positionRadius, positionRadius, 2 * positionRadius, 2 * positionRadius);
        }

        g2D.setColor(Color.white);
        if (isSelected) {
            g2D.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 8));
            g2D.drawRoundRect(positionRadius + 4, positionRadius + 4, 2 * positionRadius - 9, 2 * positionRadius - 9,
                    2 * positionRadius - 9, 2 * positionRadius - 9);
        } else if (isHovering) {
            g2D.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 5));
            g2D.drawRoundRect(positionRadius + 2, positionRadius + 2, 2 * positionRadius - 5, 2 * positionRadius - 5,
                    2 * positionRadius - 6, 2 * positionRadius - 6);
        } else if (isHighlighted) {
            g2D.setStroke(new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f,
                    new float[] { 6f, 0f, 6f }, 3f));
            g2D.drawRoundRect(positionRadius + 2, positionRadius + 2, 2 * positionRadius - 5, 2 * positionRadius - 5,
                    2 * positionRadius - 6, 2 * positionRadius - 6);
        }

    }

    @Override
    public boolean contains(int x, int y) {
        /* Controls which part of the button is clickable. */
        int radius = 2 * positionRadius;
        int center = 2 * positionRadius;
        int distX = center - x;
        int distY = center - y;
        return (distX * distX + distY * distY) <= (radius * radius);
    }

    /**
     * Returns the coordinates of the position.
     * 
     * @return the coordinates of the position
     */
    public int[] getCoords() {
        return coords;
    }

    /**
     * Setter for the isCurrentToken variable.
     * 
     * @param _isCurrentToken the value to set isCurrentToken to
     */
    public void setIsCurrentToken(boolean _isCurrentToken) {
        isCurrentToken = _isCurrentToken;
    }

    /**
     * Setter for the isHovering variable.
     * 
     * @param _isHovering the value to set isHovering to
     */
    public void setIsHovering(boolean _isHovering) {
        isHovering = _isHovering;
    }

    /**
     * Setter for the isHighlighted variable.
     * 
     * @param _isHighlighted the value to set isHighlighted to
     */
    public void setIsHighlighted(boolean _isHighlighted) {
        isHighlighted = _isHighlighted;
    }

    /**
     * Setter for the isSelected variable.
     * 
     * @param _isSelected the value to set isSelected to
     */
    public void setIsSelected(boolean _isSelected) {
        isSelected = _isSelected;
    }

    /**
     * Setter for the isInMill variable.
     * 
     * @param _isInMill the value to set isHighlighted to
     */
    public void setIsInMill(boolean _isInMill) {
        isInMill = _isInMill;
    }

    /**
     * Setter for the wasPreviousMove variable.
     * 
     * @param _wasPreviousMove the value to set wasPreviousMove to
     */
    public void setWasPreviousMove(boolean _wasPreviousMove) {
        wasPreviousMove = _wasPreviousMove;
    }

    /**
     * Setter for the wasJustRemoved variable.
     * 
     * @param _wasJustRemoved the value to set wasJustRemoved to
     */
    public void setWasJustRemoved(boolean _wasJustRemoved) {
        wasJustRemoved = _wasJustRemoved;
    }

    /**
     * Setter for the token variable.
     * 
     * @param _token the value to set token to
     */
    public void setToken(JLabel _token) {
        token = _token;
    }

    /**
     * Getter for the token variable.
     * 
     * @return the token variable
     */
    public JLabel getToken() {
        return token;
    }
}
