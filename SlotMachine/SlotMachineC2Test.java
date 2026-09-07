import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 * Pruebas unitarias del Ciclo 2 para SlotMachine: swapWheels, holdWheel,
 * releaseWheel, rotate y setConfiguration. Se ejecutan en modo invisible.
 *
 */
public class SlotMachineC2Test
{
    private SlotMachine machine;

    /**
     * Default constructor for test class SlotMachineC2Test
     */
    public SlotMachineC2Test()
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

    // swapWheels 

    @Test
    public void swapWheelsShouldExchangeSymbolsBetweenTwoValidWheels()
    {
        boolean success = machine.swapWheels(1, 2);

        assertTrue(success);
        assertTrue(machine.ok());
        assertEquals("green,yellow", machine.symbols(1));
        assertEquals("red,blue", machine.symbols(2));
    }

    @Test
    public void swapWheelsShouldFailWhenAWheelDoesNotExist()
    {
        boolean success = machine.swapWheels(1, 99);

        assertFalse(success);
        assertFalse(machine.ok());
        assertEquals("red,blue", machine.symbols(1));
    }

    // holdWheel / releaseWheel

    @Test
    public void holdWheelShouldPreventItFromSpinning()
    {
        machine.placeSymbol(1, "red");
        machine.holdWheel(1);

        machine.spin();

        assertEquals("red", machine.getWheel(0).getCurrentSymbol().getColor());
    }

    @Test
    public void releaseWheelShouldAllowItToSpinAgain()
    {
        machine.holdWheel(1);
        machine.releaseWheel(1);
        machine.spin();

        String current = machine.getWheel(0).getCurrentSymbol().getColor();
        assertTrue(current.equals("red") || current.equals("blue"));
    }


    // rotate 

    @Test
    public void rotateShouldMoveToNextSymbolInWheel()
    {
        machine.rotate(1, 1);

        assertTrue(machine.ok());
        assertEquals("blue", machine.getWheel(0).getCurrentSymbol().getColor());
    }

    @Test
    public void rotateFullCircleShouldReturnToOriginalSymbol()
    {
        machine.rotate(1, 2);

        assertEquals("red", machine.getWheel(0).getCurrentSymbol().getColor());
    }

    @Test
    public void rotateShouldFailWhenWheelHasNoSymbols()
    {
        machine.addWheel(4);

        boolean success = machine.rotate(4, 1);

        assertFalse(success);
        assertFalse(machine.ok());
    }

    @Test
    public void rotateShouldFailWhenWheelDoesNotExist()
    {
        boolean success = machine.rotate(99, 1);

        assertFalse(success);
        assertFalse(machine.ok());
    }

    // setConfiguration

    @Test
    public void setConfigurationShouldApplyWhenAllColorsAreValid()
    {
        boolean success = machine.setConfiguration("blue,yellow,black");

        assertTrue(success);
        assertTrue(machine.ok());
        assertEquals("blue,yellow,black", machine.configuration());
    }

    @Test
    public void setConfigurationShouldChangeNothingWhenOneColorIsInvalid()
    {
        String before = machine.configuration();

        boolean success = machine.setConfiguration("blue,purple,black");

        assertFalse(success);
        assertFalse(machine.ok());
        assertEquals(before, machine.configuration());
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