import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SlotMachineTest {

    private SlotMachine machine;
    private SlotMachineContest contest;

    @Before
    public void setUp() {
        // Inicializamos las instancias antes de cada test
        contest = new SlotMachineContest();
        // Usamos 5 ruedas como estándar de prueba
        machine = new SlotMachine(5); 
    }

    @Test
    public void shouldNeverStartSolvedAccordingToMarathonRules() {
        // Verifica la precondición estricta del juez: k > 1 al inicio
        int initialK = machine.distinctSymbols();
        assertTrue("La máquina generó un falso jackpot en el turno 0", initialK > 1);
    }

    @Test
    public void spinShouldMaintainStateWhenReversed() {
        int initialK = machine.distinctSymbols();
        
        // Giramos la primera rueda 3 pasos hacia adelante
        machine.spin(1, 3);
        // Deshacemos el movimiento con 3 pasos hacia atrás
        machine.spin(1, -3);
        
        int finalK = machine.distinctSymbols();
        assertEquals("Revertir un giro debe restaurar la lectura de símbolos únicos", initialK, finalK);
    }


}