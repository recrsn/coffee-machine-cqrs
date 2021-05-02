package coffee.machine.sagas;

import coffee.machine.CommandGateway;
import coffee.machine.commands.AddIngredientCommand;
import coffee.machine.events.IngredientRefillEvent;

public class IngredientRefilSaga {

    private final CommandGateway commandGateway;

    public IngredientRefilSaga(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    public void onIngredientRefillEvent(IngredientRefillEvent event) {
        this.commandGateway.send(new AddIngredientCommand(event.storeId(), event.ingredient(), event.quantity()));
    }
}
