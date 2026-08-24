import java.awt.*;

public class Symbol {
    
    private String symbol;        
    private String color;        
    private Shape shape;          
    private boolean isVisible;   
    private int xPosition;        
    private int yPosition;      
    
    public Symbol() {
        this("S", "red");
    }
    
    public Symbol(String symbol, String color) {
        this.symbol = symbol;
        this.color = color;
        this.xPosition = 0;
        this.yPosition = 0;
        this.isVisible = false;     
        this.shape = new java.awt.Rectangle(0, 0, 50, 50);
    }
    
    public String getSymbol() {
        return this.symbol;
    }

    public String getColor() {
        return this.color;
    }
    
    public void setPosition(int x, int y) {
        erase();
        this.xPosition = x;
        this.yPosition = y;
        draw();
    }
    
    public int getXPosition() {
        return this.xPosition;
    }
    
    public int getYPosition() {
        return this.yPosition;
    }
    
    public void makeVisible() {
        isVisible = true;
        draw();
    }
    

    public void makeInvisible() {
        erase();
        isVisible = false;
    }
    

    public void changeColor(String newColor) {
        this.color = newColor;
        draw();
    }

    public void moveHorizontal(int distance) {
        erase();
        this.xPosition += distance;
        draw();
    }

    public void moveVertical(int distance) {
        erase();
        this.yPosition += distance;
        draw();
    }

    private void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            java.awt.Rectangle awtRect = new java.awt.Rectangle(
                xPosition, yPosition, 50, 50
            );
            canvas.draw(this, color, awtRect);
            canvas.wait(10);
        }
    }
    

    private void erase() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}
