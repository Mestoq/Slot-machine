import java.util.*;
import javax.swing.*;
import java.awt.Dimension;



public class SlotMachine {
    private List<Wheel> wheels;
    private boolean isVisible;
    private Canvas canvas;

    private Rectangle mainContainer;
    private int xPosition;
    public static int yPosition;
    private static  int width = 400;
    private static final int HEIGHT = 300;

    /**
     * Create a new slot machine with 3 empty wheels.
     */
    public SlotMachine() {
        this.wheels = new ArrayList<>();
        this.isVisible = false;
        this.canvas = null;
        this.mainContainer = null;
        this.xPosition = 100;
        this.yPosition = 100;

        wheels.add(new Wheel(1));
        wheels.add(new Wheel(2));
        wheels.add(new Wheel(3));
    }

    /**
     * Add a new wheel to the machine at the given position number.
     * 
     * @param wheelNumber  The position number for the new wheel
     * @return true if added successfully, false if that number already exists
     */
    public boolean addWheel(int wheelNumber) {
        for (Wheel w : wheels) {
            if (w.getWheelNumber() == wheelNumber) {
                showErrorMessage("Wheel " + wheelNumber + " already exists");
                return false;
            }
        }
        Wheel wheel = new Wheel(wheelNumber);
        wheels.add(wheel);
        mainContainer.changeSize(HEIGHT, width+90);
        canvas.setCanvasSize( width+30, HEIGHT);
        draw();
        
        if (isVisible) {
            wheel.makeVisible();
        }
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
        Wheel wheel = wheels.get(index);
        if (isVisible) {
            wheel.makeInvisible();
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
        }
        for (Wheel wheel : wheels) {
            wheel.spin();
        }
        if (isVisible) {
            updateJackpotIndicator();
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
     * Draws all wheels first, then the main container on top.
     */
    public void makeVisible() {
        if (isVisible) {
            return;
        }
        isVisible = true;
        canvas = Canvas.getCanvas();
        draw();
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
        erase();
    }

    /**
     * Check if the machine is currently visible.
     * 
     * @return true if visible, false otherwise
     */
    public boolean isVisibleNow() {
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

    /*
     * Draw all wheels, then the main container rectangle on top,
     * with color reflecting jackpot state.
     */
    private void draw() {
        if (isVisible) {
            // Draw container FIRST (background), then wheels ON TOP,
            // otherwise the container paints over the wheels/symbols.
            if (mainContainer == null) {
                // Rectangle's default position is (70, 15); move it once
                // to our target (xPosition, yPosition) using the delta.
                mainContainer = new Rectangle();
                mainContainer.changeSize(HEIGHT, width);
                mainContainer.moveHorizontal(xPosition - 70);
                mainContainer.moveVertical(yPosition - 15);
            }
            mainContainer.changeColor(checkJackpot() ? "red" : "blue");
            mainContainer.makeVisible();

            for (Wheel wheel : wheels) {
                wheel.makeVisible();

            }
        }
    }

    /*
     * Update the main container's color based on jackpot state
     * and redraw it (called after a spin, while visible).
     */
    private void updateJackpotIndicator() {
        if (mainContainer != null) {
            mainContainer.changeColor(checkJackpot() ? "red" : "blue");
        }
    }

    /*
     * Erase the main container and all wheels from the canvas.
     */
    private void erase() {
        for (Wheel wheel : wheels) {
            wheel.makeInvisible();
        }
        if (mainContainer != null) {
            mainContainer.makeInvisible();
        }
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