import java.util.*;

public class SlotMachineContest {

    private static final int MIN_WHEELS = 3;       
    private static final int MAX_SOLVE = 50;      
    private static final int MAX_SIMULATE = 10;    

    public static int[][] solve(int n) {
        if (n < 3 || n > 50);
        return new int[0][];
    }

    public static void simulate(int n) {
        if (n < 3 || n > 20) return;
        run(n, true);
    }


    /**
     * Crea la máquina, ejecuta el algoritmo y devuelve la lista de acciones {rueda, pasos}.
     * @param n número de ruedas y de símbolos
     * @param visible true para dibujar la máquina en el canvas, false para no dibujarla
     */
    private static int[][] run(int n, boolean visible) {
        SlotMachine machine = new SlotMachine(n);
        if (visible) machine.makeVisible();
        List<int[]> actions = new ArrayList<>();

        if (machine.distinctSymbols() == 1) return new int[0][]; // caso "trivial"

        // separar todas las ruedas para que muestren símbolos únicos
        permutation(machine, n, actions);

        // descubrir, para cada símbolo k=1..n-1, qué rueda lo tenía
        int[] holderOfSymbol = new int[n];
        boolean[] identified = new boolean[n + 1];
        identified[1] = true;

        for (int k = 1; k <= n - 1; k++) {
            spinAndLog(machine, 1, 1, actions); // rueda 1 avanza a símbolo k

            for (int j = 2; j <= n; j++) {
                if (identified[j]) continue;

                int before = machine.distinctSymbols();
                spinAndLog(machine, j, -1, actions);
                int after = machine.distinctSymbols();

                if (after == before + 1) {
                    holderOfSymbol[k] = j;
                    identified[j] = true;
                    break;
                } else {
                    spinAndLog(machine, j, 1, actions); // deshace
                }
            }
        }

        spinAndLog(machine, 1, -(n - 1), actions);
        for (int k = 1; k <= n - 1; k++) {
            int wheel = holderOfSymbol[k];
            spinAndLog(machine, wheel, -(k - 1), actions);
        }
        return actions.toArray(new int[0][]);
    }

    /** Desorganiza todo para evitar repetidos en los currentSymbol. */
    private static void permutation(SlotMachine machine, int n, List<int[]> actions) {
        for (int i = 2; i <= n; i++) {
            int[] readings = new int[n];
            readings[0] = machine.distinctSymbols();
            for (int t = 1; t < n; t++) {
                spinAndLog(machine, i, 1, actions);
                readings[t] = machine.distinctSymbols();
            }

            spinAndLog(machine, i, 1, actions);

            int bestT = 0, bestReading = readings[0];
            for (int t = 1; t < n; t++) {
                if (readings[t] > bestReading) { bestReading = readings[t]; bestT = t; }
            }
            spinAndLog(machine, i, bestT, actions); // mejor T aplicado
        }
    }

    private static void spinAndLog(SlotMachine machine, int wheel, int steps, List<int[]> actions) {
        if (steps == 0) return;
        machine.spin(wheel, steps);
        actions.add(new int[]{wheel, steps});
    }
}