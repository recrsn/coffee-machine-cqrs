package coffee.machine.aggregates;

import coffee.machine.EventBus;
import coffee.machine.commands.CreateCoffeeOrderCommand;
import coffee.machine.commands.InitializeCoffeeMachineCommand;
import coffee.machine.commands.InitializeIngredientStoreCommand;
import coffee.machine.commands.InitializeSlotCommand;
import coffee.machine.events.*;

public class AggregateLifecycle {
    private final AggregateStore aggregateStore;
    private final EventBus eventBus;

    public AggregateLifecycle(AggregateStore aggregateStore, EventBus eventBus) {
        this.aggregateStore = aggregateStore;
        this.eventBus = eventBus;
    }

    public void initializeCoffeeMachineAggregate(CoffeeMachineAggregate __, InitializeCoffeeMachineCommand command) {
        AggregateEventBus aggregateEventBus = new AggregateEventBus(this.eventBus);
        CoffeeMachineAggregate aggregate = new CoffeeMachineAggregate(aggregateEventBus);

        aggregateStore.add(command.id(), aggregate);

        aggregateEventBus.addListener(CoffeeMachineInitializationStartEvent.class,
                aggregate::onCoffeeMachineInitializationStartEvent);
        aggregateEventBus.addListener(ActivateSlotEvent.class, aggregate::onActivateSlotEvent);
        aggregateEventBus.addListener(IngredientStoreAddEvent.class, aggregate::onIngredientStoreAddEvent);
        aggregateEventBus.addListener(CoffeeBrewRequestEvent.class, aggregate::onCoffeeBrewRequestEvent);
        aggregateEventBus.addListener(ClearSlotEvent.class, aggregate::onClearSlotEvent);

        aggregate.init(command);
    }

    public void initializeSlotAggregate(SlotAggregate __, InitializeSlotCommand command) {
        AggregateEventBus aggregateEventBus = new AggregateEventBus(this.eventBus);
        SlotAggregate slotAggregate = new SlotAggregate(aggregateEventBus);

        aggregateStore.add(command.id(), slotAggregate);

        aggregateEventBus.addListener(SlotInitializeEvent.class, slotAggregate::onSlotInitializeEvent);
        aggregateEventBus.addListener(CoffeeBrewStartEvent.class, slotAggregate::onCoffeeBrewStartEvent);
        aggregateEventBus.addListener(CoffeeBrewCompleteEvent.class, slotAggregate::onCoffeeBrewCompleteEvent);
        aggregateEventBus.addListener(CoffeeBrewCancelEvent.class, slotAggregate::onCoffeeBrewCancelEvent);

        slotAggregate.init(command);
    }

    public void initializeIngredientsStoreAggregate(IngredientsStoreAggregate __, InitializeIngredientStoreCommand command) {
        AggregateEventBus aggregateEventBus = new AggregateEventBus(this.eventBus);

        IngredientsStoreAggregate aggregate = new IngredientsStoreAggregate(aggregateEventBus);
        aggregateStore.add(command.storeId(), aggregate);

        aggregateEventBus.addListener(IngredientStoreInitializeEvent.class,
                aggregate::onIngredientStoreInitializeEvent);
        aggregateEventBus.addListener(IngredientAddEvent.class, aggregate::onIngredientAddEvent);
        aggregateEventBus.addListener(IngredientsConsumeEvent.class, aggregate::onIngredientConsumeEvent);

        aggregate.init(command);
    }

    public void initializeCoffeeOrderAggregate(CoffeeOrderAggregate __, CreateCoffeeOrderCommand command) {
        AggregateEventBus aggregateEventBus = new AggregateEventBus(this.eventBus);

        CoffeeOrderAggregate aggregate = new CoffeeOrderAggregate(aggregateEventBus);
        aggregateStore.add(command.id(), aggregate);

        aggregateEventBus.addListener(CoffeeOrderReceiveEvent.class, aggregate::onCoffeeOrderReceiveEvent);
        aggregateEventBus.addListener(CoffeeOrderStatusUpdateEvent.class, aggregate::onCoffeeOrderStatusUpdateEvent);

        aggregate.init(command);
    }
}
