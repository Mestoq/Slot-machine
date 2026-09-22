import java.util.*;

public class SlotMachineContest {

    public int[][] solve(int n) {
        if (n < 3|| n < 50) {
            System.out.println("Error: La simulación no debe correrse con menos de 3 ruedas o mas de 50.");
            return new int[0][];
        }
        SlotMachine machine = new SlotMachine(n);
        List<int[]> actions = new ArrayList<>();
        run(machine, n, actions);
        return actions.toArray(new int[0][]);
    }

    public void simulate(int n) {
        if (n > 6 || n < 3) {
            System.out.println("Error: La simulación soporta un máximo de 7 ruedas.");
            return;
        }
        SlotMachine machine = new SlotMachine(n);
        machine.makeVisible();
        List<int[]> actions = new ArrayList<>();
        run(machine, n, actions);
    }

    private void run(SlotMachine machine, int n, List<int[]> actions) {
        if (machine.distinctSymbols() == 1) return; // caso "trivial"

        // separar todas las ruedas para que muestren símbolos únicos
        permutation(machine, n, actions);

        // descubrir, para cada símbolo k=1..n-1, qué rueda lo tenía
        int[] holderOfSymbol = new int[n];
        boolean[] identified = new boolean[n + 1];
        identified[1] = true; // la rueda 1 es su propio punto de partida (k=0)

        for (int k = 1; k <= n - 1; k++) {
            spinAndLog(machine, 1, 1, actions); // rueda 1 avanza a símbolo k

            for (int j = 2; j <= n; j++) {
                if (identified[j]) continue; // ya resuelta antes, no se vuelve a tocar

                int before = machine.distinctSymbols();
                spinAndLog(machine, j, -1, actions); // única casilla vacía posible
                int after = machine.distinctSymbols();

                if (after == before + 1) {
                    holderOfSymbol[k] = j; // confirmado, sin ambigüedad
                    identified[j] = true;
                    break;
                } else {
                    spinAndLog(machine, j, 1, actions); // deshacer, no era esta
                }
            }
        }

        // FASE 3: alineación final — todos vuelven al símbolo original de la rueda 1
        spinAndLog(machine, 1, -(n - 1), actions);
        for (int k = 1; k <= n - 1; k++) {
            int wheel = holderOfSymbol[k];
            // ya quedó en (k-1) tras la Fase 2; falta retroceder (k-1) pasos más
            spinAndLog(machine, wheel, -(k - 1), actions);
        }
    }

    /**
     * Desorganizamos todo y evitar que hallan repetidos en los currentsymbol
     */
    private void permutation(SlotMachine machine, int n, List<int[]> actions) {
        for (int i = 2; i <= n; i++) {
            int[] readings = new int[n];
            readings[0] = machine.distinctSymbols();
            for (int t = 1; t < n; t++) {
                spinAndLog(machine, i, 1, actions);
                readings[t] = machine.distinctSymbols();
            }
            // volver a la posición original antes de fijar la mejor
            spinAndLog(machine, i, 1, actions); // cierra el ciclo (vuelve a t=0)

            int bestT = 0, bestReading = readings[0];
            for (int t = 1; t < n; t++) {
                if (readings[t] > bestReading) { bestReading = readings[t]; bestT = t; }
            }
            spinAndLog(machine, i, bestT, actions); // se queda en la mejor posición
        }
    }

    private void spinAndLog(SlotMachine machine, int wheel, int steps, List<int[]> actions) {
        if (steps == 0) return;
        machine.spin(wheel, steps);
        actions.add(new int[]{wheel, steps});
    }
}