package coffee.machine.commands;

import coffee.machine.aggregates.CoffeeMachineAggregate;

public record InitializeCoffeeMachineCommand(String id, int slots) implements Command<CoffeeMachineAggregate> {

    @Override
    public Class<CoffeeMachineAggregate> target() {
        return CoffeeMachineAggregate.class;
    }

    public String targetId() {
        return id;
    }
}
