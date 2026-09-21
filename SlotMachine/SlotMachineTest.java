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

    @Test
    public void solverShouldCompleteWithinActionLimit() {
        // La maratón exige un máximo de 10,000 acciones
        int[][] actions = contest.solve(5);
        
        assertNotNull("La lista de acciones no debe ser nula", actions);
        assertTrue("El algoritmo excedió el límite de 10,000 turnos del juez", actions.length <= 10000);
    }

    @Test
    public void solverShouldScaleEfficientlyWithMaximumWheels() {
        // Prueba de estrés con el límite máximo de la competencia (N = 50)
        int[][] actions = contest.solve(50);
        
        // Para N=50, un algoritmo O(N^2) limpio debería resolverlo en ~2000-4000 acciones
        assertTrue("El escalado asintótico falló para N=50", actions.length <= 10000);
    }
}