package coffee.machine.commands;

import coffee.machine.aggregates.CoffeeOrderAggregate;
import coffee.machine.models.Recipe;

public record CreateCoffeeOrderCommand(String id, String coffeeMachineId,
                                       Recipe recipe) implements Command<CoffeeOrderAggregate> {
    @Override
    public Class<CoffeeOrderAggregate> target() {
        return CoffeeOrderAggregate.class;
    }

    @Override
    public String targetId() {
        return id;
    }
}
