import java.util.*;

/**
 * A wheel in the slot machine that contains and displays symbols.
 * Each wheel can hold multiple symbols and displays one at a time.
 * 
 * @author Slot Machine Team
 * @version 1.0
 */
public class Wheel {
    private List<Symbol> symbols;
    private Symbol currentSymbol;
    private int wheelNumber;
    private boolean isVisible;

    /**
     * Create a new wheel with an empty list of symbols.
     * 
     * @param wheelNumber  The wheel's position number (e.g., 1, 2, 3)
     */
    public Wheel(int wheelNumber) {
        this.symbols = new ArrayList<>();
        this.currentSymbol = null;
        this.wheelNumber = wheelNumber;
        this.isVisible = false;
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
            currentSymbol = symbols.isEmpty() ? null : symbols.get(0);
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
     * 
     * @return The newly selected symbol, or null if no symbols exist
     */
    public Symbol spin() {
        if (symbols.isEmpty()) {
            return null;
        }
        int randomIndex = (int) (Math.random() * symbols.size());
        currentSymbol = symbols.get(randomIndex);
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
     * This makes the current symbol visible.
     */
    public void makeVisible() {
        isVisible = true;
        if (currentSymbol != null) {
            currentSymbol.makeVisible();
        }
    }

    /**
     * Make this wheel invisible.
     * This makes all symbols invisible.
     */
    public void makeInvisible() {
        isVisible = false;
        for (Symbol symbol : symbols) {
            symbol.makeInvisible();
        }
    }

    /**
     * Check if this wheel is currently visible.
     * 
     * @return true if visible, false otherwise
     */
    public boolean isVisible() {
        return isVisible;
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
}
