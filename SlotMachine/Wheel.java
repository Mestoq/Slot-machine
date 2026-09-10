import java.util.*;

/**
 * Una rueda de la máquina. Guarda sus propios símbolos y muestra
 * cuál de ellos está activo en pantalla.
 */
public class Wheel {
    private List<Symbol> symbols;
    private Symbol currentSymbol;
    private int wheelNumber;
    private boolean isVisible;
    private boolean isHeld;
    private int currentPositionIndex;

    private int xPosition;
    private int yPosition;
    private static final int SIZE = 90;
    private static final int MARGIN = 20;

    public Wheel(int wheelNumber) {
        this.symbols = new ArrayList<>();
        this.currentSymbol = null;
        this.wheelNumber = wheelNumber;
        this.isVisible = false;
        this.isHeld = false;
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
     * Agrega un símbolo nuevo a esta rueda. Si es el primero que llega,
     * se queda como el símbolo que se ve por ahora.
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
     * Quita un símbolo de esta rueda. Si era el que se estaba mostrando,
     * se elige otro para mostrar en su lugar (o ninguno si ya no quedan).
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
     * Dice cuántos símbolos tiene esta rueda guardados.
     */
    public int getSymbolCount() { return symbols.size(); }

    /**
     * Devuelve los símbolos que tiene esta rueda, en el orden en que
     * se fueron agregando.
     */
    public List<Symbol> getSymbols() { return new ArrayList<>(symbols); }

    /**
     * Revisa si esta rueda ya tiene un símbolo de este color.
     * Sirve para no repetir colores en la misma rueda.
     */
    public boolean hasColor(String color) {
        for (Symbol s : symbols) {
            if (s.getColor().equals(color)) return true;
        }
        return false;
    }

    /**
     * Gira la rueda: elige un símbolo al azar entre los que tiene y lo
     * deja mostrado.
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
     * Pone como símbolo actual uno que ya tiene guardado esta rueda,
     * buscándolo por color. No elige al azar como spin, tú decides cuál.
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
    public void hold() { isHeld = true; }

    /**
     * Suelta esta rueda: vuelve a girar normalmente con spin().
     */
    public void release() { isHeld = false; }

    /**
     * Dice si esta rueda está fija actualmente.
     */
    public boolean isHeld() { return isHeld; }

    /**
     * Rota la rueda un número de pasos sobre sus posiciones fijas
     * (0, 1, 2... según cuántos símbolos tenga). Si la rueda es
     * visible, cada paso se refleja en el canvas llamando a draw().
     * steps es el número de pasos a avanzar (negativo para retroceder)
     * retornara el símbolo que queda mostrado al final del recorrido
     */
    public Symbol rotate(int steps) {
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
     * Intercambia el contenido lógico completo de esta rueda con otra
     * (símbolos, símbolo actual y posición), sin afectar la posición
     * visual ni el número de ninguna de las dos ruedas.
     * other es la otra rueda con la que se intercambia el contenido
     */
    void swapContentWith(Wheel other) {
        List<Symbol> tempSymbols = this.symbols;
        Symbol tempCurrent = this.currentSymbol;
        int tempPosition = this.currentPositionIndex;

        this.symbols = other.symbols;
        this.currentSymbol = other.currentSymbol;
        this.currentPositionIndex = other.currentPositionIndex;

        other.symbols = tempSymbols;
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
     * constructor). Propaga el nuevo número a todos sus símbolos, ya que
     * cada símbolo depende de su rueda.
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
               (isHeld ? " [HELD]" : "");
    }
}