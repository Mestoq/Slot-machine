import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SlotMachineCC4Test {

    @Test
    public void accordingToDoOLrebelWheelRejectsLockDeleteAndSwap() {
        SlotMachine m = new SlotMachine(3);
        m.addWheel("rebel", 4);
        assertTrue(m.ok());         

        m.lock(4);
        assertFalse(m.ok());         

        m.delWheel(4);
        assertFalse(m.ok());      

        m.swap(1, 4);
        assertFalse(m.ok());        
    }

    @Test
    public void accordingToDoOLleftyWheelCopiesItsLeftNeighbour() {
        SlotMachine m = new SlotMachine(3);
        m.addWheel("lefty", 4);

        m.spin();                 

        String[] shown = m.configuration();
        assertEquals(shown[2], shown[3]);
    }
}