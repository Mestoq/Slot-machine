import java.util.*;

/**
 * A wheel in the slot machine that contains and displays symbols.
 * Each wheel draws itself as a rectangle with its current symbol
 * (circle) centered inside.
 * 
 * @author Slot Machine Team
 * @version 2.0
 */
public class Wheel {
    private static List<Symbol> symbols;
    private Symbol currentSymbol;
    private int wheelNumber;
    private boolean isVisible;

    private int xPosition;
    private int yPosition;
    private static final int SIZE = 90;
    private static final int MARGIN = 20;

    /**
     * Create a new wheel with an empty list of symbols.
     * Position on the canvas is calculated automatically from the
     * wheel number.
     * 
     * @param wheelNumber  The wheel's position number (e.g., 1, 2, 3)
     */
    public Wheel(int wheelNumber) {
        this.symbols = new ArrayList<>();
        this.currentSymbol = null;
        this.wheelNumber = wheelNumber;
        this.isVisible = false;
        this.xPosition = 120 + ((wheelNumber - 1) * (SIZE + 20));
        this.yPosition = 120;
    }

    /**
     * Get the wheel's position number.
     * 
     * @return The wheel number
     */
    public int getWheelNumber() {
        return wheelNumber;
    }

    /**
     * Add a symbol to this wheel.
     * The first symbol added becomes the current symbol.
     * 
     * @param symbol  The symbol to add
     * @return true if the symbol was added successfully
     */
    public boolean addSymbol(Symbol symbol) {
        if (symbol == null) {
            return false;
        }
        symbols.add(symbol);
        if (currentSymbol == null) {
            currentSymbol = symbol;
            if (isVisible) {
                draw();
            }
        }
        return true;
    }

    /**
     * Remove a symbol at the given index from this wheel.
     * If the removed symbol was current, the next available symbol becomes current.
     * 
     * @param index  The index of the symbol to remove
     * @return true if removal was successful, false if index is invalid
     */
    public boolean removeSymbol(int index) {
        if (index < 0 || index >= symbols.size()) {
            return false;
        }
        Symbol removed = symbols.remove(index);
        if (removed == currentSymbol) {
            if (isVisible) {
                removed.makeInvisible();
            }
            currentSymbol = symbols.isEmpty() ? null : symbols.get(0);
            if (isVisible && currentSymbol != null) {
                draw();
            }
        }
        return true;
    }

    /**
     * Get the current symbol displayed on this wheel.
     * 
     * @return The current symbol, or null if no symbols exist
     */
    public Symbol getCurrentSymbol() {
        return currentSymbol;
    }

    /**
     * Spin the wheel: randomly select a symbol from the wheel's symbols.
     * If visible, hides the previous symbol and shows the new one.
     * 
     * @return The newly selected symbol, or null if no symbols exist
     */
    public Symbol spin() {
        if (symbols.isEmpty()) {
            return null;
        }
        if (isVisible && currentSymbol != null) {
            currentSymbol.makeInvisible();
        }
        int randomIndex = (int) (Math.random() * symbols.size());
        currentSymbol = symbols.get(randomIndex);
        if (isVisible) {
            draw();
        }
        return currentSymbol;
    }

    /**
     * Get the number of symbols in this wheel.
     * 
     * @return The count of symbols
     */
    public int getSymbolCount() {
        return symbols.size();
    }

    /**
     * Get a symbol at the given index.
     * 
     * @param index  The index of the symbol
     * @return The symbol at that index, or null if index is invalid
     */
    public Symbol getSymbol(int index) {
        if (index < 0 || index >= symbols.size()) {
            return null;
        }
        return symbols.get(index);
    }

    /**
     * Make this wheel visible.
     * Draws the wheel's rectangle and its current symbol.
     */
    public void makeVisible() {
        isVisible = true;
        draw();
    }

    /**
     * Make this wheel invisible.
     * Erases the wheel's rectangle and hides all symbols.
     */
    public void makeInvisible() {
        erase();
        isVisible = false;
    }

    /**
     * Check if this wheel is currently visible.
     * 
     * @return true if visible, false otherwise
     */
    public boolean isVisible() {
        return isVisible;
    }

    /*
     * Draw the wheel's rectangle and its current symbol on screen.
     */
    private void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, "gray",
                new java.awt.Rectangle(xPosition, yPosition, SIZE, SIZE));
            canvas.wait(10);

            if (currentSymbol != null) {
                int symbolX = xPosition + MARGIN;
                int symbolY = yPosition + MARGIN;
                currentSymbol.setPosition(symbolX, symbolY);
                currentSymbol.makeVisible();
            }
        }
    }

    /*
     * Erase the wheel's rectangle and hide its current symbol.
     */
    private void erase() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
            if (currentSymbol != null) {
                currentSymbol.makeInvisible();
            }
        }
    }

    /**
     * Return a string representation of this wheel.
     * 
     * @return A string showing the wheel number and current symbol
     */
    @Override
    public String toString() {
        return "Wheel " + wheelNumber + ": " +
               (currentSymbol != null ? currentSymbol.toString() : "empty");
    }
    
    public void setPosition(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
    }
    public void setWheelIndex(int newIndex) {
        this.wheelNumber = newIndex;
        this.xPosition = 120 + (newIndex * 110);
    }
}