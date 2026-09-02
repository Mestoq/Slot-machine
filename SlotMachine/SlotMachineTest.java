/**
 * Test class for the SlotMachine simulator.
 * Demonstrates basic usage: creating a machine, adding symbols, spinning, checking jackpot.
 * 
 * Usage from BlueJ:
 * 1. Right-click SlotMachineTest → Create object → OK
 * 2. Right-click the object → Run example() or testInvisible()
 * 
 * @author Slot Machine Team
 * @version 1.0
 */
public class SlotMachineTest {

    /**
     * Example 1: Complete workflow with visible machine.
     * Creates a machine, adds symbols to each wheel, spins, and checks for jackpot.
     */
    public void example() {
        System.out.println("=== SlotMachine Example (VISIBLE MODE) ===\n");
        
        // Create machine
        SlotMachine machine = new SlotMachine();
        System.out.println("Created: " + machine);
        
        // Add symbols to wheel 0
        Symbol s1 = new Symbol("diamond", "red");
        Symbol s2 = new Symbol("heart", "red");
        Symbol s3 = new Symbol("star", "red");
        machine.addSymbol(0, s1);
        machine.addSymbol(0, s2);
        machine.addSymbol(0, s3);
        
        // Add symbols to wheel 1
        machine.addSymbol(1, new Symbol("diamond", "red"));
        machine.addSymbol(1, new Symbol("heart", "red"));
        machine.addSymbol(1, new Symbol("star", "red"));
        
        // Add symbols to wheel 2
        machine.addSymbol(2, new Symbol("diamond", "red"));
        machine.addSymbol(2, new Symbol("heart", "red"));
        machine.addSymbol(2, new Symbol("star", "red"));
        
        System.out.println("\nInitial state:");
        System.out.println(machine.consultSymbols());
        
        // Make visible
        machine.makeVisible();
        System.out.println("Machine is now VISIBLE\n");
        
        // Spin wheels
        System.out.println("Spinning...");
        machine.spin();
        System.out.println(machine.consultSymbols());
        
        // Check jackpot
        boolean isJackpot = machine.checkJackpot();
        System.out.println("Jackpot? " + isJackpot);
        
        // Clean up
        machine.makeInvisible();
        machine.exit();
    }

    /**
     * Example 2: Invisible mode (no graphics).
     * Demonstrates that logic works without any visual rendering.
     */
    public void testInvisible() {
        System.out.println("=== SlotMachine Example (INVISIBLE MODE) ===\n");
        
        // Create machine (invisible by default)
        SlotMachine machine = new SlotMachine();
        System.out.println("Created: " + machine);
        System.out.println("Machine is INVISIBLE (no rendering)\n");
        
        // Add symbols
        for (int i = 0; i < 3; i++) {
            machine.addSymbol(i, new Symbol("diamond", "red"));
            machine.addSymbol(i, new Symbol("heart", "red"));
        }
        
        System.out.println("Added symbols to all wheels");
        System.out.println(machine.consultSymbols());
        
        // Spin multiple times
        for (int spin = 1; spin <= 5; spin++) {
            System.out.println("--- Spin " + spin + " ---");
            machine.spin();
            System.out.println(machine.consultSymbols());
            System.out.println("Jackpot? " + machine.checkJackpot() + "\n");
        }
        
        machine.exit();
    }

    /**
     * Example 3: Test error handling.
     * Attempts to add symbols to invalid wheels, spin empty wheels, etc.
     */
    public void testErrors() {
        System.out.println("=== Testing Error Handling ===\n");
        
        SlotMachine machine = new SlotMachine();
        machine.makeVisible(); // Show error dialogs
        
        // Try to add symbol to non-existent wheel
        System.out.println("Attempt 1: Add symbol to wheel 10 (doesn't exist)");
        machine.addSymbol(10, new Symbol("test", "red"));
        
        // Try to spin wheel with no symbols
        System.out.println("\nAttempt 2: Spin without adding symbols");
        machine.spin();
        
        // Add symbol and then test success
        System.out.println("\nAttempt 3: Add symbol correctly and spin");
        machine.addSymbol(0, new Symbol("diamond", "red"));
        machine.addSymbol(1, new Symbol("diamond", "red"));
        machine.addSymbol(2, new Symbol("diamond", "red"));
        if (machine.spin()) {
            System.out.println("Spin succeeded!");
            System.out.println(machine.consultSymbols());
        }
        
        machine.makeInvisible();
        machine.exit();
    }
}
