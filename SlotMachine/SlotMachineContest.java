import java.util.*;

/**
 * Resuelve el problema de la maraton
 */
public class SlotMachineContest {

    // Resuelve de forma invisible 
    public int[][] solve(int n) {
        SlotMachine machine = new SlotMachine(n);
        List<int[]> actions = new ArrayList<>();
        run(machine, n, actions);
        return actions.toArray(new int[0][]);
    }

    // Corre el mismo algoritmo, pero visible, limitado por los parametros del color. 
    public void simulate(int n) {
        if (n > 6) {
            System.out.println("Error: La simulación soporta un máximo de 7 ruedas.");
            return;
        }
        SlotMachine machine = new SlotMachine(n);
        machine.makeVisible();
        List<int[]> actions = new ArrayList<>();
        run(machine, n, actions);
    }

    // aqui arranca
    private void run(SlotMachine machine, int n, List<int[]> actions) {
        if (machine.distinctSymbols() == 1) return; //resuleto

        List<Integer> group = new ArrayList<>();
        group.add(1); // la rueda 1 sera primer centinela

        List<Integer> pending = new ArrayList<>();
        for (int i = 2; i <= n; i++) pending.add(i);

        growGroupToTwo(machine, n, group, pending, actions);

        while (!pending.isEmpty()) {
            int wheel = pending.remove(0);
            alignWheelToGroup(machine, wheel, group, n, actions);
            group.add(wheel);
        }
    }

    // Encontrar la  pareja de centinelas
    private void growGroupToTwo(SlotMachine machine, int n, List<Integer> group,
                                 List<Integer> pending, List<int[]> actions) {
        int wheelA = pending.get(0);
        int wheelB = pending.get(1);

        List<Integer> candA = candidatesFromReadings(sweepWheel(machine, wheelA, n, actions));
        List<Integer> candB = candidatesFromReadings(sweepWheel(machine, wheelB, n, actions));

        int bestA = candA.get(0), bestB = candB.get(0), bestK = Integer.MAX_VALUE;
        int atA = 0, atB = 0;

        for (int ta : candA) {
            spinAndLog(machine, wheelA, ta - atA, actions);
            atA = ta;
            for (int tb : candB) {
                spinAndLog(machine, wheelB, tb - atB, actions);
                atB = tb;
                int k = machine.distinctSymbols();
                if (k < bestK) { bestK = k; bestA = ta; bestB = tb; }
            }
        }
        spinAndLog(machine, wheelA, bestA - atA, actions);
        spinAndLog(machine, wheelB, bestB - atB, actions);

        group.add(wheelA);
        group.add(wheelB);
        pending.remove(Integer.valueOf(wheelA));
        pending.remove(Integer.valueOf(wheelB));
    }

    // Alinear una rueda más contra el grupo de centinelas
    private void alignWheelToGroup(SlotMachine machine, int wheel, List<Integer> group,
                                    int n, List<int[]> actions) {
        int repA = group.get(0);
        int repB = group.get(1);
        int parkDelta = 0;

        if (group.size() > 2) {
            parkDelta = parkRestOfGroup(machine, group, actions, n);
        }

        int[] readings = sweepWheel(machine, wheel, n, actions);
        List<Integer> candidates = candidatesFromReadings(readings);
        int at = 0;

        for (int t : candidates) {
            spinAndLog(machine, wheel, t - at, actions);
            at = t;

            int before = machine.distinctSymbols();
            spinAndLog(machine, repA, 1, actions);
            spinAndLog(machine, repB, 1, actions);
            int after = machine.distinctSymbols();
            spinAndLog(machine, repA, -1, actions);
            spinAndLog(machine, repB, -1, actions);

            if (after == before + 1) break; // confirmado
        }

        if (parkDelta != 0) {
            for (int i = 2; i < group.size(); i++) {
                spinAndLog(machine, group.get(i), -parkDelta, actions);
            }
        }
    }

    /**
     * Aleja a todo el grupo excepto los 2 centinelas, para que el
     * detector de repA/repB sea consistente y va probando varios
     * desplazamientos hasta confirmar  que no
     * chocaron por casualidad con otra rueda.
     */
    private int parkRestOfGroup(SlotMachine machine, List<Integer> group, List<int[]> actions,int n) {
        for (int delta = 2; delta < n; delta++) {
            int before = machine.distinctSymbols();
            for (int i = 2; i < group.size(); i++) {
                spinAndLog(machine, group.get(i), delta, actions);
            }
            int after = machine.distinctSymbols();
            if (after == before + 1) return delta; // estacionamiento limpio

            for (int i = 2; i < group.size(); i++) { // chocó: deshacer y probar otro delta
                spinAndLog(machine, group.get(i), -delta, actions);
            }
        }
        return 0;
    }

    // Utilidades compartidas
    private int[] sweepWheel(SlotMachine machine, int wheel, int n, List<int[]> actions) {
        int[] readings = new int[n];
        readings[0] = machine.distinctSymbols();
        for (int t = 1; t < n; t++) {
            spinAndLog(machine, wheel, 1, actions);
            readings[t] = machine.distinctSymbols();
        }
        spinAndLog(machine, wheel, 1, actions); // cierra el ciclo completo
        return readings;
    }

    private List<Integer> candidatesFromReadings(int[] readings) {
        int min = Integer.MAX_VALUE;
        for (int r : readings) min = Math.min(min, r);
        List<Integer> candidates = new ArrayList<>();
        for (int t = 0; t < readings.length; t++) {
            if (readings[t] == min) candidates.add(t);
        }
        return candidates;
    }

    private void spinAndLog(SlotMachine machine, int wheel, int steps, List<int[]> actions) {
        if (steps == 0) return; // un giro de 0 no gasta acción
        machine.spin(wheel, steps);
        actions.add(new int[]{wheel, steps});
    }
}