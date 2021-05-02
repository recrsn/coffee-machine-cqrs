package coffee.machine.commands;

import coffee.machine.aggregates.IngredientsStoreAggregate;
import coffee.machine.models.Recipe;

public record ProvideCoffeeIngredientsCommand(String id,
                                              String orderId,
                                              String slotId,
                                              Recipe recipe) implements Command<IngredientsStoreAggregate> {
    @Override
    public Class<IngredientsStoreAggregate> target() {
        return IngredientsStoreAggregate.class;
    }

    @Override
    public String targetId() {
        return id;
    }
}
