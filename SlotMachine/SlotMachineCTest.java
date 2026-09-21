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
    public void shouldSupportMinimumOfThreeWheels() {
        // Creamos una máquina con el límite mínimo de 3 ruedas
        SlotMachine machine = new SlotMachine(3);
        SlotMachineContest contest = new SlotMachineContest();
        
        // Verificamos que la máquina no inicia resuelta y que el solver puede procesarla
        int initialK = machine.distinctSymbols();
        assertTrue("Una máquina de 3 ruedas debe iniciar desordenada (k > 1)", initialK > 1);
        
        int[][] actions = contest.solve(3);
        assertNotNull("El solver debe devolver un arreglo de acciones válido para N=3", actions);
    }

    @Test
    public void solverShouldScaleEfficientlyWithMaximumWheels() {
        // Prueba de estrés con el límite máximo de la competencia (N = 50)
        int[][] actions = contest.solve(50);
        
        // Para N=50, un algoritmo O(N^2) limpio debería resolverlo en ~2000-4000 acciones
        assertTrue("El escalado asintótico falló para N=50", actions.length <= 10000);
    }
}