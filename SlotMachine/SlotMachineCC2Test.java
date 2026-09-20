import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SlotMachineCC2Test
{
    public SlotMachineCC2Test()
    {
    }

    /**
     * Al fijar una rueda con lock() y luego girar toda la máquina,
     * el símbolo de esa rueda no debe cambiar.
     */
    @Test
    public void accordingDoOlShouldKeepSymbolFixedWhenWheelIsLocked()
    {
        SlotMachine machine = new SlotMachine();
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
        machine.addSymbol(2, "green");
        machine.addSymbol(3, "black");

        machine.placeSymbol(1, "red");
        machine.lock(1);
        machine.spin();

        assertEquals("red", machine.getWheel(0).getCurrentSymbol().getColor());
    }

    /**
     * La máquina debe quedar exactamente en la configuración pedida
     * con spin(String[]).
     */
    @Test
    public void accordingDoOlShouldMatchConfigurationWhenAllColorsAreValid()
    {
        SlotMachine machine = new SlotMachine();
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
        machine.addSymbol(2, "green");
        machine.addSymbol(2, "yellow");
        machine.addSymbol(3, "black");

        machine.spin(new String[]{"blue", "yellow", "black"});

        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"blue", "yellow", "black"}, machine.configuration());
    }
}