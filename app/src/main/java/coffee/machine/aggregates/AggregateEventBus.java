package coffee.machine.aggregates;

import coffee.machine.DefaultEventBus;
import coffee.machine.EventBus;

public class AggregateEventBus extends DefaultEventBus {
    private final EventBus eventBus;

    public AggregateEventBus(EventBus eventBus) {
        super();
        this.eventBus = eventBus;
    }

    @SuppressWarnings("unchecked")
    public <T> void emit(T message) {
        super.emit(message);
        eventBus.emit(message);
    }
}
