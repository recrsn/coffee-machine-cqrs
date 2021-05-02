package coffee.machine.sagas;

import coffee.machine.CommandGateway;
import coffee.machine.commands.ActivateSlotCommand;
import coffee.machine.commands.AddIngredientStoreCommand;
import coffee.machine.commands.InitializeIngredientStoreCommand;
import coffee.machine.commands.InitializeSlotCommand;
import coffee.machine.events.CoffeeMachineInitializationStartEvent;
import coffee.machine.events.IngredientStoreInitializeEvent;
import coffee.machine.events.SlotInitializeEvent;

public class CoffeeMachineSaga {

    private final CommandGateway commandGateway;

    public CoffeeMachineSaga(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    public void onCoffeeMachineInitializationStarted(CoffeeMachineInitializationStartEvent event) {
        for (int i = 0; i < event.slots(); i++) {
            String slotId = "slot-%s-%d".formatted(event.coffeeMachineId(), i);
            commandGateway.send(new InitializeSlotCommand(slotId, event.coffeeMachineId()));
        }

        String storeId = "store-" + event.coffeeMachineId();
        commandGateway.send(new InitializeIngredientStoreCommand(storeId, event.coffeeMachineId()));
    }

    public void onSlotInitialized(SlotInitializeEvent event) {
        commandGateway.send(new ActivateSlotCommand(event.coffeeMachineId(), event.slotId()));
    }

    public void onIngredientStoreInitializeEvent(IngredientStoreInitializeEvent event) {
        commandGateway.send(new AddIngredientStoreCommand(event.coffeeMachineId(), event.storeId()));
    }
}
