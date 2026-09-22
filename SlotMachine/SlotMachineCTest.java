import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SlotMachineCTest {

    private SlotMachine machine;
    private SlotMachineContest contest = new SlotMachineContest();


    @Test
    public void accordingDoOlshouldNotRunAndReturnEmptyWhenLessThanThreeWheels() {
        SlotMachineContest contest = new SlotMachineContest();
        
        // Intentamos resolver con 2 ruedas que no cumple
        int[][] actions = contest.solve(2);
        
        assertNotNull("El resultado no debe ser nulo", actions);
        assertEquals("El algoritmo no debió correr, por lo que las acciones deben ser 0", 0, actions.length);
    }

    @Test
    public void solveShouldScaleEfficientlyWithMaximumWheels() {
        // Prueba de estrés con el límite de la competencia (N = 50)
        int[][] actions = contest.solve(50);

        // Para N=50, un algoritmo O(N^2) debe quedar muy por debajo del límite
        assertTrue("El escalado asintótico falló para N=50: " + actions.length + " acciones",
                actions.length <= 10000);
    }
}