package coffee.machine.sagas;

import coffee.machine.CommandGateway;
import coffee.machine.aggregates.CoffeeOrderAggregate;
import coffee.machine.aggregates.InsufficientIngredientException;
import coffee.machine.commands.BrewCoffeeCommand;
import coffee.machine.commands.CancelCoffeeBrewCommand;
import coffee.machine.commands.CompleteCoffeeBrewCommand;
import coffee.machine.commands.MakeCoffeeCommand;
import coffee.machine.commands.PrepareSlotCommand;
import coffee.machine.commands.ProvideCoffeeIngredientsCommand;
import coffee.machine.commands.UpdateCoffeeOrderStatusCommand;
import coffee.machine.events.CoffeeBrewCancelEvent;
import coffee.machine.events.CoffeeBrewCompleteEvent;
import coffee.machine.events.CoffeeBrewRequestEvent;
import coffee.machine.events.CoffeeBrewStartEvent;
import coffee.machine.events.CoffeeOrderCreatedEvent;
import coffee.machine.events.IngredientsConsumeEvent;

public class CoffeeSaga {
    private final CommandGateway commandGateway;

    public CoffeeSaga(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    public void onCoffeeOrderCreatedEvent(CoffeeOrderCreatedEvent event) {
        this.commandGateway.send(new MakeCoffeeCommand(event.coffeeMachineId(), event.orderId(), event.recipe()));
    }

    public void onCoffeeBrewRequestEvent(CoffeeBrewRequestEvent event) {
        this.commandGateway.send(new PrepareSlotCommand(event.slotId(),
                event.orderId(),
                event.storeId(),
                event.recipe()));
    }

    public void onCoffeeBrewStartEvent(CoffeeBrewStartEvent event) {
        this.commandGateway.send(new UpdateCoffeeOrderStatusCommand(event.orderId(),
                event.slotId(),
                CoffeeOrderAggregate.Status.ASSIGNED));
        try {
            this.commandGateway.send(new ProvideCoffeeIngredientsCommand(event.storeId(), event.orderId(),
                    event.slotId(), event.recipe()));
        } catch (InsufficientIngredientException e) {
            this.commandGateway.send(new CancelCoffeeBrewCommand(event.slotId()));
        }
    }

    public void onIngredientsConsumeEvent(IngredientsConsumeEvent event) {
        this.commandGateway.send(new UpdateCoffeeOrderStatusCommand(event.orderId(),
                event.slotId(),
                CoffeeOrderAggregate.Status.BREWING));
        this.commandGateway.send(new BrewCoffeeCommand(event.slotId(), event.orderId(), event.recipe()));
    }

    public void onCoffeeBrewCompleteEvent(CoffeeBrewCompleteEvent event) {
        this.commandGateway.send(new UpdateCoffeeOrderStatusCommand(event.orderId(),
                event.slotId(),
                CoffeeOrderAggregate.Status.READY));
        this.commandGateway.send(new CompleteCoffeeBrewCommand(event.coffeeMachineId(), event.slotId()));
    }

    public void onCoffeeBrewCancelEvent(CoffeeBrewCancelEvent event) {
        this.commandGateway.send(new UpdateCoffeeOrderStatusCommand(event.orderId(),
                event.slotId(),
                CoffeeOrderAggregate.Status.CANCELLED));
        this.commandGateway.send(new CompleteCoffeeBrewCommand(event.coffeeMachineId(), event.slotId()));
    }
}
