import java.util.*;

/**
 * Una rueda de la máquina. Todas las instancias comparten la misma cinta
 * lógica de símbolos (lista estática); cada rueda mantiene su propia posición
 * actual dentro de esa cinta y su propia lista de símbolos VISUALES.
 */
public class Wheel {
    private static List<Symbol> symbols = new ArrayList<>();
    private List<Symbol> visualSymbols = new ArrayList<>();
    private Symbol currentSymbol;    
    private int wheelNumber;
    private boolean isVisible;
    private boolean isLocked;
    private int currentPositionIndex;

    private int xPosition;
    private int yPosition;
    private static final int SIZE = 90;
    private static final int MARGIN = 20;

    public Wheel(int wheelNumber) {
        this.currentSymbol = null;
        this.wheelNumber = wheelNumber;
        this.isVisible = false;
        this.isLocked = false;
        this.currentPositionIndex = 0;
        this.xPosition = 120 + ((wheelNumber - 1) * (SIZE + MARGIN));
        this.yPosition = 120;

        syncVisuals();

        if (!symbols.isEmpty()) {
            this.currentSymbol = symbols.get(0);
            this.currentPositionIndex = 0;
        }
    }

    public static void clearSharedTape() {
        symbols.clear();
    }


    /**
     * Reconcilia visualSymbols con la cinta compartida (por color):
     * reutiliza visuales existentes, crea los faltantes y descarta los sobrantes.
     * También reubica currentPositionIndex si la cinta cambió.
     * Es idempotente: se puede llamar las veces que haga falta.
     */
    void syncVisuals() {
        List<Symbol> synced = new ArrayList<>();
        for (Symbol logical : symbols) {
            Symbol visual = findVisual(logical.getColor());
            synced.add(visual != null ? visual : new Symbol(logical.getColor(), wheelNumber));
        }

        for (Symbol old : visualSymbols) {
            if (!synced.contains(old)) old.makeInvisible(); // visual huérfano
        }
        visualSymbols = synced;

        if (currentSymbol != null) {
            int idx = symbols.indexOf(currentSymbol);
            if (idx >= 0) {
                currentPositionIndex = idx; // la cinta pudo desplazar índices
            } else {                        // el símbolo actual fue eliminado
                currentSymbol = symbols.isEmpty() ? null : symbols.get(0);
                currentPositionIndex = 0;
                if (isVisible && currentSymbol != null) draw();
            }
        }
    }

    private Symbol findVisual(String color) {
        for (Symbol v : visualSymbols) {
            if (v.getColor().equals(color)) return v;
        }
        return null;
    }

    private Symbol currentVisual() {
        if (currentSymbol == null || currentPositionIndex >= visualSymbols.size()) return null;
        return visualSymbols.get(currentPositionIndex);
    }

    private void hideCurrentVisual() {
        Symbol v = currentVisual();
        if (v != null) v.makeInvisible();
    }

    /** Cambia el símbolo actual a la posición dada de la cinta y lo redibuja. */
    private void moveTo(int index) {
        hideCurrentVisual();
        currentPositionIndex = index;
        currentSymbol = symbols.get(index);
        draw();
    }

    // ===================== Consultas =====================

    public int getWheelNumber() { return wheelNumber; }
    public int getXPosition() { return xPosition; }
    public Symbol getCurrentSymbol() { return currentSymbol; }
    public int getSymbolCount() { return symbols.size(); }
    public List<Symbol> getSymbols() { return new ArrayList<>(symbols); }

    public boolean hasColor(String color) {
        for (Symbol s : symbols) {
            if (s.getColor().equals(color)) return true;
        }
        return false;
    }

    // ===================== Cinta =====================

    public boolean addSymbol(Symbol symbol) {
        if (symbol == null) return false;
        symbols.add(symbol);
        syncVisuals(); // el visual debe existir antes de poder dibujarlo
        if (currentSymbol == null) {
            currentSymbol = symbol;
            currentPositionIndex = symbols.size() - 1;
            if (isVisible) draw();
        }
        return true;
    }

    public boolean removeSymbol(Symbol symbol) {
        boolean removed = symbols.remove(symbol);
        if (removed) syncVisuals(); // oculta el visual, reubica índice y redibuja si hace falta
        return removed;
    }

    // ===================== Movimiento =====================

    public Symbol spin() {
        if (symbols.isEmpty()) return null;
        moveTo((int) (Math.random() * symbols.size()));
        return currentSymbol;
    }

    public boolean placeSymbol(String color) {
        for (int i = 0; i < symbols.size(); i++) {
            if (symbols.get(i).getColor().equals(color)) {
                moveTo(i);
                return true;
            }
        }
        return false;
    }

    public Symbol spin(int steps) {
        if (symbols.isEmpty()) return null;

        int direction = (steps < 0) ? -1 : 1;
        int totalSteps = Math.abs(steps);

        for (int i = 0; i < totalSteps; i++) {
            moveTo(Math.floorMod(currentPositionIndex + direction, symbols.size()));
        }
        return currentSymbol;
    }

    public void hold() { isLocked = true; }
    public void release() { isLocked = false; }
    public boolean isHeld() { return isLocked; }

    /**
     * Intercambia la posición en la cinta de ambas ruedas. Como cada rueda
     * tiene sus propios visuales, se ocultan antes del intercambio.
     */
    void swapContentWith(Wheel other) {
        this.hideCurrentVisual();
        other.hideCurrentVisual();

        Symbol tempCurrent = this.currentSymbol;
        int tempPosition = this.currentPositionIndex;

        this.currentSymbol = other.currentSymbol;
        this.currentPositionIndex = other.currentPositionIndex;

        other.currentSymbol = tempCurrent;
        other.currentPositionIndex = tempPosition;

        if (this.isVisible) this.draw();
        if (other.isVisible) other.draw();
    }

    // ===================== Visibilidad / posición =====================

    public void makeVisible() { isVisible = true; draw(); }
    public void makeInvisible() { erase(); isVisible = false; }
    public boolean isVisible() { return isVisible; }

    public void setPosition(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
        if (isVisible) draw();
    }

    public void setWheelIndex(int newIndex) {
        this.wheelNumber = newIndex;
        this.xPosition = 120 + ((newIndex - 1) * (SIZE + MARGIN));
        for (Symbol s : symbols) s.setWheelIndex(newIndex);      
        for (Symbol v : visualSymbols) v.setWheelIndex(newIndex);  
    }


    private void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, "gray",
                new java.awt.Rectangle(xPosition, yPosition, SIZE, SIZE));
            canvas.wait(10);
            Symbol visual = currentVisual();
            if (visual != null) {
                visual.setPosition(xPosition + MARGIN, yPosition + MARGIN);
                visual.makeVisible();
            }
        }
    }

    private void erase() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
            hideCurrentVisual();
        }
    }

    @Override
    public String toString() {
        return "Wheel " + wheelNumber + ": " +
               (currentSymbol != null ? currentSymbol.toString() : "empty") +
               (isLocked ? " [HELD]" : "");
    }
}