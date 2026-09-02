import java.awt.*;

/**
 * A symbol that can be displayed on a wheel in the slot machine.
 * Represents a single symbol (circle) with a color.
 * Symbols are immutable once created (name and color don't change).
 * 
 * @author Slot Machine Team
 * @version 1.0
 */
public class Symbol {
    private String name;
    private String color;
    private Circle visual;
    private boolean isVisible;

    /**
     * Create a new symbol with the given name and color.
     * Symbols are red circles (color may be overridden, but default is "red").
     * 
     * @param name    The symbol's identifier (e.g., "diamond", "heart", "star")
     * @param color   The symbol's color (used for rendering)
     */
    public Symbol(String name, String color) {
        this.name = name;
        this.color = color;
        this.visual = new Circle();
        this.visual.changeColor(color);
        this.isVisible = false;
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
     * Make this symbol visible on the canvas.
     * The symbol is rendered at its default position (which depends on Canvas).
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
