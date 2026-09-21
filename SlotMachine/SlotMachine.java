import java.util.*;
import javax.swing.*;
import java.awt.Dimension;

public class SlotMachine {
    private List<Wheel> wheels;
    private boolean isVisible;
    private Canvas canvas;
    public boolean result;

    private Rectangle mainContainer;
    private int xPosition;
    public static int yPosition;
    private static int width = 500;
    private static final int HEIGHT = 200;
    private static final String[] AllowedColors = {
        "red", "blue", "yellow", "green", "magenta", "white"
    };

    public SlotMachine() {
        this.wheels = new ArrayList<>();
        this.isVisible = false;
        this.canvas = null;
        this.mainContainer = null;
        this.xPosition = 100;
        this.yPosition = 100;
        this.result = false;
    }
    
    public SlotMachine(int n) {
        this.wheels = new ArrayList<>();
        this.isVisible = false;
        this.canvas = null;
        this.mainContainer = null;
        this.xPosition = 100;
        this.yPosition = 100;
        this.result = false;

        
        Wheel.clearSharedTape();

        String[] sharedColors = new String[n];
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < n; i++) {
            String baseColor = AllowedColors[i % AllowedColors.length];
            if (n <= 6) {
                sharedColors[i] = baseColor;
            } else {
                sharedColors[i] = baseColor + "-" + i;
            }
        }

        // Instanciar las ruedas
        for (int i = 1; i <= n; i++) {
            Wheel currentWheel = new Wheel(i);
            
            if (i == 1) {
                for (int j = 0; j < n; j++) {
                    currentWheel.addSymbol(new Symbol(sharedColors[j], i));
                }
            }
            
            wheels.add(currentWheel);
        }

        // Desorganizar las ruedas aleatoriamente 
        boolean allAligned = true;
        for (int i = 0; i < n; i++) {
            int randomSteps = random.nextInt(n);
            wheels.get(i).spin(randomSteps);
            
            String firstWheelColor = wheels.get(0).getCurrentSymbol().getColor();
            String currentWheelColor = wheels.get(i).getCurrentSymbol().getColor();
            
            if (i > 0 && !currentWheelColor.equals(firstWheelColor)) {
                allAligned = false;
            }
        }

