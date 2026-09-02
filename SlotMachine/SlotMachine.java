import java.util.*;
import javax.swing.*;

/**
 * A slot machine simulator.
 * Manages wheels, symbols, spinning, and jackpot detection.
 * Can operate in visible mode (graphical) or invisible mode (logic only).
 * 
 * @author Slot Machine Team
 * @version 1.0
 */
public class SlotMachine {
    private List<Wheel> wheels;
    private Symbol winningConfiguration;
    private boolean isVisible;
    private Canvas canvas;

    /**
     * Create a new slot machine with the default configuration.
     * Initializes 3 empty wheels and no winning configuration.
     */
    public SlotMachine() {
        this.wheels = new ArrayList<>();
        this.winningConfiguration = null;
        this.isVisible = false;
        this.canvas = null;
        
        // Initialize with 3 empty wheels
        addWheel(new Wheel(1));
        addWheel(new Wheel(2));
        addWheel(new Wheel(3));
    }

    /**
     * Add a wheel to the slot machine.
     * 
     * @param wheel  The wheel to add
     * @return true if added successfully
     */
    public boolean addWheel(Wheel wheel) {
        if (wheel == null) {
            return false;
        }
        wheels.add(wheel);
        return true;
    }

    /**
     * Remove a wheel at the given index.
     * 
     * @param index  The index of the wheel to remove (0-based)
     * @return true if removed successfully, false if index is invalid
     */
    public boolean removeWheel(int index) {
        if (index < 0 || index >= wheels.size()) {
            showErrorMessage("Invalid wheel index: " + index);
            return false;
        }
        wheels.remove(index);
        return true;
    }

    /**
     * Add a symbol to the wheel at the given index.
     * 
     * @param wheelIndex  The index of the wheel
     * @param symbol      The symbol to add
     * @return true if added successfully
     */
    public boolean addSymbol(int wheelIndex, Symbol symbol) {
        if (wheelIndex < 0 || wheelIndex >= wheels.size()) {
            showErrorMessage("Invalid wheel index: " + wheelIndex);
            return false;
        }
        if (symbol == null) {
            showErrorMessage("Symbol cannot be null");
            return false;
        }
        return wheels.get(wheelIndex).addSymbol(symbol);
    }

    /**
     * Remove a symbol from the wheel at the given indices.
     * 
     * @param wheelIndex   The index of the wheel
     * @param symbolIndex  The index of the symbol in the wheel
     * @return true if removed successfully
     */
    public boolean removeSymbol(int wheelIndex, int symbolIndex) {
        if (wheelIndex < 0 || wheelIndex >= wheels.size()) {
            showErrorMessage("Invalid wheel index: " + wheelIndex);
            return false;
        }
        return wheels.get(wheelIndex).removeSymbol(symbolIndex);
    }

    /**
     * Spin all wheels in the machine.
     * Each wheel selects a random symbol.
     * 
     * @return true if spin was successful (all wheels have at least 1 symbol)
     */
    public boolean spin() {
        for (Wheel wheel : wheels) {
            if (wheel.getSymbolCount() == 0) {
                showErrorMessage("Cannot spin: wheel " + wheel.getWheelNumber() + 
                               " has no symbols");
                return false;
            }
            wheel.spin();
        }
        return true;
    }

    /**
     * Get a string representation of the current symbols on all wheels.
     * 
     * @return A formatted string showing each wheel's current symbol
     */
    public String consultSymbols() {
        StringBuilder sb = new StringBuilder();
        sb.append("Slot Machine Status:\n");
        for (Wheel wheel : wheels) {
            sb.append(wheel.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Check if the current configuration is a jackpot.
     * A jackpot occurs when all wheels show the same symbol.
     * 
     * @return true if all wheels have the same symbol, false otherwise
     */
    public boolean checkJackpot() {
        if (wheels.isEmpty()) {
            return false;
        }
        Symbol firstSymbol = wheels.get(0).getCurrentSymbol();
        if (firstSymbol == null) {
            return false;
        }
        for (Wheel wheel : wheels) {
            Symbol current = wheel.getCurrentSymbol();
            if (current == null || !current.getName().equals(firstSymbol.getName())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Make the slot machine visible.
     * Initializes the canvas and displays the machine graphically.
     */
    public void makeVisible() {
        if (isVisible) {
            return;
        }
        isVisible = true;
        canvas = Canvas.getCanvas();
        for (Wheel wheel : wheels) {
            wheel.makeVisible();
        }
    }

    /**
     * Make the slot machine invisible.
     * Hides all visual elements but preserves internal state.
     */
    public void makeInvisible() {
        if (!isVisible) {
            return;
        }
        isVisible = false;
        for (Wheel wheel : wheels) {
            wheel.makeInvisible();
        }
    }

    /**
     * Check if the machine is currently visible.
     * 
     * @return true if visible, false otherwise
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * Get the number of wheels in this machine.
     * 
     * @return The wheel count
     */
    public int getWheelCount() {
        return wheels.size();
    }

    /**
     * Get a wheel at the given index.
     * 
     * @param index  The wheel index
     * @return The wheel, or null if index is invalid
     */
    public Wheel getWheel(int index) {
        if (index < 0 || index >= wheels.size()) {
            return null;
        }
        return wheels.get(index);
    }

    /**
     * Terminate the slot machine simulator.
     * Cleans up resources and exits.
     */
    public void exit() {
        makeInvisible();
        System.out.println("Slot Machine Simulator terminated.");
    }

    /**
     * Show an error message in a JOptionPane dialog.
     * Only displays if the machine is visible.
     * 
     * @param message  The error message to display
     */
    private void showErrorMessage(String message) {
        if (isVisible) {
            JOptionPane.showMessageDialog(null, message, "Error", 
                                        JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Return a string representation of the machine.
     * 
     * @return A string with machine status
     */
    @Override
    public String toString() {
        return "SlotMachine with " + wheels.size() + " wheels " + 
               (isVisible ? "[VISIBLE]" : "[INVISIBLE]");
    }
}
