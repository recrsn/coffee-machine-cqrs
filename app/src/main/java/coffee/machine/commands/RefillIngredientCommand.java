package coffee.machine.commands;

import coffee.machine.aggregates.CoffeeMachineAggregate;
import coffee.machine.models.Ingredient;

public record RefillIngredientCommand(String coffeeMachineId, Ingredient ingredient,
                                      int quantity) implements Command<CoffeeMachineAggregate> {
    @Override
    public Class<CoffeeMachineAggregate> target() {
        return CoffeeMachineAggregate.class;
    }

    public String targetId() {
        return coffeeMachineId;
    }
}
