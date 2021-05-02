package coffee.machine.commands;

import coffee.machine.aggregates.CoffeeMachineAggregate;

public record AddIngredientStoreCommand(String coffeeMachineId,
                                        String ingredientStoreId) implements Command<CoffeeMachineAggregate> {
    @Override
    public Class<CoffeeMachineAggregate> target() {
        return CoffeeMachineAggregate.class;
    }

    @Override
    public String targetId() {
        return coffeeMachineId;
    }
}
