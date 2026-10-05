import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Pruebas propias del ciclo 4: rueda cansada y tipos de símbolo.
 */
public class SlotMachineC4Test {

    @Test
    public void tiredWheelMovesHalfTheSteps() {
        SlotMachine m = new SlotMachine(3);
        m.addWheel("tired", 4);

        String[] tape = m.symbols();
        m.placeSymbol(4, tape[0]);   // la rueda 4 empieza en la posición 0 de la cinta

        m.spin(4, 4);                // pide 4 pasos, avanza 4 / 2 = 2

        assertEquals(tape[2], m.configuration()[3]);
    }

    @Test
    public void symbolTypesAreAcceptedAndUnknownIsRejected() {
        SlotMachine m = new SlotMachine(3);

        m.addSymbol("shy", 1, "black");
        assertTrue(m.ok());

        m.addSymbol("ephemeral", 1, "orange");
        assertTrue(m.ok());

        m.addSymbol("ghost", 1, "purple");
        assertFalse(m.ok());         // tipo que no existe

        assertEquals(5, m.symbols().length);   // 3 originales + shy + ephemeral
    }
}