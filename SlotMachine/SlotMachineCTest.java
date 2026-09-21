import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SlotMachineCTest {

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