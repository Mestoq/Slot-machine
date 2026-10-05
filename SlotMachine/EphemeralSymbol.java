public class EphemeralSymbol extends Symbol {
    private static final int STEP = 8;            
    private static final int MIN_DIAMETER = 2;    

    private final Triangle badge = new Triangle();
    private int badgeX = 140;                     
    private int badgeY = 15;

    public EphemeralSymbol(String color, int wheelIndex) {
        super(color, wheelIndex);
        badge.changeSize(BADGE_SIZE, BADGE_SIZE);
        badge.changeColor("black");
    }

    /** Al ser seleccionado se encoge (con una pausa para que se note). */
    @Override
    public void action() {
        pause();
        makeInvisible();
        resize(Math.max(MIN_DIAMETER, getDiameter() - STEP));
    }

    @Override
    public Symbol copy(int wheelIndex) {
        return new EphemeralSymbol(getColor(), wheelIndex);
    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        int targetX = x + BADGE_OFFSET_X + BADGE_SIZE / 2;  
        int targetY = y + BADGE_OFFSET_Y;
        badge.moveHorizontal(targetX - badgeX);
        badge.moveVertical(targetY - badgeY);
        badgeX = targetX;
        badgeY = targetY;
    }

    @Override
    public void makeVisible() {
        super.makeVisible();
        badge.makeVisible();
    }

    @Override
    public void makeInvisible() {
        super.makeInvisible();
        badge.makeInvisible();
    }
}