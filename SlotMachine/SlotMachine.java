import java.util.*;
import javax.swing.*;


public class SlotMachine {
    
    private ArrayList<Wheel> wheels;
    private HashMap<String, Symbol> symbols;
    private Canvas canvas;                 
    private boolean visible;                
    private boolean isJackpot;               
    private String lastMessage;

    public SlotMachine() {
        this.wheels = new ArrayList<Wheel>();
        this.symbols = new HashMap<String, Symbol>();
        this.canvas = Canvas.getCanvas();
        this.visible = false;
        this.isJackpot = false;
        this.lastMessage = "";
    }
    

    public void addWheel(int pos) {

        int index = pos - 1;
        
 
        if (index < 0) {
            index = 0;
        }
        if (index > wheels.size()) {
            index = wheels.size();
        }
        
 
        Wheel wheel = new Wheel(80 + (index * 120), 100);
        wheels.add(index, wheel);
        
 
        if (visible) {
            wheel.makeVisible();
        }
    }
    

    public void delWheel(int pos) {
 
        int index = pos - 1;
 
        if (index < 0 || index >= wheels.size()) {
            showMessage("Invalid wheel position: " + pos);
            return;
        }
        
        Wheel wheel = wheels.get(index);
        wheel.makeInvisible();
        wheels.remove(index);
    }
    

    public void addSymbol(int pos, String color, String symbol) {

        Symbol sym = new Symbol(symbol, color);
        
  
        symbols.put(symbol + "_" + color, sym);
        
  
        if (pos < 1 || pos > wheels.size()) {
            showMessage("Invalid wheel position: " + pos);
            return;
        }
        
        Wheel wheel = wheels.get(pos - 1);
        wheel.addSymbol(wheel.getSymbols().size() + 1, sym);
    }
    

    public void delSymbol(String symbol) {
 
        boolean found = false;
        
        for (Wheel wheel : wheels) {
            ArrayList<Symbol> wheelSymbols = wheel.getSymbols();
            for (Symbol s : wheelSymbols) {
                if (s.getSymbol().equals(symbol)) {
                    wheel.delSymbol(s);
                    found = true;
                    break;
                }
            }
        }
        
        if (!found) {
            showMessage("Symbol not found: " + symbol);
        }
    }
    

    public void spin(int wheelPos) {
        if (wheels.isEmpty()) {
            showMessage("No wheels in the machine");
            return;
        }
        
        if (wheelPos == 0) {
            for (Wheel wheel : wheels) {
                wheel.spin();
            }
        } else if (wheelPos >= 1 && wheelPos <= wheels.size()) {
 
            wheels.get(wheelPos - 1).spin();
        } else {
            showMessage("Invalid wheel position: " + wheelPos);
        }
        
 
        updateJackpotStatus();
    }
    

    public void spin() {
        spin(0);
    }
    

    public String[] symbols() {
        Set<String> distinctSymbols = new HashSet<String>();
        
        for (Wheel wheel : wheels) {
            for (Symbol sym : wheel.getSymbols()) {
                distinctSymbols.add(sym.getSymbol());
            }
        }
        
        String[] result = new String[distinctSymbols.size()];
        return distinctSymbols.toArray(result);
    }
    

    public int distinctSymbols() {
        return symbols().length;
    }
    

    public String[] configuration() {
        String[] config = new String[wheels.size()];
        
        for (int i = 0; i < wheels.size(); i++) {
            Symbol currentSymbol = wheels.get(i).getCurrentSymbol();
            if (currentSymbol != null) {
                config[i] = currentSymbol.getColor();
            } else {
                config[i] = "none";
            }
        }
        
        return config;
    }
    

    public boolean isJackpot() {
        return isJackpot;
    }
    

    public void makeVisible() {
        visible = true;
        canvas.setVisible(true);
        
      
        for (Wheel wheel : wheels) {
            wheel.makeVisible();
        }
    }

    public void makeInvisible() {
        visible = false;
        for (Wheel wheel : wheels) {
            wheel.makeInvisible();
        }
    }
    

    public void exit() {
        makeInvisible();
        System.exit(0);
    }
    
    public boolean ok() {
        return lastMessage.isEmpty();
    }
    

    public void placeSymbol(int wheelPos, int symbolPos, Symbol symbol) {
        if (wheelPos < 1 || wheelPos > wheels.size()) {
            showMessage("Invalid wheel position: " + wheelPos);
            return;
        }
        
        Wheel wheel = wheels.get(wheelPos - 1);
        wheel.addSymbol(symbolPos, symbol);
    }

    public int getWheelCount() {
        return wheels.size();
    }
    

    public Wheel getWheel(int pos) {
        if (pos < 1 || pos > wheels.size()) {
            return null;
        }
        return wheels.get(pos - 1);
    }
    

    public boolean isVisible() {
        return visible;
    }
    

    private void updateJackpotStatus() {
        if (wheels.isEmpty()) {
            isJackpot = false;
            return;
        }
        
        String[] config = configuration();
        String firstColor = config[0];
        
        if (firstColor.equals("none")) {
            isJackpot = false;
            return;
        }

        for (int i = 1; i < config.length; i++) {
            if (!config[i].equals(firstColor)) {
                isJackpot = false;
                return;
            }
        }
        
        isJackpot = true;
    }
    

    private void showMessage(String message) {
        this.lastMessage = message;
        
        if (visible) {
            JOptionPane.showMessageDialog(
                null,
                message,
                "Slot Machine",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
    

    public String getLastMessage() {
        return lastMessage;
    }
}
