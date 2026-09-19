import java.util.*;

/**
 * Una rueda de la máquina. Todas las instancias comparten la misma cinta
 * de símbolos (lista estática); cada rueda mantiene su propia posición
 * actual dentro de esa cinta compartida.
 */
public class Wheel {
    private static List<Symbol> symbols = new ArrayList<>(); // corregido: faltaba inicializar
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
    }

    /**
     * Devuelve en qué número está esta rueda ahora mismo.
     */
    public int getWheelNumber() { return wheelNumber; }

    /**
     * Devuelve en qué posición horizontal está dibujada la rueda.
     */
    public int getXPosition() { return xPosition; }

    /**
     * Agrega un símbolo nuevo a la cinta compartida. Si es el primero
     * que llega, se queda como el símbolo que se ve por ahora en esta rueda.
     */
    public boolean addSymbol(Symbol symbol) {
        if (symbol == null) return false;
        symbols.add(symbol);
        if (currentSymbol == null) {
            currentSymbol = symbol;
            currentPositionIndex = symbols.size() - 1;
            if (isVisible) draw();
        }
        return true;
    }

    /**
     * Quita el símbolo de la cinta compartida, reemplazándolo si era el actual.
     */
    public boolean removeSymbol(Symbol symbol) {
        boolean removed = symbols.remove(symbol);
        if (removed && symbol == currentSymbol) {
            if (isVisible) symbol.makeInvisible();
            currentSymbol = symbols.isEmpty() ? null : symbols.get(0);
            currentPositionIndex = 0;
            if (isVisible && currentSymbol != null) draw();
        }
        return removed;
    }

    /**
     * Muestra cuál es el símbolo que esta rueda tiene en pantalla ahora.
     */
    public Symbol getCurrentSymbol() { return currentSymbol; }

    /**
     * Dice cuántos símbolos hay en la cinta compartida.
     */
    public int getSymbolCount() { return symbols.size(); }

    /**
     * Devuelve los símbolos de la cinta compartida, en el orden en que
     * se fueron agregando.
     */
    public List<Symbol> getSymbols() { return new ArrayList<>(symbols); }

    /**
     * Revisa si la cinta compartida ya tiene un símbolo de este color.
     */
    public boolean hasColor(String color) {
        for (Symbol s : symbols) {
            if (s.getColor().equals(color)) return true;
        }
        return false;
    }

    /**
     * Gira la rueda: elige un símbolo al azar entre los de la cinta
     * compartida y lo deja mostrado en esta rueda.
     */
    public Symbol spin() {
        if (symbols.isEmpty()) return null;
        if (isVisible && currentSymbol != null) currentSymbol.makeInvisible();
        currentPositionIndex = (int) (Math.random() * symbols.size());
        currentSymbol = symbols.get(currentPositionIndex);
        if (isVisible) draw();
        return currentSymbol;
    }

    /**
     * Pone como símbolo actual uno que existe en la cinta compartida,
     * buscándolo por color.
     */
    public boolean placeSymbol(String color) {
        for (int i = 0; i < symbols.size(); i++) {
            Symbol s = symbols.get(i);
            if (s.getColor().equals(color)) {
                if (isVisible && currentSymbol != null) currentSymbol.makeInvisible();
                currentSymbol = s;
                currentPositionIndex = i;
                if (isVisible) draw();
                return true;
            }
        }
        return false;
    }

    /**
     * Fija esta rueda: mientras esté fija, spin() de la máquina no
     * debe modificarla.
     */
    public void hold() { isLocked = true; }

    /**
     * Suelta esta rueda: vuelve a girar normalmente con spin().
     */
    public void release() { isLocked = false; }

    /**
     * Dice si esta rueda está fija actualmente.
     */
    public boolean isHeld() { return isLocked; } // corregido: antes devolvía true fijo

    /**
     * Rota la rueda un número de pasos sobre la cinta compartida.
     * steps es el número de pasos a avanzar (negativo para retroceder).
     * Retorna el símbolo que queda mostrado al final del recorrido.
     */
    public Symbol spin(int steps) {
        if (symbols.isEmpty()) return null;

        int direction = (steps < 0) ? -1 : 1;
        int totalSteps = Math.abs(steps);

        for (int i = 0; i < totalSteps; i++) {
            currentPositionIndex = Math.floorMod(currentPositionIndex + direction, symbols.size());
            currentSymbol = symbols.get(currentPositionIndex);
            draw();
        }
        return currentSymbol;
    }

    /**
     * Intercambia en qué parte de la cinta compartida está posicionada
     * cada rueda (símbolo actual e índice), sin mover su posición visual
     * ni su número. Como la cinta ya es compartida, no hay nada que
     * mover a nivel de listas.
     */
    void swapContentWith(Wheel other) {
        Symbol tempCurrent = this.currentSymbol;
        int tempPosition = this.currentPositionIndex;

        this.currentSymbol = other.currentSymbol;
        this.currentPositionIndex = other.currentPositionIndex;

        other.currentSymbol = tempCurrent;
        other.currentPositionIndex = tempPosition;

        if (this.isVisible) this.draw();
        if (other.isVisible) other.draw();
    }

    /**
     * Hace que la rueda aparezca en el canvas.
     */
    public void makeVisible() { isVisible = true; draw(); }

    /**
     * Hace que la rueda desaparezca del canvas.
     */
    public void makeInvisible() { erase(); isVisible = false; }

    /**
     * Dice si la rueda está visible en este momento.
     */
    public boolean isVisible() { return isVisible; }

    /**
     * Cambia la posición de la rueda en el canvas y la vuelve a dibujar
     * si está visible.
     */
    public void setPosition(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
        if (isVisible) draw();
    }

    /**
     * Renumera esta rueda y su posición base (misma fórmula que el
     * constructor). Propaga el nuevo número a todos los símbolos de
     * la cinta compartida.
     */
    public void setWheelIndex(int newIndex) {
        this.wheelNumber = newIndex;
        this.xPosition = 120 + ((newIndex - 1) * (SIZE + MARGIN));
        for (Symbol s : symbols) {
            s.setWheelIndex(newIndex);
        }
    }

    /*
     * Dibuja el cuadrado de la rueda y el símbolo que tiene puesto ahora.
     */
    private void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, "gray",
                new java.awt.Rectangle(xPosition, yPosition, SIZE, SIZE));
            canvas.wait(10);
            if (currentSymbol != null) {
                currentSymbol.setPosition(xPosition + MARGIN, yPosition + MARGIN);
                currentSymbol.makeVisible();
            }
        }
    }

    /*
     * Borra la rueda y su símbolo del canvas.
     */
    private void erase() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
            if (currentSymbol != null) currentSymbol.makeInvisible();
        }
    }

    @Override
    public String toString() {
        return "Wheel " + wheelNumber + ": " +
               (currentSymbol != null ? currentSymbol.toString() : "empty") +
               (isLocked ? " [HELD]" : "");
    }
}