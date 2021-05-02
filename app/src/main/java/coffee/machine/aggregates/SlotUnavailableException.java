package coffee.machine.aggregates;

public class SlotUnavailableException extends IllegalStateException {
    public SlotUnavailableException() {
        super("No slots available");
    }
}
