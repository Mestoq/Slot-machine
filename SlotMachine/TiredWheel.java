/**
 * Rueda cansada: gira la mitad de los pasos que se le piden y su animación
 * de giro es más lenta.
 */
public class TiredWheel extends Wheel {
    private static final int STEP_DELAY = 150;   // ms de espera entre paso y paso

    public TiredWheel(int wheelNumber) {
        super(wheelNumber, "cyan", STEP_DELAY);
    }

    /** Gira solo la mitad de los pasos pedidos (división entera). */
    @Override
    public Symbol spin(int steps) {
        return super.spin(steps / 2);
    }

    /** Giro libre: elige pasos al azar y, como cualquier giro, se reducen a la mitad. */
    @Override
    public Symbol spin() {
        return spin((int) (Math.random() * getSymbolCount()));
    }
}