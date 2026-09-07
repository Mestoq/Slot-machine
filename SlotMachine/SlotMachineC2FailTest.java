import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 * Pruebas del Ciclo 2 para SlotMachine que fallan a proposito
 */
public class SlotMachineC2FailTest
{
    private SlotMachine machine;

    /**
     * Default constructor for test class SlotMachineC2FailTest
     */
    public SlotMachineC2FailTest()
    {
    }
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        machine = new SlotMachine();
        // Wheel 1: red, blue
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
        // Wheel 2: green, yellow
        machine.addSymbol(2, "green");
        machine.addSymbol(2, "yellow");
        // Wheel 3: black, magenta
        machine.addSymbol(3, "black");
        machine.addSymbol(3, "magenta");
    }

    @Test
    public void swapWheelsShouldFail()
    {
        machine.swapWheels(1, 2);

        // Falla a propósito: tras el swap wheel 1 queda con "green,yellow",
        // no con "red,blue" como se afirma aquí.
        assertEquals("red,blue", machine.symbols(1));
    }

    @Test
    public void holdWheelShouldFail()
    {
        machine.placeSymbol(1, "red");
        machine.holdWheel(1);
        machine.spin();

        // Falla a propósito: la rueda está fija en "red", no en "blue".
        assertEquals("blue", machine.getWheel(0).getCurrentSymbol().getColor());
    }

    @Test
    public void releaseWheelShouldFail()
    {
        machine.holdWheel(1);
        boolean success = machine.releaseWheel(1);

        // Falla a propósito: releaseWheel sobre una rueda existente
        // devuelve true, no false.
        assertFalse(success);
    }

    @Test
    public void rotateShouldFail()
    {
        machine.rotate(1, 1);

        // Falla a propósito: tras rotar 1 paso wheel 1 queda en "blue",
        // no en "red".
        assertEquals("red", machine.getWheel(0).getCurrentSymbol().getColor());
    }

    @Test
    public void setConfigurationShouldFail()
    {
        boolean success = machine.setConfiguration("blue,yellow,black");

        // Falla a propósito: la configuración es válida, así que
        // success es true, no false.
        assertFalse(success);
    }

    @Test
    public void rotateShouldErr()
    {
        // Forzamos una NullPointerException: wheel 99 no existe, getWheel
        // devuelve null y no se valida antes de usarlo.
        machine.rotate(99, 1);
        String color = machine.getWheel(99).getCurrentSymbol().getColor();
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
        machine = null;
    }
}