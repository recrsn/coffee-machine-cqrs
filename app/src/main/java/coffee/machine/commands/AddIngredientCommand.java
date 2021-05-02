package coffee.machine.commands;

import coffee.machine.aggregates.IngredientsStoreAggregate;
import coffee.machine.models.Ingredient;

public record AddIngredientCommand(String storeId, Ingredient ingredient,
                                   int quantity) implements Command<IngredientsStoreAggregate> {
    @Override
    public Class<IngredientsStoreAggregate> target() {
        return IngredientsStoreAggregate.class;
    }

    @Override
    public String targetId() {
        return storeId;
    }
}