        if (allAligned && n > 0) {
            wheels.get(0).spin(1);
        }
    }
    
    public boolean ok() {
        return result;
    }

    public void addWheel(int wheelNumber) {
        for (Wheel w : wheels) {
            if (w.getWheelNumber() == wheelNumber) {
                showErrorMessage("Wheel " + wheelNumber + " already exists");
                result = false;
                return;
            }
        }
        Wheel wheel = new Wheel(wheelNumber);
        wheels.add(wheel);

        if (isVisible) {
            mainContainer.changeSize(HEIGHT, width + 90);
            draw();
            wheel.makeVisible();
        }
        result = true;
    }

    public void delWheel(int wheelNumber) {
        for (int i = 0; i < wheels.size(); i++) {
            if (wheels.get(i).getWheelNumber() == wheelNumber) {
                Wheel wheel = wheels.get(i);
                if (isVisible) wheel.makeInvisible();
                wheels.remove(i);
                for (int j = 0; j < wheels.size(); j++) {
                    wheels.get(j).setWheelIndex(j + 1);
                }
                if (isVisible) {
                    visualSwapWheels();
                    mainContainer.changeSize(HEIGHT, width - 50);
                    draw();
                }
                result = true;
                return;
            }
        }
        showErrorMessage("Wheel " + wheelNumber + " not found");
        result = false;
    }

    private void visualSwapWheels() {
        for (Wheel wheel : wheels) {
            int targetX = 120 + ((wheel.getWheelNumber() - 1) * 110);
            wheel.setPosition(targetX, 120);
        }
    }

    public void addSymbol(int wheelNumber, String color) {
        for (Wheel w : wheels) {
            if (w.getWheelNumber() == wheelNumber) {
                if (w.hasColor(color)) {
                    showErrorMessage("Wheel " + wheelNumber + " already has a " + color + " symbol");
                    result = false;
                    return;
                }
                result = w.addSymbol(new Symbol(color, wheelNumber));
                return;
            }
        }
        showErrorMessage("Wheel " + wheelNumber + " not found");
        result = false;
    }

    /**
     * Elimina de la cinta compartida el primer símbolo que tenga este color.
     * (Ajustado a la firma del diagrama: delSymbol(symbol : String) : void)
     */
    public void delSymbol(String color) {
        for (Wheel w : wheels) {
            for (Symbol s : w.getSymbols()) {
                if (s.getColor().equals(color)) {
                    result = w.removeSymbol(s);
                    return;
                }
            }
        }
        showErrorMessage("Symbol " + color + " not found");
        result = false;
    }

    public void placeSymbol(int wheelNumber, String color) {
        for (Wheel w : wheels) {
            if (w.getWheelNumber() == wheelNumber) {
                boolean placed = w.placeSymbol(color);
                if (!placed)
                    showErrorMessage("Wheel " + wheelNumber + " has no " + color + " symbol");
                result = placed;
                return;
            }
        }
        showErrorMessage("Wheel " + wheelNumber + " not found");
        result = false;
    }

    public void spin() {
        for (Wheel wheel : wheels) {
            if (!wheel.isHeld() && wheel.getSymbolCount() == 0) {
                showErrorMessage("Cannot spin: wheel " + wheel.getWheelNumber() +
                        " has no symbols");
                result = false;
                return;
            }
        }
        for (Wheel wheel : wheels) {
            if (!wheel.isHeld()) {
                wheel.spin();
            }
        }
        if (isVisible) refreshActiveWheels();
        result = true;
    }

    public void spin(int wheel) {
        for (Wheel w : wheels) {
            if (w.getWheelNumber() == wheel) {
                if (w.getSymbolCount() == 0) {
                    showErrorMessage("Cannot spin: wheel " + wheel + " has no symbols");
                    result = false;
                    return;
                }
                w.spin();
                if (isVisible) refreshActiveWheels();
                result = true;
                return;
            }
        }
        showErrorMessage("Wheel " + wheel + " not found");
        result = false;
    }

    public void swap(int wheelNumber1, int wheelNumber2) {
        Wheel w1 = null, w2 = null;
        for (Wheel w : wheels) {
            if (w.getWheelNumber() == wheelNumber1) w1 = w;
            if (w.getWheelNumber() == wheelNumber2) w2 = w;
        }
        if (w1 == null || w2 == null) {
            showErrorMessage("One or both wheels not found");
            result = false;
            return;
        }
        w1.swapContentWith(w2);
        result = true;
    }

    public void lock(int wheel) {
        for (Wheel w : wheels) {
            if (w.getWheelNumber() == wheel) {
                w.hold();
                result = true;
                return;
            }
        }
        showErrorMessage("Wheel " + wheel + " not found");
        result = false;
    }

    /**
     * Renombrado de unLock a unlock para ajustarse al diagrama
     * (unlock(wheel : int) : void).
     */
    public void unlock(int wheel) {
        for (Wheel w : wheels) {
            if (w.getWheelNumber() == wheel) {
                w.release();
                result = true;
                return;
            }
        }
        showErrorMessage("Wheel " + wheel + " not found");
        result = false;
    }

    public void spin(int wheel, int steps) {
        for (Wheel w : wheels) {
            if (w.getWheelNumber() == wheel) {
                if (w.getSymbolCount() == 0) {
                    showErrorMessage("Cannot rotate: wheel " + wheel + " has no symbols");
                    result = false;
                    return;
                }
                w.spin(steps);
                if (isVisible) refreshActiveWheels();
                result = true;
                return;
            }
        }
        showErrorMessage("Wheel " + wheel + " not found");
        result = false;
    }

    /**
     * Deja la máquina en la configuración dada (todo o nada).
     * Firma ajustada al diagrama: spin(setSymbols : String[]) : void.
     */
    public void spin(String[] setSymbols) {
        if (setSymbols.length != wheels.size()) {
            showErrorMessage("Configuration size does not match wheel count");
            result = false;
            return;
        }

        List<Wheel> sorted = new ArrayList<>(wheels);
        sorted.sort((a, b) -> a.getWheelNumber() - b.getWheelNumber());

        for (int i = 0; i < setSymbols.length; i++) {
            if (!sorted.get(i).hasColor(setSymbols[i].trim())) {
                showErrorMessage("Wheel " + sorted.get(i).getWheelNumber() +
                        " has no " + setSymbols[i].trim() + " symbol");
                result = false;
                return;
            }
        }

        for (int i = 0; i < setSymbols.length; i++) {
            sorted.get(i).placeSymbol(setSymbols[i].trim());
        }
        if (isVisible) refreshActiveWheels();
        result = true;
    }

    public String consultSymbols() {
        StringBuilder sb = new StringBuilder();
        sb.append("Slot Machine Status:\n");
        for (Wheel wheel : wheels) {
            sb.append(wheel.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Devuelve todos los símbolos de la cinta compartida, en el orden
     * en el que fueron agregados (sin distinción por rueda, ya que la
     * cinta es una sola). Firma ajustada al diagrama: symbols() : String[].
     */
    public String[] symbols() {
        if (wheels.isEmpty()) {
            result = false;
            return new String[0];
        }
        List<Symbol> list = wheels.get(0).getSymbols(); // cualquier rueda ve la misma cinta
        String[] colors = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            colors[i] = list.get(i).getColor();
        }
        result = true;
        return colors;
    }

    /**
     * Colores de los símbolos visibles en todas las ruedas, ordenados
     * de izquierda a derecha (por número de rueda). Mismo orden que
     * antes; solo cambia el tipo de retorno a String[].
     */
    public String[] configuration() {
        List<Wheel> sorted = new ArrayList<>(wheels);
        sorted.sort((a, b) -> a.getWheelNumber() - b.getWheelNumber());
        String[] colors = new String[sorted.size()];
        for (int i = 0; i < sorted.size(); i++) {
            Symbol current = sorted.get(i).getCurrentSymbol();
            colors[i] = current != null ? current.getColor() : "empty";
        }
        result = true;
        return colors;
    }

    /**
     * Cuenta cuántos símbolos DISTINTOS se están mostrando actualmente,
     * leyendo directamente el currentSymbol de cada rueda (no la cinta
     * completa, y sin pasar por configuration()).
     * Esta es la señal "k" que reporta el amigo en el problema de la maratón.
     * Precondición del contexto de uso: SlotMachine(n) garantiza que toda
     * rueda siempre tiene un currentSymbol asignado (invariante 1 rueda : 1 símbolo).
     */
    public int distinctSymbols() {
        Set<String> visible = new HashSet<>();
        for (Wheel w : wheels) {
            Symbol current = w.getCurrentSymbol();
            if (current != null) {          // guarda defensiva, no debería dispararse nunca en este contexto
                visible.add(current.getColor());
            }
        }
        result = true;
        return visible.size();
    }

    public boolean isJackpot() {
        if (wheels.isEmpty()) return false;
        Symbol first = wheels.get(0).getCurrentSymbol();
        if (first == null) return false;
        for (Wheel wheel : wheels) {
            Symbol current = wheel.getCurrentSymbol();
            if (current == null || !current.getColor().equals(first.getColor())) {
                return false;
            }
        }
        return true;
    }

    public void makeVisible() {
        if (isVisible) {
            result = false;
            return;
        }
        isVisible = true;
        canvas = Canvas.getCanvas();
        draw();
        result = true;
    }

    public void makeInvisible() {
        if (!isVisible) {
            result = false;
            return;
        }
        isVisible = false;
        erase();
        result = true;
    }

    public boolean isVisibleNow() {
        return isVisible;
    }

    public int getWheelCount() {
        return wheels.size();
    }

    public Wheel getWheel(int index) {
        if (index < 0 || index >= wheels.size()) return null;
        return wheels.get(index);
    }

    public void exit() {
        makeInvisible();
        System.out.println("Slot Machine Simulator terminated.");
    }

    private void draw() {
        if (isVisible) {
            if (mainContainer == null) {
                mainContainer = new Rectangle();
                mainContainer.changeSize(HEIGHT, width);
                mainContainer.moveHorizontal(xPosition - 70);
                mainContainer.moveVertical(yPosition - 15);
            }
            mainContainer.changeColor(isJackpot() ? "red" : "blue");
            mainContainer.makeVisible();

            for (Wheel wheel : wheels) {
                wheel.makeVisible();
            }
        }
    }

    private void refreshActiveWheels() {
        updateJackpotIndicator();
        for (Wheel wheel : wheels) {
            if (!wheel.isHeld()) wheel.makeVisible();
        }
    }

    private void updateJackpotIndicator() {
        if (mainContainer != null) {
            mainContainer.changeColor(isJackpot() ? "red" : "blue");
        }
    }

    private void erase() {
        for (Wheel wheel : wheels) {
            wheel.makeInvisible();
        }
        if (mainContainer != null) {
            mainContainer.makeInvisible();
        }
    }

    private void showErrorMessage(String message) {
        if (isVisible) {
            JOptionPane.showMessageDialog(null, message, "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public String toString() {
        return "SlotMachine with " + wheels.size() + " wheels " +
               (isVisible ? "[VISIBLE]" : "[INVISIBLE]");
    }
}