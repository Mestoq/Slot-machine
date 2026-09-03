import java.awt.*;

/**
 * A symbol that can be displayed on a wheel in the slot machine.
 * Represents a single symbol (circle) with a name and color.
 * 
 * @author Slot Machine Team
 * @version 2.0
 */
public class Symbol {
    private String name;
    private String color;
    private Circle visual;
    private boolean isVisible;

    private int xPosition;
    private int yPosition;
    private static final int DIAMETER = 40;

    /**
     * Create a new symbol with the given name and color.
     * 
     * @param name    The symbol's identifier (e.g., "diamond", "heart", "star")
     * @param color   The symbol's color (used for rendering)
     */
    public Symbol(String name, String color) {
        this.name = name;
        this.color = color;
        this.visual = new Circle();
        this.visual.changeSize(DIAMETER);
        this.visual.changeColor(color);
        this.isVisible = false;
        this.xPosition = 20;   // Default Circle position
        this.yPosition = 15;   // Default Circle position
    }

    /**
     * Get the symbol's name.
     * 
     * @return The symbol's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the symbol's color.
     * 
     * @return The symbol's color string
     */
    public String getColor() {
        return color;
    }

    /**
     * Set the absolute position of this symbol on the canvas.
     * Moves the underlying circle to the exact (x, y) coordinates.
     * 
     * @param x  The target x coordinate
     * @param y  The target y coordinate
     */
    public void setPosition(int x, int y) {
        int deltaX = x - xPosition;
        int deltaY = y - yPosition;
        visual.moveHorizontal(deltaX);
        visual.moveVertical(deltaY);
        xPosition = x;
        yPosition = y;
    }

    /**
     * Make this symbol visible on the canvas.
     */
    public void makeVisible() {
        isVisible = true;
        visual.makeVisible();
    }

    /**
     * Make this symbol invisible on the canvas.
     */
    public void makeInvisible() {
        isVisible = false;
        visual.makeInvisible();
    }

    /**
     * Check if this symbol is currently visible.
     * 
     * @return true if visible, false otherwise
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * Return a string representation of this symbol.
     * 
     * @return A string containing the symbol's name and color
     */
    @Override
    public String toString() {
        return name + " (" + color + ")";
    }
}