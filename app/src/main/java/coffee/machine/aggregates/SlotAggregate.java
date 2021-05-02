package coffee.machine.aggregates;

import coffee.machine.EventBus;
import coffee.machine.commands.BrewCoffeeCommand;
import coffee.machine.commands.CancelCoffeeBrewCommand;
import coffee.machine.commands.InitializeSlotCommand;
import coffee.machine.commands.PrepareSlotCommand;
import coffee.machine.events.CoffeeBrewCancelEvent;
import coffee.machine.events.CoffeeBrewCompleteEvent;
import coffee.machine.events.CoffeeBrewStartEvent;
import coffee.machine.events.SlotInitializeEvent;
import coffee.machine.models.Recipe;

public class SlotAggregate implements Aggregate {

    private final EventBus eventBus;
    private String id;
    private String coffeeMachineId;
    private Recipe recipe;
    private String orderId;

    public SlotAggregate(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void init(InitializeSlotCommand command) {
        this.eventBus.emit(new SlotInitializeEvent(command.id(), command.coffeeMachineId()));
    }

    public void onSlotInitializeEvent(SlotInitializeEvent event) {
        this.id = event.slotId();
        this.coffeeMachineId = event.coffeeMachineId();
        this.recipe = null;
    }

    public void onPrepareSlotCommand(PrepareSlotCommand command) {
        if (this.recipe != null) {
            throw new IllegalStateException(this.id + " slot is busy");
        }

        this.eventBus.emit(new CoffeeBrewStartEvent(this.id(), command.orderId(), command.storeId(), command.recipe()));
    }

    public void onCoffeeBrewStartEvent(CoffeeBrewStartEvent event) {
        this.recipe = event.recipe();
        this.orderId = event.orderId();
    }

    public void onBrewCoffeeCommand(BrewCoffeeCommand command) {
        try {
            Thread.sleep(command.recipe().preparationTime());
            this.eventBus.emit(new CoffeeBrewCompleteEvent(this.id, this.coffeeMachineId, this.orderId));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void onCancelCoffeeBrewCommand(CancelCoffeeBrewCommand __) {
        this.eventBus.emit(new CoffeeBrewCancelEvent(this.id, this.coffeeMachineId, this.orderId));
    }

    public void onCoffeeBrewCompleteEvent(CoffeeBrewCompleteEvent __) {
        this.recipe = null;
        this.orderId = null;
    }

    public void onCoffeeBrewCancelEvent(CoffeeBrewCancelEvent __) {
        this.recipe = null;
        this.orderId = null;
    }


    @Override
    public String id() {
        return id;
    }
}
