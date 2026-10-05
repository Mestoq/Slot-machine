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

    Wheel.clearSharedTape();
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

        
        for (int i = 1; i <= n; i++) {
            Wheel currentWheel = new Wheel(i);

            if (i == 1) {
                for (int j = 0; j < n; j++) {
                    currentWheel.addSymbol(new Symbol(sharedColors[j], i));
                }
            }

            wheels.add(currentWheel);
        }

        
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


    


    private Wheel newWheel(String type, int wheelNumber) {
        if (type == null) return null;
        switch (type) {
            case "normal": return new Wheel(wheelNumber);
            case "lefty":  return new LeftyWheel(wheelNumber);
            case "rebel":  return new RebelWheel(wheelNumber);
            case "tired":  return new TiredWheel(wheelNumber);
            default:       return null;
        }
    }


    private Symbol newSymbol(String type, String color, int wheelNumber) {
        if (type == null) return null;
        switch (type) {
            case "normal":    return new Symbol(color, wheelNumber);
            case "ephemeral": return new EphemeralSymbol(color, wheelNumber);
            case "shy":       return new ShySymbol(color, wheelNumber);
            default:          return null;
        }
    }

    
    
    
    

    public void addWheel(int wheelNumber) {
        addWheel("normal", wheelNumber);
    }

    public void addWheel(String type, int wheelNumber) {
        for (Wheel w : wheels) {
            if (w.getWheelNumber() == wheelNumber) {
                showErrorMessage("Wheel " + wheelNumber + " already exists");
                result = false;
                return;
            }
        }
        Wheel wheel = newWheel(type, wheelNumber);
        if (wheel == null) {
            showErrorMessage("Unknown wheel type: " + type);
            result = false;
            return;
        }
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
                if (wheel.isRebel()) {
                    showErrorMessage("Wheel " + wheelNumber + " is rebel and cannot be deleted");
                    result = false;
                    return;
                }
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
        addSymbol("normal", wheelNumber, color);
    }

    public void addSymbol(String type, int wheelNumber, String color) {
        for (Wheel w : wheels) {
            if (w.getWheelNumber() == wheelNumber) {
                if (w.hasColor(color)) {
                    showErrorMessage("Wheel " + wheelNumber + " already has a " + color + " symbol");
                    result = false;
                    return;
                }
                Symbol symbol = newSymbol(type, color, wheelNumber);
                if (symbol == null) {
                    showErrorMessage("Unknown symbol type: " + type);
                    result = false;
                    return;
                }
                result = w.addSymbol(symbol);
                syncAllVisuals(); 
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
                    syncAllVisuals();
                    return;
                }
            }
        }
        showErrorMessage("Symbol " + color + " not found");
        result = false;
    }

    /** Propaga a todas las ruedas los cambios de la cinta compartida. */
    private void syncAllVisuals() {
        for (Wheel w : wheels) w.syncVisuals();
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

    
    
    

    /** @return la rueda con el número n-1, o null si no existe. */
    private Wheel leftOf(Wheel target) {
        for (Wheel w : wheels) {
            if (w.getWheelNumber() == target.getWheelNumber() - 1) return w;
        }
        return null;
    }


    
    
    private List<Wheel> sortedWheels() {
        List<Wheel> sorted = new ArrayList<>(wheels);
        sorted.sort((a, b) -> a.getWheelNumber() - b.getWheelNumber());
        return sorted;
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
        
        // Las ruedas están en orden: la n-1 gira antes que la n
        for (Wheel wheel : wheels) {
            if (!wheel.isHeld()) {
                wheel.spin(leftOf(wheel));
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
                w.spin(leftOf(w));
                if (isVisible) refreshActiveWheels();
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
                w.spin(leftOf(w), steps);
                if (isVisible) refreshActiveWheels();
                result = true;
                return;
            }
        }
        showErrorMessage("Wheel " + wheel + " not found");
        result = false;
    }


    
    public void spin(String[] setSymbols) {
        if (setSymbols.length != wheels.size()) {
            showErrorMessage("Configuration size does not match wheel count");
            result = false;
            return;
        }
    
        for (int i = 0; i < setSymbols.length; i++) {
            if (!wheels.get(i).hasColor(setSymbols[i].trim())) {
                showErrorMessage("Wheel " + wheels.get(i).getWheelNumber() +
                        " has no " + setSymbols[i].trim() + " symbol");
                result = false;
                return;
            }
        }
    
        for (int i = 0; i < setSymbols.length; i++) {
            wheels.get(i).placeSymbol(setSymbols[i].trim());
        }
        if (isVisible) refreshActiveWheels();
        result = true;
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
        if (w1.isRebel() || w2.isRebel()) {
            showErrorMessage("A rebel wheel cannot be swapped");
            result = false;
            return;
        }
        w1.swapContentWith(w2);
        result = true;
    }

    public void lock(int wheel) {
        for (Wheel w : wheels) {
            if (w.getWheelNumber() == wheel) {
                if (w.isRebel()) {
                    showErrorMessage("Wheel " + wheel + " is rebel and cannot be locked");
                    result = false;
                    return;
                }
                w.hold();
                result = true;
                return;
            }
        }
        showErrorMessage("Wheel " + wheel + " not found");
        result = false;
    }


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


    
    

    public String consultSymbols() {
        StringBuilder sb = new StringBuilder();
        sb.append("Slot Machine Status:\n");
        for (Wheel wheel : wheels) {
            sb.append(wheel.toString()).append("\n");
        }
        return sb.toString();
    }

    
    public String[] symbols() {
        if (wheels.isEmpty()) {
            result = false;
            return new String[0];
        }
        List<Symbol> list = wheels.get(0).getSymbols(); 
        String[] colors = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            colors[i] = list.get(i).getColor();
        }
        result = true;
        return colors;
    }

    
    public String[] configuration() {
        String[] colors = new String[wheels.size()];
        for (int i = 0; i < wheels.size(); i++) {
            Symbol current = wheels.get(i).getCurrentSymbol();
            colors[i] = current != null ? current.getColor() : "empty";
        }
        result = true;
        return colors;
    }


    public int distinctSymbols() {
        Set<String> visible = new HashSet<>();
        for (Wheel w : wheels) {
            Symbol current = w.getCurrentSymbol();
            if (current != null) {          
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