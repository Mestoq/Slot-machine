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
    public void accordingDoOlshouldNotRunAndReturnEmptyWhenLessThanThreeWheels() {
        SlotMachineContest contest = new SlotMachineContest();
        
        // Intentamos resolver con 2 ruedas (caso inválido/menor al mínimo)
        int[][] actions = contest.solve(2);
        
        assertNotNull("El resultado no debe ser nulo", actions);
        assertEquals("El algoritmo no debió correr, por lo que las acciones deben ser 0", 0, actions.length);
    }

    @Test
    public void accordingDoOlsolverShouldScaleEfficientlyWithMaximumWheels() {
        // Prueba de estrés con el límite máximo de la competencia (N = 50)
        int[][] actions = contest.solve(50);
        
        // Para N=50, un algoritmo O(N^2) limpio debería resolverlo en ~2000-4000 acciones
        assertTrue("El escalado asintótico falló para N=50", actions.length <= 10000);
    }
}