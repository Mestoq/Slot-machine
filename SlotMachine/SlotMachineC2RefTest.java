import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

/**
 * Pruebas del Ciclo 2 refactorizado: una prueba por cada método público
 * de SlotMachine, alternando casos de éxito (shouldAccept) y de error
 * (shouldError/Fail). Ejecutadas en modo invisible.
 *
 * Nota: symbols en Wheel es static (cinta compartida global), por eso
 * cada prueba usa colores con un sufijo único (contador de setUp) para
 * no chocar con símbolos agregados por otras pruebas.
 */
public class SlotMachineC2RefTest
{
    private SlotMachine machine;
    private static int counter = 0;

    private String red, blue, green, yellow, black, magenta;

    @BeforeEach
    public void setUp()
    {
        counter++;
        red = "red" + counter;
        blue = "blue" + counter;
        green = "green" + counter;
        yellow = "yellow" + counter;
        black = "black" + counter;
        magenta = "magenta" + counter;

        machine = new SlotMachine();
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.addSymbol(1, red);
        machine.addSymbol(1, blue);
        machine.addSymbol(2, green);
        machine.addSymbol(2, yellow);
        machine.addSymbol(3, black);
        machine.addSymbol(3, magenta);
    }

    // ---------- addWheel ----------

    @Test
    public void accordingDoOlShouldAddNewWheelWhenNumberIsUnique()
    {
        int before = machine.getWheelCount();
        machine.addWheel(100 + counter); // número que no existe aún

        assertTrue(machine.ok());
        assertEquals(before + 1, machine.getWheelCount());
    }

    @Test
    public void accordingDoOlShouldErrorWhenWheelAlreadyExists()
    {
        machine.addWheel(1); // ya existe desde setUp

        assertFalse(machine.ok());
        assertEquals(3, machine.getWheelCount());
    }

    // ---------- delWheel ----------

    @Test
    public void accordingDoOlShouldErrorWhenDeletingUnknownWheel()
    {
        machine.delWheel(999);

        assertFalse(machine.ok());
        assertEquals(3, machine.getWheelCount());
    }

    // ---------- addSymbol ----------

    @Test
    public void accordingDoOlShouldAcceptAddingSymbolToExistingWheel()
    {
        String purple = "purple" + counter;
        machine.addSymbol(1, purple);

        assertTrue(machine.ok());
        assertTrue(machine.getWheel(0).hasColor(purple));
    }

    // ---------- delSymbol ----------

    @Test
    public void accordingDoOlShouldErrorWhenDeletingUnknownSymbol()
    {
        machine.delSymbol("noExiste" + counter);

        assertFalse(machine.ok());
    }

    // ---------- placeSymbol ----------

    @Test
    public void accordingDoOlShouldAcceptPlacingExistingColor()
    {
        machine.placeSymbol(1, blue);

        assertTrue(machine.ok());
        assertEquals(blue, machine.getWheel(0).getCurrentSymbol().getColor());
    }

    // ---------- spin(int) — reemplaza a accordingDoOlShouldErrorWhenAWheelHasNoSymbols ----------
    
    @Test
    public void accordingDoOlShouldErrorWhenSpinningUnknownWheel()
    {
        machine.spin(999);
    
        assertFalse(machine.ok());
    }

    // ---------- spin(int) ----------

    @Test
    public void accordingDoOlShouldAcceptSpinningExistingWheel()
    {
        machine.spin(1);

        assertTrue(machine.ok());
        assertNotNull(machine.getWheel(0).getCurrentSymbol());
    }

    // ---------- spin(int, int) ----------

    @Test
    public void accordingDoOlShouldAcceptRotatingWheelBySteps()
    {
        machine.placeSymbol(1, red);
        int fullTurn = machine.symbols().length; // tamaño actual de la cinta compartida

        machine.spin(1, fullTurn);

        assertTrue(machine.ok());
        assertEquals(red, machine.getWheel(0).getCurrentSymbol().getColor());
    }

    // ---------- spin(String[]) ----------

    @Test
    public void accordingDoOlShouldErrorWhenConfigSizeDoesNotMatch()
    {
        String[] config = { red }; // solo 1 color, pero hay 3 ruedas

        machine.spin(config);

        assertFalse(machine.ok());
    }

    // ---------- swap ----------

    @Test
    public void accordingDoOlShouldErrorWhenSwappingUnknownWheel()
    {
        machine.swap(1, 999);

        assertFalse(machine.ok());
    }

    // ---------- lock ----------

    @Test
    public void accordingDoOlShouldAcceptLockingExistingWheel()
    {
        machine.lock(1);

        assertTrue(machine.ok());
        assertTrue(machine.getWheel(0).isHeld());
    }

    // ---------- unlock ----------

    @Test
    public void accordingDoOlShouldAcceptUnlockingLockedWheel()
    {
        machine.lock(1);
        machine.unlock(1);

        assertTrue(machine.ok());
        assertFalse(machine.getWheel(0).isHeld());
    }

    // ---------- symbols() ----------

    @Test
    public void accordingDoOlShouldReturnAllSymbolsInSharedTape()
    {
        String[] all = machine.symbols();

        assertTrue(machine.ok());
        assertTrue(Arrays.asList(all).containsAll(
            Arrays.asList(red, blue, green, yellow, black, magenta)));
    }

    // ---------- configuration() ----------

    @Test
    public void accordingDoOlShouldReturnCurrentColorPerWheel()
    {
        machine.placeSymbol(1, red);
        machine.placeSymbol(2, green);
        machine.placeSymbol(3, black);

        String[] config = machine.configuration();

        assertEquals(3, config.length);
        assertEquals(red, config[0]);
        assertEquals(green, config[1]);
        assertEquals(black, config[2]);
    }

    // ---------- distinctSymbols() ----------

    @Test
    public void accordingDoOlShouldCountUniqueColorsAcrossMachine()
    {
        int before = machine.distinctSymbols();
        machine.addSymbol(1, "unico" + counter); // color garantizado nuevo

        int after = machine.distinctSymbols();

        assertEquals(before + 1, after);
    }

    // ---------- isJackpot() ----------

    @Test
    public void accordingDoOlShouldAcceptJackpotWhenAllWheelsMatch()
    {
        String jackpotColor = "jackpot" + counter;
        machine.addSymbol(1, jackpotColor);
        machine.addSymbol(2, jackpotColor);
        machine.addSymbol(3, jackpotColor);
        machine.placeSymbol(1, jackpotColor);
        machine.placeSymbol(2, jackpotColor);
        machine.placeSymbol(3, jackpotColor);

        assertTrue(machine.isJackpot());
    }

    // ---------- makeVisible / makeInvisible ----------

    @Test
    public void accordingDoOlShouldToggleVisibilityState()
    {
        assertFalse(machine.isVisibleNow());

        machine.makeVisible();
        assertTrue(machine.isVisibleNow());

        machine.makeInvisible();
        assertFalse(machine.isVisibleNow());
    }

    // ---------- exit() ----------

    @Test
    public void accordingDoOlShouldLeaveMachineInvisibleAfterExit()
    {
        machine.makeVisible();
        machine.exit();

        assertFalse(machine.isVisibleNow());
    }

    @AfterEach
    public void tearDown()
    {
        machine = null;
    }
}