package coffee.machine.aggregates;

import coffee.machine.EventBus;
import coffee.machine.commands.ActivateSlotCommand;
import coffee.machine.commands.AddIngredientStoreCommand;
import coffee.machine.commands.CompleteCoffeeBrewCommand;
import coffee.machine.commands.InitializeCoffeeMachineCommand;
import coffee.machine.commands.MakeCoffeeCommand;
import coffee.machine.commands.RefillIngredientCommand;
import coffee.machine.events.ActivateSlotEvent;
import coffee.machine.events.ClearSlotEvent;
import coffee.machine.events.CoffeeBrewRequestEvent;
import coffee.machine.events.CoffeeMachineInitializationStartEvent;
import coffee.machine.events.CoffeeMachineInitializeCompleteEvent;
import coffee.machine.events.IngredientRefillEvent;
import coffee.machine.events.IngredientStoreAddEvent;

import java.util.HashMap;
import java.util.Map;

public class CoffeeMachineAggregate implements Aggregate {
    private final EventBus eventBus;
    private int totalSlots;

    private Map<String, Boolean> slotAvailable;
    private String id;
    private String ingredientStoreId;

    public CoffeeMachineAggregate(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void init(InitializeCoffeeMachineCommand command) {
        this.eventBus.emit(new CoffeeMachineInitializationStartEvent(command.id(), command.slots()));
    }

    @Override
    public String id() {
        return id;
    }

    public void onCoffeeMachineInitializationStartEvent(CoffeeMachineInitializationStartEvent event) {
        this.id = event.coffeeMachineId();
        this.slotAvailable = new HashMap<>();
        this.totalSlots = event.slots();
    }

    public void onActivateSlotCommand(ActivateSlotCommand command) {
        this.eventBus.emit(new ActivateSlotEvent(command.slotId()));
    }

    public void onActivateSlotEvent(ActivateSlotEvent event) {
        this.slotAvailable.put(event.slotId(), true);

        if (this.slotAvailable.size() == this.totalSlots) {
            this.eventBus.emit(new CoffeeMachineInitializeCompleteEvent(this.id()));
        }
    }

    public void onAddIngredientStoreCommand(AddIngredientStoreCommand command) {
        this.eventBus.emit(new IngredientStoreAddEvent(this.id(), command.ingredientStoreId()));
    }

    public void onIngredientStoreAddEvent(IngredientStoreAddEvent event) {
        this.ingredientStoreId = event.ingredientStoreId();
    }

    public void onRefillIngredient(RefillIngredientCommand command) {
        if (this.ingredientStoreId == null) {
            throw new IllegalStateException("Ingredient store not initialized");
        }

        this.eventBus.emit(new IngredientRefillEvent(this.ingredientStoreId,
                this.id,
                command.ingredient(),
                command.quantity()));
    }

    public void onMakeCoffeeCommand(MakeCoffeeCommand command) {
        String freeSlot = slotAvailable.entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new SlotUnavailableException());

        if (this.ingredientStoreId == null) {
            throw new IllegalStateException("Ingredient store not initialized");
        }
        this.eventBus.emit(new CoffeeBrewRequestEvent(this.id(),
                freeSlot,
                this.ingredientStoreId,
                command.orderId(),
                command.recipe()));
    }

    public void onCoffeeBrewRequestEvent(CoffeeBrewRequestEvent event) {
        this.slotAvailable.put(event.slotId(), false);
    }

    public void onCoffeeBrewComplete(CompleteCoffeeBrewCommand command) {
        this.eventBus.emit(new ClearSlotEvent(command.slotId()));
    }

    public void onClearSlotEvent(ClearSlotEvent event) {
        this.slotAvailable.put(event.slotId(), true);
    }
}
