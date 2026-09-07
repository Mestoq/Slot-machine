import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 * Pruebas de aceptación del Ciclo 2 para SlotMachine. Cada prueba
 * representa un escenario de uso completo, de principio a fin.
 *
 */
public class SlotMachineAcceptanceTest
{
    private SlotMachine machine;

    /**
     * Default constructor for test class SlotMachineAcceptanceTest
     */
    public SlotMachineAcceptanceTest()
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

    /**
     * Prueba 1
     * el contexto es que el jugador ya tiene dos ruedas mostrando símbolos que
     * le convienen y no quiere arriesgarlos en el próximo giro.
     *
     * Como las ruedas 1 y 2 están mostrando "red" y "green"
     * cuando las fija con holdWheel y luego gira toda la
     * máquina con spin()
     * entonces el giro se acepta, las ruedas fijas
     * conservan el símbolo que tenían antes de girar y
     * la rueda 3 queda con un símbolo que perteneciente
     * a su propio conjunto de símbolos.
     */
    @Test
    public void playerShouldBeAbleToHoldWinningWheelsBeforeSpinning()
    {
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "green");
        machine.holdWheel(1);
        machine.holdWheel(2);

        boolean success = machine.spin();

        assertTrue(success);
        assertTrue(machine.ok());
        assertEquals("red", machine.getWheel(0).getCurrentSymbol().getColor());
        assertEquals("green", machine.getWheel(1).getCurrentSymbol().getColor());

        String wheel3Result = machine.getWheel(2).getCurrentSymbol().getColor();
        assertTrue(wheel3Result.equals("black") || wheel3Result.equals("magenta"));
    }

    /**
     * Prueba 2
     * esta vez el contexto es que el jugador quiere reproducir una
     * combinación específica para probar cómo se ve o comportaría
     * en lugar de depender del la aleatoriedad.
     *
     * Ya que se consulta la configuración actual de la
     * máquina con configuration(),
     * cuando decide una configuración unica distinta
     * con colores que sabe que existen en cada rueda, y la aplica con
     * setConfiguration de ahi la operación se acepta correctamente y la máquina queda
     * mostrando la configuración solicitada por el jugador
     * verificable de nuevo a través de configuration().
     */
    @Test
    public void playerShouldBeAbleToSetADesiredConfigurationAfterCheckingCurrentOne()
    {
        String initialConfiguration = machine.configuration();
        assertEquals("red,green,black", initialConfiguration);

        boolean success = machine.setConfiguration("blue,yellow,magenta");

        assertTrue(success);
        assertTrue(machine.ok());
        assertEquals("blue,yellow,magenta", machine.configuration());
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
