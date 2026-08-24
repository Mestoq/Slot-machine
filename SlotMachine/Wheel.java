import java.util.*;

public class Wheel {
    
    private ArrayList<Symbol> symbols;    
    private int currentPosition;         
    private int xPosition;                
    private int yPosition;                
    private boolean isVisible;            
    
    public Wheel() {
        this(0, 0);
    }

    public Wheel(int x, int y) {
        this.symbols = new ArrayList<Symbol>();
        this.currentPosition = 0;
        this.xPosition = x;
        this.yPosition = y;
        this.isVisible = false;
    }
    

    public void addSymbol(int pos, Symbol symbol) {
        int index = pos - 1;
        
        if (index < 0) {
            index = 0;
        }
        if (index > symbols.size()) {
            index = symbols.size();
        }
        
        symbols.add(index, symbol);
        
        if (isVisible) {
            updateVisibility();
        }
    }
    
    public void delSymbol(Symbol symbol) {
        symbols.remove(symbol);
        
        
        if (currentPosition >= symbols.size() && symbols.size() > 0) {
            currentPosition = symbols.size() - 1;
        }
        
        
        if (isVisible) {
            updateVisibility();
        }
    }
    
    public Symbol getCurrentSymbol() {
        if (symbols.isEmpty()) {
            return null;
        }
        return symbols.get(currentPosition);
    }
    
    public ArrayList<Symbol> getSymbols() {
        return new ArrayList<Symbol>(symbols);
    }
    
    public void spin() {
        if (symbols.isEmpty()) {
            return;
        }
        
        currentPosition = (currentPosition + 1) % symbols.size();
        
        if (isVisible) {
            updateVisibility();
        }
    }
    
    public void makeVisible() {
        isVisible = true;
        updateVisibility();
    }
    
    public void makeInvisible() {
        for (Symbol s : symbols) {
            s.makeInvisible();
        }
        isVisible = false;
    }
    
    public int getXPosition() {
        return xPosition;
    }
    
    public int getYPosition() {
        return yPosition;
    }
    
    public void setPosition(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
        
        if (isVisible) {
            updateVisibility();
        }
    }
    
    public boolean isVisible() {
        return isVisible;
    }
    
    private void updateVisibility() {
        if (symbols.isEmpty()) {
            return;
        }
        
        for (int i = 0; i < symbols.size(); i++) {
            Symbol s = symbols.get(i);
            if (i == currentPosition) {
                s.setPosition(xPosition, yPosition);
                s.makeVisible();
            } else {
                s.makeInvisible();
            }
        }
    }
}
