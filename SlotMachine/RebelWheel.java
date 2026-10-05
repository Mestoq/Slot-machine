public class RebelWheel extends Wheel {

    public RebelWheel(int wheelNumber) {
        super(wheelNumber, "orange");
    }

    @Override
    public boolean isRebel() { return true; }
}