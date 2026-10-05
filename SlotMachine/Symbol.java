/**
 * Un símbolo que se muestra en una rueda de la máquina (tipo "normal").
 * Se identifica por su color. Los demás tipos lo extienden y sobrescriben
 * action() (qué hace al ser seleccionado) y copy() (crear un visual del mismo tipo).
 */
public class Symbol {
    protected static final int DIAMETER = 40;
    protected static final int PAUSE = 400;          
    protected static final int BADGE_SIZE = 12;      
    protected static final int BADGE_OFFSET_X = 28;  
    protected static final int BADGE_OFFSET_Y = 0;

    private String color;
    private int wheelIndex;
    private Circle visual;
    private boolean isVisible;
    private int xPosition;
    private int yPosition;
    private int diameter = DIAMETER;   // para ephemeral

    public Symbol(String color, int wheelIndex) {
        this.color = color;
        this.wheelIndex = wheelIndex;
        this.visual = new Circle();
        this.visual.changeSize(DIAMETER);
        this.visual.changeColor(color);
        this.isVisible = false;
        this.xPosition = 20;
        this.yPosition = 15;
    }

    /**
     * Qué hace el símbolo cuando la rueda lo deja seleccionado.
     * El símbolo normal no hace nada; cada subtipo responde a su manera.
     */
    public void action() { }

    /** Crea un símbolo nuevo del MISMO tipo y color (se usa para los visuales de cada rueda). */
    public Symbol copy(int wheelIndex) {
        return new Symbol(color, wheelIndex);
    }

    /** Espera un momento para que el efecto se note, solo si el símbolo se está viendo. */
    protected void pause() {
        if (isVisible) Canvas.getCanvas().wait(PAUSE);
    }

    protected int getDiameter() { return diameter; }

    /** Cambia el diámetro del círculo; el centrado lo resuelve setPosition. */
    protected void resize(int newDiameter) {
        diameter = newDiameter;
        visual.changeSize(newDiameter);
    }

    public String getColor() { return color; }
    public int getWheelIndex() { return wheelIndex; }
    public void setWheelIndex(int newIndex) { this.wheelIndex = newIndex; }

    /** Ubica el símbolo centrado en la celda (x, y), sin importar su diámetro actual. */
    public void setPosition(int x, int y) {
        int offset = (DIAMETER - diameter) / 2;
        visual.moveHorizontal(x + offset - xPosition);
        visual.moveVertical(y + offset - yPosition);
        xPosition = x + offset;
        yPosition = y + offset;
    }

    public void makeVisible() { isVisible = true; visual.makeVisible(); }
    public void makeInvisible() { isVisible = false; visual.makeInvisible(); }
    public boolean isVisible() { return isVisible; }

    @Override
    public String toString() {
        return color + " (wheel " + wheelIndex + ")";
    }
}