package coffee.machine.aggregates;

import coffee.machine.EventBus;
import coffee.machine.commands.CreateCoffeeOrderCommand;
import coffee.machine.commands.UpdateCoffeeOrderStatusCommand;
import coffee.machine.events.CoffeeOrderCreatedEvent;
import coffee.machine.events.CoffeeOrderReceiveEvent;
import coffee.machine.events.CoffeeOrderStatusUpdateEvent;

public class CoffeeOrderAggregate implements Aggregate {
    private final EventBus eventBus;

    public String id;
    public Status status;
    private String coffeeMachineId;

    public CoffeeOrderAggregate(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void init(CreateCoffeeOrderCommand command) {
        this.eventBus.emit(new CoffeeOrderReceiveEvent(command.id(), command.coffeeMachineId(), command.recipe()));
    }

    public void onCoffeeOrderReceiveEvent(CoffeeOrderReceiveEvent event) {
        this.id = event.id();
        this.coffeeMachineId = event.coffeeMachineId();
        this.status = Status.REQUESTED;

        this.eventBus.emit(new CoffeeOrderCreatedEvent(id, this.coffeeMachineId, event.recipe()));
    }

    public void onUpdateCoffeeOrderStatusCommand(UpdateCoffeeOrderStatusCommand command) {
        if (command.status().ordinal() < this.status.ordinal()) {
            throw new IllegalArgumentException("Unsupported transition");
        }

        this.eventBus.emit(new CoffeeOrderStatusUpdateEvent(id,
                this.coffeeMachineId,
                command.slotId(),
                command.status()));
    }

    public void onCoffeeOrderStatusUpdateEvent(CoffeeOrderStatusUpdateEvent event) {
        this.status = event.status();
    }

    @Override
    public String id() {
        return id;
    }

    public enum Status {
        REQUESTED,
        ASSIGNED,
        CANCELLED,
        BREWING,
        READY,
    }
}
