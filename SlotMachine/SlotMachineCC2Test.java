import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
/**
 * Pruebas compartidas del Ciclo 2 para SlotMachine.
 */
public class SlotMachineCC2Test
{
    /**
     * Default constructor for test class SlotMachineCC2Test
     */
    public SlotMachineCC2Test()
    {
    }

    /**
     * una rueda fija con holdWheel no debe cambiar aunque se gire toda la maquina
     */
    @Test
public void accordingDrRmShouldKeepSymbolFixedWhenWheelIsHeld()
    {
        SlotMachine machine = new SlotMachine();
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
        machine.addSymbol(2, "green");
        machine.addSymbol(3, "black");
        machine.placeSymbol(1, "red");
        machine.holdWheel(1);
        machine.spin();
        assertEquals("red", machine.getWheel(0).getCurrentSymbol().getColor());
    }

    /**
     * la maquina debe quedar exactamente en la configuracion pedida con setConfiguration
     */
    @Test
    public void accordingDrRmShouldMatchConfigurationWhenAllColorsAreValid()
    {
        SlotMachine machine = new SlotMachine();
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
        machine.addSymbol(2, "green");
        machine.addSymbol(2, "yellow");
        machine.addSymbol(3, "black");
        machine.setConfiguration("blue,yellow,black");
        assertEquals("blue,yellow,black", machine.configuration());
    }
}
