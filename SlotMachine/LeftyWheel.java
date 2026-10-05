
public class LeftyWheel extends Wheel {

    public LeftyWheel(int wheelNumber) {
        super(wheelNumber, "purple");
    }

    @Override
    public Symbol spin(Wheel left) {
        if (hasSomethingToCopy(left)) return copy(left);
        return spin();
    }

    @Override
    public Symbol spin(Wheel left, int steps) {
        if (hasSomethingToCopy(left)) return copy(left);
        return spin(steps);
    }

    private boolean hasSomethingToCopy(Wheel left) {
        return left != null && left.getCurrentSymbol() != null;
    }

    /** Deja en esta rueda el mismo símbolo que muestra la vecina. */
    private Symbol copy(Wheel left) {
        placeSymbol(left.getCurrentSymbol().getColor());
        return getCurrentSymbol();
    }
}