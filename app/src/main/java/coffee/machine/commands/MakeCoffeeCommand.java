package coffee.machine.commands;

import coffee.machine.aggregates.CoffeeMachineAggregate;
import coffee.machine.models.Recipe;

public record MakeCoffeeCommand(String coffeeMachineId, String orderId,
                                Recipe recipe) implements Command<CoffeeMachineAggregate> {
    @Override
    public Class<CoffeeMachineAggregate> target() {
        return CoffeeMachineAggregate.class;
    }

    public String targetId() {
        return coffeeMachineId;
    }
}
