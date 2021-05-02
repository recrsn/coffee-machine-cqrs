package coffee.machine.commands;

import coffee.machine.aggregates.CoffeeOrderAggregate;

public record UpdateCoffeeOrderStatusCommand(String id, String slotId,
                                             CoffeeOrderAggregate.Status status) implements Command<CoffeeOrderAggregate> {
    @Override
    public Class<CoffeeOrderAggregate> target() {
        return CoffeeOrderAggregate.class;
    }

    @Override
    public String targetId() {
        return id;
    }
}
