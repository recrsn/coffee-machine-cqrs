package coffee.machine.commands;

import coffee.machine.aggregates.IngredientsStoreAggregate;

public record InitializeIngredientStoreCommand(String storeId,
                                               String coffeeMachineId) implements Command<IngredientsStoreAggregate> {
    @Override
    public Class<IngredientsStoreAggregate> target() {
        return IngredientsStoreAggregate.class;
    }

    @Override
    public String targetId() {
        return storeId;
    }
}
