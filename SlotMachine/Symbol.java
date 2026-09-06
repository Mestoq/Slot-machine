import java.awt.*;

/**
 * Un símbolo que se muestra en una rueda de la máquina.
 * Se identifica por su color y sabe a qué rueda pertenece.
 */
public class Symbol {
    private String color;
    private int wheelIndex;
    private Circle visual;
    private boolean isVisible;
    private int xPosition;
    private int yPosition;
    private static final int DIAMETER = 40;

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

    public String getColor() { return color; }
    public int getWheelIndex() { return wheelIndex; }
    public void setWheelIndex(int newIndex) { this.wheelIndex = newIndex; }

    public void setPosition(int x, int y) {
        int deltaX = x - xPosition;
        int deltaY = y - yPosition;
        visual.moveHorizontal(deltaX);
        visual.moveVertical(deltaY);
        xPosition = x;
        yPosition = y;
    }

    public void makeVisible() { isVisible = true; visual.makeVisible(); }
    public void makeInvisible() { isVisible = false; visual.makeInvisible(); }
    public boolean isVisible() { return isVisible; }

    @Override
    public String toString() {
        return color + " (wheel " + wheelIndex + ")";
    }
}