public class ShySymbol extends Symbol {
    private final Rectangle badge = new Rectangle();
    private int badgeX = 70;                      
    private int badgeY = 15;
    private boolean hidden = false;              

    public ShySymbol(String color, int wheelIndex) {
        super(color, wheelIndex);
        badge.changeSize(BADGE_SIZE, BADGE_SIZE);
        badge.changeColor("black");
    }

    /** Al ser seleccionado alterna su estado (con una pausa si se estaba viendo). */
    @Override
    public void action() {
        pause();
        makeInvisible();
        hidden = !hidden;
    }

    @Override
    public Symbol copy(int wheelIndex) {
        return new ShySymbol(getColor(), wheelIndex);
    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        int targetX = x + BADGE_OFFSET_X;
        int targetY = y + BADGE_OFFSET_Y;
        badge.moveHorizontal(targetX - badgeX);
        badge.moveVertical(targetY - badgeY);
        badgeX = targetX;
        badgeY = targetY;
    }

    @Override
    public void makeVisible() {
        if (hidden) return;
        super.makeVisible();
        badge.makeVisible();
    }

    @Override
    public void makeInvisible() {
        super.makeInvisible();
        badge.makeInvisible();
    }
}