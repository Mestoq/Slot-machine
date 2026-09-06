import java.util.*;
import javax.swing.*;
import java.awt.Dimension;

public class SlotMachine {
    private List<Wheel> wheels;
    private boolean isVisible;
    private Canvas canvas;

    private Rectangle mainContainer;
    private int xPosition;
    public static int yPosition;
    private static int width = 500;
    private static final int HEIGHT = 200;

    private boolean lastOperationOk = true;

    public SlotMachine() {
        this.wheels = new ArrayList<>();
        this.isVisible = false;
        this.canvas = null;
        this.mainContainer = null;
        this.xPosition = 100;
        this.yPosition = 100;

        wheels.add(new Wheel(1));
        wheels.add(new Wheel(2));
        wheels.add(new Wheel(3));
    }

    /*
     * Guarda si la última operación salió bien o mal, y devuelve ese
     * mismo valor (para poder usarlo directo en un return).
     */
    private boolean result(boolean success) {
        lastOperationOk = success;
        return success;
    }

    /**
     * Dice si la última operación que se hizo salió bien o no.
     */
    public boolean ok() {
        return lastOperationOk;
    }

    public boolean addWheel(int wheelNumber) {
        for (Wheel w : wheels) {
            if (w.getWheelNumber() == wheelNumber) {
                showErrorMessage("Wheel " + wheelNumber + " already exists");
                return result(false);
            }
        }
        Wheel wheel = new Wheel(wheelNumber);
        wheels.add(wheel);

        if (isVisible) {
            mainContainer.changeSize(HEIGHT, width + 90);
            canvas.setCanvasSize(width + 30, HEIGHT);
            draw();
            wheel.makeVisible();
        }
        return result(true);
    }

    public boolean removeWheel(int wheelNumber) {
        for (int i = 0; i < wheels.size(); i++) {
            if (wheels.get(i).getWheelNumber() == wheelNumber) {
                Wheel wheel = wheels.get(i);
                if (isVisible) {
                    wheel.makeInvisible();
                }
                wheels.remove(i);
                for (int j = 0; j < wheels.size(); j++) {
                    wheels.get(j).setWheelIndex(j + 1);
                }
                if (isVisible) {
                    repositionWheels();
                    mainContainer.changeSize(HEIGHT, width - 50);
                    canvas.setCanvasSize(width - 30, HEIGHT);
                    draw();
                }
                return result(true);
            }
        }
        showErrorMessage("Wheel " + wheelNumber + " not found");
        return result(false);
    }

    /*
     * Reacomoda visualmente las ruedas según su número actual,
     * sin tocar la numeración lógica (eso ya lo hace setWheelIndex).
     */
    private void repositionWheels() {
        for (Wheel wheel : wheels) {
            int targetX = 120 + ((wheel.getWheelNumber() - 1) * 110);
            wheel.setPosition(targetX, 120);
        }
    }

    public boolean addSymbol(int wheelNumber, String color) {
        for (Wheel w : wheels) {
            if (w.getWheelNumber() == wheelNumber) {
                if (w.hasColor(color)) {
                    showErrorMessage("Wheel " + wheelNumber + " already has a " + color + " symbol");
                    return result(false);
                }
                return result(w.addSymbol(new Symbol(color, wheelNumber)));
            }
        }
        showErrorMessage("Wheel " + wheelNumber + " not found");
        return result(false);
    }

    public boolean removeSymbol(Symbol symbol) {
        for (Wheel w : wheels) {
            if (w.getWheelNumber() == symbol.getWheelIndex()) {
                return result(w.removeSymbol(symbol));
            }
        }
        return result(false);
    }

    public boolean placeSymbol(int wheelNumber, String color) {
        for (Wheel w : wheels) {
            if (w.getWheelNumber() == wheelNumber) {
                boolean placed = w.placeSymbol(color);
                if (!placed) showErrorMessage("Wheel " + wheelNumber + " has no " + color + " symbol");
                return result(placed);
            }
        }
        showErrorMessage("Wheel " + wheelNumber + " not found");
        return result(false);
    }

    public boolean spin() {
        for (Wheel wheel : wheels) {
            if (wheel.getSymbolCount() == 0) {
                showErrorMessage("Cannot spin: wheel " + wheel.getWheelNumber() +
                        " has no symbols");
                return result(false);
            }
        }
        for (Wheel wheel : wheels) {
            wheel.spin();
        }
        if (isVisible) {
            updateJackpotIndicator();
            for (Wheel wheel : wheels) {
                wheel.makeVisible();
            }
        }
        return result(true);
    }

    public boolean spin(int wheelNumber) {
        for (Wheel w : wheels) {
            if (w.getWheelNumber() == wheelNumber) {
                if (w.getSymbolCount() == 0) {
                    showErrorMessage("Cannot spin: wheel " + wheelNumber + " has no symbols");
                    return result(false);
                }
                w.spin();
                if (isVisible) updateJackpotIndicator();
                return result(true);
            }
        }
        showErrorMessage("Wheel " + wheelNumber + " not found");
        return result(false);
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
     * Colores de los símbolos guardados en una rueda, en el orden en
     * que fueron agregados.
     */
    public String symbols(int wheelNumber) {
        for (Wheel w : wheels) {
            if (w.getWheelNumber() == wheelNumber) {
                StringBuilder sb = new StringBuilder();
                List<Symbol> list = w.getSymbols();
                for (int i = 0; i < list.size(); i++) {
                    if (i > 0) sb.append(",");
                    sb.append(list.get(i).getColor());
                }
                result(true);
                return sb.toString();
            }
        }
        showErrorMessage("Wheel " + wheelNumber + " not found");
        result(false);
        return "";
    }

    /**
     * Colores de los símbolos visibles en todas las ruedas, ordenados
     * de izquierda a derecha (por número de rueda).
     */
    public String configuration() {
        List<Wheel> sorted = new ArrayList<>(wheels);
        sorted.sort((a, b) -> a.getWheelNumber() - b.getWheelNumber());
        StringBuilder sb = new StringBuilder();
        for (Wheel w : sorted) {
            if (sb.length() > 0) sb.append(",");
            Symbol current = w.getCurrentSymbol();
            sb.append(current != null ? current.getColor() : "empty");
        }
        result(true);
        return sb.toString();
    }

    /**
     * Cuenta cuántos colores distintos hay en toda la máquina, sin
     * repetir, sumando los símbolos de todas las ruedas.
     */
    public int distinctSymbols() {
        Set<String> colors = new HashSet<>();
        for (Wheel w : wheels) {
            for (Symbol s : w.getSymbols()) {
                colors.add(s.getColor());
            }
        }
        result(true);
        return colors.size();
    }

    public boolean checkJackpot() {
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
        if (isVisible) return;
        isVisible = true;
        canvas = Canvas.getCanvas();
        draw();
    }

    public void makeInvisible() {
        if (!isVisible) return;
        isVisible = false;
        erase();
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
            mainContainer.changeColor(checkJackpot() ? "red" : "blue");
            mainContainer.makeVisible();

            for (Wheel wheel : wheels) {
                wheel.makeVisible();
            }
        }
    }

    private void updateJackpotIndicator() {
        if (mainContainer != null) {
            mainContainer.changeColor(checkJackpot() ? "red" : "blue");
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